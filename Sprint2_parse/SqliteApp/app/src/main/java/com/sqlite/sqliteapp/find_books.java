package com.sqlite.sqliteapp;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class find_books extends ListActivity {

    SearchView search;
    ArrayAdapter<String> adapter;
    ArrayList<String> objectid = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_books);

        search = (SearchView)findViewById(R.id.searchView);
        search.setQueryHint("SearchView");

        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final ParseQuery<ParseObject> query2 = ParseQuery.getQuery("UploadBooks");
                query2.whereContains("Title", search.getQuery().toString());
<<<<<<< HEAD
=======

>>>>>>> cbdbca022c048d27de411c97669199040146e1e7
                query2.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            ArrayList<String> names = new ArrayList<String>();
                            int i = 0;
                            for (ParseObject nameObject : objects) {
                                String name = nameObject.get("Author").toString();
                                Log.d("Title", name);
                                names.add(i, name);
<<<<<<< HEAD
=======
                                objectid.add(nameObject.getObjectId());

>>>>>>> cbdbca022c048d27de411c97669199040146e1e7
                                i++;
                            }
                            adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, names);
                            getListView().setAdapter(adapter);
<<<<<<< HEAD
=======
                            getListView().setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent intent = new Intent(parent.getContext(), BookDetails.class);
                                            intent.putExtra("oid", objectid.get(position));
                                            startActivityForResult(intent, 0);

                                        }
                                    }
                            );

>>>>>>> cbdbca022c048d27de411c97669199040146e1e7

                        } else {
                            Log.d("Author", "Error: " + e.getMessage());
                        }
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               return false;
            }


        });

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