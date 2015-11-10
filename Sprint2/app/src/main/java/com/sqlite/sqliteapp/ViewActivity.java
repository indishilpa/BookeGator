package com.sqlite.sqliteapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.List;

public class ViewActivity extends Activity{
    private RatingBar ratingBar;
    private TextView ratingValue;
    private Button button;
    private ParseUser rateThisUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view);

        addListenerOnRatingBar();
        addListenerOnButton();


        Intent intent = getIntent();
        final String userName = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        final TextView textView = new TextView(this);
        textView.setTextSize(40);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", userName);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    //for (int i = 0; i < objects.size(); i++) {
                        rateThisUser = objects.get(0);
                        String add = rateThisUser.getString("Address");
                        textView.setText(userName + "\t" + add);
                        Log.d("tag", userName + "\t" + add);
                    //}
                }
            }
        });
    }

    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingValue = (TextView) findViewById(R.id.txtRatingValue);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                ratingValue.setText(String.valueOf(rating));
                rateThisUser.put("Rating", String.valueOf(rating));
                rateThisUser.saveEventually();

            }
        });
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(ViewActivity.this, String.valueOf(ratingBar.getRating()), Toast.LENGTH_LONG).show();
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
