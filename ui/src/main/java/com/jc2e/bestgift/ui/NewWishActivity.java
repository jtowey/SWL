package com.jc2e.bestgift.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class NewWishActivity extends Activity {

    public static final String TAG = NewWishActivity.class.getSimpleName();

    protected String mListId;
    protected String mUserName;

    protected ParseUser mCurrentUser;
    protected ParseObject mUserList;
    protected String mEmail;

    protected MenuItem mSaveListItem;

    // List item fields
    private String mItemId;
    private String mItemNum;
    private EditText mItemName;
    private EditText mItemDesc;
    private EditText mItemPrice;
    private EditText mItemCatNum;
    private EditText mItemQty;
    private EditText mItemSize;
    private EditText mItemColor;
    private String mListItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_wish);

        mCurrentUser = ParseUser.getCurrentUser();
        mUserName = mCurrentUser.getUsername();
        mEmail = mCurrentUser.getEmail();

        mListId = mCurrentUser.get(ParseConstants.KEY_LIST_RELATION).toString();
        mItemNum = null;
        mItemName = (EditText) findViewById(R.id.editWishName);
        mItemDesc = (EditText) findViewById(R.id.editWishDesc);
        mItemPrice = (EditText) findViewById(R.id.editPrice);
        mItemCatNum = (EditText) findViewById(R.id.editItemNum);
        mItemQty = (EditText) findViewById(R.id.editQty);
        mItemSize = (EditText) findViewById(R.id.editSize);
        mItemColor = (EditText) findViewById(R.id.editColor);
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
            createListItem();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void debug() {
        String listId = mListId;
        Log.d(TAG, listId);
    }

    protected void createListItem() {

        String mListId = mCurrentUser.get(ParseConstants.KEY_LIST_RELATION).toString();
//        String itemNum = getListItemId();
        String itemName = mItemName.getText().toString().trim();
        String itemDesc = mItemDesc.getText().toString().trim();
        String itemPrice = mItemPrice.getText().toString().trim();
        String itemCatNum = mItemCatNum.getText().toString().trim();
        String itemQty = mItemQty.getText().toString().trim();
        String itemSize = mItemSize.getText().toString().trim();
        String itemColor = mItemColor.getText().toString().trim(  );

        if (itemName.isEmpty() || itemDesc.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(NewWishActivity.this);
            builder.setMessage(R.string.new_wish_error_message);
            builder.setTitle(R.string.new_wish_error_title);      // these three could be chained
            builder.setPositiveButton(android.R.string.ok, null);

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {

            ParseObject listItem = new ParseObject(ParseConstants.CLASS_LIST_ITEM);
            listItem.put(ParseConstants.KEY_LIST_OWNER_ID, mCurrentUser.getObjectId());
            listItem.put(ParseConstants.KEY_LIST_OWNER_EMAIL, mEmail);
            listItem.put(ParseConstants.KEY_ITEM_NAME, itemName);
            listItem.put(ParseConstants.KEY_ITEM_DESC, itemDesc);
            listItem.put(ParseConstants.KEY_ITEM_PRICE, itemPrice);
            listItem.put(ParseConstants.KEY_ITEM_CAT_NUM, itemCatNum);
            listItem.put(ParseConstants.KEY_ITEM_QTY, itemQty);
            listItem.put(ParseConstants.KEY_ITEM_SIZE, itemSize);
            listItem.put(ParseConstants.KEY_ITEM_COLOR, itemColor);
            listItem.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {

                        Toast.makeText(NewWishActivity.this, "You're wish has been saved to your list!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(NewWishActivity.this, UserListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {

                        Log.e(TAG, e.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewWishActivity.this);
                        builder.setMessage(e.getMessage())
                                .setTitle(R.string.error_title)
                                .setPositiveButton(android.R.string.ok, null);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            });
        }
    }

}