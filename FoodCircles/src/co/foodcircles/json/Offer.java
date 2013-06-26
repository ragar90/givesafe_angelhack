package co.foodcircles.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Offer
{
	private String id;
	private String title;
	private String details;
	private int minDiners;
	private int discountPrice;
	private int fullPrice;
	private String imageUrl;

	public static List<Offer> parseOffers(String jsonString) throws JSONException
	{
		JSONArray jsonArray = new JSONArray(jsonString);
		List<Offer> offers = new ArrayList<Offer>();

		for (int i = 0; i < jsonArray.length(); i++)
		{
			offers.add(new Offer(jsonArray.getString(i)));
		}
		return offers;
	}

	public Offer(String jsonString) throws JSONException
	{
		JSONObject json = new JSONObject(jsonString);

		id = json.getString("id");
		title = json.getString("title");
		details = json.getString("details");
		minDiners = json.getInt("minimum_diners");
		discountPrice = 1;
		fullPrice = 9;
		imageUrl = "/media/BAhbBlsHOgZmSSIvMjAxMi8wNS8yNC8xM180N181MF81OTRfR29qb19FdGhpb3BpYW4ucG5nBjoGRVQ";
		//discountPrice = new BigDecimal(json.getString("discount_price"));
		//fullPrice = new BigDecimal(json.getString("full_price"));
		//imageUrl = json.getString("image_url");
	}

	public Offer(String id, String title, String details, int minDiners, int discountPrice, int fullPrice, String imageUrl)
	{
		super();
		this.id = id;
		this.title = title;
		this.details = details;
		this.minDiners = minDiners;
		this.discountPrice = discountPrice;
		this.fullPrice = fullPrice;
		this.imageUrl = imageUrl;
	}
	
	public Offer(boolean testOffer)
	{
		super();
		this.id = "1";
		this.title = "Loaded Crack Fries";
		this.details = "Delicious!";
		this.minDiners = 2;
		this.discountPrice = 2;
		this.fullPrice = 4;
		this.imageUrl = "/media/BAhbBlsHOgZmSSIvMjAxMi8wNS8yNC8xM180N181MF81OTRfR29qb19FdGhpb3BpYW4ucG5nBjoGRVQ";
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDetails()
	{
		return details;
	}

	public void setDetails(String details)
	{
		this.details = details;
	}

	public int getMinDiners()
	{
		return minDiners;
	}

	public void setMinDiners(int minDiners)
	{
		this.minDiners = minDiners;
	}

	public int getDiscountPrice()
	{
		return discountPrice;
	}

	public void setDiscountPrice(int discountPrice)
	{
		this.discountPrice = discountPrice;
	}

	public int getFullPrice()
	{
		return fullPrice;
	}

	public void setFullPrice(int fullPrice)
	{
		this.fullPrice = fullPrice;
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
