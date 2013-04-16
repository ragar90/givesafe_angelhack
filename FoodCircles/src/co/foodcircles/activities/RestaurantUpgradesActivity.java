package co.foodcircles.activities;

import java.math.BigDecimal;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.activities.PricePickerDialog.PricePickerDialogListener;
import co.foodcircles.json.Restaurant;
import co.foodcircles.json.Upgrade;
import co.foodcircles.util.FoodCirclesApplication;

public class RestaurantUpgradesActivity extends FragmentActivity implements PricePickerDialogListener
{
	FoodCirclesApplication app;
	Restaurant restaurant;
	private TextView restaurantNameTextView;
	private ListView upgradesListView;
	private UpgradeAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant_upgrade_list);

		app = (FoodCirclesApplication) getApplicationContext();
		restaurant = app.selectedRestaurant;

		restaurantNameTextView = (TextView) findViewById(R.id.textViewName);
		restaurantNameTextView.setText(app.selectedRestaurant.getName());

		upgradesListView = (ListView) findViewById(R.id.listViewOffers);

		adapter = new UpgradeAdapter(restaurant.getUpgrades());

		upgradesListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		upgradesListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				app.selectedUpgrade = restaurant.getUpgrades().get(position);
		        PricePickerDialog dialog = new PricePickerDialog();
		        dialog.show(getSupportFragmentManager(), "PricePickerDialog");
			}
		});
	}

	private class UpgradeAdapter extends BaseAdapter
	{
		List<Upgrade> upgrades;

		private class ViewHolder
		{
			public TextView top;
			public TextView bottom;
		}

		public UpgradeAdapter(List<Upgrade> upgrades)
		{
			this.upgrades = upgrades;
		}

		@Override
		public int getCount()
		{
			return upgrades.size();
		}

		@Override
		public Upgrade getItem(int index)
		{
			return upgrades.get(index);
		}

		@Override
		public long getItemId(int index)
		{
			return index;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null)
			{
				view = getLayoutInflater().inflate(R.layout.restaurant_upgrade_row, parent, false);
				holder = new ViewHolder();
				holder.top = (TextView) view.findViewById(R.id.textViewOfferTop);
				holder.bottom = (TextView) view.findViewById(R.id.textViewOfferBottom);
				view.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) view.getTag();
			}

			Upgrade upgrade = upgrades.get(position);
			holder.top.setText(upgrade.getName() + " - $" + upgrade.getDiscountPrice());
			holder.bottom.setText("(min party " + upgrade.getMinGuests() + " guests)");

			return view;
		}

	}

	@Override
	public void onDialogPositiveClick(BigDecimal selectedPrice)
	{
		
	}
}
