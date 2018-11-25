//package king.upihc;
//
//// Import Android API
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.text.TextUtils;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.toolbox.Volley;
//
//// Import Firebase API
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//
//// Import Java API
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.lang.reflect.Method;
//import java.util.UUID;
//
//public class Login extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        final EditText EmailText = (EditText) findViewById(R.id.EmailField);
//        final EditText PasswordText = (EditText) findViewById(R.id.PasswordField);
//
//        final Button LoginButton = (Button) findViewById(R.id.LoginButton);
//        final Button SignUpJumpButton = (Button) findViewById(R.id.SignUpJumpButton);
//
//        SignUpJumpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent registerIntent = new Intent(Login.this, Register.class);
//                startActivity(registerIntent);
//            }
//        });
//
//        LoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String Email = EmailText.getText().toString();
//                String Password = PasswordText.getText().toString();
//
//                if (Email.matches("admin") && Password.matches("1234")) {
//                    Toast.makeText(Login.this, "Success!", Toast.LENGTH_SHORT).show();
//                    EmailText.getText().clear();
//                    PasswordText.getText().clear();
//
//                    Intent displayIntent = new Intent(Login.this, Display.class);
//                    startActivity(displayIntent);
//                }
//
//                else if (TextUtils.isEmpty(Email)) {
//                    EmailText.setError("Please Enter Your Username.");
//                    return;
//                }
//
//                else if (TextUtils.isEmpty(Password)) {
//                    PasswordText.setError("Please Enter Your Password.");
//                    return;
//                }
//
//                else {
//                    Toast.makeText(Login.this, "Wrong Username or Password, Please Try Again!", Toast.LENGTH_SHORT).show();
//                    EmailText.getText().clear();
//                    PasswordText.getText().clear();
//                    return;
//                }
//            }
//        });
//    }
//}

// Online Source Code 2
package king.upihc;

// Import Android API
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

// Import Android API 2
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// Import Firebase API
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// Import Firebase API 2
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

// Import Java API
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class Login extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText EmailText = (EditText) findViewById(R.id.EmailField);
        final EditText PasswordText = (EditText) findViewById(R.id.PasswordField);

        final Button LoginButton = (Button) findViewById(R.id.LoginButton);
        final Button SignUpJumpButton = (Button) findViewById(R.id.SignUpJumpButton);

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLogInView();
        } else {

            // Online Sample Source Code - Main Page
//            mUserId = mFirebaseUser.getUid();
//
//            // Set up ListView
//            final ListView listView = (ListView) findViewById(R.id.listView);
//            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
//            listView.setAdapter(adapter);
//
//            // Add items via the Button and EditText at the bottom of the view.
//            final EditText text = (EditText) findViewById(R.id.todoText);
//            final Button button = (Button) findViewById(R.id.addButton);
//            button.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    Item item = new Item(text.getText().toString());
//                    mDatabase.child("users").child(mUserId).child("items").push().setValue(item);
//                    text.setText("");
//                }
//            });
//
//            // Use Firebase to populate the list.
//            mDatabase.child("users").child(mUserId).child("items").addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    adapter.add((String) dataSnapshot.child("title").getValue());
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    adapter.remove((String) dataSnapshot.child("title").getValue());
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//            // Delete items when clicked
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    mDatabase.child("users").child(mUserId).child("items")
//                            .orderByChild("title")
//                            .equalTo((String) listView.getItemAtPosition(position))
//                            .addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot.hasChildren()) {
//                                        DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
//                                        firstChild.getRef().removeValue();
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//                }
//            });
        }

        SignUpJumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(Login.this, Register.class);
                startActivity(registerIntent);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = EmailText.getText().toString();
                String Password = PasswordText.getText().toString();

                if (Email.matches("admin") && Password.matches("1234")) {
                    Toast.makeText(Login.this, "Success!", Toast.LENGTH_SHORT).show();
                    EmailText.getText().clear();
                    PasswordText.getText().clear();

                    Intent displayIntent = new Intent(Login.this, Display.class);
                    startActivity(displayIntent);
                }

                else if (TextUtils.isEmpty(Email)) {
                    EmailText.setError("Please Enter Your Username.");
                    return;
                }

                else if (TextUtils.isEmpty(Password)) {
                    PasswordText.setError("Please Enter Your Password.");
                    return;
                }

                else {
                    Toast.makeText(Login.this, "Wrong Username or Password, Please Try Again!", Toast.LENGTH_SHORT).show();
                    EmailText.getText().clear();
                    PasswordText.getText().clear();
                    return;
                }
            }
        });
    }

//    private void loadLogInView() {
//        Intent intent = new Intent(this, LogInActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_logout) {
//            mFirebaseAuth.signOut();
//            loadLogInView();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
