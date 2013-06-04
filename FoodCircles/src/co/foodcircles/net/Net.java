package co.foodcircles.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.util.Log;
import co.foodcircles.json.Reservation;
import co.foodcircles.json.Venue;

public class Net
{
	private static final String TAG = "Net";
	private static final String HOST = "foodcircles.net";
	private static final String API_URL = "/api";
	private static final String GET_VENUES_LIST = "/venues.json";
	private static final String GET_VENUE = "/venues/[venueId]";
	private static final String GET_RESERVATIONS_LIST = "/reservations";
	private static final String GET_RESERVATION = "/reservations/[reservationId]";
	private static final String POST_RESERVATION = "/reservations";
	private static final String PUT_RESERVATION = "/reservations/[reservationId]";

	private static String post(String path, List<BasicNameValuePair> postValues)
	{
		HttpContext httpContext = new BasicHttpContext();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://" + HOST + API_URL + path);
		String response = "";

		try
		{
			httppost.setEntity(new UrlEncodedFormEntity(postValues, HTTP.UTF_8));
			httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");

			Log.w(TAG, "Execute HTTP Post Request");
			HttpResponse httpResp = httpclient.execute(httppost, httpContext);
			response = EntityUtils.toString(httpResp.getEntity());

			Log.w(TAG, response);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return response;
	}

	private static String get(String path, List<BasicNameValuePair> urlParams)
	{
		HttpContext httpContext = new BasicHttpContext();
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://" + HOST + API_URL + path);
		String response = "";

		try
		{
			if (urlParams != null)
			{
				String paramString = URLEncodedUtils.format(urlParams, "utf-8");
				path += paramString;
			}

			Log.w(TAG, "Execute HTTP Post Request");
			HttpResponse httpResp = httpclient.execute(httpget, httpContext);
			response = EntityUtils.toString(httpResp.getEntity());

			Log.w(TAG, response);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return response;
	}

	private static String put(String path, List<BasicNameValuePair> urlParams)
	{
		HttpContext httpContext = new BasicHttpContext();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPut httpput = new HttpPut("http://" + HOST + API_URL + path);
		String response = "";

		try
		{
			String paramString = URLEncodedUtils.format(urlParams, "utf-8");
			path += paramString;

			Log.w(TAG, "Execute HTTP Post Request");
			HttpResponse httpResp = httpclient.execute(httpput, httpContext);
			response = EntityUtils.toString(httpResp.getEntity());

			Log.w(TAG, response);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return response;
	}

	public static List<Venue> getVenuesList(List<BasicNameValuePair> filters) throws NetException
	{
		try
		{
			String response = get(GET_VENUES_LIST, filters);
			return Venue.parseVenues(response);
		}
		catch (JSONException j)
		{
			throw new NetException();
		}
	}

	public static Venue getVenue(String venueId) throws NetException
	{
		try
		{
			String response = get(GET_VENUE.replace("[venueId]", venueId), null);
			return new Venue(response);
		}
		catch (JSONException j)
		{
			throw new NetException();
		}
	}

	public static List<Reservation> getReservationsList() throws NetException
	{
		try
		{
			String response = get(GET_RESERVATIONS_LIST, null);
			return Reservation.parseReservations(response);
		}
		catch (JSONException j)
		{
			throw new NetException();
		}
	}

	public static Reservation getReservation(String reservationId) throws NetException
	{
		try
		{
			String response = get(GET_RESERVATION.replace("[reservationId]", reservationId), null);
			return new Reservation(response);
		}
		catch (JSONException j)
		{
			throw new NetException();
		}
	}

	public static boolean makeReservation(Reservation reservation)
	{
		List<BasicNameValuePair> reservationPairs = new ArrayList<BasicNameValuePair>();
		reservationPairs.add(new BasicNameValuePair("user", reservation.getUser()));
		reservationPairs.add(new BasicNameValuePair("venue", reservation.getVenue().getId()));
		reservationPairs.add(new BasicNameValuePair("offer", reservation.getOffer().getId()));
		reservationPairs.add(new BasicNameValuePair("charity", reservation.getCharity().getId()));

		String response = post(POST_RESERVATION, reservationPairs);
		return response.equals("success");
	}

	public static boolean markReservationAsUsed(Reservation reservation)
	{
		String response = put(PUT_RESERVATION.replace("[reservationId]", reservation.getId()), null);
		return response.equals("success");
	}
}
