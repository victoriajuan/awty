package edu.washington.vicky37.are_we_there_yet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by victoriajuan on 2/21/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("alarm");
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
