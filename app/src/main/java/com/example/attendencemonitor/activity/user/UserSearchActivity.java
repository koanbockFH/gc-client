package com.example.attendencemonitor.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.service.UserService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IUserService;
import com.example.attendencemonitor.service.dto.Pagination;
import com.example.attendencemonitor.service.dto.UserSearchDto;
import com.example.attendencemonitor.service.model.UserModel;
import com.example.attendencemonitor.service.model.UserType;

import java.util.ArrayList;
import java.util.Collections;

public class UserSearchActivity extends BaseMenuActivity
{
    private final IUserService userService = new UserService();
    public static final String EXTRA_CURRENT_SELECTED = "SELECTED_USERS";
    public static final String EXTRA_USER_TYPE = "USER_TYPE";
    private ArrayList<UserModel> defaultSelection;
    private ArrayList<UserModel> currentSelection;
    private UserListAdapter adapter;
    private final UserSearchDto dto = new UserSearchDto();
    private EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //get additional information from calling intent
        Intent received = getIntent();
        defaultSelection = received.getParcelableArrayListExtra(EXTRA_CURRENT_SELECTED);
        int requestType = received.getIntExtra(EXTRA_USER_TYPE, UserType.TEACHER.getKey());
        String title = "Search";
        if(requestType == UserType.TEACHER.getKey())
        {
            dto.setType(UserType.TEACHER);
            title = "Teachers";
        }
        else{
            dto.setType(UserType.STUDENT);
            title = "Students";
        }

        initializeMenu(title, true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        //init search
        searchBox = findViewById(R.id.et_searchbox);
        searchBox.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                currentSelection = adapter.getCurrentSelection();
                dto.setSearch(editable.toString());
                loadData();
            }
        });

        ImageButton delSearch = findViewById(R.id.ib_delete_search_user);
        delSearch.setOnClickListener(v -> searchBox.setText(""));
        loadData();
    }

    private void loadData()
    {
        //request from backend, and register the callback handling the response
        userService.getUserList(dto, new UserListCallback());
    }

    private void readValues(UserModel[] values)
    {
        RecyclerView rv = findViewById(R.id.rv_user_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        ArrayList<UserModel> items = new ArrayList<>();
        Collections.addAll(items, values);
        ArrayList<UserModel> currentItems = new ArrayList<>();
        //setup current selection e.g. if module has already some students/teacher selected; or if a search has been initiated - search is powered by backend to reduce workload on phone
        if(currentSelection != null)
        {
            currentSelection.forEach(u -> currentItems.add(u));
        }
        else
        {
            defaultSelection.forEach(u -> currentItems.add(u));
        }
        adapter = new UserListAdapter(items, dto.getType() == UserType.TEACHER,  currentItems);

        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
    }

    public void onSave(View view)
    {
        //return list of selected items - if single selection array with 1 item
        Intent save = new Intent();
        save.putParcelableArrayListExtra(EXTRA_CURRENT_SELECTED, adapter.getCurrentSelection());
        setResult(Activity.RESULT_OK, save);
        finish();
    }

    public void onCancel(View view)
    {
        Intent save = new Intent();
        save.putParcelableArrayListExtra(EXTRA_CURRENT_SELECTED, defaultSelection);
        setResult(Activity.RESULT_OK, save);
        finish();
    }

    /***
     * Callback for response of backend on Get All students/teachers based on the search
     */
    private class UserListCallback implements ICallback<Pagination<UserModel>>
    {
        @Override
        public void onSuccess(Pagination<UserModel> value)
        {
            readValues(value.getItems());
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(UserSearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}