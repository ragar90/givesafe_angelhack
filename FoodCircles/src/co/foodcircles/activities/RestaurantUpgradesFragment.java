package co.foodcircles.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.json.Restaurant;
import co.foodcircles.json.Upgrade;
import co.foodcircles.util.FoodCirclesApplication;

public class RestaurantUpgradesFragment extends Fragment
{
	FoodCirclesApplication app;
	Restaurant restaurant;
	private Upgrade selectedUpgrade;
	private List<RadioButton> buttonGroup = new ArrayList<RadioButton>();
	private SeekBar seekBar;
	private TextView price;
	private TextView meals;

	private static final int LOWER_SEEKBAR_PERCENT = 20;
	private static final int HIGHER_SEEKBAR_PERCENT = 80;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.restaurant_upgrade_list, null);

		app = (FoodCirclesApplication) getActivity().getApplicationContext();
		restaurant = app.selectedRestaurant;

		price = (TextView) view.findViewById(R.id.textViewTotalPrice);
		meals = (TextView) view.findViewById(R.id.textViewMeals);

		LinearLayout upgradesLayout = (LinearLayout) view.findViewById(R.id.linearLayoutOffers);

		List<Upgrade> upgrades = restaurant.getUpgrades();
		for (final Upgrade upgrade : upgrades)
		{
			View upgradeView = inflater.inflate(R.layout.restaurant_upgrade_row, null);
			final RadioButton radioButton = (RadioButton) upgradeView.findViewById(R.id.radioButton);
			radioButton.setText(upgrade.getName());
			buttonGroup.add(radioButton);
			radioButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					for (RadioButton rb : buttonGroup)
						rb.setChecked(false);
					radioButton.setChecked(true);
					selectedUpgrade = upgrade;
					setPrices();
				}
			});
			upgradesLayout.addView(upgradeView);
		}

		buttonGroup.get(0).setChecked(true);
		selectedUpgrade = upgrades.get(0);

		TextView price1 = (TextView) view.findViewById(R.id.textViewPrice1);
		price1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				seekBar.setProgress(0);
				setPrices();
			}
		});

		TextView price2 = (TextView) view.findViewById(R.id.textViewPrice2);
		price2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				seekBar.setProgress(50);
				setPrices();
			}
		});

		TextView price3 = (TextView) view.findViewById(R.id.textViewPrice3);
		price3.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				seekBar.setProgress(100);
				setPrices();
			}
		});

		seekBar = (SeekBar) view.findViewById(R.id.seekBar);
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
				if (seekBar.getProgress() < LOWER_SEEKBAR_PERCENT)
				{
					seekBar.setProgress(0);
				} else if (seekBar.getProgress() < HIGHER_SEEKBAR_PERCENT)
				{
					seekBar.setProgress(50);
				} else
				{
					seekBar.setProgress(100);
				}
			}
		});

		final RadioButton charity1 = (RadioButton) view.findViewById(R.id.radioButtonCharity1);
		charity1.setChecked(true);
		final RadioButton charity2 = (RadioButton) view.findViewById(R.id.radioButtonCharity2);
		charity1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				charity2.setChecked(false);
			}
		});

		charity2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				charity1.setChecked(false);
			}
		});

		setPrices();

		Button buyButton = (Button) view.findViewById(R.id.buttonBuy);
		buyButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), UpgradePurchasedActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});

		return view;
	}

	private void setPrices()
	{
		if (seekBar.getProgress() < LOWER_SEEKBAR_PERCENT)
		{
			price.setText("$" + selectedUpgrade.getDiscountPrice());

			int numMeals = selectedUpgrade.getDiscountPrice().intValue();
			meals.setText("" + numMeals + " Meal" + (numMeals <= 1 ? "" : "s") + " Provided");
		} else if (seekBar.getProgress() < HIGHER_SEEKBAR_PERCENT)
		{
			price.setText("$" + selectedUpgrade.getFullPrice());
			int numMeals = selectedUpgrade.getFullPrice().intValue();
			meals.setText("" + numMeals + " Meal" + (numMeals <= 1 ? "" : "s") + " Provided");
		} else
		{
			price.setText("$" + selectedUpgrade.getFullPrice().add(selectedUpgrade.getFullPrice()));
			int numMeals = selectedUpgrade.getFullPrice().add(selectedUpgrade.getFullPrice()).intValue();
			meals.setText("" + numMeals + " Meal" + (numMeals <= 1 ? "" : "s") + " Provided");
		}
	}
}
