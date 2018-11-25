package king.upihc;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class UserDetails extends Fragment {


    public class UserArea extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_user_details);

            final EditText etName = (EditText) findViewById(R.id.etName);
            final EditText etAge = (EditText) findViewById(R.id.etAge);
            final EditText etGender = (EditText) findViewById(R.id.etGender);
            final EditText etUsername = (EditText) findViewById(R.id.etUsername);

            Intent intent = getIntent();
            String name = intent.getStringExtra("name");
            int age = intent.getIntExtra("age", -1);
            String gender = intent.getStringExtra("gender");
            String username = intent.getStringExtra("username");

            etName.setText(name);
            etAge.setText(age + "");
            etGender.setText(gender);
            etUsername.setText(username);
        }

        private void logout() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Confirm logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(UserArea.this, Login.class);
                        }
                    });
            alertDialogBuilder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.menuLogout) {
                logout();
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
