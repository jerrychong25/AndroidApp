package my.com.codeplay.training.marsmobilityapp;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Delayed;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.http.GET;

import static java.lang.Integer.parseInt;

public class DataActivity extends AppCompatActivity {

    private TextView tvTemperature, tvHumidity, tvHeatIndex, tvHeartRate;
    private String mDeviceName;
    private String mDeviceAddress;
    private RBLService mBluetoothLeService;
    private Map<UUID, BluetoothGattCharacteristic> map = new HashMap<UUID, BluetoothGattCharacteristic>();
    private boolean hazard = false;
    private Date lastHazardDate = null;
    private Date lastHeartRateDate = null;
    String stringArray = "";
    String tempString2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTemperature = (TextView) findViewById(R.id.temperature);
        tvHumidity = (TextView) findViewById(R.id.humidity);
        tvHeatIndex = (TextView) findViewById(R.id.heatindex);
        tvHeartRate = (TextView) findViewById(R.id.heartrate);

        // Set Background Colour To Light Blue
        View root = this.getWindow().getDecorView();
        root.setBackgroundColor(0xFF448AFF);              // Format: α (FF) + Blue 50 A200 colour (448AFF) = FF448AFF

        // Connect with Bluetooth BLE
        Intent intent = getIntent();

        mDeviceAddress = intent.getStringExtra(Device.EXTRA_DEVICE_ADDRESS);
        mDeviceName = intent.getStringExtra(Device.EXTRA_DEVICE_NAME);

        Intent gattServiceIntent = new Intent(this, RBLService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {
            mBluetoothLeService = ((RBLService.LocalBinder) service)
                    .getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("Data", "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up
            // initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (RBLService.ACTION_GATT_DISCONNECTED.equals(action)) {
            } else if (RBLService.ACTION_GATT_SERVICES_DISCOVERED
                    .equals(action)) {
                getGattService(mBluetoothLeService.getSupportedGattService());
            } else if (RBLService.ACTION_DATA_AVAILABLE.equals(action)) {
                byte[] byteArray;
                byteArray = intent.getByteArrayExtra(RBLService.EXTRA_DATA);

                String tempString = new String (byteArray);

                if (tempString != null) {
                    //Log.d("Data", "BroadcastReceiver()" + tempString);
                    //Log.d("Data", "BroadcastReceiver() Start");

                    if(stringArray!=null){
                        if(tempString.contains(System.getProperty("line.separator"))){
                            String[] tmepStringArray = tempString.split(System.getProperty("line.separator"));
                            if(tmepStringArray.length > 1){
                                if(tmepStringArray[1] == ""){
                                    stringArray += tmepStringArray[0];
                                } else {
                                    stringArray += tmepStringArray[0];
                                    tempString2 = tmepStringArray[1];
                                }
                            } else if (tmepStringArray.length == 1) {
                                stringArray += tmepStringArray[0];
                            }
                            int counter = 0;
                            for( int i=0; i<stringArray.length(); i++ ) {
                                if( stringArray.charAt(i) == '|' ) {
                                    counter++;
                                }
                            }
                            if(stringArray.indexOf('B') == 0 && counter == 8)
                            {
                                Log.d("Data", "Full String Receive "+stringArray);
                                String[] data = stringArray.split(Pattern.quote("|"));
                                for( int j=0; j<data.length; j++ ) {
                                    Log.d("Data", data[j]);
                                }
                                // Heart Rate (B)
                                Log.d("Data", data[0]+" Data "+data[1]);

                                // Humidity (H)
                                Log.d("Data", data[2]+" Data "+data[3]);

                                // Temperature (T)
                                Log.d("Data", data[4]+" Data "+data[5]);

                                // Heat Index (I)
                                Log.d("Data", data[6]+" Data "+data[7]);

                                try
                                {
                                    float h = Float.parseFloat(data[1]);
                                    if(h== -1)
                                    {
                                        Date compareDateTime = new Date();
                                        if(lastHeartRateDate!= null){
                                            if((compareDateTime.getTime() / 1000) - (lastHeartRateDate.getTime() / 1000) > 2){
                                                lastHeartRateDate = null;
                                            }
                                        } else {
                                            tvHeartRate.setText("Measuring");
                                        }
                                    } else {
                                        tvHeartRate.setText(data[1] + " BPM");
                                        if(lastHeartRateDate == null){
                                            lastHeartRateDate = new Date();
                                        }

                                    }
                                }
                                catch(NumberFormatException fdsf)
                                {
                                }
                                if(!hazard){
                                    tvHumidity.setText(data[3] + " %");
                                    tvTemperature.setText(data[5] + " °C");
                                    tvHeatIndex.setText(data[7] + " °C");
                                    try
                                    {
                                        float d = Float.parseFloat(data[3]);
                                        if(d>70 && d<=100)
                                        {
                                            hazard = true;
                                            lastHazardDate = new Date();

                                            ConstraintLayout currentLayout = (ConstraintLayout) findViewById(R.id.main_layout);
                                            currentLayout.setBackgroundColor(Color.RED);

                                            Toast.makeText(DataActivity.this,"HAZARD WEATHER!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    catch(NumberFormatException nfe)
                                    {

                                    }
                                } else {
                                    Date compareDateTime = new Date();
                                    if(lastHazardDate != null){
                                        if((compareDateTime.getTime() / 1000) - (lastHazardDate.getTime() / 1000) > 5){
                                            hazard = false;
                                            lastHazardDate = null;
                                            ConstraintLayout currentLayout = (ConstraintLayout) findViewById(R.id.main_layout);
                                            currentLayout.setBackgroundColor(0xFF448AFF);
                                        }
                                    } else {
                                        hazard = false;
                                        lastHazardDate = null;
                                        ConstraintLayout currentLayout = (ConstraintLayout) findViewById(R.id.main_layout);
                                        currentLayout.setBackgroundColor(0xFF448AFF);
                                    }
                                }
                            }
                            stringArray = "";
                            if(tempString2 != "") {
                                stringArray += tempString2;
                                tempString2 = "";
                            }
                        } else {
                            stringArray += tempString;
                        }
                    } else {
                        stringArray += tempString;
                    }
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBluetoothLeService.disconnect();
        mBluetoothLeService.close();

        System.exit(0);
    }

    private void getGattService(BluetoothGattService gattService) {
        if (gattService == null)
            return;

        BluetoothGattCharacteristic characteristic = gattService
                .getCharacteristic(RBLService.UUID_BLE_SHIELD_TX);
        map.put(characteristic.getUuid(), characteristic);

        BluetoothGattCharacteristic characteristicRx = gattService
                .getCharacteristic(RBLService.UUID_BLE_SHIELD_RX);
        mBluetoothLeService.setCharacteristicNotification(characteristicRx,
                true);
        mBluetoothLeService.readCharacteristic(characteristicRx);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(RBLService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(RBLService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(RBLService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(RBLService.ACTION_DATA_AVAILABLE);

        return intentFilter;
    }
}
