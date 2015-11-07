package com.sqlite.sqliteapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.List;

/**
 * Created by Shilpa on 11/7/2015.
 */
public class ViewActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final String userName = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        final TextView textView = new TextView(this);
        textView.setTextSize(40);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", userName);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        ParseUser u = objects.get(i);
                        String add = u.getString("Address");
                        textView.setText(userName + "\t" + add);
                        Log.d("tag",userName + "\t" + add);
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
