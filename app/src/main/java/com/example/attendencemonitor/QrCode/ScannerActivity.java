package com.example.attendencemonitor.QrCode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;

import com.example.attendencemonitor.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity  implements ZXingScannerView.ResultHandler
{
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        ViewGroup contentFrame = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult)
    {
        String studentCode = rawResult.getText();
        Log.i("Studentcode: ",studentCode);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                ScannerActivity.this.finish();
            }
        }, 800);
    }
}