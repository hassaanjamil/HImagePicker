package com.example.himagepickerlibrary.hImagePicker;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.himagepickerlibrary.R;

import java.io.File;
import java.io.IOException;

import models.ConfigIPicker;
import utils.FileUtils;
import utils.StringUtils;

/**
 * Created by hassanjamil on 3/27/2018.
 *
 * @author hassanjamil
 */

public class ImagePickManager {

    private static final String TAG = ImagePickManager.class.getSimpleName();
    private AlertDialog mAlertDialog;
    public static final String KEY_FILE_URI = "file_uri";

    private ConfigIPicker mLastAddedConfig;

    ImagePickManager() {}

    void load(@NonNull ConfigIPicker config) {

        mLastAddedConfig = config;

        /*if (!(mLastAddedConfig.getContext() instanceof Activity)) {
            Log.e(TAG, "Context is not an instance of Activity class");
        }*/

        Context context = (mLastAddedConfig.getActivity() != null) ? mLastAddedConfig.getActivity()
                : mLastAddedConfig.getFragment().getContext();

        if (checkPermissions(context)) {
            dialogImageSourceSelection(context, config);
        }
    }

    private boolean checkPermissions(Context context) {
        if (!(context instanceof Activity)) {
            Log.e(TAG, "Context is not an instance of Activity class");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!(context instanceof Activity)) {
                return false;
            }

            if (!PermissionUtils.isStoragePermissionGranted((Activity) context)) {
                PermissionUtils.requestStoragePermission((Activity) context);
                return false;
            }
            if (!PermissionUtils.isCameraPermissionGranted((Activity) context)) {
                PermissionUtils.requestCameraPermission((Activity) context);
                return false;
            }
        }
        return true;
    }

    private void dialogImageSourceSelection(final Context context, final @NonNull ConfigIPicker config) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionUtils.isStoragePermissionGranted((Activity) context)) {
                PermissionUtils.requestStoragePermission((Activity) context);
                return;
            }
        }

        dismissAlertDialog();

        final CharSequence[] items = {StringUtils.isValidString(config.getDialogStrCamera()) ?
                        config.getDialogStrCamera() : context.getString(R.string.str_camera),
                StringUtils.isValidString(config.getDialogStrGallery()) ?
                        config.getDialogStrGallery() : context.getString(R.string.str_gallery)};

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(StringUtils.isValidString(config.getDialogTitle()) ?
                config.getDialogTitle() : context.getString(R.string.str_pick_image_from));
        alertDialog.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        openCameraIntent(context, config.getRequestCodeCamera());
                        break;
                    case 1:
                        openGalleryIntent(context, config.getRequestCodeGallery());
                        break;
                }
                mAlertDialog.dismiss();
            }
        });
        alertDialog.setCancelable(true);

        mAlertDialog = alertDialog.create();
        mAlertDialog.show();
    }

    private void dismissAlertDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

    private void openCameraIntent(Context context, int requestCodeCameraIntent) {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(context.getPackageManager()) != null) {

            File photoFile;
            try {
                photoFile = createImageFile(context);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            ((Activity) context).startActivityForResult(pictureIntent, requestCodeCameraIntent);
        }
    }

    private String mCameraImageFilePath;

    private File createImageFile(Context context) throws IOException {
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(FileUtils.getNewFileName("image_", ""), ".jpg", storageDir);
        mCameraImageFilePath = image.getAbsolutePath();

        return image;
    }

    private void openGalleryIntent(final Context context, int requestCodeGalleryIntent) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity) context).startActivityForResult(intent, requestCodeGalleryIntent);
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mLastAddedConfig == null || (requestCode != mLastAddedConfig.getRequestCodeCamera()
                && requestCode != mLastAddedConfig.getRequestCodeGallery())) {
            return;
        }

        try {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = new Bundle();

                bundle.putString(KEY_FILE_URI,
                        (requestCode == mLastAddedConfig.getRequestCodeCamera()) ? mCameraImageFilePath :
                                ((data != null && data.getData() != null) ? data.getData().toString()
                                        : null));
                ClassIImagesPick.getInstance().onImagesPicked(requestCode, resultCode, bundle);
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable to retrieve image");
            e.printStackTrace();
        }
    }
}
