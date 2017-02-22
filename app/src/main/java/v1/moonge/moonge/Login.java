package v1.moonge.moonge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Login extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if(ParseUser.getCurrentUser() != null){
            Intent landing_page = new Intent(getApplicationContext(), account_balance.class);
            startActivity(landing_page);
            this.finish();
        }


        final Typeface RobotoBold = Typeface.createFromAsset(getAssets(), "Roboto/Roboto-Bold.ttf");
        final Typeface RobotoLight = Typeface.createFromAsset(getAssets(), "Roboto/Roboto-Light.ttf");
        final Typeface RobotoMedium = Typeface.createFromAsset(getAssets(), "Roboto/Roboto-Medium.ttf");


        Button newUserButton = (Button) findViewById(R.id.new_user_button);

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent y = new Intent(getApplicationContext(), register_new_user.class);
                startActivity(y);
            }
        });


        final EditText username = (EditText)findViewById(R.id.username);
        username.setTypeface(RobotoLight);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String usernameTxt = username.getText().toString();

                if (usernameTxt.trim().isEmpty()) {
                    username.setBackground(getResources().getDrawable(R.drawable.validation_input));
                } else {
                    username.setBackground(getResources().getDrawable(R.drawable.input_border));
                }

            }
        });


        final EditText password = (EditText) findViewById(R.id.password);
        password.setTypeface(RobotoLight);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String passwordTxt = password.getText().toString();

                if (passwordTxt.trim().isEmpty()) {
                    password.setBackground(getResources().getDrawable(R.drawable.validation_input));
                } else {
                    password.setBackground(getResources().getDrawable(R.drawable.input_border));
                }

            }
        });


        final CheckBox PasswordEye = (CheckBox) findViewById(R.id.show_login_password);
        PasswordEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });



        TextView new_user = (TextView) findViewById(R.id.new_user_button);
        new_user.setTypeface(RobotoMedium);


        Button forget_password = (Button) findViewById(R.id.forget_password);
        forget_password.setTypeface(RobotoMedium);



        TextView welcome = (TextView) findViewById(R.id.welcom_text);
        welcome.setTypeface(RobotoBold);


        Button login_user = (Button) findViewById(R.id.login_user);


        login_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean goodUsername = true;
                boolean goodPassword = true;

                final String usernameTxt = username.getText().toString();

                if (usernameTxt.trim().isEmpty()) {
                    username.setBackground(getResources().getDrawable(R.drawable.validation_input));
                    goodUsername = false;
                } else {
                    username.setBackground(getResources().getDrawable(R.drawable.input_border));
                }


                String passwordTxt = password.getText().toString();
                if (passwordTxt.trim().isEmpty()) {
                    password.setBackground(getResources().getDrawable(R.drawable.validation_input));
                    goodPassword = false;
                } else {
                    password.setBackground(getResources().getDrawable(R.drawable.input_border));
                }

                if (goodUsername && goodPassword) {

                    final ProgressDialog progressDialog = new ProgressDialog(Login.this);

                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();



                    ParseUser.logInInBackground(usernameTxt, passwordTxt, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            Toast validator = new Toast(Login.this);
                            validator.setGravity(Gravity.BOTTOM, 0, 100);
                            validator.setDuration(Toast.LENGTH_LONG);

                            LinearLayout server_error = (LinearLayout) getLayoutInflater().inflate(R.layout.toast_wrong_server, null);
                            TextView wrongPasswordOrUser = (TextView) server_error.findViewById(R.id.wrong);
                            wrongPasswordOrUser.setTypeface(RobotoLight);


                            validator.setView(server_error);

                            if (user != null) {
                                Intent landing_page = new Intent(getApplicationContext(), account_balance.class);
                                startActivity(landing_page);
                            } else {
                                validator.show();
                            }

                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
