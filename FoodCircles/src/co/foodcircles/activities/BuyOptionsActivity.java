package co.foodcircles.activities;

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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buy_options_activity);

		BuyFragment buyFragment = new BuyFragment();
		buyFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction().add(R.id.fragment1, buyFragment).commit();

	}

}
