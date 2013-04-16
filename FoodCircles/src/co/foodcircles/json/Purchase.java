package co.foodcircles.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Purchase
{
	private Upgrade upgrade;
	private Restaurant restaurant;
	private Date expiration;
	private String code;
	
	public static List<Purchase> parsePurchases(String jsonString)
	{
		List<Purchase> purchases = new ArrayList<Purchase>();
		purchases.add(new Purchase(""));
		purchases.add(new Purchase(""));
		purchases.add(new Purchase(""));
		
		return purchases;
	}
	
	public Purchase(String jsonString)
	{
		upgrade = new Upgrade("");
		restaurant = new Restaurant("");
		expiration = new Date();
		code = "5dig1";
	}

	public Purchase(Upgrade upgrade, Restaurant restaurant, Date expiration, String code)
	{
		super();
		this.upgrade = upgrade;
		this.restaurant = restaurant;
		this.expiration = expiration;
		this.code = code;
	}

	public Upgrade getUpgrade()
	{
		return upgrade;
	}

	public void setUpgrade(Upgrade upgrade)
	{
		this.upgrade = upgrade;
	}

	public Restaurant getRestaurant()
	{
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant)
	{
		this.restaurant = restaurant;
	}

	public Date getExpiration()
	{
		return expiration;
	}

	public void setExpiration(Date expiration)
	{
		this.expiration = expiration;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

}
