package com.sqlite.sqliteapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Pratyoush on 02-11-2015.
 */
public class ParseApplication extends Application {
    public static final String YOUR_APPLICATION_ID = "cSMb5B1Yob7iSIyMv8KaFn3odTgAQdBwWx9mNcWD";
    public static final String YOUR_CLIENT_KEY = "0dWn9WVTrFj5laRyvxboYSftoCByWnWw22QLaq06";

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

        // Test creation of object
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

        // ...
    }
}
