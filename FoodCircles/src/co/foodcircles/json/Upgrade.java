package co.foodcircles.json;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Upgrade
{
	private String name;
	private BigDecimal discountPrice;
	private BigDecimal fullPrice;
	private String minGuests;

	public static List<Upgrade> parseUpgrades(String jsonString)
	{
		List<Upgrade> upgrades = new ArrayList<Upgrade>();
		upgrades.add(new Upgrade(""));
		upgrades.add(new Upgrade(""));
		upgrades.add(new Upgrade(""));
		
		upgrades.get(1).setName("1 Appetizer and 1 Dessert");
		upgrades.get(1).setDiscountPrice(new BigDecimal(2));
		upgrades.get(1).setFullPrice(new BigDecimal(4));
		
		upgrades.get(2).setName("2 Appetizers and 2 Desserts");
		upgrades.get(2).setDiscountPrice(new BigDecimal(3));
		upgrades.get(2).setFullPrice(new BigDecimal(6));
		

		return upgrades;
	}

	public Upgrade(String jsonString)
	{
		name = "1 Appetizer";
		discountPrice = new BigDecimal("1");
		fullPrice = new BigDecimal("2");
		minGuests = "2";
	}

	public Upgrade(String name, BigDecimal discountPrice, BigDecimal fullPrice, String minGuests)
	{
		super();
		this.name = name;
		this.discountPrice = discountPrice;
		this.fullPrice = fullPrice;
		this.minGuests = minGuests;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public BigDecimal getDiscountPrice()
	{
		return discountPrice;
	}

	public void setDiscountPrice(BigDecimal discountPrice)
	{
		this.discountPrice = discountPrice;
	}

	public BigDecimal getFullPrice()
	{
		return fullPrice;
	}

	public void setFullPrice(BigDecimal fullPrice)
	{
		this.fullPrice = fullPrice;
	}

	public String getMinGuests()
	{
		return minGuests;
	}

	public void setMinGuests(String minGuests)
	{
		this.minGuests = minGuests;
	}

}
