package co.foodcircles.services;

import java.util.Calendar;
import java.util.List;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import co.foodcircles.R;
import co.foodcircles.activities.MP;
import co.foodcircles.activities.SignInActivity;
import co.foodcircles.json.Venue;
import co.foodcircles.net.Net;
import co.foodcircles.net.NetException;

public class AlarmReceiver extends BroadcastReceiver
{
	private final String SOMEACTION = "co.foodcircles.geonotification";
	private final static long MS_BEFORE_NOTIFY = AlarmManager.INTERVAL_DAY * 6;
	private MixpanelAPI mixpanel;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();
		if (SOMEACTION.equals(action))
		{
			mixpanel = MixpanelAPI.getInstance(context, context.getResources().getString(R.string.mixpanel_token));
			tryGeoNotify(context);
		}
	}

	public void tryGeoNotify(final Context context)
	{
		Log.v("NotificationService", "Attempting Notification");
		Log.v("NotificationService", "Time since last: " + timeSinceLastNotification(context));
		Log.v("NotificationService", "isOnline: " + isOnline(context));

		if (timeSinceLastNotification(context) > MS_BEFORE_NOTIFY && isOnline(context))
		{
			MP.track(mixpanel, "Notification", "Attempting");
			Location lastKnownLocation = getPassiveLocation(context);
			Log.v("NotificationService", "Location: " + lastKnownLocation);
			if ((Calendar.getInstance().getTimeInMillis() - lastKnownLocation.getTime()) < AlarmManager.INTERVAL_HOUR)
			{
				MP.track(mixpanel, "Notification", "Acquired location");
				new AsyncTask<Void, Void, String>()
				{
					@Override
					protected String doInBackground(Void... params)
					{
						try
						{
							List<Venue> venues = Net.getVenuesList(null);
							return venues.get(0).getName();
						}
						catch (NetException e)
						{
							Log.d("NotificationService", "Failed to get venues", e);
							MP.track(mixpanel, "Notification", "Failed to get venues");
							return "";
						}
					}

					@Override
					protected void onPostExecute(String name)
					{
						if (!name.equals(""))
						{
							Log.d("NotificationService", "Post Execute Success");
							MP.track(mixpanel, "Notification", "Geo notification displayed");
							makeNotification(context, "Hungry?", "Eat at " + name);
							setNotifiedTime(context);
						}
						else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
						{
							Log.d("NotificationService", "Post Execute Failure");
							MP.track(mixpanel, "Notification", "Generic notification displayed");
							makeNotification(context, "Hungry?", "Buy one feed one with FoodCircles!");
							setNotifiedTime(context);
						}
					}
				}.execute();
			}
			else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
			{
				Log.d("NotificationService", "Post Execute Failure");
				MP.track(mixpanel, "Notification", "Generic notification displayed");
				makeNotification(context, "Hungry?", "Buy one feed one with FoodCircles!");
				setNotifiedTime(context);
			}
		}
	}

	public void makeNotification(Context context, String title, String message)
	{
		Log.v("NotificationService", "Making notification: " + title + " :: " + message);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_stat_logo).setContentTitle(title).setContentText(message);

		Intent rateIntent = new Intent(context, SignInActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, rateIntent, 0);

		mBuilder.setAutoCancel(true);
		mBuilder.setContentIntent(pendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(0, mBuilder.build());
	}

	public void setNotifiedTime(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(context.getResources().getString(R.string.preferences), Context.MODE_PRIVATE);
		Editor edit = pref.edit();

		edit.putLong(context.getResources().getString(R.string.last_notification), Calendar.getInstance().getTimeInMillis());
		edit.commit();
	}

	public long timeSinceLastNotification(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(context.getResources().getString(R.string.preferences), Context.MODE_PRIVATE);
		long lastNotification = pref.getLong(context.getResources().getString(R.string.last_notification), 0);

		long now = Calendar.getInstance().getTimeInMillis();

		return now - lastNotification;
	}

	public boolean isOnline(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting())
		{
			return true;
		}
		return false;
	}

	public Location getPassiveLocation(Context context)
	{
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
	}
}