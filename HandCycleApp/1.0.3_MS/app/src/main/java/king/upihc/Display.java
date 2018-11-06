package king.upihc;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by user on 14/8/2016.
 */

public class Display extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    // Global Variables
    String selection;
    int motor1_speed, motor1_direction, motor1_duration, motor2_speed, motor2_direction, motor2_duration, motor3_duration;
    double selectedIndex=0;
    String selectedIndex2="";
    private static final String TAG = "bluetooth1";
    //    Button btnOn, btnOff;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    //    TextView txtArduino;
    Handler h;
    final int RECIEVE_MESSAGE = 1;		// Status  for Handler
    private StringBuilder sb = new StringBuilder();
    private ConnectedThread mConnectedThread;
    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // MAC-address of Bluetooth module (you must edit this line)
//    private static String address = "20:15:06:28:03:69";            // HC-05
    private static String address = "98:D3:31:90:47:5E";            // HC-06
//    private static String address = "5A:88:46:65:72:B4";            // MMD-P737
//    private static String address = "3C:DF:BD:B2:E3:AE";            // Y300

    // Youtube
    public static final String api_key = "AIzaSyCnAQhYgNJr64s2IvVrLGX72pClRN7smF0";
    public static final String video_id1 = "T5-8rkMVdCs";
//    public static final String video_id2 = "k8_xSG2saCk";
//    public static final String video_id3 = "MgRgiHyVGgw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        // Button
        final Button bSystemStart = (Button) findViewById(R.id.bSystemStart);
        final Button bSystemStop = (Button) findViewById(R.id.bSystemStop);

        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.player);
        youTubePlayerView.initialize(api_key, this);

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECIEVE_MESSAGE:													// if receive massage
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);					// create string from bytes array
                        sb.append(strIncom);												// append string
                        int endOfLineIndex = sb.indexOf("\r\n");							// determine the end-of-line
                        if (endOfLineIndex > 0) { 											// if end-of-line,
                            String sbprint = sb.substring(0, endOfLineIndex);				// extract string
                            sb.delete(0, sb.length());										// and clear
                            //  txtArduino.setText("Data from Arduino: " + sbprint); 	                                        // update TextView
                            //  btnOff.setEnabled(true);
//                                        btnOn.setEnabled(true);
                            char x[] = sbprint.toCharArray();
                            for (int i = 0; i < x.length; i++)
                            {
                                if (x[i]=='1')
                                    Toast.makeText(Display.this, "Message 1 Received!", Toast.LENGTH_SHORT).show();

                                else if (x[i]=='2')
                                    Toast.makeText(Display.this, "Message 2 Received!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //Log.d(TAG, "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
                        break;
                }
            };
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        bSystemStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert_MotorSelection();
            }
        });

        bSystemStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motor1_speed = 0;
                motor1_direction = 0;
                motor1_duration = 0;
                motor2_speed = 0;
                motor2_direction = 0;
                motor2_duration = 0;
                motor3_duration = 0;
                Toast.makeText(Display.this, "Stop", Toast.LENGTH_SHORT).show();
                sendData("0\r\n");
            }
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);

        if (!wasRestored) {
            youTubePlayer.cueVideo(video_id1);
//            youTubePlayer.cueVideo(video_id2);
//            youTubePlayer.cueVideo(video_id3);
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Fail to Initialize!", Toast.LENGTH_LONG).show();

    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };

    public void Alert_MotorSelection() {

        // where we will store or remove selected items
        final List<Integer> mSelectedItems = new ArrayList<Integer>();

        AlertDialog.Builder builder = new AlertDialog.Builder(Display.this);

        // set the dialog title
        builder.setTitle("Select Training")

                // specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive call backs when items are selected
                // R.array.choices were set in the resources res/values/strings.xml
                .setMultiChoiceItems(R.array.motor, null, new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if (isChecked) {
                            // if the user checked the item, add it to the selected items
                            mSelectedItems.add(which);
                        } else if (mSelectedItems.contains(which)) {
                            // else if the item is already in the array, remove it
                            mSelectedItems.remove(Integer.valueOf(which));
                        }
                    }

                })

                // Set the action buttons
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        // user clicked OK, so save the mSelectedItems results somewhere
                        // here we are trying to retrieve the selected items indices
                        String selectedIndex = "";
                        String j = "";
                        for (Integer i : mSelectedItems) {
                            if (i < 3) {
                                selectedIndex += i;
                            }
                            else {
                                selectedIndex = null;
                            }
                        }

                        selection = selectedIndex;
                        if (selection == "") {
                            dialog.dismiss();
                            Toast.makeText(Display.this, "Please Select At Least One Motor", Toast.LENGTH_SHORT).show();
                        }
                        else {
//                            Bundle bundle = new Bundle();
//                            //Add your data to bundle
//                            bundle.putString("selection", selection);
//                            //Add the bundle to the intent
//                            myIntent.putExtras(bundle);
//                            startActivity(myIntent);
                            char arr[] = selection.toCharArray();

                            if(arr.length == 2)
                            {
                                char temp = arr[0];
                                arr[0] = arr[1];
                                arr[1] = temp;
                            }

                            else if(arr.length == 3)
                            {
                                for(int i = 0; i < arr.length / 2; i++)
                                {
                                    char temp = arr[i];
                                    arr[i] = arr[arr.length - i - 1];
                                    arr[arr.length - i - 1] = temp;
                                }
                            }

                            for (int i = 0; i < arr.length; i++) {
                                if (arr[i] == '2'){
                                    Alert_Motor3Duration();
                                }
                                else if (arr[i] == '1'){
                                    Alert_Motor2Duration();
                                    Alert_Motor2Direction();
                                    Alert_Motor2Speed();
                                }
                                else if (arr[i] == '0') {
                                    Alert_Motor1Duration();
                                    Alert_Motor1Direction();
                                    Alert_Motor1Speed();
                                }

//                                if(i == arr.length)
//                                {
//                                    Bluetooth_Send();
//                                }
                            }
                        }
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the AlertDialog in the screen
                    }
                })

                .show();

    }

    public void Alert_Motor1Speed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Display.this);

        // Set the dialog title
        builder.setTitle("Select Hand Cycle Motor Speed")
                // specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive call backs when items are selected
                // again, R.array.choices were set in the resources res/values/strings.xml
                .setSingleChoiceItems(R.array.speed, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // showToast("Some actions maybe? Selected index: " + arg1);
                    }

                })

                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        if(selectedPosition == 0)                   // Slow Speed
                        {
                            motor1_speed = 1;
                        }

                        else if(selectedPosition == 1)              // Medium Speed
                        {
                            motor1_speed = 2;
                        }

                        else if(selectedPosition == 2)              // Fast Speed
                        {
                            motor1_speed = 3;
                        }

                        else                                        // Zero Speed If None Is Selected
                        {
                            motor1_speed = 0;
                        }
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the dialog from the screen

                    }
                })

                .show();

    }

    public void Alert_Motor1Direction() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Display.this);

        // Set the dialog title
        builder.setTitle("Select Hand Cycle Motor Direction")
                // specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive call backs when items are selected
                // again, R.array.choices were set in the resources res/values/strings.xml
                .setSingleChoiceItems(R.array.direction, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // showToast("Some actions maybe? Selected index: " + arg1);
                    }

                })

                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        if(selectedPosition == 0)                   // Forward
                        {
                            motor1_direction = 1;
                        }

                        else if(selectedPosition == 1)              // Backward
                        {
                            motor1_direction = 0;
                        }

                        else                                        // Backward If None Is Selected
                        {
                            motor1_direction = 0;
                        }
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the dialog from the screen

                    }
                })

                .show();

    }

    public void Alert_Motor1Duration() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Display.this);

        // Set the dialog title
        builder.setTitle("Select Hand Cycle Motor Duration")
                // specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive call backs when items are selected
                // again, R.array.choices were set in the resources res/values/strings.xml
                .setSingleChoiceItems(R.array.duration, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // showToast("Some actions maybe? Selected index: " + arg1);
                    }

                })

                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        if(selectedPosition == 0)                   // 10 Minutes
                        {
                            motor1_duration = 1;
                        }

                        else if(selectedPosition == 1)              // 15 Minutes
                        {
                            motor1_duration = 2;
                        }

                        else if(selectedPosition == 2)              // 20 Minutes
                        {
                            motor1_duration = 3;
                        }

                        else                                        // No Time If None Is Selected
                        {
                            motor1_duration = 0;
                        }

                        Bluetooth_Send();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the dialog from the screen

                    }
                })

                .show();

    }

    public void Alert_Motor2Speed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Display.this);

        // Set the dialog title
        builder.setTitle("Select Leg Cycle Motor Speed")
                // specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive call backs when items are selected
                // again, R.array.choices were set in the resources res/values/strings.xml
                .setSingleChoiceItems(R.array.speed, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // showToast("Some actions maybe? Selected index: " + arg1);
                    }

                })

                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        if(selectedPosition == 0)                   // Slow Speed
                        {
                            motor2_speed = 1;
                        }

                        else if(selectedPosition == 1)              // Medium Speed
                        {
                            motor2_speed = 2;
                        }

                        else if(selectedPosition == 2)              // Fast Speed
                        {
                            motor2_speed = 3;
                        }

                        else                                        // Zero Speed If None Is Selected
                        {
                            motor2_speed = 0;
                        }
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the dialog from the screen

                    }
                })

                .show();

    }

    public void Alert_Motor2Direction() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Display.this);

        // Set the dialog title
        builder.setTitle("Select Leg Cycle Motor Direction")
                // specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive call backs when items are selected
                // again, R.array.choices were set in the resources res/values/strings.xml
                .setSingleChoiceItems(R.array.direction, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // showToast("Some actions maybe? Selected index: " + arg1);
                    }

                })

                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        if(selectedPosition == 0)                   // Forward
                        {
                            motor2_direction = 1;
                        }

                        else if(selectedPosition == 1)              // Backward
                        {
                            motor2_direction = 0;
                        }

                        else                                        // Backward If None Is Selected
                        {
                            motor2_direction = 0;
                        }
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the dialog from the screen

                    }
                })

                .show();

    }

    public void Alert_Motor2Duration() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Display.this);

        // Set the dialog title
        builder.setTitle("Select Leg Cycle Motor Duration")
                // specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive call backs when items are selected
                // again, R.array.choices were set in the resources res/values/strings.xml
                .setSingleChoiceItems(R.array.duration, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // showToast("Some actions maybe? Selected index: " + arg1);
                    }

                })

                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        if(selectedPosition == 0)                   // 10 Minutes
                        {
                            motor2_duration = 1;
                        }

                        else if(selectedPosition == 1)              // 15 Minutes
                        {
                            motor2_duration = 2;
                        }

                        else if(selectedPosition == 2)              // 20 Minutes
                        {
                            motor2_duration = 3;
                        }

                        else                                        // No Time If None Is Selected
                        {
                            motor2_duration = 0;
                        }

                        Bluetooth_Send();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the dialog from the screen

                    }
                })

                .show();

    }

    public void Alert_Motor3Duration() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Display.this);

        // Set the dialog title
        builder.setTitle("Select Track Motor Duration")
                // specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive call backs when items are selected
                // again, R.array.choices were set in the resources res/values/strings.xml
                .setSingleChoiceItems(R.array.duration, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // showToast("Some actions maybe? Selected index: " + arg1);
                    }

                })

                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        if(selectedPosition == 0)                   // 10 Minutes
                        {
                            motor3_duration = 1;
                        }

                        else if(selectedPosition == 1)              // 15 Minutes
                        {
                            motor3_duration = 2;
                        }

                        else if(selectedPosition == 2)              // 20 Minutes
                        {
                            motor3_duration = 3;
                        }

                        else                                        // No Time If None Is Selected
                        {
                            motor3_duration = 0;
                        }

                        Bluetooth_Send();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the dialog from the screen

                    }
                })

                .show();

    }

    public void Bluetooth_Send() {

        if(motor1_speed == 3 && motor1_direction == 1)
        {
            Toast.makeText(Display.this, "A", Toast.LENGTH_SHORT).show();
            sendData("A\r\n");
        }

        else if(motor1_speed == 2 && motor1_direction == 1)
        {
            Toast.makeText(Display.this, "B", Toast.LENGTH_SHORT).show();
            sendData("B\r\n");
        }

        else if(motor1_speed == 1 && motor1_direction == 1)
        {
            Toast.makeText(Display.this, "C", Toast.LENGTH_SHORT).show();
            sendData("C\r\n");
        }

        else if(motor1_speed == 3 && motor1_direction == 0)
        {
            Toast.makeText(Display.this, "D", Toast.LENGTH_SHORT).show();
            sendData("D\r\n");
        }

        else if(motor1_speed == 2 && motor1_direction == 0)
        {
            Toast.makeText(Display.this, "E", Toast.LENGTH_SHORT).show();
            sendData("E\r\n");
        }

        else if(motor1_speed == 1 && motor1_direction == 0)
        {
            Toast.makeText(Display.this, "F", Toast.LENGTH_SHORT).show();
            sendData("F\r\n");
        }
        else if(motor2_speed == 3 && motor2_direction == 1)
        {
            Toast.makeText(Display.this, "G", Toast.LENGTH_SHORT).show();
            sendData("G\r\n");
        }

        else if(motor2_speed == 2 && motor2_direction == 1)
        {
            Toast.makeText(Display.this, "H", Toast.LENGTH_SHORT).show();
            sendData("H\r\n");
        }

        else if(motor2_speed == 1 && motor2_direction == 1)
        {
            Toast.makeText(Display.this, "I", Toast.LENGTH_SHORT).show();
            sendData("I\r\n");
        }

        else if(motor2_speed == 3 && motor2_direction == 0)
        {
            Toast.makeText(Display.this, "J", Toast.LENGTH_SHORT).show();
            sendData("J\r\n");
        }

        else if(motor2_speed == 2 && motor2_direction == 0)
        {
            Toast.makeText(Display.this, "K", Toast.LENGTH_SHORT).show();
            sendData("K\r\n");
        }

        else if(motor2_speed == 1 && motor2_direction == 0)
        {
            Toast.makeText(Display.this, "L", Toast.LENGTH_SHORT).show();
            sendData("L\r\n");
        }

        else if(motor3_duration > 0)
        {
            Toast.makeText(Display.this, "M", Toast.LENGTH_SHORT).show();
            sendData("M\r\n");
        }

        motor1_speed = 0;
        motor1_direction = 0;
        motor1_duration = 0;
        motor2_speed = 0;
        motor2_direction = 0;
        motor2_duration = 0;
        motor3_duration = 0;
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }
    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - try connect...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e1) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
        }

        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG, "...Connection ok...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Create Socket...");

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
    }
    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }
        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }
    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter == null) {
            // Device does not support Bluetooth
            errorExit("Fatal Error", "This device does not support Bluetooth!");
        }

        else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }
    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_SHORT).show();
        finish();
    }
    private void sendData(String message) {
        byte[] msgBuffer = message.getBytes();

        Log.d(TAG, "...Send data: " + message + "...");

        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }
    private class ConnectedThread extends Thread {
        //        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
//            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);		// Get number of bytes and message in "buffer"
                    // Send the obtained bytes to the UI activity
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();		// Send to message queue Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
            }
        }

//        public void write(byte[] bytes) {
//        try {
//            mmOutStream.write(bytes);
//        } catch (IOException e) { }
//}
//
//        /* Call this from the main activity to shutdown the connection */
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) { }
//        }
    }
}