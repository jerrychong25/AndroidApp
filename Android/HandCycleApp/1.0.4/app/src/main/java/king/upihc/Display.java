package king.upihc;

// Import Android API
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

// Import Firebase API
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// Import Youtube API
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.ValueEventListener;

// Import Java API
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by user on 14/8/2016.
 */

public class Display extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    // Global Variables
    int motor1_speed, motor1_duration, structure_orientation;
    private static final String TAG = "bluetooth1";
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    Handler h;
    final int RECEIVE_MESSAGE = 1;		// Status  for Handler
    private StringBuilder sb = new StringBuilder();
    private ConnectedThread mConnectedThread;
    String UserPushID;

    //Firebase Real Time Database
    DatabaseReference DatabaseRehabilitationDetails;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module
    private static String address = "20:15:06:28:03:69";            // HC-05
//    private static String address = "98:D3:31:90:47:5E";            // HC-06 1
//    private static String address = "98:D3:31:90:8C:AD";            // HC-06 2
//    private static String address = "5A:88:46:65:72:B4";            // MMD-P737
//    private static String address = "3C:DF:BD:B2:E3:AE";            // Y300
//    private static String address = "38:A4:ED:D2:2D:D9";            // R3S

    // Youtube
    public static final String api_key = "AIzaSyCnAQhYgNJr64s2IvVrLGX72pClRN7smF0";
//    public static final String video_id = "T5-8rkMVdCs";             // Mr Bean Cartoon
//    public static final String video_id = "k8_xSG2saCk";             // Pokemon Origins
    public static final String video_id = "MgRgiHyVGgw";           // Doraemon

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
//        Firebase.setAndroidContext(this);

//        final Firebase myFirebaseRef = new Firebase("https://telemedicine-42de1.firebaseio.com/");

        DatabaseRehabilitationDetails = FirebaseDatabase.getInstance().getReference();

        // Button
        final Button bSystemStart = (Button) findViewById(R.id.bSystemStart);
        final Button bSystemStop = (Button) findViewById(R.id.bSystemStop);

        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.player);
        youTubePlayerView.initialize(api_key, this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseRehabilitationDetails.child("UserDetails").orderByChild("UserEmail").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    UserPushID = child.getKey();
                    Log.d("Key", UserPushID);
//                Log.d("Key2", DataPushID);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECEIVE_MESSAGE:													// if receive massage
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);					// create string from bytes array
                        sb.append(strIncom);												// append string
                        int endOfLineIndex = sb.indexOf("\r\n");							// determine the end-of-line
                        if (endOfLineIndex > 0) { 											// if end-of-line,
                            String sbprint = sb.substring(0, endOfLineIndex);				// extract string
                            sb.delete(0, sb.length());										// and clear

                            char x[] = sbprint.toCharArray();
                            for (int i = 0; i < x.length; i++)
                            {
                                if (x[i]=='1')
                                    Toast.makeText(Display.this, "Message 1 Received!", Toast.LENGTH_SHORT).show();

                                else if (x[i]=='2')
                                    Toast.makeText(Display.this, "Message 2 Received!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        break;
                }
            };
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        bSystemStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert_StructureOrientation();
            }
        });

        bSystemStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motor1_speed = 0;
                motor1_duration = 0;
                structure_orientation = 0;
                Toast.makeText(Display.this, "Stop", Toast.LENGTH_SHORT).show();
                sendData("0\r\n");

//                DatabaseUserDetails = FirebaseDatabase.getInstance().getReference("UserDetails");
//                myFirebaseRef.child("message").setValue("System Stop");
//                THINK ON HOW TO SEND STOP MESSAGE!!!!!!!!!!!!!!!!!!!!!!
            }
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);

        if (!wasRestored) {
            youTubePlayer.cueVideo(video_id);
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

    public void Alert_StructureOrientation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Display.this);

        // Set the dialog title
        builder.setTitle("Select Orientation")
                // specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive call backs when items are selected
                // again, R.array.choices were set in the resources res/values/strings.xml
                .setSingleChoiceItems(R.array.orientation, 0, new DialogInterface.OnClickListener() {
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

                        if(selectedPosition == 0)                   // Left
                        {
                            structure_orientation = 1;
                            Alert_Motor1Duration();
                            Alert_Motor1Speed();
                        }

                        else if(selectedPosition == 1)              // Right
                        {
                            structure_orientation = 2;
                            Alert_Motor1Duration();
                            Alert_Motor1Speed();
                        }

                        else                                        // No Orientation If None Is Selected
                        {
                            structure_orientation = 0;
                            dialog.dismiss();
                            Toast.makeText(Display.this, "Please Select Orientation", Toast.LENGTH_SHORT).show();
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

    public void Alert_Motor1Speed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Display.this);

        // Set the dialog title
        builder.setTitle("Select Motor Speed")
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

                        if(selectedPosition == 0)                   // Low Speed
                        {
                            motor1_speed = 1;
                        }

                        else if(selectedPosition == 1)              // Medium Speed
                        {
                            motor1_speed = 2;
                        }

                        else if(selectedPosition == 2)              // High Speed
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

    public void Alert_Motor1Duration() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Display.this);

        // Set the dialog title
        builder.setTitle("Select Motor Duration")
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

    public void Bluetooth_Send() {

        Date date = new Date();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");

        String currentDate = dateFormatter.format(date);
        String startTime = timeFormatter.format(date);
        String endTime = timeFormatter.format(date);

        String DataPushID = DatabaseRehabilitationDetails.push().getKey();

        if(structure_orientation == 1 && motor1_speed == 3 && motor1_duration == 3)
        {
//            Toast.makeText(Display.this, "A", Toast.LENGTH_SHORT).show();
            sendData("A\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "20 Minutes", "High", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 1 && motor1_speed == 3 && motor1_duration == 2)
        {
            sendData("A\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "15 Minutes", "High", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 1 && motor1_speed == 3 && motor1_duration == 1)
        {
            sendData("A\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "10 Minutes", "High", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 1 && motor1_speed == 2 && motor1_duration == 3)
        {
            sendData("B\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "20 Minutes", "Medium", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 1 && motor1_speed == 2 && motor1_duration == 2)
        {
            sendData("B\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "15 Minutes", "Medium", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 1 && motor1_speed == 2 && motor1_duration == 1)
        {
            sendData("B\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "10 Minutes", "Medium", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 1 && motor1_speed == 1 && motor1_duration == 3)
        {
            sendData("C\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "20 Minutes", "Low", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 1 && motor1_speed == 1 && motor1_duration == 2)
        {
            sendData("C\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "15 Minutes", "Low", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 1 && motor1_speed == 1 && motor1_duration == 1)
        {
            sendData("C\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "10 Minutes", "Low", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 2 && motor1_speed == 3 && motor1_duration == 3)
        {
            sendData("D\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "20 Minutes", "High", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 2 && motor1_speed == 3 && motor1_duration == 2)
        {
            sendData("D\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "15 Minutes", "High", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 2 && motor1_speed == 3 && motor1_duration == 1)
        {
            sendData("D\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "10 Minutes", "High", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 2 && motor1_speed == 2 && motor1_duration == 3)
        {
            sendData("E\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "20 Minutes", "Medium", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 2 && motor1_speed == 2 && motor1_duration == 2)
        {
            sendData("E\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "15 Minutes", "Medium", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 2 && motor1_speed == 2 && motor1_duration == 1)
        {
            sendData("E\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "10 Minutes", "Medium", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 2 && motor1_speed == 1 && motor1_duration == 3)
        {
            sendData("F\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "20 Minutes", "Low", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 2 && motor1_speed == 1 && motor1_duration == 2)
        {
            sendData("F\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "15 Minutes", "Low", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        else if(structure_orientation == 2 && motor1_speed == 1 && motor1_duration == 1)
        {
            sendData("F\r\n");

            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "10 Minutes", "Low", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);
        }

        motor1_speed = 0;
        motor1_duration = 0;
        structure_orientation = 0;
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
                    h.obtainMessage(RECEIVE_MESSAGE, bytes, -1, buffer).sendToTarget();		// Send to message queue Handler
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
    }
}