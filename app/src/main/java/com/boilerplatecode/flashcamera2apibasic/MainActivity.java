package com.boilerplatecode.flashcamera2apibasic;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnEnable;
    private ImageView imageFlashLight;
    private static final int CAMERA_REQUEST = 50;
    private boolean flashLightStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageFlashLight = findViewById(R.id.imageFlashLight);
        btnEnable = findViewById(R.id.buttonEnable);

        final boolean hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        boolean isEnabled = ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        btnEnable.setEnabled(!isEnabled);
        imageFlashLight.setEnabled(isEnabled);
        btnEnable.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
                                         }


                                     }


        );


        imageFlashLight.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (hasCameraFlash) {

                    if (flashLightStatus) {
                        flashLightOff();
                    } else {
                        flashLightOn();
                    }


                } else {
                    Toast.makeText(MainActivity.this, "No flash on your device", Toast.LENGTH_SHORT).show();


                }
            }
        });
    }
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];

            cameraManager.setTorchMode(cameraId,true);
            flashLightStatus = true;
            imageFlashLight.setImageResource(R.drawable.btn_switch_on);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void flashLightOff() {

        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];

            cameraManager.setTorchMode(cameraId,false);
            flashLightStatus = false;
            imageFlashLight.setImageResource(R.drawable.btn_switch_off);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case CAMERA_REQUEST:
                if (grantResults.length>0  && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                 btnEnable.setEnabled(false);
                    btnEnable.setText("Camera Enabled");
                    imageFlashLight.setEnabled(true);
                }
                else

                {

                    Toast.makeText(MainActivity.this, "Permission Denied for Camera", Toast.LENGTH_SHORT).show();




                }

                break;
        }
    }
}


