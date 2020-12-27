package com.rahulproject.airqualitycheck;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
{
    final int rn = 123;
    final String WEATHER_URL = "http://api.airvisual.com/v2/nearest_city?lat=28.702924&lon=77.213586";
    final String key = "b740cc5da570bdd49b1068eade9bfb5d3c99999";
    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;

    final String Rain = "Develop Your Self";

    final String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;// just manages GPS Setting

    // Member Variables:
    TextView mCityLabel;
    View mWeatherImage;
    TextView mTemperatureLabel;

    // TODO: Declare a LocationManager and a LocationListener here:
    LocationManager mLocationManager;//start and stop providing location
    LocationListener mLocationListener;// location actually changed


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aqi_layout);

        // Linking the elements in the layout to Java code
        mCityLabel = (TextView) findViewById(R.id.locationTV);
        mWeatherImage =findViewById(R.id.weatherSymbolIV);
        mTemperatureLabel = (TextView) findViewById(R.id.tempTV);
        ImageButton changeCityButton = (ImageButton) findViewById(R.id.changeCityButton);
        ImageButton imageButton = (ImageButton) findViewById(R.id.iButton);


        // TODO: Add an OnClickListener to the changeCityButton here:
        changeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent =new Intent(MainActivity.this,newcity.class);
                startActivity(myintent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj= new Intent(MainActivity.this,inifo.class);
                startActivity(obj);
            }
        });


    }


    // TODO: Add onResume() here:
    @Override
    protected void onResume()// onResume is one of the life cycle method that execute just after on create method and just before user can interact with main activity
    {
        super.onResume();
        Intent n= getIntent();
        String city = n.getStringExtra("City");
        if(city!=null)
        {
            Log.d(Rain,"new city is called ");
            getWeatherForNewCity(city);
        }
        else
        {
            getWeatherForCurrentLocation();
        }
        Log.d(Rain, "OnResume() is called");
        Log.d(Rain, "Chutiya mat kat apni life ass hole Getting Weather update");
        getWeatherForCurrentLocation();
    }




    // TODO: Add getWeatherForNewCity(String city) here:
    private void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//So this is the line of code that gets hold of a location manager and assigns that location manager object to the member variable
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d(Rain, "On Location changed() callback received");
                String lon = String.valueOf(location.getLongitude());
                String lat = String.valueOf(location.getLatitude());
                Log.d(Rain, "Longitude===="+lon);
                Log.d(Rain, "Latitude===="+lat);
                //params and bundle them long & latitu & This variable will hold the querrey
                RequestParams params = new RequestParams();
                params.put("token",key);
                letsDoSomeNetworking(params,WEATHER_URL);


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(Rain, "onProviderDisabled() callback received");
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling/
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, rn);
            return;
        }
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);//1) request Permission // User Act

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Checking against the request code we specified earlier.
        if (requestCode == 123) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(Rain, "onRequestPermissionsResult(): Permission granted!");

                // Getting weather only if we were granted permission.
                getWeatherForCurrentLocation();
            } else {
                Log.d(Rain, "Permission denied =( ");
            }
        }

    }




    // TODO: Add getWeatherForCurrentLocation() here:
    private void getWeatherForNewCity(String city)
    {
        final String ad=city;
        RequestParams params = new RequestParams();
//     params.put("q",city);
        params.put("token",key);
        letsDoSomeNetworking(params,WEATHER_URL);
    }



    // TODO: Add letsDoSomeNetworking(RequestParams params) here:
    private void letsDoSomeNetworking(RequestParams params,String WEATHER_URL)
    {
        final String as = WEATHER_URL;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("data_pre", "Success! JSON: " + response.toString());
                Log.d("urldk","af"+as);
                WeatherDataModel weatherData = WeatherDataModel.fromJson(response);
                updateUI(weatherData);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.e(Rain, "Fail " + e.toString());
                //  Log.d("urldk","af"+WEATHER_URL);
                Log.d(Rain,"StatusCode"+statusCode);
                //updateUI(weatherData);.this, "Request Failed", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Request to  show data", Toast.LENGTH_SHORT).show();
            }

        });
    }


    private void updateUI(WeatherDataModel weather) {
        mTemperatureLabel.setText(weather.getTemperature());
        mCityLabel.setText(weather.getCity());

        // Update the icon based on the resource id of the image in the drawable folder.
        int resourceID = getResources().getIdentifier(weather.getIconName(), "drawable", getPackageName());
        mWeatherImage.setBackgroundResource(resourceID);
    }

    // Freeing up resources when the app enters the paused state.
    @Override
    protected void onPause() {
        super.onPause();

        if (mLocationManager != null) mLocationManager.removeUpdates(mLocationListener);
    }

}
