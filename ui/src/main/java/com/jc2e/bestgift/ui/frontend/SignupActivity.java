package com.jc2e.bestgift.ui.frontend;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.jc2e.bestgift.ui.MainActivity;
import com.jc2e.bestgift.ui.ParseConstants;
import com.jc2e.bestgift.ui.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import static com.jc2e.bestgift.ui.R.string;


public class SignupActivity extends Activity {

    public static final String TAG = SignupActivity.class.getSimpleName();

    protected EditText mUsername;
    protected EditText mPassword;
    protected EditText mEmail;
    protected EditText mBirthday;
    protected EditText mBirthyear;
    protected Button mSignUpButton;

    public String mListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_signup);

        mUsername = (EditText) findViewById(R.id.usernameField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mEmail = (EditText) findViewById(R.id.emailField);
        mBirthday = (EditText) findViewById(R.id.birthDay);
        mBirthyear = (EditText) findViewById(R.id.birthYear);
        mSignUpButton = (Button) findViewById(R.id.signupButton);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String birthDay = mBirthday.getText().toString().trim();
                String birthDate;
                String birthYear = mBirthyear.getText().toString().trim();

                if (!birthYear.isEmpty()) {
                    birthYear = "/" + birthYear;
                    birthDate = birthDay + birthYear;
                } else {
                    birthDate = birthDay;
                }

                // Check if user fields have been filled out.
                if (username.isEmpty() || password.isEmpty() || email.isEmpty() || birthDay.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage(string.signup_error_message);
                    builder.setTitle(string.signup_error_title);      // these three could be chained
                    builder.setPositiveButton(android.R.string.ok, null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // create new user
                    setProgressBarIndeterminateVisibility(true);

                    final ParseObject list = new ParseObject(ParseConstants.CLASS_LISTS);
                    list.put(ParseConstants.KEY_LIST_OWNER_NAME, username);
                    list.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Error creating list.");
                            } else {
                                mListId = list.getObjectId();
                                Log.d(TAG, "ListID:" + mListId);
                            }
                        }
                    });

                    final ParseUser newUser = new ParseUser();
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    newUser.setEmail(email);
                    newUser.put(ParseConstants.KEY_BIRTHDATE, birthDate);
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            setProgressBarIndeterminateVisibility(false);

                            if (e == null) {
                                // Success!

                                newUser.put(ParseConstants.KEY_LIST_RELATION, mListId);
                                newUser.saveInBackground();

                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                builder.setMessage(e.getMessage())
                                        .setTitle(string.signup_error_title)      // these three have been chained from above
                                        .setPositiveButton(android.R.string.ok, null);

                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }
                            list.put(ParseConstants.KEY_LIST_OWNER_ID, newUser.getObjectId());
                            list.saveInBackground();
                        }
                    });
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // TODO clean up old code in all classes.
}
