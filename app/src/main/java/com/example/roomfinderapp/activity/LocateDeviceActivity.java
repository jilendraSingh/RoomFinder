package com.example.roomfinderapp.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.roomfinderapp.R;
import com.example.roomfinderapp.Utility;
import com.example.roomfinderapp.jave_classes.DirectionParser;
import com.example.roomfinderapp.roomdatabase_folder.DatabaseClient;
import com.example.roomfinderapp.roomdatabase_folder.LatLongModel;
import com.example.roomfinderapp.service_classes.LocationClass;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class LocateDeviceActivity extends AppCompatActivity implements OnMapReadyCallback {
    double currentDeviceLatitude = 0.0;
    double currentDeviceLongitude = 0.0;
    double dbDeviceLatitude = 0.0;
    double dbDeviceLongitude = 0.0;


    LatLng mOrigin;
    LatLng mDestination;
    private GoogleMap mMap;
    List<LatLongModel> latLongModelList;
    SupportMapFragment mapFragment;
    Spinner deviceSpinner;
    String[] deviceArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_device);

        latLongModelList = new ArrayList<>();
        deviceArray = new String[2];
        deviceArray[0] = "Select your device";
        new LoadLatLongAsync().execute(Utility.getDeviceName());

        deviceSpinner = findViewById(R.id.deviceSpinner);
        deviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 0) {
                    String deviceName = adapterView.getItemAtPosition(i).toString();
                    getLatLongByDevieName(deviceName);
                    deviceSpinner.setSelection(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getLatLongByDevieName(String deviceName) {
        LoadLatLongAsync loadLatLongAsync = new LoadLatLongAsync();
        loadLatLongAsync.execute(deviceName);
    }


    public class LoadLatLongAsync extends AsyncTask<String, Void, List<LatLongModel>> {

        @Override
        protected List<LatLongModel> doInBackground(String... param) {
            String deviceName = param[0];
            latLongModelList.clear();
            latLongModelList = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().roomDao().getSingleDeviceRecord(deviceName);
            return latLongModelList;
        }

        @Override
        protected void onPostExecute(List<LatLongModel> latLongModels) {

            dbDeviceLatitude = Double.parseDouble(latLongModels.get(latLongModels.size() - 1).getDeviceLatitude());
            dbDeviceLongitude = Double.parseDouble(latLongModels.get(latLongModels.size() - 1).getDeviceLongitude());
            deviceArray[1] = latLongModels.get(0).getDeviceName();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(LocateDeviceActivity.this, android.R.layout.simple_spinner_item, deviceArray);
            arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            deviceSpinner.setAdapter(arrayAdapter);
            loadMap();
        }
    }

    private void loadMap() {

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LocationClass.LocationResult locationResult = new LocationClass.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                //Got the location!
                currentDeviceLatitude = location.getLatitude();
                currentDeviceLongitude = location.getLongitude();


                drawRouteOnMap(googleMap);
            }
        };
        LocationClass myLocation = new LocationClass();
        myLocation.getLocation(LocateDeviceActivity.this, locationResult);
    }

    private void drawRouteOnMap(GoogleMap googleMap) {

        mOrigin = new LatLng(currentDeviceLatitude, currentDeviceLongitude);
        mDestination = new LatLng(dbDeviceLatitude, dbDeviceLongitude);
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Show marker on the screen and adjust the zoom level
        mMap.addMarker(new MarkerOptions().position(mOrigin).title("Origin"));
        mMap.addMarker(new MarkerOptions().position(mDestination).title("Destination"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin, 14f));

        new TaskDirectionRequest().execute(getRequestedUrl(mOrigin, mDestination));
    }

    private String getRequestedUrl(LatLng origin, LatLng destination) {
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String strDestination = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";

        String param = strOrigin + "&" + strDestination + "&" + sensor + "&" + mode;
        String output = "json";
        String APIKEY = getResources().getString(R.string.google_maps_key);

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param + APIKEY;
        return url;
    }

    private String requestDirection(String requestedUrl) {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(requestedUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            responseString = stringBuffer.toString();
            bufferedReader.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        httpURLConnection.disconnect();
        return responseString;
    }


    public class TaskDirectionRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String responseString) {
            super.onPostExecute(responseString);
            //Json object parsing
            TaskParseDirection parseResult = new TaskParseDirection();
            parseResult.execute(responseString);
        }
    }

    public class TaskParseDirection extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonString) {
            List<List<HashMap<String, String>>> routes = null;
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(jsonString[0]);
                DirectionParser parser = new DirectionParser();
                routes = parser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);
            ArrayList<LatLng> points = null;
            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList<LatLng>();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lng"));

                    points.add(new LatLng(lat, lon));
                }
                polylineOptions.addAll(points);
                polylineOptions.width(15f);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }
            if (polylineOptions != null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found", Toast.LENGTH_LONG).show();
            }
        }
    }
}