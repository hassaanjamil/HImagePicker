package utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.himagepickerlibrary.R;
import com.example.himagepickerlibrary.hImagePicker.PermissionUtils;

public class DialogUtils {

    public static AlertDialog createAlertDialog(final Context context, @DrawableRes int resId,
                                                String title, String message, boolean cancelable,
                                                boolean showDialog, String[] buttonTexts,
                                                DialogInterface.OnClickListener listener) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        if (resId != 0)
            alertDialogBuilder.setIcon(resId);

        alertDialogBuilder.setCancelable(cancelable);

        if (StringUtils.isValidString(title))
            alertDialogBuilder.setTitle(title);

        if (StringUtils.isValidString(message))
            alertDialogBuilder.setMessage(message);

        if (buttonTexts != null && buttonTexts.length > 0 && listener != null)
            alertDialogBuilder.setPositiveButton(buttonTexts[0], listener);

        if (buttonTexts != null && buttonTexts.length > 1 && listener != null)
            alertDialogBuilder.setNegativeButton(buttonTexts[1], listener);

        if (buttonTexts != null && buttonTexts.length > 2 && listener != null)
            alertDialogBuilder.setNeutralButton(buttonTexts[2], listener);

        AlertDialog alertDialog = alertDialogBuilder.create();

        if (showDialog)
            alertDialog.show();

        return alertDialog;
    }

    public static MaterialDialog createProgressDialog(Context context, String title, String text,
                                                      boolean cancelable, boolean show) {

        MaterialDialog.Builder progressDialogBuilder = new MaterialDialog.Builder(context);

        if (StringUtils.isValidString(title))
            progressDialogBuilder.title(title);

        if (StringUtils.isValidString(text))
            progressDialogBuilder.content(text);

        progressDialogBuilder.progress(true, 0);
        progressDialogBuilder.cancelable(cancelable);
        MaterialDialog materialDialog = progressDialogBuilder.build();

        /*if (StringUtils.isValidString(context.getString(R.string.cancel)) && listener != null)
            materialDialog.on(context.getString(R.string.cancel), listener);*/

        if (cancelable) {
            materialDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.dismiss();
                }
            });
        }

        if (show && !materialDialog.isShowing())
            materialDialog.show();

        return materialDialog;
    }

    public static MaterialDialog createProgressDialog(Context context, String title, String text,
                                                      boolean cancelable, boolean show,
                                                      DialogInterface.OnCancelListener cancelListener,
                                                      DialogInterface.OnDismissListener dismissListener,
                                                      DialogInterface.OnKeyListener keyListener) {

        MaterialDialog progressDialog = createProgressDialog(context, title, text, cancelable, show);

        if (cancelListener != null)
            progressDialog.setOnCancelListener(cancelListener);

        if (dismissListener != null)
            progressDialog.setOnDismissListener(dismissListener);

        if (keyListener != null)
            progressDialog.setOnKeyListener(keyListener);

        return progressDialog;
    }

    public static void dialogReasonPermission(final Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(activity.getString(R.string.str_retry), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                PermissionUtils.requestStorageCameraPermission(activity);
            }
        });
        builder.setNegativeButton(activity.getString(R.string.dismiss), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void dialogReasonPermissionSettings(final Activity activity, String message, String[] buttons) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(buttons[0], new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AppUtils.goToAppDetailsForPermissionSettings(activity);
            }
        });
        builder.setNegativeButton(buttons[1], new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void dialogReasonLocationPermission(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.reason_location_permission));
        builder.setCancelable(false);
        builder.setPositiveButton(activity.getString(R.string.str_retry), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                PermissionUtils.requestLocationPermission(activity);
            }
        });
        builder.setNegativeButton(activity.getString(R.string.dismiss), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void dialogReasonLocationPermissionToSettings(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.reason_location_permission));
        builder.setCancelable(false);
        builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AppUtils.goToAppDetailsForPermissionSettings(activity);
            }
        });
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void dialogReasonRecordPermission(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.reason_record_permission));
        builder.setCancelable(false);
        builder.setPositiveButton(activity.getString(R.string.str_retry), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                PermissionUtils.requestStorageCameraPermission(activity);
            }
        });
        builder.setNegativeButton(activity.getString(R.string.dismiss), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
