package co.foodcircles.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Charity
{
	public String id;
	private String name;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String description;
	private String imageUrl;

	public static List<Charity> parseCharities(String jsonString) throws JSONException
	{
		JSONArray jsonArray = new JSONArray(jsonString);
		List<Charity> charities = new ArrayList<Charity>();

		for (int i = 0; i < jsonArray.length(); i++)
		{
			charities.add(new Charity(jsonArray.getString(i)));
		}
		return charities;
	}

	public Charity(String jsonString) throws JSONException
	{
		JSONObject json = new JSONObject(jsonString);

		id = json.getString("id");
		name = json.getString("name");
		address = json.getString("address");
		city = json.getString("city");
		state = json.getString("state");
		zip = json.getString("zip");
		description = json.getString("description");
		imageUrl = json.getString("image_url");
	}

	public Charity(String id, String name, String address, String city, String state, String zip, String description, String imageUrl)
	{
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.description = description;
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

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
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
