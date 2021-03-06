package com.example.attendencemonitor.activity.qr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends BaseMenuActivity implements ZXingScannerView.ResultHandler
{
    public static final String EXTRA_RESULT_PAYLOAD = "SCANNER_RESULT";
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state)
    {
        initializeMenu("Scanner", true);
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
        //handling pausing of app, e.g. if user switches app - camera should be deactivated meanwhile
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        //handling pausing of app, e.g. if user switches app - camera should be deactivated meanwhile
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult)
    {
        //handle result from ZXing Scanner
        Intent payload = new Intent();
        payload.putExtra(EXTRA_RESULT_PAYLOAD,  rawResult.getText());
        setResult(Activity.RESULT_OK, payload);

        new Handler().postDelayed(this::finish, 800);
    }
}