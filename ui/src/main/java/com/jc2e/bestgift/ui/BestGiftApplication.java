package com.jc2e.bestgift.ui;

import android.app.Application;

import com.jc2e.bestgift.ui.model.ListItem;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Jtowey on 8/31/2014.
 */
public class BestGiftApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(ListItem.class);
        Parse.initialize(this, "81fP2TR7zvJer8MzOGFvbbcYqqqm0E3dM2LYqPZX", "AjJ52wLgtJfWQ0bWTmHPR12KAHfe18rth5civK5b");

/*        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
// Optionally enable public read access while disabling public write access.
// defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);*/
    }
}
