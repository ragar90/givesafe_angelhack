package co.foodcircles.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Venue
{
	private String id;
	private String name;
	private String address;
	private String city;
	private String state;
	private String zip;
	private double latitude;
	private double longitude;
	private String shortDescription;
	private String description;
	private String phone;
	private String url;
	private List<String> tags;
	private List<Offer> offers;
	private List<String> openTimes;
	private String imageUrl;

	public static List<Venue> parseVenues(String jsonString) throws JSONException
	{
		JSONArray jsonArray = new JSONArray(jsonString);
		List<Venue> venues = new ArrayList<Venue>();

		for (int i = 0; i < jsonArray.length(); i++)
		{
			venues.add(new Venue(jsonArray.getString(i)));
		}
		return venues;
	}

	public Venue(String jsonString) throws JSONException
	{
		JSONObject json = new JSONObject(jsonString);

		id = json.getString("id");
		name = json.getString("name");
		address = json.getString("address");
		city = json.getString("city");
		state = json.getString("state");
		zip = json.getString("zip");
		latitude = json.getDouble("latitude");
		longitude = json.getDouble("longitude");
		shortDescription = json.getString("short_description");
		description = json.getString("description");
		phone = json.getString("phone");
		url = json.getString("url");

		tags = new ArrayList<String>();
		JSONArray tagsJson = json.getJSONArray("tags");
		for (int i = 0; i < tagsJson.length(); i++)
		{
			tags.add(tagsJson.getString(i));
		}

		offers = new ArrayList<Offer>();
		JSONArray offersJson = json.getJSONArray("offers");
		for (int i = 0; i < offersJson.length(); i++)
		{
			offers.add(new Offer(offersJson.getString(i)));
		}

		openTimes = new ArrayList<String>();
		JSONArray openTimesJson = json.getJSONArray("open_times");
		for (int i = 0; i < openTimesJson.length(); i++)
		{
			openTimes.add(openTimesJson.getString(i));
		}

		imageUrl = json.getString("image_url");

	}

	public Venue(String id, String name, String address, String city, String state, String zip, double latitude, double longitude, String shortDescription, String description, String phone,
			String url, List<String> tags, List<Offer> offers, List<String> openTimes, String imageUrl)
	{
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.latitude = latitude;
		this.longitude = longitude;
		this.shortDescription = shortDescription;
		this.description = description;
		this.phone = phone;
		this.url = url;
		this.tags = tags;
		this.offers = offers;
		this.openTimes = openTimes;
		this.imageUrl = imageUrl;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getZip()
	{
		return zip;
	}

	public void setZip(String zip)
	{
		this.zip = zip;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	public String getShortDescription()
	{
		return shortDescription;
	}

	public void setShortDescription(String shortDescription)
	{
		this.shortDescription = shortDescription;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public List<String> getTags()
	{
		return tags;
	}

	public void setTags(List<String> tags)
	{
		this.tags = tags;
	}

	public List<Offer> getOffers()
	{
		return offers;
	}

	public void setOffers(List<Offer> offers)
	{
		this.offers = offers;
	}

	public List<String> getOpenTimes()
	{
		return openTimes;
	}

	public void setOpenTimes(List<String> openTimes)
	{
		this.openTimes = openTimes;
	}

	public String getImageUrl()
	{
		return imageUrl;
	}

	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

}
