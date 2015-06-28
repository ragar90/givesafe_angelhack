package co.foodcircles.json;

import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.foodcircles.util.AndroidUtils;

public class Venue
{
	private String id;
	private String name;
    private String slug;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String distance;
	private double latitude;
	private double longitude;
	private String description;
	private String phone;
	private String web;
	private List<String> tags;
	private List<Social> socials;
	private List<Offer> offers;
	private String openTimes;
	private String imageUrl;
	private String largeImageUrl;
	private int vouchersAvailable;
    private int totalPeopleAided;
    private int peopleAided;
    private int weeklyGoal;
	private String deviceId;

	public static List<Venue> parseVenues(String jsonString) throws JSONException
	{
		JSONObject jsonObject=new JSONObject(jsonString);
		JSONArray jsonArray = jsonObject.getJSONArray("content");
		List<Venue> venues = new ArrayList<Venue>();

		for (int i = 0, ii = jsonArray.length(); i < ii; i++)
		{
			try {
                Venue venue = new Venue(jsonArray.getString(i));
                int totalPeopleAided=AndroidUtils.safelyGetJsonInt(jsonObject, "total_people_aided");
                int peopleAided=AndroidUtils.safelyGetJsonInt(jsonObject, "people_aided");
                int weeklyGoal=AndroidUtils.safelyGetJsonInt(jsonObject, "weekly_goal");
                venue.setTotalPeopleAided(totalPeopleAided);
                venue.setPeopleAided(peopleAided);
                venue.setWeeklyGoal(weeklyGoal);
                venues.add(venue);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return venues;
	}
	public static Venue parseHomeless(String jsonString){
		try{
			JSONObject json = new JSONObject(jsonString);
			Venue v = new Venue(AndroidUtils.safelyGetJsonString(json,"content"));
			return v;
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public Venue(String jsonString) throws JSONException
	{
		JSONObject json = new JSONObject(jsonString);
		id = AndroidUtils.safelyGetJsonString(json,"id");
		name = AndroidUtils.safelyGetJsonString(json,"name");
        slug = AndroidUtils.safelyGetJsonString(json,"slug");
		address = AndroidUtils.safelyGetJsonString(json,"address");;
		city = AndroidUtils.safelyGetJsonString(json,"city");;
		state = AndroidUtils.safelyGetJsonString(json,"state");;
		zip = AndroidUtils.safelyGetJsonString(json,"zip");
		distance = AndroidUtils.safelyGetJsonString(json, "distance");
		latitude = AndroidUtils.safelyGetJsonDouble(json, "lat");
		longitude = AndroidUtils.safelyGetJsonDouble(json, "lon");
		description = AndroidUtils.safelyGetJsonString(json, "description");
		phone = AndroidUtils.safelyGetJsonString(json,"phone");
		web = AndroidUtils.safelyGetJsonString(json, "web");
		vouchersAvailable=AndroidUtils.safelyGetJsonInt(json, "vouchers_available");
		openTimes = AndroidUtils.safelyGetJsonString(json, "open_times");
		deviceId = AndroidUtils.safelyGetJsonString(json, "device_id");
		tags = new ArrayList<String>();
		JSONArray tagsJson = AndroidUtils.safelyGetJsonArray(json,"tags");

		for (int i = 0; i < tagsJson.length(); i++)
		{
			tags.add(AndroidUtils.safelyGetJsonString(tagsJson.getJSONObject(i),"name"));
		}

		offers = new ArrayList<Offer>();
		JSONArray offersJson = AndroidUtils.safelyGetJsonArray(json,"offers");
		for (int i = 0; i < offersJson.length(); i++)
		{
			try{
				offers.add(new Offer(offersJson.getString(i)));
			}catch(Exception e){ }
		}

		socials = new ArrayList<Social>();
		JSONArray socialsJson = AndroidUtils.safelyGetJsonArray(json,"social_links");
		for (int i = 0; i < socialsJson.length(); i++)
		{
			try{
			socials.add(new Social(socialsJson.getString(i)));
			}catch(Exception e){
			}
		}
		

		try{
			imageUrl = AndroidUtils.safelyGetJsonString(json,"timeline_image");
		}catch(Exception e){}
		try{
			largeImageUrl = AndroidUtils.safelyGetJsonString(json,"main_image");
		}catch(Exception e){}

	}

	public Venue(String id, String name, String slug, String address, String city, String state, String zip, double latitude, double longitude, String description, String phone,
			String url, List<String> tags, List<Offer> offers, String openTimes, String imageUrl)
	{
		super();
		this.id = id;
		this.name = name;
        this.slug = slug;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.latitude = latitude;
		this.longitude = longitude;
		this.description = description;
		this.phone = phone;
		this.web = url;
		this.tags = tags;
		this.offers = offers;
		this.openTimes = openTimes;
		this.imageUrl = imageUrl;
	}
	
	public Venue(boolean testVenue)
	{
		super();
		this.id = "1";
		this.name = "Stella's Lounge";
        this.slug = "test-slug";
		this.address = "Address";
		this.city = "City";
		this.state = "State";
		this.zip = "Zip";
		this.distance = "10mi";
		this.latitude = 80;
		this.longitude = 80;
		this.description = "Description";
		this.phone = "Phone";
		this.web = "Web";
		this.tags = new ArrayList<String>();
		this.offers = new ArrayList<Offer>();
		offers.add(new Offer(true));
		offers.add(new Offer(true));
		this.openTimes = "8:00AM - 10:00PM";
		this.imageUrl = "/media/BAhbBlsHOgZmSSIvMjAxMi8wNS8yNC8xM180N181MF81OTRfR29qb19FdGhpb3BpYW4ucG5nBjoGRVQ";
		this.largeImageUrl = "/media/BAhbBlsHOgZmSSIvMjAxMi8wNS8yNC8xM180N181MF81OTRfR29qb19FdGhpb3BpYW4ucG5nBjoGRVQ";
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

	public String getDistance()
	{
		return distance;
	}

	public void setDistance(String distance)
	{
		this.distance = distance;
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

	public int checkEmpty() 
	{
		if (this.vouchersAvailable > 0) {
			return View.GONE;
		} else {
			return View.VISIBLE;
		}
	}

	public String getWeb()
	{
		return web;
	}

	public void setWeb(String web)
	{
		this.web = web;
	}

	public List<String> getTags()
	{
		return tags;
	}

	public void setTags(List<String> tags)
	{
		this.tags = tags;
	}
	
	public String getTagsString()
	{
		return tags.toString().replace("[", "").replace("]",  "");
	}
	
	public String getFirstTag()
	{
		if(tags.size() == 0) return "";
		else return tags.get(0);
	}

	public List<Offer> getOffers()
	{
		return offers;
	}

	public void setOffers(List<Offer> offers)
	{
		this.offers = offers;
	}

	public List<Social> getSocial()
	{
		return socials;
	}

	public void setSocial(List<Social> social)
	{
		this.socials = social;
	}

	public String getOpenTimes()
	{
		return openTimes;
	}

	//This call will return the open times arranged as HH:MM for the current date.
	
	
	public void setOpenTimes(String openTimes)
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

	public String getLargeImageUrl()
	{
		return largeImageUrl;
	}

	public void setLargeImageUrl(String imageUrl)
	{
		this.largeImageUrl = imageUrl;
	}

	public int getVouchersAvailable() {
		
		return vouchersAvailable;
	}

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getTotalPeopleAided() {
        return totalPeopleAided;
    }

    public void setTotalPeopleAided(int total_people_aided) {
        this.totalPeopleAided = total_people_aided;
    }

    public int getPeopleAided() {
        return peopleAided;
    }

    public void setPeopleAided(int people_aided) {
        this.peopleAided = people_aided;
    }

    public int getWeeklyGoal() {
        return weeklyGoal;
    }

    public void setWeeklyGoal(int weekly_goal) {
        this.weeklyGoal = weekly_goal;
    }
}
