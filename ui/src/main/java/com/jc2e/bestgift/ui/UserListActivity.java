package com.jc2e.bestgift.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.parse.ParseUser;

public class UserListActivity extends Activity {

    public static final String TAG = UserListActivity.class.getSimpleName();

    protected ParseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_user_list);


        mCurrentUser = ParseUser.getCurrentUser();

        setProgressBarIndeterminateVisibility(true);

        // TODO convert to list items
//        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        query.orderByAscending(ParseConstants.KEY_USERNAME);
//        query.setLimit(1000);
//        query.findInBackground(new FindCallback<ParseUser>() {
//
//            public void done(List<ParseUser> users, ParseException e) {
//
//                setProgressBarIndeterminateVisibility(false);
//
//                if (e == null) {
//                    // Success
//                    mUsers = users;
//                    String[] usernames = new String[mUsers.size()];
//                    int i = 0;
//                    for (ParseUser user : mUsers) {
//                        usernames[i] = user.getUsername();
//                        i++;
//                    }
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserListActivity.this,
//                            android.R.layout.simple_list_item_checked,
//                            usernames);
//                    setListAdapter(adapter);
//
//                    addFriendCheckmarks();
//
//                } else {
//                    Log.e(TAG, e.getMessage());
//                    AlertDialog.Builder builder = new AlertDialog.Builder(UserListActivity.this);
//                    builder.setMessage(e.getMessage())
//                            .setTitle(R.string.error_title)
//                            .setPositiveButton(android.R.string.ok, null);
//
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                }
//            }
//        });
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
