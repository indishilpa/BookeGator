package com.sqlite.sqliteapp;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sqlite.sqliteapp.Views.MenuFly;

public class my_book extends ListActivity {
    MenuFly root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.root = (MenuFly) this.getLayoutInflater().inflate(R.layout.activity_my_book, null);
        setContentView(root);
        final ParseQuery<ParseObject> query2 = ParseQuery.getQuery("UploadBooks");
      //  query2.whereContains("Owner1", query2.get);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_book, menu);
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
