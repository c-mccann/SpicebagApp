package com.example.carlmccann2.spicebag.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.carlmccann2.spicebag.IP;
import com.example.carlmccann2.spicebag.R;
import com.example.carlmccann2.spicebag.adapters.ReviewAdapter;
import com.example.carlmccann2.spicebag.activities.AddReviewActivity;
import com.example.carlmccann2.spicebag.entities.Review;
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
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by carlmccann2 on 19/06/2017.
 */

public class StreamFragment extends Fragment {



    private ListView listView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tabbed_hub_2_stream, container, false);

        listView = (ListView) rootView.findViewById(R.id.list_view_tab_2_stream);


        final String urlString = "http://" + IP.address + ":8080/spicebag-1.0/spicebag/reviews/getAllNewestFirst";


        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
            new AsyncTask<String, Void, String>() {

                @Override
                protected String doInBackground(String... strings) {

                    StringBuffer string = new StringBuffer("");
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
                    try {
                        System.out.println("Response Code: " + conn.getResponseCode());
                        InputStream in = new BufferedInputStream(conn.getInputStream());

                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String line = "";
                        while ((line = br.readLine()) != null) {
                            string.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    setResult(string.toString());
                    System.out.println("Response JSON: " + string.toString());
                    String toastInfo = "";
                    return string.toString();
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    ObjectMapper mapper = new ObjectMapper();
                    // Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

                    try {
//                        reviews = mapper.readValue(s,Review[].class);
                        Review[] reviews = mapper.readValue(s, Review[].class);

//                        ArrayAdapter<Review> adapter = new ArrayAdapter<Review>(getActivity().getApplicationContext(),R.layout.review_in_stream, R.id.text_view_review_in_stream,reviews);
                        ArrayList<Review> reviewsList = new ArrayList<Review>();
                        reviewsList.addAll(Arrays.asList(reviews));

                        ReviewAdapter adapter = new ReviewAdapter(getActivity().getApplicationContext(), R.layout.review_in_stream, reviewsList);
                        listView.setAdapter(adapter);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }.execute();
        }
        return rootView;
    }

    private void launchAddReviewActivity(){
        Intent intent = new Intent(getActivity(), AddReviewActivity.class);
        startActivity(intent);
    }
}
