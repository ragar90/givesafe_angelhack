package co.foodcircles.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import co.foodcircles.R;
import co.foodcircles.util.C;

public class ViewVoucherActivity extends FragmentActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voucher_receipt);
		C.overrideFonts(this, findViewById(R.id.root));
	}
}
