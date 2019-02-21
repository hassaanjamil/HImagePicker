package imageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

class Utils {
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public static String imageToBase64String(Context context, Uri image_path, int desiredWidth, int desiredHeight)
            throws FileNotFoundException {
        String strImageBase64;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(image_path), null, options);
        int srcWidth = options.outWidth;
        int scale = 1;

        while (srcWidth / 2 > desiredWidth) {
            srcWidth /= 2;
            scale *= 2;
        }
        options.inJustDecodeBounds = false;
//        options.inDither = false; // Was deprecated.
        options.inSampleSize = scale;
        Bitmap sampledSrcBitmap = BitmapFactory
                .decodeStream(context.getContentResolver().openInputStream(image_path), null, options);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(sampledSrcBitmap, desiredWidth, desiredHeight, false);
        scaledBitmap = new ImageProcessor().rotateImageIfNeeded(scaledBitmap, image_path.getPath());
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] ba = bao.toByteArray();
        strImageBase64 = Base64.encodeToString(ba, Base64.DEFAULT);
        return strImageBase64;
    }

    @SuppressWarnings("unused")
    public static Bitmap getBitmapFromUri(Context context, Uri image_path, int desiredWidth, int desiredHeight)
            throws FileNotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(image_path), null, options);
        int srcWidth = options.outWidth;
        int scale = 1;

        while (srcWidth / 2 > desiredWidth) {
            srcWidth /= 2;
            scale *= 2;
        }
        options.inJustDecodeBounds = false;
//        options.inDither = false;
        options.inSampleSize = scale;
        Bitmap sampledSrcBitmap = BitmapFactory
                .decodeStream(context.getContentResolver().openInputStream(image_path), null, options);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(sampledSrcBitmap, desiredWidth, desiredHeight, false);
        scaledBitmap = new ImageProcessor().rotateImageIfNeeded(scaledBitmap, image_path.getPath());
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        return scaledBitmap;
    }
}