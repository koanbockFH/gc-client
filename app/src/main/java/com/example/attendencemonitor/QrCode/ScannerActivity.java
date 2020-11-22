package com.example.attendencemonitor.QrCode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.ViewGroup;

import com.example.attendencemonitor.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity  implements ZXingScannerView.ResultHandler
{
    public static final String EXTRA_RESULT_PAYLOAD = "SCANNER_RESULT";
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state)
    {
        super.onCreate(state);
        setContentView(R.layout.activity_scanner);

        ViewGroup contentFrame = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState)
    {
        super.onSaveInstanceState(outState, outPersistentState);
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
        Intent payload = new Intent();
        payload.putExtra(EXTRA_RESULT_PAYLOAD,  rawResult.getText());
        setResult(Activity.RESULT_OK, payload);

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