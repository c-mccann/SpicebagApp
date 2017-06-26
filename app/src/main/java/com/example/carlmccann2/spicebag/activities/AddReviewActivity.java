package com.example.carlmccann2.spicebag.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.carlmccann2.spicebag.IP;
import com.example.carlmccann2.spicebag.R;
import com.example.carlmccann2.spicebag.entities.Restaurant;
import com.example.carlmccann2.spicebag.entities.Review;
import com.example.carlmccann2.spicebag.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;


//https://stackoverflow.com/questions/5991319/capture-image-from-camera-and-display-in-activity
public class AddReviewActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private Button addImage;
    private ImageView imageView;
    private Button submitButton;
    private RatingBar ratingBar;
    private EditText editText;
    private static int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        addImage = (Button) findViewById(R.id.button_add_review_image);
        imageView = (ImageView) findViewById(R.id.image_view_add_review);
        submitButton = (Button) findViewById(R.id.button_add_review);
        editText = (EditText) findViewById(R.id.edit_text_add_review);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar_add_review);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();

            }
        });

        final Bundle bundle = getIntent().getExtras();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imageView.getDrawable() == null){
                    return;
                }
                // https://stackoverflow.com/questions/37779515/how-can-i-convert-an-imageview-to-byte-array-in-android-studio
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] imageInByte = baos.toByteArray();

                final String reviewText = editText.getText().toString();
                final int starRating = Math.round(ratingBar.getRating());
                final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                int userIdTemp = 0;
                User userTemp = null;

                final ObjectMapper mapper = new ObjectMapper();
                try {
                    userTemp = mapper.readValue(bundle.getString("user"), User.class);

                    userIdTemp = Integer.parseInt(userTemp.getUser_id());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(userIdTemp == 0){
                    return;
                }

                final int userId = userIdTemp;
                final User user = userTemp;



                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {

                        // Restaurant POST
                        new AsyncTask<String,Void, String>(){

                            @Override
                            protected String doInBackground(String... strings) {

                                String urlString = "http://" + IP.address + ":8080/spicebag-1.0/spicebag/restaurants";
                                ObjectMapper mapper = new ObjectMapper();
                                try {
                                    // TODO: fetch real restaurant info
                                    Restaurant restaurant = new Restaurant(null, bundle.getString("restaurant"), "Fake Address", new BigDecimal(0), new BigDecimal(0), "493FAKE");
                                    String jsonString = mapper.writeValueAsString(restaurant);

                                    URL url = new URL(urlString);
                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                                    connection.setDoOutput(true);
                                    connection.setUseCaches(false);

                                    connection.setRequestMethod("POST");
                                    connection.setRequestProperty("Content-Type", "application/json");
                                    connection.setRequestProperty("charset", "UTF8");

                                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                                    outputStream.write(jsonString.getBytes("UTF8"));

                                    return Integer.toString(connection.getResponseCode());


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                super.onPostExecute(s);
//                                Toast.makeText(AddReviewActivity.this, "Restaurant post Result: " + s, Toast.LENGTH_SHORT).show();
                            }
                        }.execute();



                        // Review Post

                        new AsyncTask<String, Void, String>() {

                            @Override
                            protected String doInBackground(String... strings) {
                                final String urlString = "http://" + IP.address + ":8080/spicebag-1.0/spicebag/reviews";



                                // TODO: need logic to fetch restaurant id from database rather than count
                                Review review = new Review(reviewText, imageInByte, starRating, timestamp, new Restaurant(1,"China House", "Firhouse Village", new BigDecimal("56.456000"), new BigDecimal("-6.345000"), "4938887"), user);
                                count++;
                                try {
                                    String jsonString = mapper.writeValueAsString(review);

                                    URL url = new URL(urlString);
                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                                    connection.setDoOutput(true);
                                    connection.setUseCaches(false);

                                    connection.setRequestMethod("POST");
                                    connection.setRequestProperty("Content-Type", "application/json");
                                    connection.setRequestProperty("charset", "UTF8");

                                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                                    outputStream.write(jsonString.getBytes("UTF8"));
                                    return Integer.toString(connection.getResponseCode());



                                } catch (JsonProcessingException e) {
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
//                                Toast.makeText(AddReviewActivity.this, "Review post Result: " + s, Toast.LENGTH_SHORT).show();

                            }
                        }.execute();

                        finish();

            }
        }




    });}

    private void launchCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            addImage.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }
        else {
            addImage.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }
    }
}
