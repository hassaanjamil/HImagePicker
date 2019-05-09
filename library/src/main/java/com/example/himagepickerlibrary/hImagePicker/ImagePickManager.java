package com.example.himagepickerlibrary.hImagePicker;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.esafirm.imagepicker.features.ImagePicker;
import com.example.himagepickerlibrary.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;

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

    private void dialogImageSourceSelection(final Activity activity, final @NonNull ConfigIPicker config) {

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
        alertDialogBuilder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        openCameraIntent();
                        break;
                    case 1:
                        openGalleryIntent();
                        break;
                }
                mAlertDialog.dismiss();
            }
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
                .folderMode(false) // set folder mode (false by default)
                .includeVideo(false) // include video (false by default)
                .toolbarArrowColor(Color.WHITE) // set toolbar arrow up color
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .multi() // multi image selection mode
                .limit(mConfig.isSingleTrue() ? 1 : mConfig.limit())// max images can be selected (99 by default)
                .showCamera(mConfig.showCamera()); // show camera or not (true by default)
        //.imageDirectory(mConfig.dirName());   // captured image directory name ("Camera" folder by default)
        //.imageFullDirectory(mConfig.dirPath()); // can be full path
        //.imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
        //.imageFullDirectory(Environment.getExternalStorageDirectory().getPath()); // can be full path

        /*if (mConfig.include())
            imagePicker.origin(mConfig.images()); // original selected images, used in multi mode
        else
            imagePicker.exclude(mConfig.images());*/

        return imagePicker;
    }

    private void openCameraIntent() {

        if (mConfig.getFragment() == null) {
            ImagePicker.cameraOnly()
                    .imageFullDirectory(mConfig.dirPath())
                    .start(mConfig.getActivity());
        } else {
            ImagePicker.cameraOnly()
                    .imageFullDirectory(mConfig.dirPath())
                    .start(mConfig.getFragment());
        }

        /*Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(activity.getPackageManager()) != null) {

            File photoFile;
            try {
                photoFile = createImageFile(activity);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(activity,
                    activity.getPackageName() + ".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            activity.startActivityForResult(pictureIntent, requestCodeCameraIntent);
        }*/
    }

    /*private String mCameraImageFilePath;

    private File createImageFile(Context context) throws IOException {
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(FileUtils.getNewFileName("image_", ""), ".jpg", storageDir);
        mCameraImageFilePath = image.getAbsolutePath();

        return image;
    }*/

    private void openGalleryIntent() {
        getImagePicker().start(HImagePicker.RC_IMAGE_PICKER);

        /*Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity) context).startActivityForResult(intent, rcGallery);*/
    }

    private String croppedDestinationPath;

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Initiating paths array
        String[] paths;

        // Preparing paths array from image pick results
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            mConfig.images().clear();
            mConfig.images().addAll(ImagePicker.getImages(data));

            paths = new String[mConfig.isSingleTrue() ? 1 : mConfig.images().size()];

            for (int i = 0; i < mConfig.images().size(); i++) {
                paths[i] = mConfig.images().get(i).getPath();
            }

            if (!mConfig.isCropMode()) {
                ClassIImagesPick.getInstance().onImagesPicked(requestCode, resultCode, mConfig.images());
            }

            // Added crop mode in case of set single true
            if (mConfig.isSingleTrue() && mConfig.isCropMode()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(new File(paths[0]).getAbsolutePath(), options);

                // Taking cropped destination path globally so can be returned in successful cropping
                // case.
                croppedDestinationPath = getDestinationUri(paths[0]);

                // Start Cropping
                UCrop.of(Uri.fromFile(new File(paths[0])), Uri.fromFile(new File(croppedDestinationPath)))
                        //.withAspectRatio(16, 9)
                        .withMaxResultSize(options.outWidth, options.outHeight)
                        .start(mConfig.getActivity());
            } else if (mConfig.isCropMode()) {
                Log.e("HImagePicker", "Call ConfigIPicker.setSingleTrue(true) to enable crop mode.");
            }
            //ClassIImagesPick.getInstance().onImagesPicked(requestCode, resultCode, paths);
        }

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            // Overwriting crop image path to the array because user should have return the
            // cropped image path instead of original if he enable the crop mode
            File fileCropped = new File(croppedDestinationPath);
            mConfig.images().get(0).setPath(fileCropped.getPath());
            mConfig.images().get(0).setName(fileCropped.getName());
            ClassIImagesPick.getInstance().onImagesPicked(requestCode, resultCode, mConfig.images());
        }



        /*if(mConfig == null || (requestCode != mConfig.getRcCamera()
                && requestCode != mConfig.getRcGallery())) {
            return;
        }

        try {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = new Bundle();

                bundle.putString(KEY_FILE_URI,
                        (requestCode == mConfig.getRcCamera()) ? mCameraImageFilePath :
                                ((data != null && data.getData() != null) ? data.getData().toString()
                                        : null));
                ClassIImagesPick.getInstance().onImagesPicked(requestCode, resultCode, bundle);
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable to retrieve image");
            e.printStackTrace();
        }*/
    }

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.onRequestPermissionResult(mConfig.getActivity(), requestCode, permissions, grantResults);

        if (PermissionUtils.isStorageCameraPermissionGranted(mConfig.getActivity())) {
            dialogImageSourceSelection(mConfig.getActivity(), mConfig);
        }
    }

    private String getDestinationUri(String path) {
        int indexExt = path.indexOf(".", path.length() - 6);
        String substringExt = path.substring(indexExt);
        path = path.replace(substringExt,
                "-cropped-" + StringUtils.getCurrentTimeStampInFormat("YYYYMMDD-HHmmss")
                        + substringExt);
        return path;
    }
}
