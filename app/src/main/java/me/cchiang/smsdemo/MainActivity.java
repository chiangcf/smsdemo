package me.cchiang.smsdemo;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.Manifest.permission.*;    // Permissions


public class MainActivity extends AppCompatActivity {

    // Contacts stuff
    private static final String TAG = MainActivity.class.getSimpleName();
    private Uri uriContact;
    private String contactID;     // contacts unique ID
    TextView txt1;
    ArrayList<Contact> conList;

    /**
     * On Run time method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt1 = (TextView)findViewById(R.id.txt1);
        conList = new ArrayList<>();
        requestPerms();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            String name = retrieveContactName();
            String num = retrieveContactNumber();
            conList.add(new Contact(name,num));

            setContacts();

        }
    }


    /**
     * btn1: Send the SMS
     */
    public void sends(View btnSends) {
        sendSMS();
    }

    /**
     * btn2: Opens Contact List
     */
    public void openContactList(View btnSelectContact) {
        // Intent.ACTION_PICK = Pick an item from the data, returning what was selected
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 1);
    }

    /**
     * Set Contacts on TextView (txt1)
     */

    public void setContacts(){
        String name, num, combo;
        String total = "";
        for(int i = 0; i < conList.size(); i++){
            name = conList.get(i).name;
            num = conList.get(i).phone;
            combo = name + " " + num + "\n";
            total += combo;
        }
        txt1.setText(total);

    }

    /**
     * Retrieve Contact Number
     */
    private String retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        return contactNumber;
//        Toast.makeText(getApplicationContext(), contactNumber, Toast.LENGTH_SHORT).show();
    }

    /**
     * Retrieve Contact Name
     */
    private String retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        return contactName;

    }


    /**
     * Sends the SMS to the assign numbers using the SmsManager
     */
    protected void sendSMS(){
        SmsManager manager = SmsManager.getDefault();
        for(int i = 0; i < conList.size(); i++){
            String name = conList.get(i).name;
            String num = conList.get(i).phone;
            manager.sendTextMessage(num, null, "Hello " + name, null, null);
            Log.w("SENT TO: ", name + " " + num);
        }

    }


    /**
     * Makes the contact list
     */
    public static Contact[] mockContacts(){
//        Contact[] contacts = new Contact[];
        // Number formatting doesn't matter for the contacts
        ArrayList<Contact> contactList = new ArrayList<Contact>();
        Contact con1 = new Contact("Christian", "+1 (515) 865-0942");
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

    /**
     * Request Permissions for Sending SMS, Reading Contacts, Accessing Locations
     */
    protected void requestPerms(){
        requestPermissions(new String[]{SEND_SMS, READ_CONTACTS, ACCESS_FINE_LOCATION}, 1);

    }

}
