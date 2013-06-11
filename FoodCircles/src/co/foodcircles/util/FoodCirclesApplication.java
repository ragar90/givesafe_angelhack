package co.foodcircles.util;

import java.util.List;

import android.app.Application;
import co.foodcircles.activities.SignUpActivity;
import co.foodcircles.json.Charity;
import co.foodcircles.json.Offer;
import co.foodcircles.json.Venue;

public class FoodCirclesApplication extends Application
{
	public SignUpActivity signUpActivity;
	public Venue selectedVenue;
	public Offer selectedOffer;
	
	public List<Venue> venues;
	public List<Charity> charities;
}
