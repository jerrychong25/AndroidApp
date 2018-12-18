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

        final EditText NameText = (EditText) findViewById(R.id.NameField);
        final EditText AgeText = (EditText) findViewById(R.id.AgeField);
        final EditText GenderText = (EditText) findViewById(R.id.GenderField);
        final EditText EmailText = (EditText) findViewById(R.id.EmailField);
        final EditText PasswordText = (EditText) findViewById(R.id.PasswordField);

        final Button bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String Name = NameText.getText().toString();
//                final  int age = Integer.parseInt(etAge.getText().toString());
                final  String Age = AgeText.getText().toString();
                final  String Gender = GenderText.getText().toString();
                final  String Email = EmailText.getText().toString();
                final  String Password = PasswordText.getText().toString();

                if (TextUtils.isEmpty(Name)) {
                    NameText.setError("Please enter your name.");
                    return;
                }

                else if (TextUtils.isEmpty(Age)) {
                    AgeText.setError("Please enter your age.");
                    return;
                }

                else if (TextUtils.isEmpty(Gender)) {
                    GenderText.setError("Please enter your gender.");
                    return;
                }

                else if (TextUtils.isEmpty(Email)) {
                    EmailText.setError("Please enter your username.");
                    return;
                }

                else if (TextUtils.isEmpty(Password)) {
                    PasswordText.setError("Please enter your password.");
                    return;
                }

                else {
                    Toast.makeText(Register.this, "Success!", Toast.LENGTH_SHORT).show();
                    NameText.getText().clear();
                    AgeText.getText().clear();
                    GenderText.getText().clear();
                    EmailText.getText().clear();
                    PasswordText.getText().clear();
                    return;
                }
            }
        });
    }
}
