package com.example.himagepickerlibrary.hImagePicker;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

import java.io.File;

public class MyMediaScanner implements MediaScannerConnectionClient {

    private MediaScannerConnection mMs;
    private File mFile;

    public MyMediaScanner(Context context, String path) {
        mFile = new File(path);
        mMs = new MediaScannerConnection(context, this);
        mMs.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mMs.scanFile(mFile.getAbsolutePath(), null);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mMs.disconnect();
    }
}
