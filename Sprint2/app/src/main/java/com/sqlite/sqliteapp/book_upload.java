package com.sqlite.sqliteapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sqlite.sqliteapp.Views.MenuFly;

import java.util.List;

public class book_upload extends AppCompatActivity {
    MenuFly root;
    EditText editTitle, editAuthor, editIsbn, editEdition, editYearOfPublication, editRelated_course, editDepositAmount;
    Button addBooks;
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

        addBookData();
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
                            ParseObject uploadBooks = new ParseObject("UploadBooks");
                            uploadBooks.put("Title", title);
                            uploadBooks.put("Author", author);
                            uploadBooks.put("ISBN", isbn);
                            uploadBooks.put("Edition",edition);
                            uploadBooks.put("Year_Of_Publication", yearOfPublication);
                            uploadBooks.put("Related_Course", related_course);
                            uploadBooks.put("Deposit_Amount", depositAmount);
                            ParseUser user= ParseUser.getCurrentUser();
                            uploadBooks.put("Owner1",user);

                            int x = user.getInt("BooksNum");

                            user.put("BooksNum", x +1 );
                           //****** uploadBooks.put("UserID", user.getObjectId());
                            uploadBooks.saveInBackground();
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
