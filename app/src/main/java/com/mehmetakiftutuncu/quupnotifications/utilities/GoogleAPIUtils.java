package com.mehmetakiftutuncu.quupnotifications.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.orhanobut.logger.Logger;

public class GoogleAPIUtils {
    public static final int GOOGLE_PLAY_SERVICES_REQUEST_CODE = 1;

    public static void checkGooglePlayServices(final Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int availabilityResult = googleApiAvailability.isGooglePlayServicesAvailable(activity);

        if (availabilityResult == ConnectionResult.SUCCESS) {
            return;
        }

        if (googleApiAvailability.isUserResolvableError(availabilityResult)) {
            Dialog errorDialog = googleApiAvailability.getErrorDialog(activity, availabilityResult, GOOGLE_PLAY_SERVICES_REQUEST_CODE, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Logger.d("Dialog cancelled.");
                    activity.finish();
                }
            });

            errorDialog.setCanceledOnTouchOutside(false);
            errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override public void onDismiss(DialogInterface dialogInterface) {
                    Logger.d("Dialog dismissed.");
                    activity.finish();
                }
            });

            errorDialog.show();
        } else {
            Logger.d("Irrecoverable error, device is not supported!");
        }
    }
}
