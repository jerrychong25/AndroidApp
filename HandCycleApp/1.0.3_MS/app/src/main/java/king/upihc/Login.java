package king.upihc;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final Button bRegisterLink = (Button) findViewById(R.id.bRegisterLink);

        bRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(Login.this, Register.class);
                startActivity(registerIntent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (username.matches("admin") && password.matches("1234")) {
                    Toast.makeText(Login.this, "Success!", Toast.LENGTH_SHORT).show();
                    etUsername.getText().clear();
                    etPassword.getText().clear();

                    Intent displayIntent = new Intent(Login.this, Display.class);
                    startActivity(displayIntent);
                }

                else if (TextUtils.isEmpty(username)) {
                    etUsername.setError("Please Enter Your Username.");
                    return;
                }

                else if (TextUtils.isEmpty(password)) {
                    etPassword.setError("Please Enter Your Password.");
                    return;
                }

                else {
                    Toast.makeText(Login.this, "Wrong Username or Password, Please Try Again!", Toast.LENGTH_SHORT).show();
                    etUsername.getText().clear();
                    etPassword.getText().clear();
                    return;
                }

//                Response.Listener<String> responseListener = new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonResponse = new JSONObject(response);
//                            boolean success = jsonResponse.getBoolean("success");
//
//                            if (success){
//                                String name = jsonResponse.getString("name");
//                                int age = jsonResponse.getInt("age");
//                                String gender = jsonResponse.getString("gender");
//
//                                Intent intent = new Intent(Login.this, Display.class);
//                                intent.putExtra("name", name);
//                                intent.putExtra("age", age);
//                                intent.putExtra("gender", gender);
//                                intent.putExtra("username", username);
//
//                                Login.this.startActivity(intent);
//                            }else{
//                                AlertDialog.Builder builder = new  AlertDialog.Builder(Login.this);
//                                builder.setMessage("Login Failed")
//                                        .setNegativeButton("Retry", null)
//                                        .create()
//                                        .show();
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                };
//
//                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
//                RequestQueue queue = Volley.newRequestQueue(Login.this);
//                queue.add(loginRequest);
            }
        });
    }

}