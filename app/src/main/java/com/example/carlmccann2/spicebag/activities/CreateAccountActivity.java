package com.example.carlmccann2.spicebag.activities;

import android.content.Context;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by carlmccann2 on 18/06/2017.
 */

public class CreateAccountActivity extends AppCompatActivity {
    private final int SUCCESS = -1;
    private final int EMAIL_EXISTS = 0;
    private final int PASSWORD_MISMATCH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Button button = (Button) findViewById(R.id.create_account_post);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (createAccount()) {
                    case EMAIL_EXISTS:
                        Toast.makeText(CreateAccountActivity.this, "Email already in use", Toast.LENGTH_SHORT).show();
                        break;

                    case PASSWORD_MISMATCH:
                        Toast.makeText(CreateAccountActivity.this, "Please ensure passwords match", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
            }
        });


    }

    public int createAccount() {
        final String firstName = ((EditText) findViewById(R.id.create_account_first_name)).getText().toString();
        final String lastName = ((EditText) findViewById(R.id.create_account_last_name)).getText().toString();
        final String username = ((EditText) findViewById(R.id.create_account_username)).getText().toString();
        final String email = ((EditText) findViewById(R.id.create_account_email)).getText().toString();
        final String password = ((EditText) findViewById(R.id.create_account_password)).getText().toString();
        final String retypePassword = ((EditText) findViewById(R.id.create_account_retype_password)).getText().toString();


        // TODO: get on username email etc to verify new user, then post
        if (password.equals(retypePassword) && !firstName.equals("") && !lastName.equals("") &&
                !username.equals("") && !email.equals("") && !password.equals("")) {

            ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
                new AsyncTask<String, Void, String>() {

                    @Override
                    protected String doInBackground(String... strings) {
                        String urlString = "http://" + IP.address + ":8080/spicebag-1.0/spicebag/user/add";
                        User user = new User(firstName, lastName, username, email, password);
                        ObjectMapper mapper = new ObjectMapper();

                        try {
                            String json = mapper.writeValueAsString(user);
                            URL url = new URL(urlString);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            connection.setDoOutput(true);
                            connection.setUseCaches(false);

                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setRequestProperty("charset", "UTF8");

                            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                            outputStream.write(json.toString().getBytes("UTF8"));

                            return Integer.toString(connection.getResponseCode());

                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (s.equals("204")){
                            Toast.makeText(CreateAccountActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                }.execute();


                return SUCCESS;
            }


        }
        return SUCCESS;
    }
}