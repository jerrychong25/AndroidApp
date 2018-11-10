package com.handcycle;

// Import Android API
import android.app.AlertDialog;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// Import Firebase API
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by user on 14/8/2016.
 */

public class Display extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    // Global Variables
    int motor1_speed, motor1_duration, structure_orientation;
    private static final String TAG = "bluetooth1";
    static Handler h;
    final int RECEIVE_MESSAGE = 1;		// Status  for Handler
    private StringBuilder sb = new StringBuilder();
    String UserPushID;

    //Firebase Real Time Database
    DatabaseReference DatabaseRehabilitationDetails;

    // Youtube
    public static final String api_key = "AIzaSyCnAQhYgNJr64s2IvVrLGX72pClRN7smF0";
    public static final String video_id = "2KfvxE7iQO8";           // Doraemon

    BluetoothFunction bluetoothFunction = new BluetoothFunction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

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
                bluetoothFunction.sendData("0\r\n");

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
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "20 Minutes", "High", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("A\r\n");
        }

        else if(structure_orientation == 1 && motor1_speed == 3 && motor1_duration == 2)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "15 Minutes", "High", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("A\r\n");
        }

        else if(structure_orientation == 1 && motor1_speed == 3 && motor1_duration == 1)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "10 Minutes", "High", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("A\r\n");
        }

        else if(structure_orientation == 1 && motor1_speed == 2 && motor1_duration == 3)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "20 Minutes", "Medium", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("B\r\n");
        }

        else if(structure_orientation == 1 && motor1_speed == 2 && motor1_duration == 2)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "15 Minutes", "Medium", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("B\r\n");
        }

        else if(structure_orientation == 1 && motor1_speed == 2 && motor1_duration == 1)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "10 Minutes", "Medium", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("B\r\n");
        }

        else if(structure_orientation == 1 && motor1_speed == 1 && motor1_duration == 3)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "20 Minutes", "Low", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("C\r\n");
        }

        else if(structure_orientation == 1 && motor1_speed == 1 && motor1_duration == 2)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "15 Minutes", "Low", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("C\r\n");
        }

        else if(structure_orientation == 1 && motor1_speed == 1 && motor1_duration == 1)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "10 Minutes", "Low", "Left");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("C\r\n");
        }

        else if(structure_orientation == 2 && motor1_speed == 3 && motor1_duration == 3)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "20 Minutes", "High", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("D\r\n");
        }

        else if(structure_orientation == 2 && motor1_speed == 3 && motor1_duration == 2)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "15 Minutes", "High", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("D\r\n");
        }

        else if(structure_orientation == 2 && motor1_speed == 3 && motor1_duration == 1)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "10 Minutes", "High", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("D\r\n");
        }

        else if(structure_orientation == 2 && motor1_speed == 2 && motor1_duration == 3)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "20 Minutes", "Medium", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("E\r\n");
        }

        else if(structure_orientation == 2 && motor1_speed == 2 && motor1_duration == 2)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "15 Minutes", "Medium", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("E\r\n");
        }

        else if(structure_orientation == 2 && motor1_speed == 2 && motor1_duration == 1)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "10 Minutes", "Medium", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("E\r\n");
        }

        else if(structure_orientation == 2 && motor1_speed == 1 && motor1_duration == 3)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "20 Minutes", "Low", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("F\r\n");
        }

        else if(structure_orientation == 2 && motor1_speed == 1 && motor1_duration == 2)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "15 Minutes", "Low", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("F\r\n");
        }

        else if(structure_orientation == 2 && motor1_speed == 1 && motor1_duration == 1)
        {
            RehabilitationDetails RehabilitationSend = new RehabilitationDetails(currentDate, startTime, "Unknown", "10 Minutes", "Low", "Right");

            DatabaseRehabilitationDetails.child("UserDetails").child(UserPushID).child("RehabilitationDetails").child(DataPushID).setValue(RehabilitationSend);

            bluetoothFunction.sendData("F\r\n");
        }

        motor1_speed = 0;
        motor1_duration = 0;
        structure_orientation = 0;
    }

//    public void onPause() {
//        super.onPause();
//        bluetoothFunction.onPause();
//    }

//    public void onResume() {
//        super.onResume();
//        bluetoothFunction.onResume();
//    }
}