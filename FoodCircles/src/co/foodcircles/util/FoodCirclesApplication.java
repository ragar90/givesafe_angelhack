package co.foodcircles.util;

import android.app.Application;
import co.foodcircles.json.Purchase;
import co.foodcircles.json.Restaurant;
import co.foodcircles.json.Upgrade;

public class FoodCirclesApplication extends Application
{
	public Restaurant selectedRestaurant;
	public Upgrade selectedUpgrade;
	public Purchase selectedPurchase;

}
