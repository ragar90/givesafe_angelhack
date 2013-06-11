package co.foodcircles.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Reservation
{
	private String id;
	private String user; // TODO: this is probably a User type
	private Venue venue;
	private Offer offer;
	private Charity charity;
	private long datePurchased;

	public static List<Reservation> parseReservations(String jsonString) throws JSONException
	{
		List<Reservation> reservations = new ArrayList<Reservation>();
		return reservations;
		/*
		JSONArray jsonArray = new JSONArray(jsonString);
		List<Reservation> reservations = new ArrayList<Reservation>();

		for (int i = 0; i < jsonArray.length(); i++)
		{
			reservations.add(new Reservation(jsonArray.getString(i)));
		}
		return reservations;
		*/
	}

	public Reservation(String jsonString) throws JSONException
	{
		JSONObject json = new JSONObject(jsonString);
		id = json.getString("id");
		user = json.getString("user");
		venue = new Venue(json.getString("venue"));
		offer = new Offer(json.getString("offer"));
		charity = new Charity(json.getString("charity"));
		datePurchased = json.getLong("date_purchased");
	}

	public Reservation(String id, String user, Venue venue, Offer offer, Charity charity, long datePurchased)
	{
		super();
		this.id = id;
		this.user = user;
		this.venue = venue;
		this.offer = offer;
		this.charity = charity;
		this.datePurchased = datePurchased;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public Venue getVenue()
	{
		return venue;
	}

	public void setVenue(Venue venue)
	{
		this.venue = venue;
	}

	public Offer getOffer()
	{
		return offer;
	}

	public void setOffer(Offer offer)
	{
		this.offer = offer;
	}

	public Charity getCharity()
	{
		return charity;
	}

	public void setCharity(Charity charity)
	{
		this.charity = charity;
	}

	public long getDatePurchased()
	{
		return datePurchased;
	}

	public void setDatePurchased(long datePurchased)
	{
		this.datePurchased = datePurchased;
	}

}
