package com.sqlite.sqliteapp;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.Vector;

public class find_books extends AppCompatActivity {

    ListView lv;
    SearchView search;
    TextView textView3;

    String names[] ={ "Praks", "Kunal", "Shilpa"};
//    Vector<String> names1;

    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_books);

        lv = (ListView) findViewById(R.id.listView);

        Parse.initialize(this, "cSMb5B1Yob7iSIyMv8KaFn3odTgAQdBwWx9mNcWD", "0dWn9WVTrFj5laRyvxboYSftoCByWnWw22QLaq06");
        final ParseQuery<ParseObject> query2 = ParseQuery.getQuery("UploadBooks");

       // SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        //search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        search = (SearchView)findViewById(R.id.searchView);
        textView3 = (TextView)findViewById(R.id.textView3);
        search.setQueryHint("SearchView");



        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final ParseQuery<ParseObject> query2 = ParseQuery.getQuery("UploadBooks");
                query2.whereContains("Title", search.getQuery().toString());
                query2.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {

                            for (ParseObject nameObject : objects) {
                                String name = nameObject.get("Author").toString();
                                Log.d("Title", name);
                                textView3.setText(name);
                                //names1.add(name);
                            }

                        } else {
                            Log.d("Author", "Error: " + e.getMessage());
                        }
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                textView3.setText("no text");
                //textView3.getText().length();
                return false;
            }


        });

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        lv.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_books, menu);
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


