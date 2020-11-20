package com.example.attendencemonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.attendencemonitor.service.ModuleService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IModuleService;
import com.example.attendencemonitor.service.model.ModuleModel;

import java.util.ArrayList;

public class Modules extends AppCompatActivity {

    IModuleService moduleService = new ModuleService();

    private RecyclerView mRecyclerview;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button add_mods;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);

        add_mods = (Button) findViewById(R.id.add_modules);

        add_mods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Modules.this, AddModule.class));
            }
        });



        ArrayList<ModuleModel> modulesList = new ArrayList<>();

        ModuleModel mod1 = new ModuleModel();
        ModuleModel mod2 = new ModuleModel();
        ModuleModel mod3 = new ModuleModel();
        ModuleModel mod4 = new ModuleModel();
        mod1.setName("English");
        mod2.setName("German");
        mod3.setName("Spanish");
        mod4.setName("French");
        modulesList.add(mod1);
        modulesList.add(mod2);
        modulesList.add(mod3);
        modulesList.add(mod4);


        mRecyclerview = findViewById(R.id.modulelist);
        mRecyclerview.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ModAdapter(modulesList);

        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);
    }
}