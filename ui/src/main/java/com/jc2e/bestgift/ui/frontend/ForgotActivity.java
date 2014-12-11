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
import android.widget.Toast;

import com.jc2e.bestgift.ui.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;


public class ForgotActivity extends Activity {

    public static final String TAG = ForgotActivity.class.getSimpleName();

    protected EditText mForgotField;
    protected Button mForgotButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_forgot);

        mForgotButton = (Button) findViewById(R.id.forgotButton);
        mForgotField = (EditText) findViewById(R.id.forgotField);

        mForgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String field = mForgotField.getText().toString().trim();


                // Check if user fields have been filled out.
                if (field.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotActivity.this);
                    builder.setMessage(R.string.forgot_error_message);
                    builder.setTitle(R.string.forgot_error_title);      // these three could be chained
                    builder.setPositiveButton(android.R.string.ok, null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    setProgressBarIndeterminateVisibility(true);
                    // Send password reset request to parse.
                    ParseUser.requestPasswordResetInBackground(field,
                            new RequestPasswordResetCallback() {
                                public void done(ParseException e) {

                                    setProgressBarIndeterminateVisibility(false);

                                    if (e == null) {
                                        // An email was successfully sent with reset instructions.

//                                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotActivity.this);
//                                        builder.setMessage(R.string.forgot_complete);
//                                        builder.setTitle(R.string.forgot_complete_title);      // these three could be chained
//                                        builder.setPositiveButton(android.R.string.ok, null);
//
//                                        AlertDialog dialog = builder.create();
//                                        dialog.show();

                                        Intent intent = new Intent(ForgotActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    } else {
                                        // Something went wrong. Look at the ParseException to see what's up.
                                        Log.i(TAG, "password reset error " + e);

                                        Toast.makeText(ForgotActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.forgot, menu);
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
}
