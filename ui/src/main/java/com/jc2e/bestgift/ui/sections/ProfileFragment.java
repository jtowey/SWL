package com.jc2e.bestgift.ui.sections;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jc2e.bestgift.ui.ParseConstants;
import com.jc2e.bestgift.ui.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ProfileFragment extends Fragment {


    public static final String TAG = ProfileFragment.class.getSimpleName();
	
	public ProfileFragment(){}

    protected ParseUser mCurrentUser;
    protected String mCurrentUserName;
    protected TextView mUserName;
    protected String mBirthdate;
    protected TextView mBirthLabel;

    //Profile settings member variables
    protected EditText mNewBirthdate;
    protected Button mSaveButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mUserName = (TextView) getView().findViewById(R.id.profileNameLabel);
        mBirthLabel = (TextView) getView().findViewById(R.id.profileDateView);

        mCurrentUser = ParseUser.getCurrentUser();

        mCurrentUserName = mCurrentUser.getUsername();
        Log.d(TAG, mCurrentUserName);
        mUserName.setText(mCurrentUserName);

        updateBirthLabel();
        profileData();


    }

    private void updateBirthLabel() {
        try {
            mBirthdate = mCurrentUser.get(ParseConstants.KEY_BIRTHDATE).toString();
            mBirthLabel.setText(mBirthdate);
            Log.d(TAG, mBirthdate);
            return;
        } catch (NullPointerException e) {
            Log.d(TAG, "User hasn\'t entered a birthdate");
            Toast.makeText(mBirthLabel.getContext(), "Enter a birthdate", Toast.LENGTH_LONG).show();
            return;
        } finally {
            return;
        }
    }

    // Profile settings TODO make into own class
    protected void profileData() {

        mNewBirthdate = (EditText) getView().findViewById(R.id.editBirthday);
        mSaveButton = (Button) getView().findViewById(R.id.saveButton);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String birthDate = mNewBirthdate.getText().toString().trim();
                mCurrentUser.put(ParseConstants.KEY_BIRTHDATE, birthDate);
                mCurrentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.d(TAG, birthDate);
                        updateBirthLabel();
                        mNewBirthdate.getText().clear();
                        return;
                    }
                });
            }
        });
    }
}
