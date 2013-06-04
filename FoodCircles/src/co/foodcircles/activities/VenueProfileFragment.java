package co.foodcircles.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import co.foodcircles.R;
import co.foodcircles.json.Venue;
import co.foodcircles.util.C;
import co.foodcircles.util.FoodCirclesApplication;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class VenueProfileFragment extends Fragment implements OnClickListener, OnMarkerClickListener, OnMapClickListener, OnInfoWindowClickListener
{
	FoodCirclesApplication app;
	Venue venue;

	GoogleMap map;
	MarkerOptions destinationMarker;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.restaurant_profile, null);
		C.overrideFonts(getActivity(), view);

		app = (FoodCirclesApplication) getActivity().getApplicationContext();
		venue = app.selectedVenue;
		((TextView) view.findViewById(R.id.textViewName)).setText(venue.getName());
		((TextView) view.findViewById(R.id.textViewTags)).setText(venue.getTagsString());
		((TextView) view.findViewById(R.id.textViewHours)).setText(venue.getOpenTimes().toString());
		((TextView) view.findViewById(R.id.textViewDescription)).setText(venue.getDescription());
		((TextView) view.findViewById(R.id.textViewAddress)).setText(venue.getAddress());
		((ImageButton) view.findViewById(R.id.buttonCall)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:" + venue.getPhone()));
				startActivity(intent);
			}
		});

		((ImageButton) view.findViewById(R.id.buttonWebsite)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(venue.getWeb()));
				startActivity(i);
			}
		});

		try
		{
			FragmentManager myFragmentManager = getActivity().getSupportFragmentManager();
			SupportMapFragment mySupportMapFragment = (SupportMapFragment) myFragmentManager.findFragmentById(R.id.map);
			map = mySupportMapFragment.getMap();

			MapsInitializer.initialize(getActivity());
			LatLng destinationLatLng = new LatLng(venue.getLatitude(), venue.getLongitude());
			destinationMarker = new MarkerOptions();

			destinationMarker = destinationMarker.position(destinationLatLng);
			destinationMarker = destinationMarker.title(venue.getName());

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
			Toast.makeText(getActivity(), "Google Play Services are not available on this device. Cannot display map.", Toast.LENGTH_SHORT).show();
		}

		return view;
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
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + venue.getAddress()));
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
