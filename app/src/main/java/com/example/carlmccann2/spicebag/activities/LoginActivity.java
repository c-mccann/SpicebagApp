package com.example.carlmccann2.spicebag.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carlmccann2.spicebag.IP;
import com.example.carlmccann2.spicebag.R;
import com.example.carlmccann2.spicebag.entities.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button createAccountButton = (Button) findViewById(R.id.create_account_activity_launch_button);

        createAccountButton
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                        startActivity(intent);

                    }
                });

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserDetails();

            }
        });



    }

    public void checkUserDetails(){

        final String username = ((EditText) findViewById(R.id.login_email_edit_text)).getText().toString();
        final String password = ((EditText) findViewById(R.id.login_password_edit_text)).getText().toString();


        final String urlString = "http://" + IP.address + ":8080/spicebag-1.0/spicebag/user/" + username + "/" + password;
        try {
            URL url = new URL(urlString);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null && cm. getActiveNetworkInfo().isConnected()){
            new AsyncTask<String, Void, String>(){

                @Override
                protected String doInBackground(String... strings) {
                    StringBuffer string  = new StringBuffer("");
                    URL url = null;

                    try {
                        url = new URL(urlString);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }



                    HttpURLConnection conn = null;
                    try {
                        conn = (HttpURLConnection) url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    try {
                        conn.setRequestMethod("GET");
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                    try{
                        System.out.println("Response Code: " + conn.getResponseCode());
                        InputStream in = new BufferedInputStream(conn.getInputStream());

                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String line = "";
                        while((line = br.readLine()) != null){
                            string.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    setResult(string.toString());
                    System.out.println("Response JSON: " + string.toString());

                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


                    try {
                        User user = mapper.readValue(string.toString(), User.class);
                        if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                            Intent intent = new Intent(LoginActivity.this, HubActivity.class);
                            intent.putExtra("user", mapper.writeValueAsString(user));
                            startActivity(intent);
                            finish();
                        }
                        else if(user.getUsername().equals(username) && !user.getPassword().equals(password)){
                            Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                        else if(!user.getUsername().equals(username) && user.getPassword().equals(password)){
                            Toast.makeText(LoginActivity.this, "Wrong Username", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Wrong Details", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return string.toString();
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                }
            }.execute();

        }
        else{
            Toast.makeText(this, "Connect to the Internet", Toast.LENGTH_LONG).show();
        }
    }

}
