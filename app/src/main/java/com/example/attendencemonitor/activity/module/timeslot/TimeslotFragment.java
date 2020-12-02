package com.example.attendencemonitor.activity.module.timeslot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.qr.ScannerActivity;
import com.example.attendencemonitor.service.AttendanceService;
import com.example.attendencemonitor.service.TimeslotService;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.IAttendanceService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.ITimeslotService;
import com.example.attendencemonitor.service.dto.AttendDto;
import com.example.attendencemonitor.service.model.ModuleModel;
import com.example.attendencemonitor.service.model.TimeslotModel;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class TimeslotFragment extends Fragment
{
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private static final int SCANNER_REQUEST = 2;
    private int timeslotId = -1;
    private ModuleModel module;
    ITimeslotService timeslotService = new TimeslotService();
    IAttendanceService attendanceService = new AttendanceService();

    public TimeslotFragment()
    {
        // Required empty public constructor
    }

    public static TimeslotFragment newInstance(ModuleModel module)
    {
        TimeslotFragment fragment = new TimeslotFragment();
        fragment.module = module;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            timeslotService.getAll(module.getId(), new TimeslotListCallback());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_timeslot, container, false);

        view.findViewById(R.id.fab_timeslot_add).setOnClickListener(this::onAdd);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        timeslotService.getAll(module.getId(), new TimeslotListCallback());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ZXING_CAMERA_PERMISSION)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent scanner = new Intent(getActivity(), ScannerActivity.class);
                this.startActivity(scanner);
            }
            else
            {
                Toast.makeText(getActivity(), R.string.onRequestPermissionsResult, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCANNER_REQUEST && data != null)
        {
            String payload = data.getStringExtra(ScannerActivity.EXTRA_RESULT_PAYLOAD);
            int studentId = Integer.parseInt(payload);

            AttendDto dto = new AttendDto();
            dto.setStudentId(studentId);
            dto.setTimeslotId(timeslotId);
            attendanceService.attend(dto, new AttendTimeslotCallback());
        }
    }


    private void readValues(TimeslotModel[] values)
    {
        RecyclerView rv = getActivity().findViewById(R.id.rv_timeslot_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        ArrayList<TimeslotModel> items = new ArrayList<>();
        Collections.addAll(items, values);
        TimeslotListAdapter adapter = new TimeslotListAdapter(items, new ListItemListener());

        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
    }

    public void onAdd(View view)
    {
        Intent addTimeslot = new Intent(getActivity(), TimeslotFormActivity.class);
        addTimeslot.putExtra(TimeslotFormActivity.EXTRA_MODULE_ID, module.getId());
        startActivity(addTimeslot);
    }

    private void onOpenScanner(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        }
        else {
            Intent scanner = new Intent(getActivity(), ScannerActivity.class);
            this.startActivityForResult(scanner, SCANNER_REQUEST);
        }
    }

    private void onDeleteTimeslot(TimeslotModel item){
        timeslotService.delete(item, new DeleteTimeslotCallback());
    }

    private void onEditTimeslot(TimeslotModel timeslot) {
        Intent i = new Intent(getActivity(), TimeslotFormActivity.class);
        i.putExtra(TimeslotFormActivity.EXTRA_MODULE_ID, module.getId());
        i.putExtra(TimeslotFormActivity.EXTRA_TIMESLOT_ID, timeslot.getId());
        startActivity(i);
    }

    private void makeToast(String message)
    {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private class TimeslotListCallback implements ICallback<TimeslotModel[]>
    {

        @Override
        public void onSuccess(TimeslotModel[] value)
        {
            readValues(value);
        }

        @Override
        public void onError(Throwable error)
        {
            makeToast("Something went wrong");
        }
    }

    private class DeleteTimeslotCallback implements IActionCallback
    {

        @Override
        public void onSuccess()
        {
            makeToast("Timeslot has been deleted!");
        }

        @Override
        public void onError(Throwable error)
        {
            makeToast("Something went wrong!");
        }
    }

    private class AttendTimeslotCallback implements IActionCallback{

        @Override
        public void onSuccess()
        {
            makeToast("Attendance recorded!");
        }

        @Override
        public void onError(Throwable error)
        {
            makeToast("Something went wrong!");
        }
    }

    private class ListItemListener implements IRecyclerViewItemEventListener<TimeslotModel>
    {
        @Override
        public void onClick(TimeslotModel item)
        {
            timeslotId = item.getId();
            onOpenScanner();
        }

        @Override
        public void onLongPress(TimeslotModel item)
        {
            timeslotId = item.getId();
            onDeleteTimeslot(item);
        }

        @Override
        public void onActionClick(TimeslotModel item)
        {
            timeslotId = item.getId();
            onEditTimeslot(item);
        }
    }
}