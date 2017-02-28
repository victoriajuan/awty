package edu.washington.vicky37.are_we_there_yet_2;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by victoriajuan on 2/23/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsManager smsManager = SmsManager.getDefault();

        String message = intent.getStringExtra("alarm");
        String phonenumber = MainActivity.phoneNumberSMS;
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        smsManager.sendTextMessage(phonenumber, null, message, null, null);

    }

    public void sendMessage(String number, String message, Context context) {
        Intent sentIntent = new Intent("sent");
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        context.registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (getResultCode() == Activity.RESULT_OK) {
                    Toast.makeText(context, "Send Successful!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Send Failed...", Toast.LENGTH_LONG).show();
                }
            }
        }, new IntentFilter("sent"));

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, message, sentPI, null);
    }
}