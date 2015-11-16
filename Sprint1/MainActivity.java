package com.sqlite.sqliteapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;
import com.sqlite.sqliteapp.Views.MenuFly;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    EditText editName, editPhone, editEmail, editAddress, editUfid;
    Button addDataButton, viewDataButton;
    MenuFly root;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDB = new DatabaseHelper(this);
        String ufid = getIntent().getExtras().getString("ufid");
            this.root = (MenuFly) this.getLayoutInflater().inflate(R.layout.activity_main, null);
            setContentView(root);

            TextView tx = (TextView) findViewById(R.id.Title);
            Typeface cd = Typeface.createFromAsset(getAssets(), "fonts/Caviar_Dreams_Bold.ttf");
            tx.setTypeface(cd);


            editUfid = (EditText) findViewById((R.id.ufid));
            editUfid.setText("UFID - " + ufid);
            editUfid.setEnabled(false);
            editName = (EditText) findViewById(R.id.name);
            editPhone = (EditText) findViewById(R.id.phone);
            editEmail = (EditText) findViewById(R.id.email);
            editAddress = (EditText) findViewById(R.id.address);
            addDataButton = (Button) findViewById(R.id.submitbutton);
            // viewDataButton = (Button)findViewById(R.id.viewdata);
            addData();

            //viewAll();
       // }
    }

    public void toggleMenu(View v){
        this.root.toggleMenu();
    }

    public void addData(){
        addDataButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        boolean invalid = false;
                        //String ufid = editUfid.getText().toString();
                        String name = editName.getText().toString();
                        String phone_no = editPhone.getText().toString();
                        String email = editEmail.getText().toString();
                        Pattern pattern = Patterns.EMAIL_ADDRESS;
                        if (name.length() == 0) {
                            editName.setError("Name is required!");
                            invalid = true;
                        }
                        if ((phone_no.length() > 0) && (phone_no.length() != 10)) {
                            editPhone.setError("Invalid Phone Number!");
                            invalid = true;
                        }
                        if (!pattern.matcher(email).matches()) {
                            editEmail.setError("Invalid Email!");
                            invalid = true;
                        }
                        if (!invalid) {
                            boolean isInserted = myDB.insertData(editUfid.getText().toString(),
                                    name,
                                    phone_no,
                                    editEmail.getText().toString(),
                                    editAddress.getText().toString());

                            if (isInserted) {
                                Toast.makeText(MainActivity.this, " Data is Inserted!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(v.getContext(), MainActivity3.class);
                                intent.putExtra("message", name);
                                startActivityForResult(intent, 0);
                            }
                            else
                                Toast.makeText(MainActivity.this, " Data is NOT Inserted!", Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );
    }
    public void viewAll(){
        viewDataButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Cursor res = myDB.getAllData();
                        if(res.getCount() == 0){
                            showMessage("Error", "Nothing Found");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while(res.moveToNext()){
                            buffer.append("Name: " + res.getString(0)+"\n");
                            buffer.append("Phone: " + res.getString(1)+"\n");
                            buffer.append("E-mail: " + res.getString(2)+"\n");
                            buffer.append("Address: " + res.getString(3)+"\n\n");
                        }
                        showMessage("Data", buffer.toString());
                    }
                }
        );
    }
    public boolean Exists(String id) {
        if(myDB.Exists(id))
            return true;
        return false;
    }
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
