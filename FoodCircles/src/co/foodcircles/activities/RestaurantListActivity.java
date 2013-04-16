package co.foodcircles.activities;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.json.Restaurant;
import co.foodcircles.net.Net;
import co.foodcircles.util.FoodCirclesApplication;

public class RestaurantListActivity extends ListActivity
{
	private List<Restaurant> restaurants;
	private RestaurantAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant_list);

		restaurants = Net.getRestaurants();

		adapter = new RestaurantAdapter(restaurants);
		this.setListAdapter(adapter);
		adapter.notifyDataSetChanged();

		getListView().setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				FoodCirclesApplication app = (FoodCirclesApplication) RestaurantListActivity.this.getApplicationContext();
				app.selectedRestaurant = restaurants.get(position);
				Intent intent = new Intent(RestaurantListActivity.this, RestaurantUpgradesActivity.class);
				startActivity(intent);
			}
		});
	}

	private class RestaurantAdapter extends BaseAdapter
	{
		List<Restaurant> restaurants;

		private class ViewHolder
		{
			public ImageView logo;
			public TextView name;
			public TextView cuisine;
			public TextView upgrades;
			public TextView distance;
		}

		public RestaurantAdapter(List<Restaurant> restaurants)
		{
			this.restaurants = restaurants;
		}

		@Override
		public int getCount()
		{
			return restaurants.size();
		}

		@Override
		public Object getItem(int index)
		{
			return restaurants.get(index);
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
				view = getLayoutInflater().inflate(R.layout.restaurant_row, parent, false);
				holder = new ViewHolder();
				holder.logo = (ImageView) view.findViewById(R.id.imageViewLogo);
				holder.name = (TextView) view.findViewById(R.id.textViewName);
				holder.cuisine = (TextView) view.findViewById(R.id.textViewCuisine);
				holder.upgrades = (TextView) view.findViewById(R.id.textViewUpgrades);
				holder.distance = (TextView) view.findViewById(R.id.textViewDistance);
				view.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) view.getTag();
			}

			Restaurant restaurant = restaurants.get(position);
			// TODO: set the restaurant logo using that crazy lazy loading
			// library
			holder.name.setText(restaurant.getName());
			holder.cuisine.setText(restaurant.getCuisine());
			holder.upgrades.setText(restaurant.getUpgradeText());
			holder.distance.setText("10.0");

			return view;
		}

	}

}
