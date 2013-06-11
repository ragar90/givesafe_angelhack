package co.foodcircles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.json.Offer;
import co.foodcircles.json.Venue;
import co.foodcircles.util.C;
import co.foodcircles.util.FoodCirclesApplication;

public class VenueItemFragment extends Fragment
{
	ImageView itemImage;
	TextView itemName;
	TextView itemFlavorText;
	Button button;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.venue_profile, null);
		C.overrideFonts(getActivity(), view);

		itemImage = (ImageView) view.findViewById(R.id.imageView);
		itemName = (TextView) view.findViewById(R.id.textViewItemName);
		itemFlavorText = (TextView) view.findViewById(R.id.textViewItemFlavorText);
		button = (Button) view.findViewById(R.id.button);

		final FoodCirclesApplication app = (FoodCirclesApplication) getActivity().getApplicationContext();
		Venue venue = app.selectedVenue;
		Offer offer = venue.getOffers().get(0);
		
		itemName.setText(offer.getTitle());
		itemFlavorText.setText(offer.getDetails());

		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().startActivity(new Intent(getActivity(), BuyOptionsActivity.class));
				app.addPoppableActivity(getActivity());
			}
		});

		return view;
	}
}
