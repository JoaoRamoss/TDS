package com.firstapp.braguia.UI;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class EmergencyCall {

    public static void makeEmergencyCall(Context context) {
        final String emergencyPhoneNumber = "112";

        // Create a new AlertDialog.Builder to let the user confirm if he really wants to do this call
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chamada de emergência");
        builder.setMessage("Você quer realmente ligar para o número de emergência " + emergencyPhoneNumber + "?");

        // Set the positive button (Yes)
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Create a new Intent to call the emergency number
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + emergencyPhoneNumber));

                // Check if the device has the CALL_PHONE permission before starting the call, it's necessary because of security and privacy
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    context.startActivity(callIntent);
                } else {
                    // Request the CALL_PHONE permission if it's not already granted
                    ActivityCompat.requestPermissions(((Activity) context), new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
            }
        });

        // Set the negative button (No)
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
