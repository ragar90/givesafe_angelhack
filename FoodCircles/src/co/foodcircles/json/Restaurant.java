package co.foodcircles.json;

import java.util.ArrayList;
import java.util.List;

public class Restaurant
{
	private String name;
	private String cuisine;
	private String upgradeText;
	private String phone;
	private String hours;
	private String url;
	private String address;
	private String details;
	private Double latitude;
	private Double longitude;
	private List<Upgrade> upgrades;
	
	public static List<Restaurant> parseRestaurants(String jsonString)
	{
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		for(int i = 0; i < 3; i++)
		{
			restaurants.add(new Restaurant(""));
		}
		return restaurants;
	}
	
	public Restaurant(String jsonString)
	{
		name = "Super Burgers";
		cuisine = "American";
		upgradeText = "the best burgers";
		phone = "555-123-5151";
		hours = "M-F 10am - 9pm";
		url = null;
		address = "123 Main St";
		details = "The best burgers you'll ever have!";
		latitude = 75.55;
		longitude = 30.12;
		upgrades = Upgrade.parseUpgrades("");
	}

	public Restaurant(String name, String cuisine, String upgradeText, String phone, String hours, String url, String address, String details, Double latitude, Double longitude, List<Upgrade> upgrades)
	{
		super();
		this.name = name;
		this.cuisine = cuisine;
		this.upgradeText = upgradeText;
		this.phone = phone;
		this.hours = hours;
		this.url = url;
		this.address = address;
		this.details = details;
		this.latitude = latitude;
		this.longitude = longitude;
		this.setUpgrades(upgrades);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCuisine()
	{
		return cuisine;
	}

	public void setCuisine(String cuisine)
	{
		this.cuisine = cuisine;
	}

	public String getUpgradeText()
	{
		return upgradeText;
	}

	public void setUpgradeText(String upgradeText)
	{
		this.upgradeText = upgradeText;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getHours()
	{
		return hours;
	}

	public void setHours(String hours)
	{
		this.hours = hours;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getDetails()
	{
		return details;
	}

	public void setDetails(String details)
	{
		this.details = details;
	}

	public Double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(Double latitude)
	{
		this.latitude = latitude;
	}

	public Double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(Double longitude)
	{
		this.longitude = longitude;
	}

	public List<Upgrade> getUpgrades()
	{
		return upgrades;
	}

	public void setUpgrades(List<Upgrade> upgrades)
	{
		this.upgrades = upgrades;
	}

}
