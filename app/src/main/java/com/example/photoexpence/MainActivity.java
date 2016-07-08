package com.example.photoexpence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends Activity {







    private VideoView videoPreview;
    private Button btnCapturePicture;
    private  Button openCamera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openCamera= (Button)findViewById(R.id.btnPhoto);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context,CameraActivity.class);
                startActivity(intent);


                    }
                });

            }
    }

