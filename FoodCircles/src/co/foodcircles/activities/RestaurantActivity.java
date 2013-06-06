package co.foodcircles.activities;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import co.foodcircles.R;
import co.foodcircles.json.Venue;
import co.foodcircles.util.C;
import co.foodcircles.util.FoodCirclesApplication;

import com.viewpagerindicator.TabPageIndicator;

public class RestaurantActivity extends FragmentActivity {
    private static String[] CONTENT = new String[] { "OFFER", "INFO" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.simple_tabs);
        
        FoodCirclesApplication app = (FoodCirclesApplication) getApplicationContext();
        Venue selectedVenue = app.selectedVenue;
        CONTENT[0] = selectedVenue.getName().toUpperCase(Locale.getDefault());

        FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);

		C.overrideFonts(this, findViewById(R.id.root));
    }

    class GoogleMusicAdapter extends FragmentPagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	switch(position)
        	{
        		case 0: return new VenueItemFragment();
        		case 1: return new VenueProfileFragment();
        		default: return null;
        	}
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase(Locale.getDefault());
        }

        @Override
        public int getCount() {
          return CONTENT.length;
        }
    }
}
