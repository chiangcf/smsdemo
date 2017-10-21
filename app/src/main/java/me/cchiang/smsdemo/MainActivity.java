package me.cchiang.smsdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    private Button mBtn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Sets the Buttons
        mBtn1 = (Button) findViewById(R.id.btn1);

        // Request permissions or request them
        if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            mBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendSMS();
                }
            });
        }else{
            requestPerms();
        }




    }

    protected void sendSMS(){
//        Toast.makeText(getApplicationContext(), "Ripping Peperoni", Toast.LENGTH_SHORT).show();
        String number = "5158670942";
        String message = "Hello";

        SmsManager manager  =SmsManager.getDefault();
        manager.sendTextMessage(number, null, message, null, null);
        Toast.makeText(getApplicationContext(), "send succesfully", Toast.LENGTH_LONG).show();
    }

    protected void requestPerms(){
        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
        requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS}, 1);

    }

}
