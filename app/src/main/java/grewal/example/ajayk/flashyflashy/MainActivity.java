package grewal.example.ajayk.flashyflashy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private CameraManager mCameraManager;
    private ImageView mImageView;
    private Boolean mIsTorchOn;
    private String mCameraId;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.but_on_off);
        mIsTorchOn = false;

        Boolean mIsFlashAvailable = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if(!mIsFlashAvailable){
            AlertDialog mAlertDialog = new AlertDialog.Builder(MainActivity.this).create();
            mAlertDialog.setTitle("Error");
            mAlertDialog.setMessage("Flash Unavailable");
            mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    System.exit(0);
                }
            });
            mAlertDialog.show();
            return;
        }

        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId =mCameraManager.getCameraIdList()[0];
        }
        catch (CameraAccessException e) {
            e.printStackTrace();
        }

        mImageView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if(mIsTorchOn){
                    turnOff();
                    mIsTorchOn = false;
                }
                else{
                    turnOn();
                    mIsTorchOn = true;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void turnOn() {
        try {
            mCameraManager.setTorchMode(mCameraId, true);
            mImageView.setImageResource(R.drawable.light_off);
        }
        catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void turnOff() {
        try {
            mCameraManager.setTorchMode(mCameraId,false);
            mImageView.setImageResource(R.drawable.light_on);
        }
        catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStop() {
        super.onStop();
        if(mIsTorchOn){
            turnOff();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if(mIsTorchOn){
            turnOn();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onPause() {
        super.onPause();
        if(mIsTorchOn){
            turnOff();
        }
    }
}
