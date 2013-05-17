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
import co.foodcircles.json.Restaurant;
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

		itemImage = (ImageView) view.findViewById(R.id.imageView);
		itemName = (TextView) view.findViewById(R.id.textViewItemName);
		itemFlavorText = (TextView) view.findViewById(R.id.textViewItemFlavorText);
		button = (Button) view.findViewById(R.id.button);

		FoodCirclesApplication app = (FoodCirclesApplication) getActivity().getApplicationContext();
		Restaurant restaurant = app.selectedRestaurant;

		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//BuyDialogFragment dialog = new BuyDialogFragment();
				//dialog.show(getActivity().getSupportFragmentManager(), "dialogFragment");
				getActivity().startActivity(new Intent(getActivity(), BuyOptionsActivity.class));
				getActivity().finish();
			}
		});

		return view;
	}
}
