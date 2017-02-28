package edu.washington.vicky37.are_we_there_yet_2;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final int INTENT_ID = 1;
    private EditText message;
    private static EditText phoneNumber;
    private EditText minutes;
    private static Button button;
    public PendingIntent pendingIntent;
    private boolean validInput;
    private boolean validPhone;
    private boolean validInterval;
    private boolean Started;
    public static String phoneNumberSMS;
    public static String messageSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Started = isStarted();
        message = (EditText) findViewById(R.id.message);
        phoneNumber = (EditText) findViewById(R.id.phone);
        minutes = (EditText) findViewById(R.id.minutes);
        button = (Button) findViewById(R.id.button);

        button.setText("Start");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                validateInput();
                if(validInput&&!Started){
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.SEND_SMS)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                Manifest.permission.SEND_SMS)) {
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.SEND_SMS},
                                    MY_PERMISSIONS_REQUEST_SEND_SMS);
                        }
                    }else {
                        Started = true;
                        button.setText("Stop");
                        intent.putExtra("alarm", "Texting" + phoneNumber.getText() + " :  " + message.getText());
                        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, INTENT_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                        phoneNumberSMS = phoneNumber.getText().toString();
                        messageSMS = message.getText().toString();
                        start(phoneNumberSMS, messageSMS);
                    }
                }else if(Started){
                    Started = false;
                    button.setText("Start");
                    stop();
                }else if(!validPhone && validInterval){
                    Toast.makeText(MainActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }else if(validPhone && !validInterval){
                    Toast.makeText(MainActivity.this, "Invalid Interval", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "No controls is filled out with legitimate values", Toast.LENGTH_SHORT).show();
                }
                phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
            }
        });
    }

    public Boolean isStarted(){
        boolean start = (PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(MainActivity.this, AlarmReceiver.class), PendingIntent.FLAG_NO_CREATE) != null);
        return start;
    }

    private void validateInput() {
        String phone = phoneNumber.getText().toString();
        String interval = minutes.getText().toString();
        validPhone = phone.matches("\\d{3}\\d{3}\\d{4}");
        validInterval = !interval.isEmpty() && !interval.matches("0+");
        validInput = validPhone && validInterval;
    }


    private void start(String phoneNumberSMS, String messageSMS) {
        int interval = Integer.parseInt(minutes.getText().toString()) * 1000 * 60;
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage(phoneNumberSMS, null, messageSMS, null, null);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(MainActivity.this, "Sent ", Toast.LENGTH_SHORT).show();
    }

    private void stop() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        pendingIntent.cancel();
        Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
