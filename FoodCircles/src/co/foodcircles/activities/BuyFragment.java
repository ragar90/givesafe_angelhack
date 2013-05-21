package co.foodcircles.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.json.Restaurant;
import co.foodcircles.json.Upgrade;
import co.foodcircles.util.C;
import co.foodcircles.util.FoodCirclesApplication;

public class BuyFragment extends Fragment
{
	FoodCirclesApplication app;
	Restaurant restaurant;
	private Upgrade selectedUpgrade;
	private Spinner numFriends;
	private SeekBar seekBar;
	private TextView price;
	private TextView meals;
	private Spinner donateTo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view;

		view = inflater.inflate(R.layout.buy_options, null);
		C.overrideFonts(getActivity(), view);

		app = (FoodCirclesApplication) getActivity().getApplicationContext();
		restaurant = app.selectedRestaurant;

		numFriends = (Spinner) view.findViewById(R.id.spinnerNumFriends);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new ArrayList<String>()
		{
			{
				add("1 Appetizer");
				add("2 Appetizers");
			}
		});
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		numFriends.setAdapter(adapter);

		donateTo = (Spinner) view.findViewById(R.id.spinnerDonateTo);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new ArrayList<String>()
		{
			{
				add("Charity 1");
				add("Charity 2");
			}
		});
		// Specify the layout to use when the list of choices appears
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		donateTo.setAdapter(adapter2);

		price = (TextView) view.findViewById(R.id.textViewTotalPrice);
		meals = (TextView) view.findViewById(R.id.textViewDonated);

		List<Upgrade> upgrades = restaurant.getUpgrades();

		selectedUpgrade = upgrades.get(0);

		int price = selectedUpgrade.getFullPrice().intValue();

		seekBar = (SeekBar) view.findViewById(R.id.seekBar);
		seekBar.setMax(100 * (price * 2 - 1) + 99);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				setPrices();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
			}
		});

		setPrices();

		Button buyButton = (Button) view.findViewById(R.id.buttonBuy);

		buyButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				TaskStackBuilder stackBuilder = TaskStackBuilder.from(getActivity());
				stackBuilder.addNextIntent(new Intent(getActivity(), MainActivity.class).putExtra("tab", 0));
				stackBuilder.addNextIntent(new Intent(getActivity(), ViewVoucherActivity.class));

				stackBuilder.startActivities();
				getActivity().finish();
			}
		});
		

		ImageView i1 = (ImageView) view.findViewById(R.id.imageViewI1);
		i1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				FragmentManager fm = getActivity().getSupportFragmentManager();
				SimpleDialogFragment dialog = new SimpleDialogFragment();
				dialog.setText("Bringing Friends Description", "This is a description about buying multiple items.");
				dialog.show(fm, "simple_dialog");
			}
		});

		ImageView i2 = (ImageView) view.findViewById(R.id.imageViewI2);
		i2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				FragmentManager fm = getActivity().getSupportFragmentManager();
				SimpleDialogFragment dialog = new SimpleDialogFragment();
				dialog.setText("Charity Description", "This is a description about the charities.");
				dialog.show(fm, "simple_dialog");
			}
		});

		return view;
	}

	private void setPrices()
	{
		int priceAmount = (seekBar.getProgress() / 100) + 1;
		price.setText("$" + priceAmount + ".00");
		meals.setText(priceAmount + " meals donated to...");
	}
}
