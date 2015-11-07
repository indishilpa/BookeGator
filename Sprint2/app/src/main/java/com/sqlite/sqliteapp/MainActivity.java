package com.sqlite.sqliteapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.sqlite.sqliteapp.Views.BookeUser;
import com.sqlite.sqliteapp.Views.MenuFly;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.sqlite.sqliteapp.MESSAGE";

    DatabaseHelper myDB;
    EditText username, editPhone, editEmail, editAddress, editUfid;
    Button addDataButton, viewDataButton;
    MenuFly root;

    ImageButton imageButton;
    ImageView im;
    private Bitmap bm;
    private ParseFile parseFile;
    String imgPath;
    private String selectedImagePath = "";
    private BookeUser bookUser;

    String name, phone_no, email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.root = (MenuFly) this.getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(root);

        //  setContentView(R.layout.activity_main);
        imageButton = (ImageButton) findViewById(R.id.imageButton1);
        im = (ImageView)findViewById(R.id.iv);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                selectImage();
            }
        });

   //     myDB = new DatabaseHelper(this);
        String ufid = getIntent().getExtras().getString("ufid");

        TextView tx = (TextView) findViewById(R.id.Title);
        Typeface cd = Typeface.createFromAsset(getAssets(), "fonts/Caviar_Dreams_Bold.ttf");
        tx.setTypeface(cd);


        editUfid = (EditText) findViewById((R.id.ufid));
        editUfid.setText("UFID - " + ufid);
        editUfid.setEnabled(false);
        username = (EditText) findViewById(R.id.name);
        editPhone = (EditText) findViewById(R.id.phone);
        editEmail = (EditText) findViewById(R.id.email);
        editAddress = (EditText) findViewById(R.id.address);
        addDataButton = (Button) findViewById(R.id.submitbutton);
        // viewDataButton = (Button)findViewById(R.id.viewdata);
        addData();

        //viewAll();
        // }
    }

    private void savePhoto(byte[] data) {

        parseFile = new ParseFile("image.jpg", data);
     //   parseFile.saveInBackground();

    /*    final ParseObject pObject = new ParseObject("ExampleObject");
        pObject.put("username", "alice");
        pObject.put("image", parseFile);
        pObject.saveInBackground();*/
    }

    public Bitmap decodeFile(String path) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            final int REQUIRED_SIZE = 70;

            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getImagePath() {
        return imgPath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                bm = (Bitmap) data.getExtras().get("data");

            /*    selectedImagePath = getImagePath();
                bm = decodeFile(selectedImagePath);*/
                if (bm != null) {
                    im.setImageBitmap(bm);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    byte[] imageData = bytes.toByteArray();
                    savePhoto(imageData);
                }
            } else if (requestCode == 2) {
                selectedImagePath = getAbsolutePath(data.getData());
                bm = decodeFile(selectedImagePath);
                if(bm != null) {
                    im.setImageBitmap(bm);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    byte[] imageData = bytes.toByteArray();
                    savePhoto(imageData);
                }
            }
        }
    }

    public Uri setImageUri() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) , "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    public String getAbsolutePath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);

                 /*   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);*/
                } else if (items[item].equals("Choose from Library")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 2);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void toggleMenu(View v){
        this.root.toggleMenu();
    }

    public Boolean validate_data(String name, String phone_no, String email){
        Boolean invalid = false;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (name.length() == 0) {
            username.setError("Name is required!");
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
        return invalid;
    }

    public void addData(){
        addDataButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        // Retrieve the text entered from the EditText
                        name = username.getText().toString();
                        phone_no = editPhone.getText().toString();
                        email = editEmail.getText().toString();

                        boolean invalid = validate_data(name, phone_no, email);

                        if (!invalid) {
                            ParseUser userB = new ParseUser();
                            userB.setUsername(name);
                            userB.setEmail(email);
                            userB.setPassword("213");
                            userB.put("PhoneNumber", phone_no);
                            userB.put("Address", editAddress.getText().toString());

                      /*      bookUser.setAddress(editAddress.getText().toString());
                            bookUser.setPhoneNumber(phone_no);
                            bookUser.setUsername(name);
                            bookUser.setEmailAddress(email);*/

                            userB.signUpInBackground(new SignUpCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getApplicationContext(),
                                                "Successfully Signed up.",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Sign up Error", Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            });

                            if (parseFile != null) {
                                parseFile.saveInBackground(new SaveCallback() {
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            Toast.makeText(getApplicationContext(),
                                                    "File could not be added.",
                                                    Toast.LENGTH_LONG).show();
                                        } else {
                                            ParseUser currentUser = ParseUser.getCurrentUser();
                                            currentUser.put("image", parseFile);
                                            currentUser.saveEventually();

                                            Toast.makeText(getApplicationContext(),
                                                    "added image to database", Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                });
                            }
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void findUserName(View view) {
        String user2 = "shilpa goel";

        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra(EXTRA_MESSAGE, user2);
        startActivity(intent);
    }
}
