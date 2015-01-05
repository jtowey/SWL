package com.jc2e.bestgift.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UserListActivity extends ListActivity {

    public static final String TAG = UserListActivity.class.getSimpleName();

    protected ParseUser mCurrentUser;
    protected List<ParseObject> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_user_list);


        mCurrentUser = ParseUser.getCurrentUser();

        setProgressBarIndeterminateVisibility(true);

//        TODO convert to list items
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CLASS_LIST_ITEM);
        query.whereEqualTo(ParseConstants.KEY_LIST_OWNER_ID, mCurrentUser.getObjectId());
        query.setLimit(12);
        query.findInBackground(new FindCallback<ParseObject>() {

            public void done(List<ParseObject> items, ParseException e) {

                setProgressBarIndeterminateVisibility(false);

                if (e == null) {
                    // Success
                    mItems = items;
                    String[] listItems = new String[mItems.size()];
                    int i = 0;
                    for (ParseObject item : mItems) {
                        listItems[i] = item.getString(ParseConstants.KEY_ITEM_NAME);
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserListActivity.this,
                            android.R.layout.simple_expandable_list_item_1,
                            listItems);
                    setListAdapter(adapter);


                } else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserListActivity.this);
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        Toast.makeText(this, "This activity is under construction", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            navigateToLogin();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, com.jc2e.bestgift.ui.frontend.LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
