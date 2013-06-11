package co.foodcircles.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import co.foodcircles.json.Charity;
import co.foodcircles.json.Offer;
import co.foodcircles.json.Venue;

public class FoodCirclesApplication extends Application
{
	public List<Activity> activities;
	public Venue selectedVenue;
	public Offer selectedOffer;
	public boolean purchasedVoucher = false;

	public List<Venue> venues;
	public List<Charity> charities;

	public void addPoppableActivity(Activity activity)
	{
		if(activities == null) activities = new ArrayList<Activity>();
		activities.add(activity);
	}

	public void newTop()
	{
		for (Activity activity : activities)
		{
			try
			{
				activity.finish();
			}
			catch (Exception e)
			{
			}
		}
		activities = new ArrayList<Activity>();
	}
}
