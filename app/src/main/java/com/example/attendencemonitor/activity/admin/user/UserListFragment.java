package com.example.attendencemonitor.activity.admin.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.auth.RegisterActivity;
import com.example.attendencemonitor.activity.module.ModuleListActivity;
import com.example.attendencemonitor.service.UserService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IUserService;
import com.example.attendencemonitor.service.dto.Pagination;
import com.example.attendencemonitor.service.dto.UserSearchDto;
import com.example.attendencemonitor.service.model.UserModel;
import com.example.attendencemonitor.service.model.UserType;
import com.example.attendencemonitor.util.IRecyclerViewItemEventListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UserListFragment extends Fragment
{
    private UserAdminListAdapter adapter;
    List<UserModel> userList;
    boolean isTeacher;
    IUserService userService = new UserService();

    public UserListFragment()
    {
        // Required empty public constructor
    }

    public static UserListFragment newInstance(boolean isTeacher)
    {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        fragment.isTeacher = isTeacher;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        UserSearchDto dto = new UserSearchDto();
        dto.setType(isTeacher ? UserType.TEACHER : UserType.STUDENT);
        userService.getUserList(dto, new GetCallback());
        View view = inflater.inflate(R.layout.fragment_user_admin, container, false);

        RecyclerView rv = view.findViewById(R.id.rv_user_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        adapter = new UserAdminListAdapter(filter(null), new ListItemListener());

        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
        EditText searchBox = view.findViewById(R.id.et_searchbox);
        searchBox.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){}

            @Override
            public void afterTextChanged(Editable editable)
            {
                String searchValue = editable.toString();
                adapter.setItems(filter(searchValue));
            }
        });

        ImageButton delSearch = view.findViewById(R.id.ib_delete_search_classlist);
        delSearch.setOnClickListener(v -> {
            searchBox.setText("");
        });

        FloatingActionButton fabAdd = view.findViewById(R.id.fab_user_add);
        fabAdd.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), RegisterActivity.class);
            i.putExtra(RegisterActivity.EXTRA_IS_TEACHER, isTeacher);
            startActivity(i);
        });

        return view;
    }


    private List<UserModel> filter(String searchValue)
    {
        List<UserModel> filteredList = new ArrayList<>();
        if(userList == null)
        {
            return filteredList;
        }

        for(UserModel u: userList)
        {
            if(searchValue == null || searchValue.isEmpty())
            {
                filteredList.add(u);
            }
            else if(u.getMail().toLowerCase().contains(searchValue.toLowerCase()) ||
                    u.getCode().toLowerCase().contains(searchValue.toLowerCase()) ||
                    u.getFullName().toLowerCase().contains(searchValue.toLowerCase()))
            {
                filteredList.add(u);
            }
        }

        return filteredList;
    }

    private class GetCallback implements ICallback<Pagination<UserModel>>
    {

        @Override
        public void onSuccess(Pagination<UserModel> value)
        {
            userList = value.getItemList();
            adapter.setItems(filter(null));
        }

        @Override
        public void onError(Throwable error)
        {

        }
    }

    private class ListItemListener implements IRecyclerViewItemEventListener<UserModel>
    {

        @Override
        public void onClick(UserModel item)
        {
            if(item.getUserType() == UserType.STUDENT)
            {
                Intent i = new Intent(getActivity(), StudentModuleStatisticActivity.class);
                i.putExtra(StudentModuleStatisticActivity.EXTRA_STUDENT_ID, item.getId());
                i.putExtra(StudentModuleStatisticActivity.EXTRA_STUDENT_NAME, item.getFullName());
                startActivity(i);
            }
            else if(item.getUserType() == UserType.TEACHER)
            {
                Intent i = new Intent(getActivity(), ModuleListActivity.class);
                i.putExtra(ModuleListActivity.EXTRA_TEACHER_ID, item.getId());
                i.putExtra(ModuleListActivity.EXTRA_TITLE, item.getFullName() + " | Modules");
                startActivity(i);
            }
        }

        @Override
        public void onLongPress(UserModel item)
        {

        }

        @Override
        public void onPrimaryClick(UserModel item)
        {

        }

        @Override
        public void onSecondaryActionClick(UserModel item)
        {

        }
    }
}