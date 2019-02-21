package models;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.himagepickerlibrary.hImagePicker.ClassIImagesPick;

/**
 * Created by hassanjamil on 3/29/2018.
 *
 * @author hassanjamil
 */

public class ConfigIPicker {

    private int requestCodeCamera;
    private int requestCodeGallery;
    private Activity activity;
    private Fragment fragment;
    private String filesDirPath;
    private String dialogTitle, dialogStrCamera, dialogStrGallery;
    private ClassIImagesPick.ImagePick listener;

    /**
     *
     * @param requestCodeCamera RequestCode for camera native interface intent
     * @param requestCodeGallery RequestCode for gallery native interface intent
     * @param activity Activity from where you want to call the functionality
     * @param dTitle Picker Dialog Title
     * @param dStrCamera Picker Dialog Camera option string
     * @param dStrGallery Picker Dialog Camera option string
     * @param filesDirPath
     * @param listener
     */
    public ConfigIPicker(int requestCodeCamera, int requestCodeGallery,
                         Activity activity, String dTitle, String dStrCamera, String dStrGallery,
                         String filesDirPath, ClassIImagesPick.ImagePick listener) {
        this.requestCodeCamera = requestCodeCamera;
        this.requestCodeGallery = requestCodeGallery;
        this.activity = activity;
        this.dialogTitle = dTitle;
        this.dialogStrCamera = dStrCamera;
        this.dialogStrGallery = dStrGallery;
        this.filesDirPath = filesDirPath;
        this.listener = listener;
    }

    public ConfigIPicker(int requestCodeCamera, int requestCodeGallery,
                         Fragment fragment,  String dTitle, String dStrCamera, String dStrGallery,
                         String filesDirPath, ClassIImagesPick.ImagePick listener) {
        this.requestCodeCamera = requestCodeCamera;
        this.requestCodeGallery = requestCodeGallery;
        this.fragment = fragment;
        this.dialogTitle = dTitle;
        this.dialogStrCamera = dStrCamera;
        this.dialogStrGallery = dStrGallery;
        this.filesDirPath = filesDirPath;
        this.listener = listener;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public int getRequestCodeCamera() {
        return requestCodeCamera;
    }

    public void setRequestCodeCamera(int requestCodeCamera) {
        this.requestCodeCamera = requestCodeCamera;
    }

    public Context getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getFilesDirPath() {
        return filesDirPath;
    }

    public void setFilesDirPath(String filesDirPath) {
        this.filesDirPath = filesDirPath;
    }

    public ClassIImagesPick.ImagePick getListener() {
        return listener;
    }

    public void setListener(ClassIImagesPick.ImagePick listener) {
        this.listener = listener;
    }

    public int getRequestCodeGallery() {
        return requestCodeGallery;
    }

    public void setRequestCodeGallery(int requestCodeGallery) {
        this.requestCodeGallery = requestCodeGallery;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public String getDialogStrCamera() {
        return dialogStrCamera;
    }

    public void setDialogStrCamera(String dialogStrCamera) {
        this.dialogStrCamera = dialogStrCamera;
    }

    public String getDialogStrGallery() {
        return dialogStrGallery;
    }

    public void setDialogStrGallery(String dialogStrGallery) {
        this.dialogStrGallery = dialogStrGallery;
    }
}
