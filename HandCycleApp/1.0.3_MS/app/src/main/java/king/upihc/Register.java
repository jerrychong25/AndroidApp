package king.upihc;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etAge = (EditText) findViewById(R.id.etAge);
        final EditText etGender = (EditText) findViewById(R.id.etGender);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        final Button bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String name = etName.getText().toString();
//                final  int age = Integer.parseInt(etAge.getText().toString());
                final  String age = etAge.getText().toString();
                final  String gender = etGender.getText().toString();
                final  String username = etUsername.getText().toString();
                final  String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    etName.setError("Please enter your name.");
                    return;
                }

                else if (TextUtils.isEmpty(age)) {
                    etAge.setError("Please enter your age.");
                    return;
                }

                else if (TextUtils.isEmpty(gender)) {
                    etGender.setError("Please enter your gender.");
                    return;
                }

                else if (TextUtils.isEmpty(username)) {
                    etUsername.setError("Please enter your username.");
                    return;
                }

                else if (TextUtils.isEmpty(password)) {
                    etPassword.setError("Please enter your password.");
                    return;
                }

                else {
                    Toast.makeText(Register.this, "Success!", Toast.LENGTH_SHORT).show();
                    etName.getText().clear();
                    etAge.getText().clear();
                    etGender.getText().clear();
                    etUsername.getText().clear();
                    etPassword.getText().clear();
                    return;
                }

//                Response.Listener<String> responseListener = new Response.Listener<String>(){
//
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonResponse  = new JSONObject(response);
//                            boolean success = jsonResponse.getBoolean("success");
//
//                            if (success){
//                                AlertDialog.Builder builder = new  AlertDialog.Builder(Register.this);
//                                builder.setMessage("Register Succeeded")
//                                        .create()
//                                        .show();
//
//                                Intent intent = new Intent(Register.this, Login.class);
//                                Register.this.startActivity(intent);
//
//                            }else{
//                                AlertDialog.Builder builder = new  AlertDialog.Builder(Register.this);
//                                builder.setMessage("Register Failed")
//                                        .setNegativeButton("Retry", null)
//                                        .create()
//                                        .show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//
//                RegisterRequest registerRequest = new RegisterRequest(name, age, gender, username, password, responseListener);
//                RequestQueue queue = Volley.newRequestQueue(Register.this);
//                queue.add(registerRequest);
            }
        });
    }
}
