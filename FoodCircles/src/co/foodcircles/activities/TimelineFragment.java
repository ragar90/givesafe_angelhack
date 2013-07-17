package co.foodcircles.activities;

import java.util.List;

import org.json.JSONException;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import co.foodcircles.R;
import co.foodcircles.json.Reservation;
import co.foodcircles.util.C;
import co.foodcircles.util.FoodCirclesApplication;

public class TimelineFragment extends ListFragment
{
	private List<Reservation> reservations;
	private ReservationAdapter adapter;
	
	MixpanelAPI mixpanel;

	@Override
	public void onStart()
	{
		super.onStart();
		mixpanel = MixpanelAPI.getInstance(getActivity(), getResources().getString(R.string.mixpanel_token));
	}

	@Override
	public void onDestroy()
	{
		mixpanel.flush();
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.timeline_list, null);
		C.overrideFonts(getActivity(), view);

		try
		{
			reservations = Reservation.parseReservations("");
			reservations.addAll(Reservation.parseReservations(""));
			reservations.addAll(Reservation.parseReservations(""));
			reservations.addAll(Reservation.parseReservations(""));
			reservations.addAll(Reservation.parseReservations(""));
		}
		catch (JSONException e)
		{
		}

		adapter = new ReservationAdapter(reservations);
		this.setListAdapter(adapter);
		adapter.notifyDataSetChanged();

		final boolean connectedToSocialAccount = false;
		TextView inviteOrImportFriends = (TextView) view.findViewById(R.id.textViewInviteOrImport);

		if (connectedToSocialAccount)
		{
			inviteOrImportFriends.setText("Invite Friends");
		}
		else
		{
			inviteOrImportFriends.setText("Import Friends");
		}

		LinearLayout inviteFriends = (LinearLayout) view.findViewById(R.id.inviteFriendsLayout);
		inviteFriends.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				boolean connectedToSocialAccount = false;
				if (connectedToSocialAccount)
				{
					MP.track(mixpanel, "Timeline", "Clicked invite friends");
					Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
					shareIntent.setType("text/plain");
					shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Food Circles");
					shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Buy one appetizer or dessert for $1, feed one child in need.");
					startActivity(Intent.createChooser(shareIntent, "Share with"));
				}
				else
				{
					MP.track(mixpanel, "Timeline", "Clicked import friends");
					Intent intent = new Intent(getActivity(), AccountOptionsActivity.class);
					startActivity(intent);
				}
			}
		});
		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		FoodCirclesApplication app = (FoodCirclesApplication) getActivity().getApplicationContext();
		app.selectedOffer = reservations.get(position).getOffer();
		app.selectedVenue = reservations.get(position).getVenue();

		FragmentManager fm = getActivity().getSupportFragmentManager();
		ReceiptDialogFragment receiptDialog = new ReceiptDialogFragment();
		receiptDialog.show(fm, "receipt_dialog");

		MP.track(mixpanel, "Timeline", "Opened voucher");
	}

	private class ReservationAdapter extends BaseAdapter
	{
		public final int YOU_AND_FRIENDS_TYPE = 0;
		public final int VOUCHER_TYPE = 1;
		public final int FRIEND_TYPE = 2;
		public final int MONTH_TYPE = 3;
		public final int USED_VOUCHER_TYPE = 4;
		public final int EXPIRING_VOUCHER_TYPE = 5;
		List<Reservation> reservations;

		private class TimelineHolder
		{
			public TextView date;
			public TextView venue;
			public TextView childrenFed;

			public TextView me;
			public TextView friends;

			public TextView name;
			public ImageView image;

			public TextView year;
			public TextView month;

			public ImageView settingsButton;
		}

		public ReservationAdapter(List<Reservation> reservations)
		{
			this.reservations = reservations;
		}

		@Override
		public int getCount()
		{
			return reservations.size() + 1;
		}

		@Override
		public int getViewTypeCount()
		{
			return 6;
		}

		@Override
		public boolean areAllItemsEnabled()
		{
			return false;
		}

		@Override
		public boolean isEnabled(int position)
		{
			return getItemViewType(position) == VOUCHER_TYPE || getItemViewType(position) == EXPIRING_VOUCHER_TYPE;
		}

		@Override
		public int getItemViewType(int position)
		{
			if (position == 0)
				return YOU_AND_FRIENDS_TYPE;
			else if (position % 5 == 0)
				return FRIEND_TYPE;
			else if (position % 5 == 1)
				return VOUCHER_TYPE;
			else if (position % 5 == 2)
				return USED_VOUCHER_TYPE;
			else if (position % 5 == 3)
				return EXPIRING_VOUCHER_TYPE;
			else
				return MONTH_TYPE;
		}

		@Override
		public Object getItem(int index)
		{
			if (index == 0)
				return null;
			else
				return reservations.get(index);
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
					case YOU_AND_FRIENDS_TYPE:
						view = getActivity().getLayoutInflater().inflate(R.layout.timeline_top_row, parent, false);
						C.overrideFonts(parent.getContext(), view);
						holder = new TimelineHolder();
						holder.me = (TextView) view.findViewById(R.id.textViewMe);
						holder.friends = (TextView) view.findViewById(R.id.textViewFriends);
						holder.settingsButton = (ImageView) view.findViewById(R.id.button);
						holder.settingsButton.setOnClickListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								Intent intent = new Intent(getActivity(), AccountOptionsActivity.class);
								startActivity(intent);
							}
						});
						view.setTag(holder);
						break;
					case VOUCHER_TYPE:
						view = getActivity().getLayoutInflater().inflate(R.layout.timeline_row, parent, false);
						C.overrideFonts(parent.getContext(), view);
						holder = new TimelineHolder();
						holder.date = (TextView) view.findViewById(R.id.textViewDate);
						holder.venue = (TextView) view.findViewById(R.id.textViewVenue);
						holder.childrenFed = (TextView) view.findViewById(R.id.textViewChildrenFed);
						view.setTag(holder);
						break;
					case EXPIRING_VOUCHER_TYPE:
						view = getActivity().getLayoutInflater().inflate(R.layout.timeline_row_expiring, parent, false);
						C.overrideFonts(parent.getContext(), view);
						holder = new TimelineHolder();
						holder.date = (TextView) view.findViewById(R.id.textViewDate);
						holder.venue = (TextView) view.findViewById(R.id.textViewVenue);
						holder.childrenFed = (TextView) view.findViewById(R.id.textViewChildrenFed);
						view.setTag(holder);
						break;
					case USED_VOUCHER_TYPE:
						view = getActivity().getLayoutInflater().inflate(R.layout.timeline_row_used, parent, false);
						C.overrideFonts(parent.getContext(), view);
						holder = new TimelineHolder();
						holder.date = (TextView) view.findViewById(R.id.textViewDate);
						holder.venue = (TextView) view.findViewById(R.id.textViewVenue);
						holder.childrenFed = (TextView) view.findViewById(R.id.textViewChildrenFed);
						view.setTag(holder);
						break;
					case FRIEND_TYPE:
						view = getActivity().getLayoutInflater().inflate(R.layout.timeline_row_friend, parent, false);
						C.overrideFonts(parent.getContext(), view);
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
						C.overrideFonts(parent.getContext(), view);
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

			// Reservation reservation = reservations.get(position);

			return view;
		}

	}
}
