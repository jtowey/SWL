package com.jc2e.bestgift.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jc2e.bestgift.ui.adapter.NavDrawerListAdapter;
import com.jc2e.bestgift.ui.model.NavDrawerItem;
import com.jc2e.bestgift.ui.sections.CommunityFragment;
import com.jc2e.bestgift.ui.sections.FindPeopleFragment;
import com.jc2e.bestgift.ui.sections.FriendsFragment;
import com.jc2e.bestgift.ui.sections.HomeFragment;
import com.jc2e.bestgift.ui.sections.ListsFragment;
import com.jc2e.bestgift.ui.sections.ProfileFragment;
import com.parse.ParseAnalytics;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int FILE_SIZE_LIMIT = 1024 * 1024 * 10; // 10 MB
    private static final int TAKE_PHOTO_REQUEST = 0;
    private static final int TAKE_VIDEO_REQUEST = 1;
    private static final int SELECT_PHOTO_REQUEST = 2;
    private static final int SELECT_VIDEO_REQUEST = 3;
    private static final int MEDIA_TYPE_IMAGE = 4;
    private static final int MEDIA_TYPE_VIDEO = 5;
    protected Uri mMediaUri;

    protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0: // Take Picture
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    if (mMediaUri == null) {
                        // display an error
                        Toast.makeText(MainActivity.this, R.string.error_external_storage, Toast.LENGTH_LONG).show();
                    } else {
                        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                        startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                    }
                    break;

                case 1: // Take Video
                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                    if (mMediaUri == null) {
                        // display an error
                        Toast.makeText(MainActivity.this, R.string.error_external_storage, Toast.LENGTH_LONG).show();
                    } else {
                        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                        startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUEST);
                    }
                    break;

                case 2: // Choose Picture
                    Intent selectPhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    selectPhotoIntent.setType("image/*");
                    startActivityForResult(selectPhotoIntent, SELECT_PHOTO_REQUEST);
                    break;

                case 3: // Choose Video
                    Intent selectVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    selectVideoIntent.setType("video/*");
                    Toast.makeText(MainActivity.this, R.string.video_file_size_warning, Toast.LENGTH_LONG).show();
                    startActivityForResult(selectVideoIntent, SELECT_VIDEO_REQUEST);
                    break;
            }
        }

        private Uri getOutputMediaFileUri(int mediaType) {
            if (isExternalStorageAvailable()) {
                // get Uri

                // 1. Get the external storage directory
                String appName = MainActivity.this.getString(R.string.app_name);
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appName);

                // 2. Create our subdirectory
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.e(TAG, "Failed to create directory");
                        return null;
                    }
                }
                // 3. Create a file name
                File mediaFile;
                Date now = new Date();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);

                // 4. Create the file

                String path = mediaStorageDir.getPath() + File.separator;
                if (mediaType == MEDIA_TYPE_IMAGE) {
                    mediaFile = new File(path + "IMG_" + timeStamp + ".jpg");
                } else if (mediaType == MEDIA_TYPE_VIDEO) {
                    mediaFile = new File(path + "VID_" + timeStamp + ".mp4");
                } else {
                    return null;
                }

                Log.d(TAG, "File: " + Uri.fromFile(mediaFile));

                // 5. Return the file's URI
                return Uri.fromFile(mediaFile);
            } else {
                return null;
            }
        }

        private boolean isExternalStorageAvailable() {
            String state = Environment.getExternalStorageState();

            if (state.equals(Environment.MEDIA_MOUNTED)) {
                return true;
            } else {
                return false;
            }
        }
    };

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private Boolean mListOption;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        parseAnal();

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));


        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PHOTO_REQUEST || requestCode == SELECT_VIDEO_REQUEST) {
                if (data == null) {
                    Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_LONG).show();
                } else {
                    mMediaUri = data.getData();
                }

                Log.i(TAG, "mMediaUri: " + mMediaUri);
                if (requestCode == SELECT_VIDEO_REQUEST) {
                    // Make sure file size is less than 10 MB
                    int fileSize = 0;

                    InputStream inputStream = null;
                    try {
                        inputStream = getContentResolver().openInputStream(mMediaUri);
                        fileSize = inputStream.available();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_LONG).show();
                        return;
                    } catch (IOException e) {
                        Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_LONG).show();
                        return;
                    } finally {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (fileSize >= FILE_SIZE_LIMIT) {
                        Toast.makeText(this, R.string.error_file_size_too_large, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            } else {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                sendBroadcast(mediaScanIntent);
            }
        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar action clicks
        switch (item.getItemId()) {
            case R.id.action_view_list:
                Intent viewListIntent = new Intent(this, UserListActivity.class);
                startActivity(viewListIntent);
                return true;
            case R.id.action_new_wish:
                Intent newWishIntent = new Intent(this, NewWishActivity.class);
                startActivity(newWishIntent);
                return true;
            case R.id.action_logout:
                ParseUser.logOut();
                navigateToLogin();
                return true;
            case R.id.action_edit_friends:
                Intent editFriendsIntent = new Intent(this, EditFriendsActivity.class);
                startActivity(editFriendsIntent);
                return true;
            case R.id.action_camera:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.camera_choices, mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        if (drawerOpen) {
            menu.setGroupVisible(Menu.NONE, !drawerOpen);
            menu.findItem(R.id.action_logout).setVisible(!drawerOpen);
            menu.findItem(R.id.action_new_wish).setVisible(!drawerOpen);
        } else {
            menu.findItem(R.id.action_view_list).setVisible(mListOption);
            menu.findItem(R.id.action_new_wish).setVisible(!mListOption);
            menu.findItem(R.id.action_logout).setVisible(!mListOption);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        Boolean profileSelected = false;

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new FindPeopleFragment();
                break;
            case 2:
                fragment = new ProfileFragment();
                profileSelected = true;
                break;
            case 3:
                fragment = new CommunityFragment();
                break;
            case 4:
                fragment = new FriendsFragment();
                break;
            case 5:
                fragment = new ListsFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

            if (profileSelected == true) {
                mListOption = true;
            } else {
                mListOption = false;
            }
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }


    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void parseAnal() {
        ParseAnalytics.trackAppOpened(getIntent());

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            // intent to login activity from main.
            navigateToLogin();
        } else {
            Log.i(TAG, currentUser.getUsername());
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, com.jc2e.bestgift.ui.frontend.LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

}