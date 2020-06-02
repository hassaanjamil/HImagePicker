package com.example.himagepickerlibrary.hImagePicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.himagepickerlibrary.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import models.ConfigIPicker;
import utils.StringUtils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by hassanjamil on 3/27/2018.
 *
 * @author hassanjamil
 */

class ImagePickManager {

    private static final String TAG = ImagePickManager.class.getSimpleName();
    private AlertDialog mAlertDialog;
    //public static final String KEY_FILE_URI = "file_uri";

    private ConfigIPicker mConfig;

    private String croppedDestinationPath;

    ImagePickManager() {
    }

    void load(@NonNull ConfigIPicker config) {

        mConfig = config;

        /*if (!(mConfig.getContext() instanceof Activity)) {
            Log.e(TAG, "Context is not an instance of Activity class");
        }*/

        /*Context context = (mConfig.getActivity() != null) ? mConfig.getActivity()
                : mConfig.getFragment().getContext();*/

        if (checkPermissions(mConfig.getActivity())) {
            dialogImageSourceSelection(mConfig.getActivity(), config);
        }
    }

    private boolean checkPermissions(Context context) {
        if (!(context instanceof Activity)) {
            Log.e(TAG, "Context is not an instance of Activity class");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(context instanceof Activity)) {
                return false;
            }

            if (!PermissionUtils.isStorageCameraPermissionGranted((Activity) context)) {
                PermissionUtils.requestStorageCameraPermission((Activity) context);
                return false;
            }

        }
        return true;
    }

    private boolean optionSelected = false;
    private void dialogImageSourceSelection(final Activity activity, final @NonNull ConfigIPicker config) {
        optionSelected = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionUtils.isStorageCameraPermissionGranted(activity)) {
                PermissionUtils.requestStorageCameraPermission(activity);
                return;
            }
        }

        dismissAlertDialog();

        final CharSequence[] items = {StringUtils.isValidString(config.getDialogStrCamera()) ?
                config.getDialogStrCamera() : activity.getString(R.string.str_camera),
                StringUtils.isValidString(config.getDialogStrGallery()) ?
                        config.getDialogStrGallery() : activity.getString(R.string.str_gallery)};


        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity,
                (!StringUtils.isValidString(config.getLayoutDirection())
                        || config.getLayoutDirection().equals("ltr"))
                        ? R.style.AlertDialogLTR
                        : R.style.AlertDialogRTL);
        alertDialogBuilder.setTitle(StringUtils.isValidString(config.getDialogTitle()) ?
                config.getDialogTitle() : activity.getString(R.string.str_pick_image_from));
        alertDialogBuilder.setOnDismissListener(dialog -> ClassIImagesPick.getInstance().onDismissed(optionSelected));
        alertDialogBuilder.setOnCancelListener(dialog -> ClassIImagesPick.getInstance().onDismissed(optionSelected));
        alertDialogBuilder.setSingleChoiceItems(items, -1,
                (dialog, item) -> {
                    switch (item) {
                        case 0:
                            optionSelected = true;
                            openCameraIntent();
                            mConfig.setIsRequestFromGallery(false);
                            break;
                        case 1:
                            optionSelected = true;
                            openGalleryIntent();
                            mConfig.setIsRequestFromGallery(true);
                            break;
                    }
                    mAlertDialog.dismiss();
                });
        alertDialogBuilder.setCancelable(true);

        mAlertDialog = alertDialogBuilder.create();
        mAlertDialog.show();
    }

    private void dismissAlertDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

    private ImagePicker getImagePicker() {
        ImagePicker imagePicker;
        if (mConfig.getFragment() == null)
            imagePicker = ImagePicker.create(mConfig.getActivity());
        else
            imagePicker = ImagePicker.create(mConfig.getFragment());
        imagePicker.language("en") // Set image picker language
                .theme(R.style.ef_AppTheme)
                .folderMode(mConfig.isFolderModeTrue()) // set folder mode (false by default)
                .includeVideo(false) // include video (false by default)
                .toolbarArrowColor(Color.WHITE) // set toolbar arrow up color
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .multi() // multi image selection mode
                .limit(mConfig.isSingleTrue() ? 1 : mConfig.limit())// max images can be selected (99 by default)
                .showCamera(mConfig.showCamera()); // show camera or not (true by default)
        //.imageDirectory(mConfig.dirName());   // captured image directory name ("Camera" folder by default)
        //.imageFullDirectory(mConfig.dirPath()); // can be full path

        if (mConfig.getIncludeImages() != null && mConfig.getIncludeImages().size() > 0)
            imagePicker.origin(mConfig.getIncludeImages()); // original selected images, used in multi mode
        /*else
            imagePicker.exclude(mConfig.images());*/

        return imagePicker;
    }

    private void openCameraIntent() {

        if (!mConfig.isSingleTrue() && mConfig.getIncludeImages().size() >= mConfig.limit()) {
            Toast.makeText(mConfig.getActivity(), "Image selection limit exceeded", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (mConfig.getFragment() == null) {
            ImagePicker.cameraOnly()
                    .imageFullDirectory(mConfig.dirPath())
                    .start(mConfig.getActivity());
        } else {
            ImagePicker.cameraOnly()
                    .imageFullDirectory(mConfig.dirPath())
                    .start(mConfig.getFragment());
        }
    }

    private void openGalleryIntent() {
        getImagePicker().start(HImagePicker.RC_IMAGE_PICKER);
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<Image> images = new ArrayList<>();

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Collecting received images in our array list
            images = new ArrayList<>(ImagePicker.getImages(data));
            // Returning images if not crop mode
            if (!mConfig.isCropMode()) {
                ClassIImagesPick.getInstance().onImagesPicked(requestCode, resultCode, images, mConfig.isRequestFromGallery());
            }

            // Added crop mode in case of set single true
            if (mConfig.isSingleTrue() && mConfig.isCropMode() && images.size() > 0) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(new File(images.get(0).getPath()).getAbsolutePath(), options);

                // Taking cropped destination path globally so can be returned in successful cropping
                // case.
                croppedDestinationPath = getDestinationUri(images.get(0).getPath());

                // Run Media Scanner to scan the cropped file so that it will appear instantly in gallery
                new MyMediaScanner(mConfig.getActivity(), croppedDestinationPath);

                // Start Cropping
                UCrop.of(Uri.fromFile(new File(images.get(0).getPath())),
                        Uri.fromFile(new File(croppedDestinationPath)))
                        .withMaxResultSize(options.outWidth, options.outHeight)
                        .start(mConfig.getActivity());
            } else if (mConfig.isCropMode()) {
                Log.e("HImagePicker", "Call ConfigIPicker.setSingleTrue() to enable crop mode.");
            }
        }

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            // Overwriting crop image path to the array because user should have return the
            // cropped image path instead of original if he enable the crop mode
            File fileCropped = new File(croppedDestinationPath);
            images.add(new Image(new Random().nextInt(), fileCropped.getName(), fileCropped.getPath()));

            // Run Media Scanner to scan the cropped file so that it will appear instantly in gallery
            new MyMediaScanner(mConfig.getActivity(), croppedDestinationPath);

            ClassIImagesPick.getInstance().onImagesPicked(requestCode, resultCode, images, mConfig.isRequestFromGallery());
        }

        if (resultCode == 0 && data == null) {
            ClassIImagesPick.getInstance().onCancelled();
        }
    }

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.onRequestPermissionResult(mConfig.getActivity(), requestCode, permissions, grantResults);

        if (PermissionUtils.isStorageCameraPermissionGranted(mConfig.getActivity())) {
            dialogImageSourceSelection(mConfig.getActivity(), mConfig);
        }
    }

    private String getDestinationUri(String path) {
        String path2 = path;
        String substringExt = null;

        // if extensions exist
        while (path2.contains(".")) {
            int indexExt = path2.indexOf(".", 1);
            substringExt = path2.substring(indexExt);
            path2 = path2.replace(substringExt, "");
        }

        // if cropped name already exists in the file path/name
        String cropSubStr = "-cropped-";
        String croppedName;
        if (path2.contains(cropSubStr)) {
            int index = path2.indexOf(cropSubStr, 1);
            croppedName = path2.substring(index);
            path2 = path2.replace(croppedName, "");
        }

        croppedName = cropSubStr
                + StringUtils.getCurrentTimeStampInFormat("yyyymmdd-HHmmssSSS")
                + substringExt;

        path2 = path2 + croppedName;

        return path2;
    }
}
