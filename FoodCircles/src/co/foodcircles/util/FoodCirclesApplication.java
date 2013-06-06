package co.foodcircles.util;

import android.app.Application;
import co.foodcircles.activities.SignInActivity;
import co.foodcircles.json.Purchase;
import co.foodcircles.json.Restaurant;
import co.foodcircles.json.Upgrade;
import co.foodcircles.json.Venue;

public class FoodCirclesApplication extends Application
{
	public Venue selectedVenue;
	public Restaurant selectedRestaurant;
	public Upgrade selectedUpgrade;
	public Purchase selectedPurchase;
	
	public Purchase justPurchased;
}
