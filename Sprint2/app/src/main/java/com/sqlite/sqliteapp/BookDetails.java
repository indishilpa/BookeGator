package com.sqlite.sqliteapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class BookDetails extends AppCompatActivity {
    TextView textTitle, textAuthor, textEdition, textYear, textDeposit, textISBN;
    Button contactOwner, issueRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        textTitle = (TextView)findViewById(R.id.title);
        textAuthor = (TextView)findViewById(R.id.author);
        textEdition = (TextView)findViewById(R.id.edition);
        textYear = (TextView)findViewById(R.id.year);
        contactOwner = (Button)findViewById(R.id.contactOwner);
        textISBN = (TextView)findViewById(R.id.isbn);
        textDeposit = (TextView)findViewById(R.id.deposit);

        String oid = getIntent().getExtras().getString("oid");
        viewAll(oid);
    }

    public void viewAll(final String oid) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UploadBooks");
        query.getInBackground(oid, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                    textTitle.setText(object.getString("Title"));
                    //textTitle.setEnabled(false);
                    textAuthor.setText("by " + object.getString("Author"));
                    // textAuthor.setEnabled(false);
                    if(object.getString("Edition") != null) {
                        textEdition.setText( "Edition - " + object.getString("Edition"));
                    }
                    if(object.getString("Year_Of_Publication") != null) {
                        textYear.setText("Year of Publication - " + object.getString("Year_Of_Publication"));
                    }
                    if(object.getString("ISBN") != null) {
                        textISBN.setText("ISBN - " + object.getString("ISBN"));
                    }
                    if(object.getString("Deposit_Amount") != null) {
                        textDeposit.setText("Deposit Amount - " + object.getString("Deposit_Amount"));
                    }

                } else {
                    textTitle.setText(e.toString());

                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
