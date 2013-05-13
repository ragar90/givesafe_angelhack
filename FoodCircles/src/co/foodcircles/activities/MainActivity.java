package co.foodcircles.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import co.foodcircles.R;

import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends FragmentActivity
{
	private static final String[] CONTENT = new String[] { "Account", "News", "Food" };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_tabs);

		FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);

		if (getIntent().hasExtra("tab"))
		{
			pager.setCurrentItem(getIntent().getIntExtra("tab", 1));
		} else
		{
			pager.setCurrentItem(1);
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
					return new TimelineFragment();
				case 1:
					return new CarouselFragment();
				case 2:
					return new RestaurantGridFragment();
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
