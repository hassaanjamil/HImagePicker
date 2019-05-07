package models;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.esafirm.imagepicker.model.Image;
import com.example.himagepickerlibrary.hImagePicker.ClassIImagesPick;

import java.util.ArrayList;

/**
 * Created by hassanjamil on 3/29/2018.
 *
 * @author hassanjamil
 */

public class ConfigIPicker {

    private Activity activity;
    private Fragment fragment;
    private String dialogTitle;
    private String dialogStrCamera;
    private String dialogStrGallery;
    private String layoutDirection;
    private ClassIImagesPick.ImagePick listener;

    private ArrayList<Image> images = new ArrayList<>();
    private String dirPath;
    private boolean showCamera;
    private int limit;
    private boolean include;

    /**
     * @param activity Activity from where you want to call the functionality
     */
    public ConfigIPicker(Activity activity) {
        this.activity = activity;
    }

    public ConfigIPicker(Fragment fragment) {
        this.fragment = fragment;
        setActivity(fragment.getActivity());
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
        setActivity(fragment.getActivity());
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ClassIImagesPick.ImagePick getListener() {
        return listener;
    }

    public ConfigIPicker setListener(ClassIImagesPick.ImagePick listener) {
        this.listener = listener;
        return this;
    }

    public String getLayoutDirection() {
        return layoutDirection;
    }

    public ConfigIPicker setLayoutDirection(String layoutDirection) {
        this.layoutDirection = layoutDirection;
        return this;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public ConfigIPicker setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
        return this;
    }

    public String getDialogStrCamera() {
        return dialogStrCamera;
    }

    public ConfigIPicker setDialogStrCamera(String dialogStrCamera) {
        this.dialogStrCamera = dialogStrCamera;
        return this;
    }

    public String getDialogStrGallery() {
        return dialogStrGallery;
    }

    public ConfigIPicker setDialogStrGallery(String dialogStrGallery) {
        this.dialogStrGallery = dialogStrGallery;
        return this;
    }

    public String dirPath() {
        return dirPath;
    }

    public ConfigIPicker setDirPath(String dirPath) {
        this.dirPath = dirPath;
        return this;
    }

    public boolean showCamera() {
        return showCamera;
    }

    public ConfigIPicker setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
        return this;
    }

    public int limit() {
        return limit;
    }

    public ConfigIPicker setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public boolean include() {
        return include;
    }

    public ConfigIPicker setInclude(boolean include) {
        this.include = include;
        return this;
    }

    public ArrayList<Image> images() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }
}
