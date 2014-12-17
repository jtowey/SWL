package com.jc2e.bestgift.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jc2e.bestgift.ui.model.ListItem;
import com.parse.ParseUser;


public class NewWishActivity extends Activity {

    public static final String TAG = NewWishActivity.class.getSimpleName();

    protected String mListId;
    protected String mUserName;

    protected ParseUser mCurrentUser;
//    protected ParseObject mUserList;

    protected MenuItem mSaveListItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_wish);

        mCurrentUser = ParseUser.getCurrentUser();
        mUserName = mCurrentUser.getUsername();

        mListId = mCurrentUser.get(ParseConstants.KEY_LIST_RELATION).toString();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_wish_actvity, menu);
        mSaveListItem = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save) {
            debug();
//            createListItem();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void debug() {
        String listId = mListId;
        Log.d(TAG, listId);
    }

    protected ListItem createListItem() {
        ListItem listItem = new ListItem() {


        };
        return listItem;
    }
}
