package com.example.attendencemonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.attendencemonitor.service.ModuleItem;

import java.util.ArrayList;

public class Modules extends AppCompatActivity {

    private RecyclerView mRecyclerview;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);

        ArrayList<ModuleItem> modulesList = new ArrayList<>();
        modulesList.add(new ModuleItem("English"));
        modulesList.add(new ModuleItem("German"));
        modulesList.add(new ModuleItem("French"));
        modulesList.add(new ModuleItem("Spanish"));


        mRecyclerview = findViewById(R.id.modulelist);
        mRecyclerview.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ModAdapter(modulesList);

        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);
    }
}