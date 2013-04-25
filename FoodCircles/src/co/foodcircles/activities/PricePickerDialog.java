package co.foodcircles.activities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.json.Upgrade;
import co.foodcircles.util.FoodCirclesApplication;

public class PricePickerDialog extends DialogFragment
{
	private static final int LOWER_SEEKBAR_PERCENT = 20;
	private static final int HIGHER_SEEKBAR_PERCENT = 80;

	public interface PricePickerDialogListener
	{
		public void onDialogPositiveClick(BigDecimal selectedPrice);
	}

	PricePickerDialogListener mListener;
	Upgrade upgrade;
	BigDecimal selectedPrice;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		FoodCirclesApplication app = (FoodCirclesApplication) activity.getApplicationContext();
		upgrade = app.selectedUpgrade;
	}

	public void setListener(PricePickerDialogListener listener)
	{
		mListener = listener;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		Context context = new ContextThemeWrapper(getActivity(), R.style.AppBaseTheme);
		LayoutInflater localInflater = inflater.cloneInContext(context);

		View view = localInflater.inflate(R.layout.price_chooser_dialog, null);
		final TextView currentPriceTextView = (TextView) view.findViewById(R.id.textViewCurrentPrice);
		TextView price1TextView = (TextView) view.findViewById(R.id.textViewPrice1);
		TextView price2TextView = (TextView) view.findViewById(R.id.textViewPrice2);
		TextView price3TextView = (TextView) view.findViewById(R.id.textViewPrice3);
		SeekBar seekbar = (SeekBar) view.findViewById(R.id.seekBar1);
		final TextView numMealsTextView = (TextView) view.findViewById(R.id.textViewNumMeals);
		Spinner kidsLocationSpinner = (Spinner) view.findViewById(R.id.spinnerKidsLocation);

		price1TextView.setText("$" + upgrade.getDiscountPrice());
		price2TextView.setText("full price");
		price3TextView.setText("2x price");

		seekbar.setMax(100);
		seekbar.setProgress(0);
		selectedPrice = upgrade.getDiscountPrice();
		currentPriceTextView.setText("$" + selectedPrice);
		numMealsTextView.setText(selectedPrice.toBigInteger() + " meals");

		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				if (seekBar.getProgress() < LOWER_SEEKBAR_PERCENT)
				{
					selectedPrice = upgrade.getDiscountPrice();
				} else if (seekBar.getProgress() < HIGHER_SEEKBAR_PERCENT)
				{
					selectedPrice = upgrade.getFullPrice();
				} else
				{
					selectedPrice = upgrade.getFullPrice().add(upgrade.getFullPrice());
				}
				currentPriceTextView.setText("$" + selectedPrice);
				numMealsTextView.setText(selectedPrice.toBigInteger() + " meals");
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

		List<String> kidsLocations = new ArrayList<String>();
		kidsLocations.add("Location 1");
		kidsLocations.add("Location 2");

		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, kidsLocations);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		kidsLocationSpinner.setAdapter(spinnerAdapter);

		kidsLocationSpinner.setSelection(0);

		builder.setView(view).setPositiveButton("Buy", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
			{
				mListener.onDialogPositiveClick(selectedPrice);
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				PricePickerDialog.this.getDialog().cancel();
			}
		});
		return builder.create();
	}
}