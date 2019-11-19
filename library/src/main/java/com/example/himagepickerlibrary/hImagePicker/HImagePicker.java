package com.example.himagepickerlibrary.hImagePicker;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import models.ConfigIPicker;

/**
 * Created by hassanjamil on 3/27/2018.
 *
 * @author hassanjamil
 */

public class HImagePicker {

    private static HImagePicker mInstance;
    private ImagePickManager mManager;
    static final int RC_IMAGE_PICKER = 553;

    private HImagePicker() {
        mManager = new ImagePickManager();
    }

    public static HImagePicker getInstance() {
        if (mInstance == null) {
            mInstance = new HImagePicker();
        }
        return mInstance;
    }

    /*public HImagePicker listener(@NonNull ClassIImagesPick.ImagePick listener) {
        ClassIImagesPick.getInstance().addListener(listener);
        return this;
    }*/

    private ArrayList<ConfigIPicker> mConfigs = new ArrayList<>();

    public HImagePicker config(@NonNull ConfigIPicker config) {
        if (!isConfig(config))
            mConfigs.add(config);

        ClassIImagesPick.getInstance().addListener(config.getListener());

        return this;
    }

    private boolean isConfig(@NonNull ConfigIPicker config) {
        for (ConfigIPicker iConfig : mConfigs) {
            Context contextDest = (iConfig.getActivity() != null) ? iConfig.getActivity()
                    : iConfig.getFragment().getContext();
            Context contextSrc = (config.getActivity() != null) ? config.getActivity()
                    : config.getFragment().getContext();
            if (contextSrc.getClass().getSimpleName().equals(contextDest.getClass().getSimpleName()))
                return true;
        }
        return false;
    }

    // Set camera images storage path with package name dir will be in root external path
    /*public HImagePicker setFilesDir(String dir) {
        mManager.setFilesDir(mContext, dir);
        return this;
    }*/

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mManager.onActivityResult(requestCode, resultCode, data);
    }

    public void load() {
        mManager.load(mConfigs.get(mConfigs.size() - 1));
    }

    public void onDestroy() {
        clearAllConfigs();
        ClassIImagesPick.getInstance().onDestroy();
        mInstance = null;

        /*if(mConfigs != null && mConfigs.size() > 0)
            mConfigs.remove((mConfigs.size() > 0) ? mConfigs.size() - 1 : mConfigs.size());
        ClassIImagesPick.getInstance().onDestroy();*/
    }

    private void clearAllConfigs() {
        mConfigs.clear();
    }

}
