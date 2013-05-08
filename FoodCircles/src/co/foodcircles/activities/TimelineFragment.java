package co.foodcircles.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.json.Purchase;
import co.foodcircles.util.FoodCirclesApplication;

public class TimelineFragment extends ListFragment
{
	private List<Purchase> purchases;
	private PurchaseAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.timeline_list, null);

		purchases = Purchase.parsePurchases("");

		adapter = new PurchaseAdapter(purchases);
		this.setListAdapter(adapter);
		adapter.notifyDataSetChanged();
		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		FoodCirclesApplication app = (FoodCirclesApplication) getActivity().getApplicationContext();
		app.selectedPurchase = purchases.get(position);
		app.selectedRestaurant = purchases.get(position).getRestaurant();

		Intent intent = new Intent(getActivity(), ViewVoucherActivity.class);
		startActivity(intent);
	}

	private class PurchaseAdapter extends BaseAdapter
	{
		List<Purchase> purchases;

		private class MyPurchaseHolder
		{
			public TextView venue;
			public TextView childrenFed;
			public TextView used;
		}

		public PurchaseAdapter(List<Purchase> purchases)
		{
			this.purchases = purchases;
		}

		@Override
		public int getCount()
		{
			return purchases.size();
		}

		@Override
		public Object getItem(int index)
		{
			return purchases.get(index);
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
			final MyPurchaseHolder holder;
			if (convertView == null)
			{
				view = getActivity().getLayoutInflater().inflate(R.layout.timeline_row, parent, false);
				holder = new MyPurchaseHolder();
				holder.venue = (TextView) view.findViewById(R.id.textViewVenue);
				holder.childrenFed = (TextView) view.findViewById(R.id.textViewChildrenFed);
				holder.used = (TextView) view.findViewById(R.id.textViewUsed);
				view.setTag(holder);
			} else
			{
				holder = (MyPurchaseHolder) view.getTag();
			}

			Purchase purchase = purchases.get(position);
			holder.venue.setText("STELLA'S LOUNGE");
			holder.childrenFed.setText("2 children fed");
			holder.used.setText("Used");

			return view;
		}

	}
}
