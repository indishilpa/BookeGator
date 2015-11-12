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
        test.setText("line 0");
        final SSLUtil sslUtil = new SSLUtil(null,new TrustAllTrustManager());
        test.setText("line 1");
        final SocketFactory socketFactory;
        test.setText("line 2");
        //LDAPConnection ldapConnection = null;
        test.setText("line 3");
        try {

            // Create the socket factory that will be used to make a secure
            // connection to the server.
            socketFactory = sslUtil.createSSLSocketFactory();
            test.setText("line 4");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ldapConnection = new LDAPConnection(socketFactory,"128.227.9.22", 636);
            test.setText("line 5");

        } catch(LDAPException ldapException) {

            test.append(ldapException.toString());
            // System.err.println(ldapException);
            // System.exit(ldapException.getResultCode().intValue());

        }
        catch(GeneralSecurityException exception) {
            test.setText(exception.toString());
            // System.err.println(exception);
            // System.exit(1);

        }

        // if(ldap(name,name2)==true)
        //{
            //Intent intent = new Intent(this,SecondActivity.class);
           // startActivity(intent);
        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        try {

                          //  String dn = "uid=pratyoush.s";
                          //  String password = "PsRrAiTv16";
                           // long maxResponseTimeMillis = 1000;

                                final String name = username.getText().toString();
                                final String name2 = password.getText().toString();
                                BindRequest bindRequest = new SimpleBindRequest("uid=" + name, name2);
                                test.setText("line 6");
                                // bindRequest.setResponseTimeoutMillis(maxResponseTimeMillis);
                                test.append("uid=" + name + name2);
                                BindResult bindResult = ldapConnection.bind(bindRequest);
                                test.append("line 8");

                                if (true) {
                                    test.append("line 15");
                                    test.setText(bindResult.getResultCode().toString());
                                    /*if(myDB.Exists(name)){
                                        test.append("line 16");
                                        Intent intent = new Intent(v.getContext(), MainActivity3.class);
                                        //intent.putExtra("ufid", name);
                                        startActivityForResult(intent, 0);

                                    }
                                    else {*/
                                        test.append("line 17");
                                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                                        intent.putExtra("ufid", name);
                                        startActivityForResult(intent, 0);
                                        ldapConnection.close();
                                   // }
                                }
                                ldapConnection.close();


                            // Check for the password expiring or password expired
                            // response controls
                        /*PasswordExpiredControl pwdExpired =
                            PasswordExpiredControl.get(bindResult);
                        if(pwdExpired == null) {
                             System.out.println("The password expired control was not included in " +
                             "the BIND response.");
                        } else {
                        System.err.println("WARNING:  You must change your password " +
                        "before you will be allowed to perform any other operations.");
                        }

                        PasswordExpiringControl pwdExpiring =
                        PasswordExpiringControl.get(bindResult);
                        if(pwdExpiring == null) {
                            System.out.println("The password expiring control was not included in" +
                            " the BIND response.");
                        } else {
                        System.err.println("WARNING:  Your password will expire in " +
                        pwdExpiring.getSecondsUntilExpiration() + " seconds.");
                        }
                        */


                        } catch (LDAPException e) {
                            test.append(e.toString());
                            ldapConnection.close();
                        }
                    }

                    // if(ldap(name,name2)==true) {

                    //    }
                    //  }
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



