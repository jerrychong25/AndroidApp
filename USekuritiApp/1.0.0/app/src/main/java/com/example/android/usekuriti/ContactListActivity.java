package com.example.android.usekuriti;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.Manifest.permission.CALL_PHONE;

public class ContactListActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        // Create an arrayList of words
        ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("Encik Ali", "A-1-1", "0166581019"));
        words.add(new Word("Ms Jocelyn","B-1-1", "0175561642"));
        words.add(new Word("Cik Wahidah","C-1-1", "0132843154"));
//        words.add(new Word("Mr Wong (US)","D-1-1", "011111111"));     // To Test VoIP Innovations

        WordAdapter adapter = new WordAdapter(this, words);
        final ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position,
                                    long id) {
                Log.d("ContactListActivity", "onCreate, position: " + position);
//                Log.d("PhrasesActivity", "onCreate, listView.getItemAtPosition(position).toString(): " + listView.getItemAtPosition(position).toString());
//                makePhoneCall(listView.getItemAtPosition(position).toString());

//                Log.d("PhrasesActivity", "onCreate, listView.getAdapter().getItem(position).toString(): " + listView.getAdapter().getItem(position).toString());
//                makePhoneCall(listView.getAdapter().getItem(position).toString());

//                HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getItem(position);
//                String name = (String) obj.get("name");

                if( (position==0) )
                {
                    makePhoneCall("0166581019");
                }
                else if( (position==1) )
                {
                    makePhoneCall("0175561642");
                }
                else if( (position==2) )
                {
                    makePhoneCall("0132843154");
                }
//                else if( (position==3) )
//                {
//                    makeVoIPPhoneCall("0166581019");
//                }
            }

        });
    }

    private void makePhoneCall(String number) {

        Log.d("ContactListActivity", "makePhoneCall, number: " + number);

        if (number.trim().length() > 0) {

            Log.d("ContactListActivity", "makePhoneCall 1");

            if (ContextCompat.checkSelfPermission(ContactListActivity.this,
                    CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                Log.d("ContactListActivity", "makePhoneCall 2");

                ActivityCompat.requestPermissions(ContactListActivity.this,
                        new String[]{CALL_PHONE}, REQUEST_CALL);
            } else {

                Log.d("ContactListActivity", "makePhoneCall 3");

                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Log.d("ContactListActivity", "makePhoneCall 4");

            Toast.makeText(ContactListActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeVoIPPhoneCall(String number) {

        Log.d("ContactListActivity", "makeVoIPPhoneCall, number: " + number);

        String api_key = "8cc2366a";
        String api_secret = "843470446027283ec73b528690edbcf5";

//        // Source: https://stackoverflow.com/questions/52458591/apidaze-rest-api-http-post-call-in-android-java
//        HttpPost httpPost = new HttpPost("https://api.apidaze.io/" + api_key + "/sms/send");
//
//        String json = "{"api_secret":" + api_secret + "}";
//        StringEntity entity = new StringEntity(json);
//        httpPost.setEntity(entity);
//        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
//
//        CloseableHttpResponse response = client.execute(httpPost);
    }
}
