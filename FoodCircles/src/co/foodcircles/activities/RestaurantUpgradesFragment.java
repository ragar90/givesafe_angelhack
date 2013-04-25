package co.foodcircles.activities;

import java.math.BigDecimal;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

public class RestaurantUpgradesFragment extends Fragment implements PricePickerDialogListener
{
	FoodCirclesApplication app;
	Restaurant restaurant;
	private TextView restaurantNameTextView;
	private ListView upgradesListView;
	private UpgradeAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.restaurant_upgrade_list, null);

		app = (FoodCirclesApplication) getActivity().getApplicationContext();
		restaurant = app.selectedRestaurant;

		restaurantNameTextView = (TextView) view.findViewById(R.id.textViewName);
		restaurantNameTextView.setText(app.selectedRestaurant.getName());

		upgradesListView = (ListView) view.findViewById(R.id.listViewOffers);

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
				dialog.setListener(RestaurantUpgradesFragment.this);
				dialog.show(getActivity().getSupportFragmentManager(), "PricePickerDialog");
			}
		});

		return view;
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
				view = getActivity().getLayoutInflater().inflate(R.layout.restaurant_upgrade_row, parent, false);
				holder = new ViewHolder();
				holder.top = (TextView) view.findViewById(R.id.textViewOfferTop);
				holder.bottom = (TextView) view.findViewById(R.id.textViewOfferBottom);
				view.setTag(holder);
			} else
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
		Intent intent = new Intent(getActivity(), UpgradePurchasedActivity.class);
		startActivity(intent);
		getActivity().finish();
	}
}
