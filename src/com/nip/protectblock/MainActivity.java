package com.nip.protectblock;

import java.util.List;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private LatLngBounds BOGOTA = new LatLngBounds(new LatLng(4.59354,-74.26964), new LatLng(4.79952,-73.98262));
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

		final GoogleMap map = ((SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		//para que solo muestre a bogota
		
		map.setMyLocationEnabled(true);
		map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			Marker marker;
			@Override
			public void onMapClick(LatLng point) {
				if(marker!=null)
					marker.remove();
				marker= map.addMarker(new MarkerOptions().position(point)
						.title(getAddress(point).get(0).getAddressLine(0)).snippet(getAddress(point).get(0).getAddressLine(1)+" - "+getAddress(point).get(0).getAddressLine(2)));
				marker.showInfoWindow();
				getAddress(point);
			}
		});
		
		map.setOnCameraChangeListener(new OnCameraChangeListener() {

		    @Override
		    public void onCameraChange(CameraPosition arg0) {
		        // Move camera.
		        map.moveCamera(CameraUpdateFactory.newLatLngBounds(BOGOTA, 10));
		        // Remove listener to prevent position reset on camera move.
		        map.setOnCameraChangeListener(null);
		    }
			
		});
		
		
//		map.addMarker(new MarkerOptions()
//		.position(new LatLng(0, 0))
//		.title("Hello world")
//		.draggable(true));
//		.icon(BitmapDescriptorFactory.fromAsset("finger-thump-black.jpg")));
//		final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
//		Marker melbourne = map.addMarker(new MarkerOptions()
//		.position(MELBOURNE)
//		.title("Melbourne")
//		.snippet("Population: 4,137,400"));
	}

	public List<Address> getAddress(LatLng point) {
		try {
			Geocoder geocoder;
			List<Address> addresses;
			geocoder = new Geocoder(this);
			if (point.latitude != 0 || point.longitude != 0) {
				addresses = geocoder.getFromLocation(point.latitude ,
						point.longitude, 1);
				String address = addresses.get(0).getAddressLine(0);
				String city = addresses.get(0).getAddressLine(1);
				String country = addresses.get(0).getAddressLine(2);
				System.out.println(address+" - "+city+" - "+country);
				return addresses;
			} else {
				Toast.makeText(this, "latitude and longitude are null",
						Toast.LENGTH_LONG).show();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
