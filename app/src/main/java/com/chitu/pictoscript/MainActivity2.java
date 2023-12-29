package com.chitu.pictoscript;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

    public class MainActivity2 extends AppCompatActivity {
    Button frmcam, frmgallery, button3;
    ImageView imageView;
    public  Uri croppedImageUri;
    boolean camera_accepted;
    boolean writeStorageaccepted;
    boolean readStorageaccepted;

    String mCameraFileName;
    final static int REQUEST_CAMERA = 1;
    final static int REQUEST_GALLERY = 2;
    String[] cameraPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar actionBar = findViewById(R.id.actionbar);
        setSupportActionBar(actionBar);

        imageView = findViewById(R.id.imageView);
        button3 = findViewById(R.id.button3);
        frmcam = findViewById(R.id.from_camera);
        frmgallery = findViewById(R.id.from_gallery);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(croppedImageUri==null){
                    Toast.makeText(MainActivity2.this, "Image is not selected", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(MainActivity2.this,MainActivity3.class);
                    intent.putExtra("img",croppedImageUri);
                    startActivity(intent);
                }
            }
        });

        requestPermissions(cameraPermission, REQUEST_CAMERA);

        frmcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (camera_accepted && writeStorageaccepted && readStorageaccepted) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(MainActivity2.this, "Please enable camera and storage permissions", Toast.LENGTH_SHORT).show();
                    requestPermissions(cameraPermission, REQUEST_CAMERA);
                }
            }
        });

        frmgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickImgIntent = new Intent(Intent.ACTION_PICK);
                pickImgIntent.setType("image/*");
                startActivityForResult(pickImgIntent, REQUEST_GALLERY);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA){
                if (grantResults.length > 0) {
                   camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                   readStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                   writeStorageaccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                }
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            CropImage.activity(selectedImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Uri imageUri = null;

            if(data!=null){
                imageUri = data.getData();
            }
            if (imageUri == null && mCameraFileName != null) {
                imageUri = Uri.fromFile(new File(mCameraFileName));
            }
            File file = new File(mCameraFileName);
            if (!file.exists()) {
                file.mkdir();
            }
            if(imageUri != null){
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                croppedImageUri = result.getUri();
                imageView.setImageURI(croppedImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent takePictureIntent = new Intent();
        takePictureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        Locale locale = new Locale("English");

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("-mm-ss",locale);

        String newPicFile = df.format(date) + ".jpg";
        String outPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + newPicFile;
        File outfile = new File(outPath);

        mCameraFileName = outfile.toString();
        Uri outuri = Uri.fromFile(outfile);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
        startActivityForResult(takePictureIntent, REQUEST_CAMERA);

    }
}