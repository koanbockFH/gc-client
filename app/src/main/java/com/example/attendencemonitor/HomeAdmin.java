package com.example.attendencemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class HomeAdmin extends AppCompatActivity {

    private Button reg_btn;
    private Button module_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);


        reg_btn = (Button) findViewById(R.id.btn_register);
        module_btn = (Button) findViewById(R.id.btn_modules);

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeAdmin.this, Register.class));
            }
        });

    }
}