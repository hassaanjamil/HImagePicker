package utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hassanjamil on 3/27/2018.
 *
 * @author hassanjamil
 */

public class FileUtils {
    public static String getNewFileName(String prefix, String extension) {
        String fileName;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat s = new SimpleDateFormat("ddMMyyyy_HHmmss");
        String currentDateTime = s.format(new Date());
        fileName = prefix + currentDateTime + extension;
        return fileName;
    }
}
