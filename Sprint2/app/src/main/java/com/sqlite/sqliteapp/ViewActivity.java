package com.sqlite.sqliteapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sqlite.sqliteapp.Views.MenuFly;

import java.util.List;

public class ViewActivity extends Activity{
    private RatingBar ratingBar;
    private TextView nameValue, emailValue, phoneValue;
    MenuFly root;

    private ParseUser rateThisUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.root = (MenuFly) this.getLayoutInflater().inflate(R.layout.activity_view, null);
        setContentView(root);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        }, intentFilter);


        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
       /* Button button = (Button) findViewById(R.id.btnSave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

            }
        });*/
        Intent intent = getIntent();
        String EXTRA_MESSAGE = "MESSAGE";
        final String userName = intent.getStringExtra(EXTRA_MESSAGE);

        final ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", userName);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {

                try {
                    if (e == null) {
                        rateThisUser = objects.get(0);

                        String existing_rating = rateThisUser.getString("Rating");
                        ParseFile image = rateThisUser.getParseFile("image");
                        final ParseImageView imageView = (ParseImageView) findViewById(R.id.icon);
                        imageView.setParseFile(image);
                        imageView.loadInBackground(new GetDataCallback() {
                            public void done(byte[] data, ParseException e) {
                                // The image is loaded and displayed!
                            }
                        });

                        int num_shared = (int) rateThisUser.getNumber("BooksNum");
                        int max = 0;
                        query.orderByDescending("BooksNum");

                        float newRating = (float) 0.0;
                        ParseUser u = query.find().get(0);
                        max = (int) u.getNumber("BooksNum");
                        if (max != (float) 0.0){
                            newRating = (5) * (num_shared / max);
                        }
                        newRating = (float) (newRating * 0.6);

                        nameValue = (TextView) findViewById(R.id.nameV);
                        emailValue = (TextView) findViewById(R.id.emailV);
                        phoneValue = (TextView) findViewById(R.id.phoneV);

                        emailValue.setText(String.valueOf(rateThisUser.getEmail()));
                        nameValue.setText(String.valueOf(rateThisUser.getString("ActualName")));
                        phoneValue.setText(String.valueOf(rateThisUser.getString("PhoneNumber")));

                        if (existing_rating != null) {
                            ratingBar.setRating(Float.parseFloat(existing_rating));
                        }else{
                            existing_rating = "0.0";
                            ratingBar.setRating(Float.parseFloat(Float.toString(newRating)));
                        }

                        final float finalNewRating = newRating;
                        final String finalExisting_rating = existing_rating;
                        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                float rate = (float) ((((Float.parseFloat(finalExisting_rating) + rating ) / 2 )  * 0.5) + finalNewRating);
                                ratingBar.setRating(rating);
                                rateThisUser.put("Rating", String.valueOf(rate));
                                rateThisUser.saveEventually();
                            }
                        });
                    }
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void viewAccount(View view){
        String button_text;
        button_text = ((Button) view).getText().toString();
        if(button_text.equals("My Account")){
            Intent intent = new Intent(this, ViewActivity.class);
            intent.putExtra("MESSAGE", ParseUser.getCurrentUser().getUsername());
            startActivity(intent);
        }
    }

    public void addListenerOnButton() {

        //   ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        //  button = (Button) findViewById(R.id.button);

     /*   button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(ViewActivity.this, String.valueOf(ratingBar.getRating()), Toast.LENGTH_LONG).show();
            }

        });*/

    }

    public void toggleMenu(View v){
        this.root.toggleMenu();
    }

    public void openBook(View view){
        String button_text;
        button_text = ((Button) view).getText().toString();
        if(button_text.equals("Add Book")){
            Intent intent = new Intent(this, book_upload.class);
            startActivity(intent);
        }
    }

    public void myBooks(View view){
        String button_text;
        button_text = ((Button) view).getText().toString();
        if(button_text.equals("My Books")){
            Intent intent = new Intent(this, my_book.class);
            startActivity(intent);
        }
    }

    public void searchBooks(View view){
        String button_text;
        button_text = ((Button) view).getText().toString();
        if(button_text.equals("Search Books")){
            Intent intent = new Intent(this, find_books.class);
            startActivity(intent);
        }
    }

    public void logOut(View view){
        String button_text;
        button_text = ((Button) view).getText().toString();
        if(button_text.equals("Logout")){
            ParseUser.logOut();
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.package.ACTION_LOGOUT");
            sendBroadcast(broadcastIntent);
            /*ParseUser.logOut();
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);*/
        }
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
