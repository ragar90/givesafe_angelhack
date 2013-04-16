package co.foodcircles.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import co.foodcircles.R;
import co.foodcircles.json.Restaurant;
import co.foodcircles.util.FoodCirclesApplication;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class RestaurantInfoActivity extends FragmentActivity implements OnClickListener, OnMarkerClickListener, OnMapClickListener, OnInfoWindowClickListener
{
	FoodCirclesApplication app;
	Restaurant restaurant;

	GoogleMap map;
	MarkerOptions destinationMarker;
	ImageView image;
	TextView addressTextView;
	TextView phoneNumberTextView;
	TextView hoursTextView;
	TextView urlTextView;
	TextView detailsTextView;

	ImageView twitterImage;
	ImageView facebookImage;
	ImageView yelpImage;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		app = (FoodCirclesApplication) getApplicationContext();
		restaurant = app.selectedRestaurant;

		image = (ImageView) findViewById(R.id.imageViewLogo);

		try
		{
			FragmentManager myFragmentManager = getSupportFragmentManager();
			SupportMapFragment mySupportMapFragment = (SupportMapFragment) myFragmentManager.findFragmentById(R.id.map);
			map = mySupportMapFragment.getMap();

			MapsInitializer.initialize(this);
			LatLng destinationLatLng = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
			destinationMarker = new MarkerOptions();

			destinationMarker = destinationMarker.position(destinationLatLng);
			destinationMarker = destinationMarker.title(restaurant.getName());

			map.addMarker(destinationMarker).showInfoWindow();
			map.setOnMarkerClickListener(this);
			map.setOnMapClickListener(this);
			map.setOnInfoWindowClickListener(this);

			UiSettings settings = map.getUiSettings();
			map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(destinationLatLng, 13.5f, 30f, 112.5f)), 1, null); // bearing
			map.setTrafficEnabled(false);
			settings.setAllGesturesEnabled(false);
			settings.setCompassEnabled(false);
			settings.setMyLocationButtonEnabled(false);
			settings.setRotateGesturesEnabled(false);
			settings.setScrollGesturesEnabled(false);
			settings.setTiltGesturesEnabled(false);
			settings.setZoomControlsEnabled(false);
			settings.setZoomGesturesEnabled(false);
		}
		catch (GooglePlayServicesNotAvailableException e)
		{
			Toast.makeText(this, "Google Play Services are not available on this device. Cannot display map.", Toast.LENGTH_SHORT).show();
		}

		addressTextView = (TextView) findViewById(R.id.textViewAddress);
		phoneNumberTextView = (TextView) findViewById(R.id.textViewPhone);
		hoursTextView = (TextView) findViewById(R.id.textViewHours);
		urlTextView = (TextView) findViewById(R.id.textViewUrl);
		detailsTextView = (TextView) findViewById(R.id.textViewDetails);
		
		addressTextView.setText(restaurant.getAddress());
		phoneNumberTextView.setText(restaurant.getPhone());
		hoursTextView.setText(restaurant.getHours());
		urlTextView.setText(restaurant.getUrl());
		detailsTextView.setText(restaurant.getDetails());
	}

	@Override
	public void onMapClick(LatLng point)
	{
		map.addMarker(destinationMarker).showInfoWindow();
	}

	@Override
	public void onInfoWindowClick(Marker marker)
	{
		onMarkerClick(marker);
	}

	@Override
	public boolean onMarkerClick(Marker marker)
	{
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + restaurant.getAddress()));
		intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		startActivity(intent);
		return true;
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub

	}

}
