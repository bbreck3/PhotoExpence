package com.example.photoexpence;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {


    private Uri fileUri; // file url to store image/video

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Receipts";
    //private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

    private VideoView videoPreview;
    private GridView grid;
    //private GridviewAdapter
    private File[] listFile;
    private File file;

    public String[] FilePathStrings;
    public String[] FileNameStrings;
    private Button btnCapturePicture;
    private  Button openCamera;
    private SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_main);
        loadPictures();
        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipe);
        /**swipeContainer.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);*/
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadPictures();
                        swipeContainer.setRefreshing(false);
                    }
                }, 3000);
            }
        });








        openCamera = (Button) findViewById(R.id.btnPhoto);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Context context = getApplicationContext();
                //Intent intent = new Intent(context,CameraActivity.class);
                //startActivity(intent);
                captureImage();

            }
        });

        } // end on create

    public void loadPictures(){

        file = new File(Environment.getExternalStorageDirectory() +File.separator + "Pictures"+ File.separator + "Receipts");
        if(file.isDirectory()){
            listFile = file.listFiles();
            // Log.d("String num files: ", Integer.toString(listFile.length));
            FilePathStrings = new String[listFile.length];
            FileNameStrings = new String[listFile.length];
            for(int i=0; i<listFile.length; i++){
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                //Log.d("File PAth: ", FilePathStrings[i].toString());
                FileNameStrings[i] = listFile[i].getName();
                //Log.d("File Name: ", FileNameStrings[i].toString());
            }
        }

        //Log.d("FileNameStrings Size ", Integer.toString(FileNameStrings.length));
        // Log.d("FilePathStrings Size ", Integer.toString(FilePathStrings.length));

        for(File file: listFile) {
            // Log.d("File Path ", file.getAbsoluteFile().toString());
            // Log.d("File Name ", file.getName());

        }


        GridView gv = (GridView) findViewById(R.id.gridview);
        ImageAdapter adapter = new ImageAdapter(this, FilePathStrings, FileNameStrings);
        gv.setAdapter(adapter);//new ImageAdapter(this, FilePathStrings, FileNameStrings));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, ViewImage.class);
                i.putExtra("filepath", FilePathStrings);
                i.putExtra("filename", FileNameStrings);
                i.putExtra("position", position);
                startActivity(i);
            }
        });
    }




    /*
        * Capturing Camera Image will lauch camera app requrest image capture
        */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                                                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        Log.d("File Uri: ", fileUri.toString());

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // video successfully recorded
                // preview the recorded video

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /*
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {

        try {
            // hide video preview


            //imgPreview.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 2;

            Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            // bitmap.setHeight(400);
            //bitmap.setWidth(400);



            //imgPreview.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 1000, 1000, false));
            //.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        /*try {
            // hide video preview


            imgPreview.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

	/*
	 * Creating file uri to store image/video
	 */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }



}



/***
    ImageAdapter

 **/


/**
 * Created by breck on 8/19/2016.
 */
