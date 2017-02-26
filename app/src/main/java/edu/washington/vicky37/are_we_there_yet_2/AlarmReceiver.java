package edu.washington.vicky37.are_we_there_yet_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by victoriajuan on 2/23/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String message = intent.getStringExtra("alarm");
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(MainActivity.phoneNumberSMS, null, MainActivity.messageSMS, null, null);
    }
}