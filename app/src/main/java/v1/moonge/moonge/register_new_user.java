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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class register_new_user extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_new_user);

        final Typeface RobotoBold = Typeface.createFromAsset(getAssets(), "Roboto/Roboto-Bold.ttf");
        final Typeface RobotoLight = Typeface.createFromAsset(getAssets(), "Roboto/Roboto-Light.ttf");
        final Typeface RobotoMedium = Typeface.createFromAsset(getAssets(), "Roboto/Roboto-Medium.ttf");

        TextView welcomeSignUpTxt = (TextView) findViewById(R.id.welcome_sign_up_txt);
        welcomeSignUpTxt.setTypeface(RobotoBold);

        Button AlreadyHaveAnAccount = (Button) findViewById(R.id.have_acount_already);
        AlreadyHaveAnAccount.setTypeface(RobotoMedium);

        AlreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginPage = new Intent(getApplicationContext(), Login.class);
                startActivity(LoginPage);
                finish();
            }
        });

        final EditText PasswordR = (EditText) findViewById(R.id.password_register);
        PasswordR.setTypeface(RobotoLight);

        final CheckBox PasswordEye = (CheckBox) findViewById(R.id.password_eye);
        PasswordEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    PasswordR.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    PasswordR.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        PasswordR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String passwordTxt = PasswordR.getText().toString();

                if (passwordTxt.trim().isEmpty() || passwordTxt.trim().length() < 6) {
                    PasswordR.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                } else {
                    PasswordR.setBackground(getResources().getDrawable(R.drawable.input_fields));
                }

            }
        });


        final EditText firstName = (EditText) findViewById(R.id.first_name);
        firstName.setTypeface(RobotoLight);

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String firstNameTxt = firstName.getText().toString();

                if (firstNameTxt.trim().isEmpty()) {
                    firstName.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                } else {
                    firstName.setBackground(getResources().getDrawable(R.drawable.input_fields));
                }

            }
        });


        final EditText lastName = (EditText) findViewById(R.id.last_name);
        lastName.setTypeface(RobotoLight);

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String lastNameTxt = lastName.getText().toString();

                if (lastNameTxt.trim().isEmpty()) {
                    lastName.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                } else {
                    lastName.setBackground(getResources().getDrawable(R.drawable.input_fields));
                }

            }
        });


        final EditText email = (EditText) findViewById(R.id.email);
        email.setTypeface(RobotoLight);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String emailTxt = email.getText().toString();

                if (emailTxt.trim().isEmpty() || !isValidEmail(emailTxt)) {
                    email.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                } else {
                    email.setBackground(getResources().getDrawable(R.drawable.input_fields));
                }

            }
        });


        final EditText phoneNumber = (EditText) findViewById(R.id.phone_number);
        phoneNumber.setTypeface(RobotoLight);

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneTxt = phoneNumber.getText().toString();

                if (phoneTxt.trim().isEmpty() || !isValidMobile(phoneTxt)) {
                    phoneNumber.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                } else {
                    phoneNumber.setBackground(getResources().getDrawable(R.drawable.input_fields));
                }
            }
        });


        Button SignUp = (Button) findViewById(R.id.sign_up_button);
        SignUp.setTypeface(RobotoMedium);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstNameInput = firstName.getText().toString();
                firstName.setBackground(getResources().getDrawable(R.drawable.input_fields));

                if (firstNameInput.trim().isEmpty()) {
                    firstName.setError("First Name is required", null);
                    firstName.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                    return;
                }

                String lastNameInput = lastName.getText().toString();
                lastName.setBackground(getResources().getDrawable(R.drawable.input_fields));

                if (lastNameInput.trim().isEmpty()) {
                    lastName.setError("Last Name is required", null);
                    lastName.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                    return;
                }

                String emailInput = email.getText().toString();
                email.setBackground(getResources().getDrawable(R.drawable.input_fields));

                if (emailInput.trim().isEmpty()) {
                    email.setError("Email is required", null);
                    email.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                    return;
                }

                if (!isValidEmail(emailInput)) {
                    email.setError("Invalid email", null);
                    email.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                    return;
                }

                String phoneNumberInput = phoneNumber.getText().toString();
                phoneNumber.setBackground(getResources().getDrawable(R.drawable.input_fields));

                if (phoneNumberInput.trim().isEmpty()) {
                    phoneNumber.setError("Phone is required", null);
                    phoneNumber.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                    return;
                }

                if (!isValidMobile(phoneNumberInput)) {
                    phoneNumber.setError("Invalid Phone number", null);
                    phoneNumber.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                    return;
                }

                final String passwordInput = PasswordR.getText().toString();
                PasswordR.setBackground(getResources().getDrawable(R.drawable.input_fields));

                if (passwordInput.trim().isEmpty()) {
                    PasswordR.setError("Password is required", null);
                    PasswordR.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                    return;
                }

                if (passwordInput.trim().length() < 6) {
                    PasswordR.setError("Should be greater than 6 char", null);
                    PasswordR.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                    return;
                }


                final ParseUser newUser = new ParseUser();

                newUser.setUsername(emailInput);
                newUser.setPassword(passwordInput);
                newUser.setEmail(emailInput);
                newUser.put("phone", phoneNumberInput);
                newUser.put("first_name", firstNameInput);
                newUser.put("last_name", lastNameInput);


                final ProgressDialog progressDialog = new ProgressDialog(register_new_user.this);

                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();
                        if (e == null) {
                            Intent landing_page = new Intent(getApplicationContext(), account_balance.class);
                            startActivity(landing_page);
                            finish();
                        } else {
                            if (e.getCode() == ParseException.USERNAME_TAKEN) {

                                email.setError("Email has already been used", null);
                                email.setBackground(getResources().getDrawable(R.drawable.validate_input_withbackground));
                            }
                        }
                    }
                });
            }
        });
    }

    private boolean isValidEmail(String target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isValidMobile(String phone)
    {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    @Override
    public void onBackPressed() {
        Intent loginPage = new Intent(getApplicationContext(), Login.class);
        startActivity(loginPage);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register_new_user, menu);
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
