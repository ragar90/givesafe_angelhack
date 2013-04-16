package co.foodcircles.net;

import java.util.ArrayList;
import java.util.List;

import co.foodcircles.json.Restaurant;

public class Net
{
	public static List<Restaurant> getRestaurants()
	{
		String response = "";
		return Restaurant.parseRestaurants(response);
	}
}
