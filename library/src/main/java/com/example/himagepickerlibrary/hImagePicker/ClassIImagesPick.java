package com.example.himagepickerlibrary.hImagePicker;

import android.os.Bundle;

import java.util.ArrayList;

public class ClassIImagesPick {

    public interface ImagePick {
        void onImagesPicked(int requestCode, int resultCode, Bundle bundle);
    }

    private static ClassIImagesPick mInstance;
    private ArrayList<ImagePick> mListeners = new ArrayList<>();

    private ClassIImagesPick() {
    }

    static ClassIImagesPick getInstance() {
        if (mInstance == null) {
            mInstance = new ClassIImagesPick();
        }
        return mInstance;
    }

    void addListener(ImagePick listener) {
        if (!isListener(listener)) {
            mListeners.add(listener);
        }
    }

    private boolean isListener(ImagePick listener) {
        for (int i = 0; i < mListeners.size(); i++) {
            if (mListeners.get(i).equals(listener)) {
                return true;
            }
        }
        return false;
    }

    void onImagesPicked(int requestCode, int resultCode, Bundle bundle) {

        ClassIImagesPick.ImagePick listener = null;

        if (mListeners != null && mListeners.size() > 0) {
            listener = mListeners.get(mListeners.size() - 1);
        }

        if (listener != null) {
            listener.onImagesPicked(requestCode, resultCode, bundle);
        }
    }

    void removeRecentListener() {
        if(mListeners != null && mListeners.size() > 0)
            mListeners.remove((mListeners.size() > 0) ? mListeners.size() - 1 : mListeners.size());
    }

    void onDestroy() {
        mListeners = null;
        mInstance = null;
    }

}