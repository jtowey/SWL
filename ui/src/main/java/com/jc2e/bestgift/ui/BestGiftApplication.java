package com.jc2e.bestgift.ui;

import android.app.Application;

import com.jc2e.bestgift.ui.model.ListItem;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Jtowey on 8/31/2014.
 */
public class BestGiftApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, ParseConstants.PARSE_APPLICATION_ID, ParseConstants.PARSE_CLIENT_KEY);

/*        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
// Optionally enable public read access while disabling public write access.
// defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);*/
    }
}
