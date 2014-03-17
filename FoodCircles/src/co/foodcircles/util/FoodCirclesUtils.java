package co.foodcircles.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;

public class FoodCirclesUtils {
	public static final String TAG = "foodcircles";

	public static void saveToken(Context me, String token) {
		AndroidUtils.saveSharePrefferences(me, TAG, "token", token);
	}

	public static String getToken(Context me) {
		return AndroidUtils.getSharePrefferences(me, TAG, "token", "");
	}

	public static void saveEmail(Context me, String email) {
		AndroidUtils.saveSharePrefferences(me, TAG, "email", email);
	}

	public static String getEmail(Context me) {
		return AndroidUtils.getSharePrefferences(me, TAG, "email", "");
	}

	public static void savePassword(Context me, String password) {
		AndroidUtils.saveSharePrefferences(me, TAG, "password", password);
	}

	public static void clearPassword(Context me) {
		AndroidUtils.clearSharePrefferences(me, TAG, "password");
	}

	public static String getPassword(Context me) {
		return AndroidUtils.getSharePrefferences(me, TAG, "password", "");
	}

	public static void saveName(Context me, String name) {
		AndroidUtils.saveSharePrefferences(me, TAG, "name", name);
	}

	public static void saveFBUserId(Context me, String name) {
		AndroidUtils.saveSharePrefferences(me, TAG, "fbuserid", name);
	}
	public static String getFBUserId(Context me) {
	return	AndroidUtils.getSharePrefferences(me, TAG, "fbuserid","");
	}
	public static String getName(Context me) {
		return AndroidUtils.getSharePrefferences(me, TAG, "name", "");
	}

	public static boolean isConnectedToSocialAccounts(Context me) {
		return isTwitterConnected(me) || isFacebookConnected(me);
	}

	public static void saveIsFacebookConnected(Context me, boolean isConnected) {
		AndroidUtils.saveSharePrefferences(me, TAG, "isfacebookconnected", ""
				+ isConnected);
	}

	public static void saveIsTwitterConnected(Context me, boolean isConnected) {
		AndroidUtils.saveSharePrefferences(me, TAG, "istwitterconnected", ""
				+ isConnected);
	}
	

	public static boolean isFacebookConnected(Context me) {
		String isConected = AndroidUtils.getSharePrefferences(me, TAG,
				"isfacebookconnected");
		return (isConected.equalsIgnoreCase("true")) ? true : false;
	}

	public static boolean isTwitterConnected(Context me) {
		String isConected = AndroidUtils.getSharePrefferences(me, TAG,
				"istwitterconnected");
		return (isConected.equalsIgnoreCase("true")) ? true : false;
	}

	public static String convertLongIntoStringDate(long datePurchased) {
		DateFormat formatter = new SimpleDateFormat("MM/dd");
		String dt = formatter.format(convertLongToDate(datePurchased).getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(dt));
		} catch (java.text.ParseException e) {}
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd");
		String output = sdf1.format(c.getTime());
		return output;
	}

	public static String convertLongToFormattedDateString(long date,
			String format) {
		DateFormat formatter = new SimpleDateFormat(format);
		String dt = formatter.format(convertLongToDate(date).getTime());
		return dt;
	}

	public static Date convertLongToDate(long date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		return calendar.getTime();
	}
}
