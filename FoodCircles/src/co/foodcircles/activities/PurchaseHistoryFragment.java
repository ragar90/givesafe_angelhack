package co.foodcircles.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.json.Purchase;
import co.foodcircles.net.Net;
import co.foodcircles.util.FoodCirclesApplication;

public class PurchaseHistoryFragment extends ListFragment
{
	private List<Purchase> purchases;
	private PurchaseAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.purchase_history, null);

		purchases = Purchase.parsePurchases("");

		adapter = new PurchaseAdapter(purchases);
		this.setListAdapter(adapter);
		adapter.notifyDataSetChanged();
		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		FoodCirclesApplication app = (FoodCirclesApplication) PurchaseHistoryFragment.this.getActivity().getApplicationContext();
		app.selectedPurchase = purchases.get(position);
		app.selectedRestaurant = purchases.get(position).getRestaurant();

		Intent intent = new Intent(getActivity(), ViewVoucherActivity.class);
		startActivity(intent);
	}

	private class PurchaseAdapter extends BaseAdapter
	{
		private final int VIEW_TYPE_LEFT = 0;
		private final int VIEW_TYPE_RIGHT = 1;

		List<Purchase> purchases;

		private class ViewHolder
		{
			public TextView date;
			public TextView kidsFed;
			public TextView restaurant;
			public ImageView branch;
		}

		public PurchaseAdapter(List<Purchase> purchases)
		{
			this.purchases = purchases;
		}

		@Override
		public boolean areAllItemsEnabled()
		{
			return false;
		}

		@Override
		public boolean isEnabled(int position)
		{
			return position < purchases.size();
		}

		@Override
		public int getCount()
		{
			return purchases.size() + 1;
		}

		@Override
		public Object getItem(int index)
		{
			if (index < purchases.size())
				return purchases.get(index);
			else
				return null;
		}

		@Override
		public int getItemViewType(int position)
		{
			return position % 2;
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
				if (getItemViewType(position) == VIEW_TYPE_LEFT)
					view = getActivity().getLayoutInflater().inflate(R.layout.purchase_history_row_left, parent, false);
				else
					view = getActivity().getLayoutInflater().inflate(R.layout.purchase_history_row_right, parent, false);

				holder = new ViewHolder();
				holder.date = (TextView) view.findViewById(R.id.textViewDate);
				holder.kidsFed = (TextView) view.findViewById(R.id.textViewKidsFed);
				holder.restaurant = (TextView) view.findViewById(R.id.textViewRestaurant);
				holder.branch = (ImageView) view.findViewById(R.id.imageViewBranch);
				view.setTag(holder);
			} else
			{
				holder = (ViewHolder) view.getTag();
			}

			if (position < purchases.size())
			{
				Purchase purchase = purchases.get(position);
				holder.date.setText("Date");
				holder.kidsFed.setText("5 kids fed via");
				holder.restaurant.setText("Super Burger");
			} else
			{
				holder.date.setText("");
				holder.kidsFed.setText("");
				holder.restaurant.setText("");
			}
			
			if(position == 0)
			{
				holder.branch.setVisibility(View.INVISIBLE);
			}
			else
			{
				holder.branch.setVisibility(View.VISIBLE);
			}

			return view;
		}

	}
}
