package co.foodcircles.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import co.foodcircles.R;
import co.foodcircles.util.C;

public class ViewVoucherActivity extends FragmentActivity
{
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

		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
		{
			teeth.setBackgroundDrawable(teethDrawable);
		}
		else
		{
			teeth.setBackground(teethDrawable);
		}
		
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
