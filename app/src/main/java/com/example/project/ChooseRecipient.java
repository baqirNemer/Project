package com.example.project;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ChooseRecipient extends AppCompatActivity {
    protected void onCreate(Bundle SavedInstance){
        super.onCreate(SavedInstance);
        setContentView(R.layout.choose_recipient_layout);

        Intent intent = getIntent();
        String sms = intent.getStringExtra("SMS");

        TextView phone = findViewById(R.id.PhoneNumber);
        TextView send = findViewById(R.id.Send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phone.getText().toString().trim();
                sendSMS(phoneNumber, sms);
            }
        });

    }
    private void sendSMS(String number, String sms){
        if (TextUtils.isEmpty(sms)){
            Toast.makeText(this, "No SMS Message Entered", Toast.LENGTH_LONG).show();
        }
        else{
            if(ContextCompat.checkSelfPermission(
                    getApplicationContext(), android.Manifest.permission.SEND_SMS) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ChooseRecipient.this, new
                        String[]{android.Manifest.permission.SEND_SMS}, 0);
            } else {
                BroadcastReceiver sentReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        switch (getResultCode()) {
                            case ChooseRecipient.RESULT_OK:
                                Toast.makeText(context, "SMS Sent", Toast.LENGTH_LONG).show();
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                Toast.makeText(context, "SMS Failed - Generic Failure", Toast.LENGTH_LONG).show();
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                Toast.makeText(context, "SMS Failed - No Service", Toast.LENGTH_LONG).show();
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                Toast.makeText(context, "SMS Failed - Null PDU", Toast.LENGTH_LONG).show();
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                Toast.makeText(context, "SMS Failed - Radio Off", Toast.LENGTH_LONG).show();
                                break;
                        }
                        unregisterReceiver(this);
                    }
                };
                registerReceiver(sentReceiver, new IntentFilter("SMS_SENT"), Context.RECEIVER_EXPORTED);
                Intent sentIntent = new Intent("SMS_SENT");
                PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, sentIntent, PendingIntent.FLAG_IMMUTABLE);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, sms, sentPI, null);
            }
        }
    }
}
