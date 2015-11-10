package com.sqlite.sqliteapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sqlite.sqliteapp.Views.MenuFly;

import java.io.ByteArrayOutputStream;

public class book_upload extends AppCompatActivity {
    MenuFly root;
    EditText editTitle, editAuthor, editIsbn, editEdition, editYearOfPublication, editRelated_course, editDepositAmount;
    Button addBooks, imageButton;
    Bitmap bm;
    private String selectedImagePath = "";
    ParseFile parseFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.root = (MenuFly) this.getLayoutInflater().inflate(R.layout.book_upload, null);
        setContentView(root);

        //setContentView(R.layout.book_upload);

        editAuthor = (EditText)findViewById(R.id.author);
        editTitle = (EditText)findViewById(R.id.title);
        editIsbn = (EditText)findViewById(R.id.isbn);
        editEdition = (EditText)findViewById(R.id.edition);
        editYearOfPublication = (EditText)findViewById(R.id.year_of_publication);
        editRelated_course = (EditText)findViewById(R.id.course);
        editDepositAmount = (EditText)findViewById(R.id.depositAmount);
        addBooks = (Button)findViewById(R.id.addBook);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "cSMb5B1Yob7iSIyMv8KaFn3odTgAQdBwWx9mNcWD", "0dWn9WVTrFj5laRyvxboYSftoCByWnWw22QLaq06");

        imageButton = (Button) findViewById(R.id.imageButton1);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                selectImage();
            }
        });

        addBookData();
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                selectedImagePath = getAbsolutePath(data.getData());
                bm = decodeFile(selectedImagePath);
                if(bm != null) {
                 //   im.setImageBitmap(bm);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    byte[] imageData = bytes.toByteArray();
                    parseFile = new ParseFile("Book_image.jpg", imageData);
                }
            }
        }
    }

    private void selectImage() {
        final CharSequence[] items = {  "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(book_upload.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Choose from Library")) {
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


    public void addBookData(){
        addBooks.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v2) {

                        boolean invalid = false;
                        String author = editAuthor.getText().toString();
                        String title = editTitle.getText().toString();
                        String isbn  = editIsbn.getText().toString();
                        String edition = editEdition.getText().toString();
                        String related_course = editRelated_course.getText().toString();
                        String yearOfPublication = editYearOfPublication.getText().toString();
                        String depositAmount = editDepositAmount.getText().toString();

                        if (title.length() == 0) {
                            editTitle.setError("Title of the Book is required");
                            invalid = true;
                        }


                        if (author.length() == 0) {
                            editAuthor.setError("Author is required");
                            invalid = true;
                        }

                        if (!invalid) {
                            final ParseObject uploadBooks = new ParseObject("UploadBooks");
                            uploadBooks.put("Title", title);
                            uploadBooks.put("Author", author);
                            uploadBooks.put("ISBN", isbn);
                            uploadBooks.put("Edition",edition);
                            uploadBooks.put("Year_Of_Publication", yearOfPublication);
                            uploadBooks.put("Related_Course", related_course);
                            uploadBooks.put("Deposit_Amount", depositAmount);
                           //****** uploadBooks.put("UserID", user.getObjectId());
                            uploadBooks.saveInBackground();

                            if (parseFile != null) {
                                parseFile.saveInBackground(new SaveCallback() {
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            Toast.makeText(getApplicationContext(),
                                                    "File could not be added.",
                                                    Toast.LENGTH_LONG).show();
                                        } else {
                                            uploadBooks.put("image", parseFile);
                                            uploadBooks.saveEventually();

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

    public void toggleMenu(View v){

        this.root.toggleMenu();
    }


    public void searchBooks(View view){
        String button_text;
        button_text = ((Button) view).getText().toString();
        if(button_text.equals("Search Books")){
            Intent intent = new Intent(this, find_books.class);
            startActivity(intent);
        }
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
