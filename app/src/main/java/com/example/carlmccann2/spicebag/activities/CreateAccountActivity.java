package com.example.carlmccann2.spicebag.activities;

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
                switch (createAccount()){
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

    public int createAccount(){
        String firstName = ((EditText) findViewById(R.id.create_account_first_name)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.create_account_last_name)).getText().toString();
        String username = ((EditText) findViewById(R.id.create_account_username)).getText().toString();
        String email = ((EditText) findViewById(R.id.create_account_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.create_account_password)).getText().toString();
        String retypePassword = ((EditText) findViewById(R.id.create_account_retype_password)).getText().toString();

        if(password.equals(retypePassword)){

            //TODO: Check if email already exists in db
            if(true){

                User user = new User(firstName, lastName, username, email, password);
                ObjectMapper mapper = new ObjectMapper();

                try {
                    String json = mapper.writeValueAsString(user);

                    String urlString = "http://" + IP.address + ":8080/spicebag-1.0/spicebag/user/add";

                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setDoOutput(true);
                    connection.setUseCaches(false);

                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("charset", "UTF8");

                    for (int i = 0; i < 1000; i++) {
                        System.out.println(json);
                        System.out.println(url);
                    }

                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.write(json.toString().getBytes("UTF8"));

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                return EMAIL_EXISTS;
            }


        }
        else{
            return PASSWORD_MISMATCH;
        }
        return SUCCESS;
    }


}
