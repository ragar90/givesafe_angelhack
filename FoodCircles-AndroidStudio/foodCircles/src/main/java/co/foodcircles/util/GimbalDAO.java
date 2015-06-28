package co.foodcircles.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import co.foodcircles.R;
import co.foodcircles.activities.DonationActivity;
import co.foodcircles.services.HomelessDiscoveryService;

public class GimbalDAO {
    public static final String GIMBAL_NEW_EVENT_ACTION = "GIMBAL_EVENT_ACTION";
    public static final String PLACE_MONITORING_PREFERENCE = "pref_place_monitoring";
    public static final String SHOW_OPT_IN_PREFERENCE = "pref_show_opt_in";
    private static final String EVENTS_KEY = "events";

    // --------------
    // OPT IN
    // --------------

    public static boolean showOptIn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SHOW_OPT_IN_PREFERENCE, true);
    }

    public static void setOptInShown(Context context) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(SHOW_OPT_IN_PREFERENCE, false);
        editor.commit();

    }

    // --------------
    // GIMBAL EVENTS
    // --------------

    public static List<GimbalEvent> getEvents(Context context) {
        List<GimbalEvent> events = new ArrayList<GimbalEvent>();
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String jsonString = prefs.getString(EVENTS_KEY, null);
            if (jsonString != null) {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    GimbalEvent event = new GimbalEvent();
                    event.setType(GimbalEvent.TYPE.valueOf(jsonObject.getString("type")));
                    event.setTitle(jsonObject.getString("title"));
                    event.setDate(new Date(jsonObject.getLong("date")));
                    events.add(event);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return events;
    }

    public static void setEvents(Context context, List<GimbalEvent> events) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            JSONArray jsonArray = new JSONArray();
            for (GimbalEvent event : events) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", event.getType().name());
                jsonObject.put("title", event.getTitle());
                jsonObject.put("date", event.getDate().getTime());
                jsonArray.put(jsonObject);
            }
            String jstr = jsonArray.toString();
            Editor editor = prefs.edit();
            editor.putString(EVENTS_KEY, jstr);
            editor.commit();

            // Notify activity
            Intent intent = new Intent();
            intent.setAction(GIMBAL_NEW_EVENT_ACTION);
            context.sendBroadcast(intent);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void sendNotification(Context context, NotificationManager mNotifyMgr, GimbalEvent event){
        if(event.getType()!= GimbalEvent.TYPE.BECON_DETECTION)
            return;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", event.getType().name());
            jsonObject.put("title", event.getTitle());
            jsonObject.put("date", event.getDate().getTime());
            jsonObject.put("device_id", event.getDeviceId());
            String jstr = jsonObject.toString();
            Log.d("JSON Gimbal Event", jstr);
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                    R.mipmap.ic_notification);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_status_notification)
                            .setLargeIcon(icon)
                            .setContentTitle("GiveSafe")
                            .setContentText("Give $1 to someone in need");

            Intent resultIntent = new Intent(context, DonationActivity.class);
            resultIntent.putExtra(HomelessDiscoveryService.BECON_DEVICE_ID, jstr);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(alarmSound);
            int mNotificationId = 001;
            Log.i("SendNotification", "Inside the notification");
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
