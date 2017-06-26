package com.example.carlmccann2.spicebag.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carlmccann2.spicebag.IP;
import com.example.carlmccann2.spicebag.activities.AddReviewActivity;
import com.example.carlmccann2.spicebag.activities.LoginActivity;
import com.example.carlmccann2.spicebag.activities.TabbedHubActivity;
import com.example.carlmccann2.spicebag.entities.Restaurant;
import com.example.carlmccann2.spicebag.entities.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.example.carlmccann2.spicebag.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.List;


/**
 * Created by carlmccann2 on 19/06/2017.
 */

public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;
    private static int UPDATE_INTERVAL = 30000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    private boolean firstTimeOpen = true;


    private static final float ZOOM = 12;


    private GoogleMap googleMap;
    private MapView mapView;

    private GoogleApiClient googleApiClient;

    private Location lastLocation;
    private LocationRequest locationRequest;
    private LocationManager locationManager;

    // https://stackoverflow.com/questions/19353255/how-to-put-google-maps-v2-on-a-fragment-using-viewpager
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView = inflater.inflate(R.layout.fragment_tabbed_hub_3_map, container, false);

        mapView = (MapView) rootView.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplication());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {


                // https://www.youtube.com/watch?v=gDQnC-QA95Q checking permissions etc

                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_CODE);


                }
//                else{
//                    if(checkPlayServices()){
//                        buildGoogleApiClient();
//                        createLocationRequest()
//                    }
//                }

                googleMap = map;
                googleMap.setMyLocationEnabled(true);


                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Intent intent = new Intent(getActivity(), AddReviewActivity.class);

                        Bundle bundle = getActivity().getIntent().getExtras();
                        String user =  bundle.getString("user");
                        String restaurant = marker.getTitle();
                        intent.putExtra("user", user);
                        intent.putExtra("restaurant", restaurant);
                        startActivity(intent);
                        return false;
                    }
                });
                // https://www.youtube.com/watch?v=Z3mKhMkdUFk
                buildGoogleApiClient();
                googleApiClient.connect();


                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            }
        });


        // TODO: maybe add markers here?


        return rootView;


    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext());
//        if(resultCode != ConnectionResult.SUCCESS){
//            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
//                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            }
//            else{
//                Toast.makeText(getActivity().getApplicationContext(), "This device is not supported", Toast.LENGTH_SHORT).show();
//
//            }
//            return false;
//
//        }
//        return true;
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if(checkPlayServices()){
//                        buildGoogleApiClient();
//                    }
                }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            Toast.makeText(getActivity().getApplicationContext(), "Can't get current location", Toast.LENGTH_SHORT).show();
        }
        else{
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, ZOOM);

            if(firstTimeOpen)  {
                googleMap.animateCamera(update);
                firstTimeOpen = false;
            }





            final String urlString = "http://" + IP.address + ":8080/spicebag-1.0/spicebag/yelp/" +
                    (new Double(location.getLatitude())).toString() + "/" +
                    (new Double(location.getLongitude())).toString();


            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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



                        // result with the restaurants json
                        return string.toString();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s); // s is toastInfo from above
                        //TODO: make markers from json
                        ObjectMapper mapper = new ObjectMapper();

                        Restaurant[] restaurants = new Restaurant[0];
                        try {
                            restaurants = mapper.readValue(s,Restaurant[].class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        for(Restaurant r: restaurants){
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(r.getLatitude().doubleValue(),
                                    r.getLongitude().doubleValue()));
                            markerOptions.title(r.getName());
                            googleMap.addMarker(markerOptions);
                        }
                    }
                }.execute();

            }
            else{
                Toast.makeText(getActivity(), "Connect to the Internet", Toast.LENGTH_LONG).show();
            }


        }
    }
}
