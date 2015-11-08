package com.sqlite.sqliteapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;
import com.unboundid.ldap.sdk.BindRequest;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SimpleBindRequest;
import com.unboundid.ldap.sdk.controls.PasswordExpiredControl;
import com.unboundid.ldap.sdk.controls.PasswordExpiringControl;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

import javax.net.SocketFactory;

import java.security.GeneralSecurityException;


public class Main2Activity extends AppCompatActivity {
    LDAPConnection ldapConnection = null;
    DatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        TextView tx = (TextView)findViewById(R.id.Title);
        Typeface cd = Typeface.createFromAsset(getAssets(), "fonts/Caviar_Dreams_Bold.ttf");
        tx.setTypeface(cd);


        myDB = new DatabaseHelper(this);

        Button login = (Button)findViewById(R.id.login);

        final EditText username = (EditText) findViewById(R.id.username);
     //   final String name = username.getText().toString();
        final EditText password = (EditText) findViewById(R.id.password);

        final EditText test = (EditText)findViewById(R.id.test);

         login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                            try {


                                final SocketFactory socketFactory;
                                final SSLUtil sslUtil = new SSLUtil(null,new TrustAllTrustManager());
                                socketFactory = sslUtil.createSSLSocketFactory();
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                ldapConnection = new LDAPConnection(socketFactory,"128.227.9.22", 636);
                                final String name = username.getText().toString();
                                final String name2 = password.getText().toString();
                                BindRequest bindRequest = new SimpleBindRequest("uid=" + name, name2);

                                BindResult bindResult = ldapConnection.bind(bindRequest);
                               if (bindResult.getResultCode() == ResultCode.SUCCESS) {

                                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                                    intent.putExtra("ufid", name);
                                    startActivityForResult(intent, 0);
                                    ldapConnection.close();
                                    // }
                                }


                            }
                            catch (LDAPException e) {
                                test.setText("Incorrect Username/Password.\nPlease try again!");
                                test.setEnabled(false);
                                //test.append(e.toString());
                                ldapConnection.close();

                            }
                            catch(GeneralSecurityException exception) {
                                //test.setText(exception.toString());
                                ldapConnection.close();
                            }
                        }

                    }

        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
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



