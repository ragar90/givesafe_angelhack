package co.foodcircles.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.util.C;
import co.foodcircles.util.FoodCirclesApplication;

public class ReceiptDialogFragment extends DialogFragment
{
	Button markAsUsedButton;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		setStyle(STYLE_NO_FRAME, R.style.AppBaseTheme);
		this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		View v = inflater.inflate(R.layout.voucher_receipt, container, false);
		C.overrideFonts(getActivity(), v.findViewById(R.id.root));
		
		final FoodCirclesApplication app = (FoodCirclesApplication) getActivity().getApplicationContext();

		View teeth = v.findViewById(R.id.viewTiledTeeth);
		BitmapDrawable teethDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.receipt_tooth));
		teethDrawable.setTileModeX(TileMode.REPEAT);

		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
		{
			teeth.setBackgroundDrawable(teethDrawable);
		}
		else
		{
			teeth.setBackground(teethDrawable);
		}
		
		TextView venueText = (TextView) v.findViewById(R.id.textViewVenue);
		venueText.setPaintFlags(venueText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

		markAsUsedButton = (Button) v.findViewById(R.id.buttonMarkAsUsed);
		markAsUsedButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("Are you sure?").setPositiveButton("Yes", null).setNegativeButton("No", null).show();
			}
		});
		
		TextView venueTextView = (TextView) v.findViewById(R.id.textViewVenue);
		venueTextView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), VenueProfileActivity.class);
				app.selectedVenue = app.venues.get(0);
				startActivity(intent);
			}
		});

		ImageView facebook = (ImageView) v.findViewById(R.id.imageViewFacebook);
		facebook.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
			}
		});
		
		return v;
	}
}
