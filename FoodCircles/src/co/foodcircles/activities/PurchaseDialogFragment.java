package co.foodcircles.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.json.Restaurant;
import co.foodcircles.json.Upgrade;
import co.foodcircles.util.FoodCirclesApplication;

public class PurchaseDialogFragment extends DialogFragment
{
	FoodCirclesApplication app;
	Restaurant restaurant;
	private Upgrade selectedUpgrade;
	private SeekBar seekBar;
	private TextView price;
	private TextView meals;

	private static final int LOWER_SEEKBAR_PERCENT = 20;
	private static final int HIGHER_SEEKBAR_PERCENT = 80;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view;

		view = inflater.inflate(R.layout.buy_dialog, null);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

		app = (FoodCirclesApplication) getActivity().getApplicationContext();
		restaurant = app.selectedRestaurant;

		price = (TextView) view.findViewById(R.id.textViewTotalPrice);
		meals = (TextView) view.findViewById(R.id.textViewMeals);

		List<Upgrade> upgrades = restaurant.getUpgrades();

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
				}
				else if (seekBar.getProgress() < HIGHER_SEEKBAR_PERCENT)
				{
					seekBar.setProgress(50);
				}
				else
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
				TaskStackBuilder stackBuilder = TaskStackBuilder.from(getActivity());
				stackBuilder.addNextIntent(new Intent(getActivity(), MainActivity.class).putExtra("tab", 0));
				stackBuilder.addNextIntent(new Intent(getActivity(), ViewVoucherActivity.class));
				
				stackBuilder.startActivities();
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
		}
		else if (seekBar.getProgress() < HIGHER_SEEKBAR_PERCENT)
		{
			price.setText("$" + selectedUpgrade.getFullPrice());
			int numMeals = selectedUpgrade.getFullPrice().intValue();
			meals.setText("" + numMeals + " Meal" + (numMeals <= 1 ? "" : "s") + " Provided");
		}
		else
		{
			price.setText("$" + selectedUpgrade.getFullPrice().add(selectedUpgrade.getFullPrice()));
			int numMeals = selectedUpgrade.getFullPrice().add(selectedUpgrade.getFullPrice()).intValue();
			meals.setText("" + numMeals + " Meal" + (numMeals <= 1 ? "" : "s") + " Provided");
		}
	}
}
