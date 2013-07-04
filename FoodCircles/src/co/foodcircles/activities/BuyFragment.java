package co.foodcircles.activities;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.json.Charity;
import co.foodcircles.json.Offer;
import co.foodcircles.util.C;
import co.foodcircles.util.FoodCirclesApplication;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class BuyFragment extends Fragment
{
	int CENTS_IN_DOLLAR = 100;
	FoodCirclesApplication app;
	private Offer selectedOffer;
	private Charity selectedCharity;
	private Spinner offerSpinner;
	private SeekBar seekBar;
	private TextView price;
	private TextView meals;
	private Spinner donateTo;
	private TextView minPrice, medianPrice, maxPrice;
	private int priceValue;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view;

		view = inflater.inflate(R.layout.buy_options, null);
		C.overrideFonts(getActivity(), view);

		app = (FoodCirclesApplication) getActivity().getApplicationContext();

		final List<Offer> offers = app.selectedVenue.getOffers();
		selectedOffer = offers.get(0);
		selectedCharity = app.charities.get(0);

		offerSpinner = (Spinner) view.findViewById(R.id.spinnerNumFriends);
		ArrayList<String> offersList = new ArrayList<String>()
		{
			{
				for (Offer offer : offers)
				{
					add(offer.getTitle());
				}
			}
		};

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, offersList)
		{
			public View getView(int position, View convertView, ViewGroup parent)
			{
				View v = super.getView(position, convertView, parent);
				TextView tv = (TextView) v;
				String str = tv.getText().toString();

				if (str.contains("("))
				{

					Spannable spannable = new SpannableString(str);
					float textSize = tv.getTextSize();
					spannable.setSpan(new TextAppearanceSpan(getActivity(), R.style.TextAppearanceNormal), str.indexOf("("), str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.secondary_text)), str.indexOf("("), str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					spannable.setSpan(new AbsoluteSizeSpan((int) textSize), str.indexOf("("), str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					tv.setText(spannable);
				}

				return v;
			}
		};
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(R.layout.spinner_content_text);
		// Apply the adapter to the spinner
		offerSpinner.setAdapter(adapter);

		offerSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
			{
				selectedOffer = offers.get(position);

				minPrice.setText(NumberFormat.getCurrencyInstance().format(position + 1).replaceAll("\\.00", ""));

				int maxValue = selectedOffer.getFullPrice() * (position + 1) * 2;
				int range = maxValue - (position + 1);
				int medianValue = (range / 2) + position + 1;

				medianPrice.setText(NumberFormat.getCurrencyInstance().format(medianValue).replaceAll("\\.00", ""));
				maxPrice.setText(NumberFormat.getCurrencyInstance().format(maxValue).replaceAll("\\.00", ""));
				setPrices();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView)
			{
			}
		});

		donateTo = (Spinner) view.findViewById(R.id.spinnerDonateTo);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, new ArrayList<String>()
		{
			{
				for (Charity charity : app.charities)
					add(charity.getName());
			}
		});
		// Specify the layout to use when the list of choices appears
		adapter2.setDropDownViewResource(R.layout.spinner_content_text);
		// Apply the adapter to the spinner
		donateTo.setAdapter(adapter2);

		donateTo.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
			{
				selectedCharity = app.charities.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView)
			{
			}
		});

		price = (TextView) view.findViewById(R.id.textViewTotalPrice);
		meals = (TextView) view.findViewById(R.id.textViewDonated);

		seekBar = (SeekBar) view.findViewById(R.id.seekBar);
		seekBar.setMax(100 * (selectedOffer.getFullPrice() * 2 - 1));
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

		minPrice = (TextView) view.findViewById(R.id.textViewPrice1);
		medianPrice = (TextView) view.findViewById(R.id.textViewPrice2);
		maxPrice = (TextView) view.findViewById(R.id.textViewPrice3);

		setPrices();

		Button buyButton = (Button) view.findViewById(R.id.buttonBuy);

		buyButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				PayPalPayment voucherPayment = new PayPalPayment(new BigDecimal(priceValue), "USD", "Food Circles");

				Intent intent = new Intent(getActivity(), PaymentActivity.class);

				intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_NO_NETWORK);
				intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "ATpY8BAwAkcjGxyOJ9IjArCzDNfrqdQV3FaADv-iWszrCOxpjQ_I2elLntHS");
				intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, "jtkumario@gmail.com");
				intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "your-customer-id-in-your-system");
				intent.putExtra(PaymentActivity.EXTRA_PAYMENT, voucherPayment);

				startActivityForResult(intent, 0);
			}
		});

		minPrice.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				seekBar.setProgress(0);
				setPrices();
			}
		});

		medianPrice.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				seekBar.setProgress(seekBar.getMax() / 2);
				setPrices();
			}
		});

		maxPrice.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				seekBar.setProgress(seekBar.getMax());
				setPrices();
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
				dialog.setText(selectedCharity.getName(), selectedCharity.getDescription());
				dialog.show(fm, "simple_dialog");
			}
		});

		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == Activity.RESULT_OK)
		{
			PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
			if (confirm != null)
			{
				try
				{
					Log.i("paymentExample", confirm.toJSONObject().toString(4));
					// TODO: Server Verification here!!!
					app.purchasedVoucher = true;

					getActivity().finish();
					app.newTop();
				}
				catch (JSONException e)
				{
					Log.e("PaypalResult", "an extremely unlikely failure occurred: ", e);
				}
			}
		}
		else if (resultCode == Activity.RESULT_CANCELED)
		{
			Log.i("PaypalResult", "The user canceled.");
		}
		else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID)
		{
			Log.i("PaypalResult", "An invalid payment was submitted. Please see the docs.");
		}
	}

	private void setPrices()
	{
		int voucherLevel = offerSpinner.getSelectedItemPosition() + 1;
		// Seekbar.getprogress = number of cents
		int priceAmount = seekBar.getProgress();

		priceAmount += CENTS_IN_DOLLAR;
		priceAmount = priceAmount * voucherLevel;
		priceAmount = priceAmount / CENTS_IN_DOLLAR;
		priceValue = priceAmount;

		String priceText = NumberFormat.getCurrencyInstance().format(priceAmount);
		priceText = priceText.replaceAll("\\.00", "");
		price.setText(priceText);
		meals.setText("" + priceAmount + " meal" + (priceAmount > 1 ? "s" : ""));
	}
}
