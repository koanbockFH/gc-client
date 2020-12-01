package com.example.attendencemonitor.activity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
    IUserService userService = new UserService();
    public static final String EXTRA_CURRENT_SELECTED = "SELECTED_USERS";
    public static final String EXTRA_USER_TYPE = "USER_TYPE";
    private ArrayList<UserModel> defaultSelection;
    private UserListAdapter adapter;
    private final UserSearchDto dto = new UserSearchDto();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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

        EditText searchBox = findViewById(R.id.et_searchbox);
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
                dto.setSearch(editable.toString());
                loadData();
            }
        });
        loadData();
    }

    private void loadData()
    {
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
        defaultSelection.forEach(u -> currentItems.add(u));
        adapter = new UserListAdapter(items, dto.getType() == UserType.TEACHER,  currentItems);

        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
    }

    public void onSave(View view)
    {
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
            Toast.makeText(UserSearchActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}