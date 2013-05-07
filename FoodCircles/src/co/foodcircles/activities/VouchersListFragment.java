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

public class VouchersListFragment extends ListFragment
{
	private List<Purchase> purchases;
	private PurchaseAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.list, null);

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

		private class ViewHolder
		{
			public ImageView logo;
			public TextView name;
			public TextView upgrade;
			public TextView expiration;
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
			final ViewHolder holder;
			if (convertView == null)
			{
				view = getActivity().getLayoutInflater().inflate(R.layout.voucher_row, parent, false);
				holder = new ViewHolder();
				holder.logo = (ImageView) view.findViewById(R.id.imageViewLogo);
				holder.name = (TextView) view.findViewById(R.id.textViewName);
				holder.upgrade = (TextView) view.findViewById(R.id.textViewUpgrade);
				holder.expiration = (TextView) view.findViewById(R.id.textViewExpiration);
				view.setTag(holder);
			} else
			{
				holder = (ViewHolder) view.getTag();
			}

			Purchase purchase = purchases.get(position);
			// TODO: set the restaurant logo using that crazy lazy loading
			// library
			holder.name.setText("Name");
			holder.upgrade.setText("Upgrade");
			holder.expiration.setText("Expir");

			return view;
		}

	}
}
