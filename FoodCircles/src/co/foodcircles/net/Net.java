package co.foodcircles.net;

import java.util.List;

import co.foodcircles.json.Purchase;
import co.foodcircles.json.Restaurant;

public class Net
{
	public static List<Restaurant> getRestaurants()
	{
		String response = "";
		return Restaurant.parseRestaurants(response);
	}
	
	public static List<Purchase> getPurchases()
	{
		String response = "";
		return Purchase.parsePurchases(response);
	}
}
