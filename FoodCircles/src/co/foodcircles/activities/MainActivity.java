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

import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends FragmentActivity
{
	private static final String[] CONTENT = new String[] { "NEWS", "FOOD", "MEALS", "ME" };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.simple_tabs);

		FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		C.overrideFonts(this, pager);

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);

		pager.setCurrentItem(getIntent().getIntExtra("tab", 0));
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
				case 3:
					return new AccountOptionsFragment();
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
