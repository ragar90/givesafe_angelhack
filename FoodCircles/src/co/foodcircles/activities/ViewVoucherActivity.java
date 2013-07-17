package co.foodcircles.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.util.C;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class ViewVoucherActivity extends FragmentActivity
{
	Button markAsUsedButton;

	MixpanelAPI mixpanel;

	@Override
	public void onStart()
	{
		super.onStart();
		mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.mixpanel_token));
	}

	@Override
	public void onDestroy()
	{
		mixpanel.flush();
		super.onDestroy();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.voucher_receipt);
		C.overrideFonts(this, findViewById(R.id.root));

		View teeth = findViewById(R.id.viewTiledTeeth);
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

		TextView venueName = (TextView) findViewById(R.id.textViewVenue);
		SpannableString content = new SpannableString(venueName.getText());
		content.setSpan(new UnderlineSpan(), 0, venueName.getText().length(), 0);
		venueName.setText(content);

		markAsUsedButton = (Button) findViewById(R.id.buttonMarkAsUsed);
		markAsUsedButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MP.track(mixpanel, "Voucher", "Clicked mark as used");
				AlertDialog.Builder builder = new AlertDialog.Builder(ViewVoucherActivity.this);
				builder.setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						MP.track(mixpanel, "Voucher", "Marked as used");
					}
				}).setNegativeButton("No", null).show();
			}
		});

		ImageView facebook = (ImageView) findViewById(R.id.imageViewFacebook);
		facebook.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
			}
		});
	}
}
