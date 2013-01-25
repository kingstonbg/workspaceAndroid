package taxi.route;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import android.provider.Settings;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	private LocationManager locationMangaer=null;  
	private LocationListener locationListener=null;   
	  
	private Button btnGetLocation = null;  
	private EditText editLocation = null;
	private EditText editDistance = null;
	private ProgressBar pb =null;  
	   
	private static final String TAG = "Debug";  
	private Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);

		editLocation = (EditText) findViewById(R.id.editTextLocation);
		editDistance = (EditText) findViewById(R.id.editTextDistance);
		btnGetLocation = (Button) findViewById(R.id.btnLocation);
		btnGetLocation.setOnClickListener(this);

		locationMangaer = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }
    
	public void onClick(View v) {
		flag = displayGpsStatus();
		if (flag) {

			Log.v(TAG, "onClick");

			editLocation.setText("Please move your device to"
					+ " see the changes in coordinates." + "\nWait..");

			pb.setVisibility(View.VISIBLE);
			locationListener = new MyLocationListener();

			locationMangaer.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 500, 10, locationListener);

		} else {
			alertbox("Gps Status!!", "Your GPS is: OFF");
		}

	}  

	private Boolean displayGpsStatus() {
		ContentResolver contentResolver = getBaseContext().getContentResolver();
		boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(
				contentResolver, LocationManager.GPS_PROVIDER);
		if (gpsStatus) {
			return true;

		} else {
			return false;
		}
	}  
	
	protected void alertbox(String title, String mymessage) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Your Device's GPS is Disabled")
				.setCancelable(false)
				.setTitle("** Gps Status **")
				.setPositiveButton("Gps On",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent myIntent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
								startActivity(myIntent);
								dialog.cancel();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
	private class MyLocationListener implements LocationListener {
		
		static final double eQuatorialEarthRadius = 6378.1370D;
	    static final double d2r = (Math.PI / 180D);
	    
	    double dlat;	double dlon; //distance
	    double lat1;	double lon1; //latitude
	    double lat2 = 0;	double lon2 = 0; //longitude

	    double distanceTraveled = 0;
	    
	    boolean km;
		@Override
		public void onLocationChanged(Location loc) {
			
			double distance = 0;
			
			lat1 = lat2;
			lon1 = lon2;
			
			lat2 = loc.getLatitude();
			lon2 = loc.getLongitude();
			
			editLocation.setText("");
			editDistance.setText("");
			pb.setVisibility(View.INVISIBLE);
			Toast.makeText(
					getBaseContext(),
					"Location changed : Lat: " + loc.getLatitude() + " Lng: "
							+ loc.getLongitude(), Toast.LENGTH_SHORT).show();
			String longitude = "Longitude: " + loc.getLongitude();
			lon1 = loc.getLongitude();
			Log.v(TAG, longitude);
			String latitude = "Latitude: " + loc.getLatitude();
			Log.v(TAG, latitude);

			/*----------to get City-Name from coordinates ------------- */
			String cityName = null;
			Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
			List<Address> addresses;
			try {
				addresses = gcd.getFromLocation(loc.getLatitude(),
						loc.getLongitude(), 1);
				if (addresses.size() > 0)
					System.out.println(addresses.get(0).getLocality());
				cityName = addresses.get(0).getLocality();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (lat1!=0) {
			distance = processKm(HaversineKM(lat1, lon1, lat2, lon2));
			distanceTraveled = distanceTraveled + distance;
			}
			String s = longitude + "\n" + latitude
					+ "\n\nMy Currrent City is: " + cityName;
			editLocation.setText(s);
			editDistance.setText("Distance Traveled" + "\n" + setDistance(distanceTraveled));
		}
		
		public double HaversineKM(double lat1, double lon1, double lat2, double lon2) {
			dlon = (lon2 - lon1) * d2r;
			dlat = (lat2 - lat1) * d2r;
			double a = Math.pow(Math.sin(dlat/ 2D), 2D) + 
					Math.cos(lat1*d2r) * Math.cos(lat2*d2r) *
					Math.pow(Math.sin(dlon / 2D), 2D);
			double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a ));
			double distanceInKm = eQuatorialEarthRadius * c;
			Math.round(distanceInKm);
			
			return distanceInKm;
		}
		
		public double processKm(double distance) {
			double result = 0;
			if (distance<1) {
				distance = 1000D * distance;
				km = false;
				result = distance;
			}
			else {
				km = true;
				result = distance;
			}
			
			return result;
	    }
		
		public String setDistance(double distance) {
			String result = "";
			if (km)
				return result = round(distance, 3, BigDecimal.ROUND_HALF_UP) + "km";
			else
				return result = round(distance, 2, BigDecimal.ROUND_HALF_UP) + "m";
		}
		
		public double round(double unrounded, int precision, int roundingMode)
		{
		    BigDecimal bd = new BigDecimal(unrounded);
		    BigDecimal rounded = bd.setScale(precision, roundingMode);
		    return rounded.doubleValue();
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}

}
