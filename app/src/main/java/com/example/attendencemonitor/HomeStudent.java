package com.example.attendencemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeStudent extends AppCompatActivity {

    private Button openQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);

        openQRCode = (Button) findViewById(R.id.btn_openQRCode);

        openQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO startActivity(new Intent(HomeStudent.this, QRCode.class));
            }
        });
    }

}