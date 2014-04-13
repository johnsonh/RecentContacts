package com.example.recentcontacts;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {
	
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public final static String CONTACT_NAME = "com.example.myfirstapp.C_NAME";
	public final static String CONTACT_NUMBER = "com.example.myfirstapp.C_NUMBER";
	public final static String CONTACT_ADDRESS = "com.example.myfirstapp.C_ADDRESS";
	public final static String LOCATION_LATITUDE = "com.example.myfirstapp.L_LATITUDE";
	public final static String LOCATION_LONGITUDE = "com.example.myfirstapp.L_LONGITUDE";
	
	private Location m_Location = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Get Location
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
	        public void onLocationChanged(Location location) {
	            // Called when a new location is found by the network location provider.
	        	m_Location = location;
	        }
	
	        public void onStatusChanged(String provider, int status, Bundle extras) {}
	
	        public void onProviderEnabled(String provider) {}
	
	        public void onProviderDisabled(String provider) {}
        };
        String locationProvider = LocationManager.GPS_PROVIDER;
        m_Location = locationManager.getLastKnownLocation(locationProvider);

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    /**
     * Called when user clicks on save contact button
     */
    public void saveContact(View view)
    {
    	if (m_Location == null && Geocoder.isPresent())
    	{
    		return;
    	}
    	Geocoder geocoder = new Geocoder(this);
    	String location = null;
    	try {
			List<Address> addresses = geocoder.getFromLocation(m_Location.getLatitude(), m_Location.getLongitude(), 1);
			location = addresses.get(0).toString();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
    	
    	Intent intent = new Intent(this, ContactActivity.class);
    	EditText editTextName = (EditText) findViewById(R.id.edit_name);
    	String name = editTextName.getText().toString();
    	EditText editTextNumber = (EditText) findViewById(R.id.edit_number);
    	String number = editTextNumber.getText().toString();
    	intent.putExtra(EXTRA_MESSAGE, "SaveContact");
    	intent.putExtra(CONTACT_NAME, name);
    	intent.putExtra(CONTACT_NUMBER, number);
    	intent.putExtra(CONTACT_ADDRESS, location);
    	startActivity(intent);
    }
    

    /**
     * Called when user clicks on get location button
     */
    public void getLocation(View view)
    {
    	Intent intent = new Intent(this, LocationActivity.class);
    	intent.putExtra(EXTRA_MESSAGE, "GetLocation");
    	startActivity(intent);
    }
    
}
