package imageLoader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.media.ExifInterface;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageProcessor {

    public ImageProcessor() {
    }

    @SuppressWarnings("unused")
    public static int getScale(int originalWidth, int originalHeight,
                               final int requiredWidth, final int requiredHeight) {
        //a scale of 1 means the original dimensions
        //of the image are maintained
        int scale = 1;

        //calculate scale only if the height or width of
        //the image exceeds the required value.
        if ((originalWidth > requiredWidth) || (originalHeight > requiredHeight)) {
            //calculate scale with respect to
            //the smaller dimension
            if (originalWidth < originalHeight)
                scale = Math.round((float) originalWidth / requiredWidth);
            else
                scale = Math.round((float) originalHeight / requiredHeight);

        }

        return scale;
    }


    @SuppressWarnings("unused")
    public Bitmap getResizedImageFromNetworkIfNeeded(String imgPath, InputStream inputStream, int desiredWidth, int desiredHeight) {
        Bitmap image;

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(inputStream, null, options);

        int imageWidth = options.outWidth;

        int imageHeight = options.outHeight;

        int scaleFactor = Math.min(imageWidth / desiredWidth, imageHeight / desiredHeight);

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;

        try {
            URL url = new URL(imgPath);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            inputStream = connection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        image = BitmapFactory.decodeStream(inputStream, null, options);

        System.out.println("Image = " + image);

        return image;
    }

    @SuppressWarnings("unused")
    public Bitmap getResizedImageIfNeeded(String imagePath, int desiredWidth, int desiredHeight) {
        Bitmap image;

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imagePath, options);

        int imageWidth = options.outWidth;

        int imageHeight = options.outHeight;

        int scaleFactor = Math.min(imageWidth / desiredWidth, imageHeight / desiredHeight);

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;

        //if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
        // options.inPurgeable = true;

        image = BitmapFactory.decodeFile(imagePath, options);

        return rotateImageIfNeeded(image, imagePath);
    }

    public Bitmap rotateImageIfNeeded(Bitmap image, String imagePath) {
        try {
            ExifInterface exifInterface = new ExifInterface(imagePath);

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(image, 90);

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(image, 180);

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(image, 270);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rotateImage(image, 0);
    }

    public static Bitmap scaleAndRotate(Bitmap bitmap, int maxDim, int rotation) {
        float scale = 1;
        if (bitmap.getWidth() > maxDim && bitmap.getWidth() > bitmap.getHeight())
            scale = maxDim * 1.0f / bitmap.getWidth();
        else if (bitmap.getHeight() > maxDim)
            scale = maxDim * 1.0f / bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        matrix.postRotate(rotation);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    public static int getImageOrientation(String imagePath) {
        int rotate = 0;
        try {

            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }

    private Bitmap rotateImage(Bitmap image, int rotation) {
        Matrix matrix = new Matrix();

        if (rotation != 0) {
            matrix.preRotate(rotation);
        }

        return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
    }

// --Commented out by Inspection START (2017-12-28 7:07 PM):
//    public static Bitmap getScaledBitmapFromPath(Context context, String path) {
//        Bitmap scaledBitmap;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        options.inSampleSize = 4;        //Modified by Hassan
//        try {
//            BitmapFactory.decodeStream(context.getContentResolver()
//                    .openInputStream(Uri.parse(path)), null, options);
//        } catch (FileNotFoundException e1) {
//            e1.printStackTrace();
//            return null;
//        }
//
//        int srcWidth = options.outWidth;
//        int scale = 1;
//        while (srcWidth / 2 > 60) {
//            srcWidth /= 2;
//            scale *= 2;
//        }
//
//        options.inJustDecodeBounds = false;
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
//            //noinspection deprecation
//            options.inDither = false; // Was deprecated
//        }
//        options.inSampleSize = scale;
//
//        try {
//            scaledBitmap = BitmapFactory.decodeStream(context
//                    .getContentResolver().openInputStream(Uri.parse(path)), null, options);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        return scaledBitmap;
//    }
// --Commented out by Inspection STOP (2017-12-28 7:07 PM)

    public static Bitmap getScaledBitmapFromUri(Context context, Uri uriImageFile) {
        Bitmap scaledBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 4;        //Modified by Hassan
        try {
            BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(uriImageFile), null, options);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return null;
        }

        int srcWidth = options.outWidth;
        int scale = 1;
        while (srcWidth / 2 > 60) {
            srcWidth /= 2;
            scale *= 2;
        }

        options.inJustDecodeBounds = false;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            //noinspection deprecation
            options.inDither = false; // Was deprecated
        }
        options.inSampleSize = scale;

        try {
            scaledBitmap = BitmapFactory.decodeStream(context
                    .getContentResolver().openInputStream(uriImageFile), null, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return scaledBitmap;
    }

    public static void saveBitmap(Uri uriImageFile, Bitmap bitmap) {
        File file = new File(uriImageFile.getPath());
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

// --Commented out by Inspection START (2017-12-28 7:07 PM):
//    public static Bitmap decodeSampledBitmapFromResource(Resources res, Bitmap bitmap,
//                                                         int reqWidth, int reqHeight) {
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        //BitmapFactory.decodeResource(res, resId, options);
//
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
//        byte[] bitmapdata = bos.toByteArray();
//        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
//        BitmapFactory.decodeStream(bs);
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeStream(bs);
//    }
// --Commented out by Inspection STOP (2017-12-28 7:07 PM)


}