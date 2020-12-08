package com.example.attendencemonitor.activity.student;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendencemonitor.R;
import com.example.attendencemonitor.activity.base.BaseMenuActivity;
import com.example.attendencemonitor.service.UserService;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IUserService;
import com.example.attendencemonitor.service.model.UserModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

public class StudentHomeActivity extends BaseMenuActivity
{
    private final IUserService userService =  new UserService();
    private ImageView imageView;
    private TextView tv_StudentName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        initializeMenu("Student", false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        imageView = findViewById(R.id.iv_student_id);
        tv_StudentName = findViewById(R.id.tv_student_name);


        //request from backend, and register the callback handling the response
        userService.getCurrentUser(new UserCallback());
    }

    /***
     * Callback for response of backend on Get user
     */
    private class UserCallback implements ICallback<UserModel>
    {
        @Override
        public void onSuccess(UserModel user)
        {
            tv_StudentName.setText(user.getFullName());
            createQrCode(user.getCode());
        }

        @Override
        public void onError(Throwable error)
        {
            Toast.makeText(StudentHomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /***
     * create qr code with payload
     * @param data payload that should be encoded into qr code
     */
    private void createQrCode(String data)
    {
        try
        {
            Bitmap bitmap = TextToImageEncode(data);
            imageView.setImageBitmap(bitmap);
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }

    }

    /***
     * helper method to create bitmap of qrcode
     * @param text payload in qr code
     * @return btimap
     * @throws WriterException
     */
    private Bitmap TextToImageEncode(String text) throws WriterException
    {
        final int WIDTH = 200;
        final int HEIGHT = 200;
        BitMatrix result;
        try
        {

            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.MARGIN, 1);
            result = new MultiFormatWriter().encode(String.valueOf(text), BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
        }
        catch (IllegalArgumentException iae)
        {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++)
        {
            int offset = y * width;
            for (int x = 0; x < width; x++)
            {
                pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.QRBlack, getTheme()) : getResources().getColor(R.color.QRWhite, getTheme());
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}