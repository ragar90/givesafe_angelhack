package co.foodcircles.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import co.foodcircles.R;
import co.foodcircles.util.C;
import co.foodcircles.util.FoodCirclesApplication;

import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends FragmentActivity
{
	private static final String[] CONTENT = new String[] { "NEWS", "FOOD", "YOU" };
	ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.simple_tabs);

		FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);

		pager.setCurrentItem(getIntent().getIntExtra("tab", 0));

		C.overrideFonts(this, findViewById(R.id.root));
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		FoodCirclesApplication app = (FoodCirclesApplication) getApplicationContext();
		if (app.purchasedVoucher)
		{
			pager.setCurrentItem(2);

			FragmentManager fm = getSupportFragmentManager();
			ReceiptDialogFragment receiptDialog = new ReceiptDialogFragment();
			receiptDialog.show(fm, "receipt_dialog");

			app.purchasedVoucher = false;
		}
	}

	class GoogleMusicAdapter extends FragmentPagerAdapter
	{
		public GoogleMusicAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position)
		{
			switch (position)
			{
				case 0:
					return new CarouselFragment();
				case 1:
					return new RestaurantGridFragment();
				case 2:
					return new TimelineFragment();
				default:
					return null;
			}
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getCount()
		{
			return CONTENT.length;
		}
	}
}
