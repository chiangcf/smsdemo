package me.cchiang.smsdemo;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private Button mBtn1, mContactBtn;

    /**
     * On Run time method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gets the Buttons
        mBtn1 = (Button) findViewById(R.id.btn1);
//        mContactBtn = (Button) findViewById(R.id.contactBtn);

        //Request Permissions
        requestPerms();

        // Runs the OpenContacts app
        final Contact[] contacts = openContacts();

        // Request permissions or request them
//        if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            mBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendSMS(contacts);
                }
            });
//        }else{
//        }



    }

    /**
     * Sends the SMS to the assign numbers using the SmsManager
     */
    protected void sendSMS(Contact[] contacts){
//        Toast.makeText(getApplicationContext(), "Ripping Peperoni", Toast.LENGTH_SHORT).show();
//        String number = "5158670942";
//        String message = "Hello";

        SmsManager manager = SmsManager.getDefault();
        String message = contacts[0].name;
        manager.sendTextMessage(contacts[0].phone, null, message, null, null);

        Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_SHORT).show();


    }

    /**
     * Request Permissions for Sending SMS, Reading Contacts, Accessing Locations
     */
    protected void requestPerms(){
        requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION}, 1);

    }

    public static Contact[] openContacts(){
//        Contact[] contacts = new Contact[];
        ArrayList<Contact> contactList = new ArrayList<Contact>();
        Contact con1 = new Contact("Christian", "5158670942");
        Contact con2 = new Contact("Bryan", "5157356690");
        Contact con3 = new Contact("Faizul", "5157086652");

        contactList.add(con1);
        contactList.add(con2);
        contactList.add(con3);

        Contact[] contacts = new Contact[contactList.size()];
        for(int i = 0; i < contactList.size(); i++){
            contacts[i] = contactList.get(i);
        }

        return contacts;

    }

}
