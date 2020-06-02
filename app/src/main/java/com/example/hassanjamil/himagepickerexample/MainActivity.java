package com.example.hassanjamil.himagepickerexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.esafirm.imagepicker.model.Image;
import com.example.himagepickerlibrary.hImagePicker.ClassIImagesPick;
import com.example.himagepickerlibrary.hImagePicker.HImagePicker;

import java.util.ArrayList;

import models.ConfigIPicker;

public class MainActivity extends AppCompatActivity implements ClassIImagesPick.ImagePick {

    private TextView tvPickedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPickedImages = findViewById(R.id.tvPickedImages);
    }

    public void onPickClick(View view) {
        /*if(mImages.size() > 0)
            mImages.clear();*/
        ConfigIPicker config = new ConfigIPicker(MainActivity.this)
                // Call the function with translated string resource if working with multiple
                // locale and call setLayoutDirection("rtl" | "ltr") for rightToLeft or LeftToRight
                // layout direction of image source selection dialog
                .setLayoutDirection("ltr")
                .setDialogTitle("Pick Image via")
                .setDialogStrCamera("Camera")
                .setDialogStrGallery("Gallery")
                .setLayoutDirection("ltr")
                .setSingleTrue()
                .setCropModeTrue()
                .setFolderModeTrue()
                .setListener(this)
                .setShowCamera(false)
                .setDirPath(getCustomDirectoryPath(this));

        HImagePicker.getInstance().config(config).load();
    }

    private String getCustomDirectoryPath(Context context) {
        String externalStoragePath = ((android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED)))
                ? android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
                : context.getFilesDir().getAbsolutePath();
        return externalStoragePath + "/" + context.getString(R.string.app_name);
    }

    @Override
    public void onImagesPicked(int requestCode, int resultCode, ArrayList<Image> images, boolean isReqFromGallery) {
        StringBuilder p = new StringBuilder();
        for (Image image : images) {
            p.append("\n").append(image.getPath());
        }
        tvPickedImages.setText(p);
    }

    @Override
    public void onCancelled() {
        Log.d("MainActivity", "Cancelled");
    }

    @Override
    public void onDismissed(boolean optionSelected) {
        Log.d("MainActivity", "Dismissed " + optionSelected);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        HImagePicker.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HImagePicker.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onNextActivityClick(View view) {
        startActivity(new Intent(this, MyFragmentActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HImagePicker.getInstance().onDestroy();
    }
}
