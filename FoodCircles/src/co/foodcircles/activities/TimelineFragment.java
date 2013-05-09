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
		purchases.addAll(Purchase.parsePurchases(""));
		purchases.addAll(Purchase.parsePurchases(""));
		purchases.addAll(Purchase.parsePurchases(""));
		purchases.addAll(Purchase.parsePurchases(""));

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
		public final int HEADER_TYPE = 0;
		public final int VOUCHER_TYPE = 1;
		public final int FRIEND_TYPE = 2;
		public final int MONTH_TYPE = 3;
		List<Purchase> purchases;

		private class TimelineHolder
		{
			public TextView date;
			public TextView venue;
			public TextView childrenFed;
			public TextView used;

			public TextView me;
			public TextView friends;

			public TextView name;
			public ImageView image;

			public TextView year;
			public TextView month;
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
		public int getViewTypeCount()
		{
			return 4;
		}

		@Override
		public boolean areAllItemsEnabled()
		{
			return false;
		}

		@Override
		public boolean isEnabled(int position)
		{
			return getItemViewType(position) == VOUCHER_TYPE;
		}

		@Override
		public int getItemViewType(int position)
		{
			if (position == 0)
				return HEADER_TYPE;
			else if (position % 3 == 0)
				return FRIEND_TYPE;
			else if (position % 3 == 1)
				return VOUCHER_TYPE;
			else
				return MONTH_TYPE;
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
			final TimelineHolder holder;
			if (convertView == null)
			{
				switch (getItemViewType(position))
				{
					case HEADER_TYPE:
						view = getActivity().getLayoutInflater().inflate(R.layout.timeline_top_row, parent, false);
						holder = new TimelineHolder();
						holder.me = (TextView) view.findViewById(R.id.textViewMe);
						holder.friends = (TextView) view.findViewById(R.id.textViewFriends);
						view.setTag(holder);
						break;
					case VOUCHER_TYPE:
						view = getActivity().getLayoutInflater().inflate(R.layout.timeline_row, parent, false);
						holder = new TimelineHolder();
						holder.date = (TextView) view.findViewById(R.id.textViewDate);
						holder.venue = (TextView) view.findViewById(R.id.textViewVenue);
						holder.childrenFed = (TextView) view.findViewById(R.id.textViewChildrenFed);
						holder.used = (TextView) view.findViewById(R.id.textViewUsed);
						view.setTag(holder);
						break;
					case FRIEND_TYPE:
						view = getActivity().getLayoutInflater().inflate(R.layout.timeline_row_friend, parent, false);
						holder = new TimelineHolder();
						holder.date = (TextView) view.findViewById(R.id.textViewDate);
						holder.venue = (TextView) view.findViewById(R.id.textViewVenue);
						holder.name = (TextView) view.findViewById(R.id.textViewName);
						holder.childrenFed = (TextView) view.findViewById(R.id.textViewChildrenFed);
						holder.image = (ImageView) view.findViewById(R.id.imageView1);
						view.setTag(holder);
						break;
					case MONTH_TYPE:
						view = getActivity().getLayoutInflater().inflate(R.layout.timeline_row_month, parent, false);
						holder = new TimelineHolder();
						holder.month = (TextView) view.findViewById(R.id.textViewMonth);
						holder.year = (TextView) view.findViewById(R.id.textViewYear);
						view.setTag(holder);
						break;
				}
			}
			else
			{
				holder = (TimelineHolder) view.getTag();
			}

			Purchase purchase = purchases.get(position);

			return view;
		}

	}
}
