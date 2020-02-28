package com.hadi.thenewsproject.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class CustomTabsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String url = intent.getDataString();

//        Toast.makeText(context, "Copy link pressed. URL = " + url, Toast.LENGTH_SHORT).show();
//
        PackageManager pm = context.getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");

            waIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
            waIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            String text = "This is  a Test"; // Replace with your own message.

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, url);
            Intent intent_ = Intent.createChooser(waIntent, "Share with");
            context.startActivity(intent_);

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();

        }
    }
}