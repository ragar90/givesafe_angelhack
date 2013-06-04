package co.foodcircles.activities;

import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import co.foodcircles.R;

public class BuyOptionsActivity extends FragmentActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Intent intent = new Intent(this, PayPalService.class);

		// live: don't put any environment extra
		// sandbox: use PaymentActivity.ENVIRONMENT_SANDBOX
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_NO_NETWORK);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "<YOUR_CLIENT_ID>");

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buy_options_activity);

		BuyFragment buyFragment = new BuyFragment();
		buyFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction().add(R.id.fragment1, buyFragment).commit();
	}

	@Override
	public void onDestroy()
	{
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
	}
}
