package co.foodcircles.net;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import co.foodcircles.exception.NetException2;
import co.foodcircles.json.Charity;
import co.foodcircles.json.Reservation;
import co.foodcircles.json.Venue;
import co.foodcircles.json.Voucher;


public class Net {
	public static final String HOST = "http://staging.foodcircles.net";
	private static final String API_URL = "/api";
	private static final String GET_VENUES = "/venues/%f/%f";
	private static final String GET_RESERVATION = "/reservations/[reservationId]";
	private static final String GET_CHARITY_1 = "/charities";
	private static final String GET_NEWS = "/news";
	private static final String MARK_REDEEMED = "/payment/used?code=";
	private static final String GET_TIMELINE="/timeline?auth_token=%s";
	public static String logo="http://staging.foodcircles.net/media/BAhbBlsHOgZmSSIkMjAxMy8wOC8yMC8xNl81Nl8xMV84MzNfRkFRLnBuZwY6BkVU";
	public static String website="http://staging.foodcircles.net";
	
	private static String post(String path, List<BasicNameValuePair> postValues) {
		HttpContext httpContext = new BasicHttpContext();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(HOST + API_URL + path);
		String response = "";

		try {
			httppost.setEntity(new UrlEncodedFormEntity(postValues, HTTP.UTF_8));
			httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			HttpResponse httpResp = httpclient.execute(httppost, httpContext);
			response = EntityUtils.toString(httpResp.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	private static String postRedeemed(String id) {
		String responseString = null;
		HttpClient httpclient = new DefaultHttpClient();
	    HttpResponse response = null;
		try {
			response = httpclient.execute(new HttpGet(HOST + MARK_REDEEMED + id));
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
	    	response.getEntity().writeTo(out);
	    	out.close();
	    	responseString = out.toString();
	    } catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		return responseString;
	}
	
	private static String get(String path, List<BasicNameValuePair> urlParams) {
	    StringBuilder builder = new StringBuilder();
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(HOST + API_URL + path);

		try {
	        HttpResponse response = httpclient.execute(httpGet);
	        StatusLine statusLine = response.getStatusLine();
	        int statusCode = statusLine.getStatusCode();
	        if (statusCode == 200) {
	          HttpEntity entity = response.getEntity();
	          InputStream content = entity.getContent();
	          BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	          String line;
	          while ((line = reader.readLine()) != null) {
	            builder.append(line);
	          }
	        } else { }
	      } catch (ClientProtocolException e) {
	        e.printStackTrace();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
		return builder.toString();
	    }	
	
	private static String put(String path, List<BasicNameValuePair> urlParams) {
		HttpContext httpContext = new BasicHttpContext();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPut httpput = new HttpPut(HOST + API_URL + path);
		String response = "";

		try {
			String paramString = URLEncodedUtils.format(urlParams, "utf-8");
			path += paramString;
			HttpResponse httpResp = httpclient.execute(httpput, httpContext);
			response = EntityUtils.toString(httpResp.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	@SuppressLint("DefaultLocale")
	public static List<Venue> getVenues(double longitude,double latitude,List<BasicNameValuePair> filters)
			throws NetException {
		try {
			String url=String.format(GET_VENUES,latitude,longitude);
			String response = get(url, filters);
			return Venue.parseVenues(response);
		} catch (JSONException j) {
			throw new NetException();
		}
	}

	public static List<Reservation> getReservationsList(String token) throws NetException {
		try {
			String url=String.format(GET_TIMELINE,token);
			String response = get(url, null);
			return Reservation.parseReservations(response);
		} catch (JSONException j) {
			throw new NetException();
		}
	}

	public static Reservation getReservation(String reservationId)
			throws NetException {
		try {
			String response = get(
					GET_RESERVATION.replace("[reservationId]", reservationId),
					null);
			return new Reservation(response);
		} catch (JSONException j) {
			throw new NetException();
		}
	}

	public static List<Charity> getCharities() throws NetException {
		List<Charity> charities = new ArrayList<Charity>();
		String response = get(GET_CHARITY_1, null);
		try {
			JSONObject jsonObject=new JSONObject(response);
			JSONArray jsonArray=jsonObject.getJSONArray("content");
			for(int ctr=0;ctr<jsonArray.length();ctr++){
			charities.add(new Charity(jsonArray.getJSONObject(ctr).toString()));
			}
		} catch (JSONException e) {
			throw new NetException();
		}
		return charities;
	}

	public static String signUp(String userEmail, String userPassword)
			throws NetException2 {
		List<BasicNameValuePair> postValues = new ArrayList<BasicNameValuePair>();
		postValues.add(new BasicNameValuePair("user_email", userEmail));
		postValues.add(new BasicNameValuePair("user_password", userPassword));
		String html = post("/sessions/sign_up", postValues);
		NetException2 n = new NetException2();
		try {
			JSONObject json = new JSONObject(html);
			if (json.getBoolean("error") == true) {
				n.setMessage(json.getString("description"));
				throw n;
			}
			String token=json.getString("auth_token");
			return token;
		} catch (JSONException e) {
			n.setMessage(e.getMessage());
			throw n;
		}
	}
	
	public static String twitterSignUp(String userEmail, String UID)
			throws NetException2 {
		List<BasicNameValuePair> postValues = new ArrayList<BasicNameValuePair>();
		postValues.add(new BasicNameValuePair("user_email", userEmail));
		postValues.add(new BasicNameValuePair("uid", UID));
		String html = post("/sessions/sign_up", postValues);
		NetException2 n = new NetException2();
		try {
			JSONObject json = new JSONObject(html);
			if (json.getBoolean("error") == true) {
				n.setMessage(json.getString("description"));
				throw n;
			}
			String token=json.getString("auth_token");
			System.out.println("TOKEN ::"+token);
			return token;
		} catch (JSONException e) {
			n.setMessage(e.getMessage());
			throw n;
		}
	}
	
	public static String twittersignIn(String uid)
			throws NetException2 {
		List<BasicNameValuePair> postValues = new ArrayList<BasicNameValuePair>();
		postValues.add(new BasicNameValuePair("uid", uid));
		String html = post("/sessions/sign_in", postValues);
		JSONObject json;
		NetException2 n = new NetException2();
		try {
			json = new JSONObject(html);
			if (json.getBoolean("error") == true) {
				n.setMessage(json.getString("description"));
				return json.getString("description");
			}
			return json.getString("auth_token");
		} catch (JSONException e) {
			n.setMessage(e.getMessage());
			throw n;
		}
	}

	public static String facebookSignUp(String userID,String emailid)
			throws NetException2 {
		List<BasicNameValuePair> postValues = new ArrayList<BasicNameValuePair>();
		postValues.add(new BasicNameValuePair("uid", userID));
		postValues.add(new BasicNameValuePair("user_email", emailid));
		String html = post("/sessions/sign_up", postValues);
		NetException2 n = new NetException2();
		try {
			JSONObject json = new JSONObject(html);
			if (json.getBoolean("error") == true) {
				n.setMessage(json.getString("description"));
				throw n;
			}
			String token=json.getString("auth_token");
			return token;
		} catch (JSONException e) {
			n.setMessage(e.getMessage());
			throw n;
		}
	}
	
	public static String signIn(String userEmail, String userPassword)
			throws NetException2 {
		List<BasicNameValuePair> postValues = new ArrayList<BasicNameValuePair>();
		postValues.add(new BasicNameValuePair("user_email", userEmail));
		postValues.add(new BasicNameValuePair("user_password", userPassword));
		String html = post("/sessions/sign_in", postValues);
		JSONObject json;
		NetException2 n = new NetException2();
		try {
			json = new JSONObject(html);
			if (json.getBoolean("error") == true) {
				n.setMessage(json.getString("description"));
				throw n;
			}
			return json.getString("auth_token");
		} catch (JSONException e) {
			n.setMessage(e.getMessage());
			throw n;
		}
	}

	public static void updateUserInfo(String authToken,String userEmail, String userPassword,
			String name, String phone) throws NetException2 {
		List<BasicNameValuePair> postValues = new ArrayList<BasicNameValuePair>();
		
		postValues.add(new BasicNameValuePair("user_email", userEmail));
		postValues.add(new BasicNameValuePair("user_password", userPassword));
		postValues.add(new BasicNameValuePair("name", name));
		postValues.add(new BasicNameValuePair("phone", phone));
		String html = put("/sessions/update?auth_token="+authToken, postValues);

		NetException2 n = new NetException2();
		JSONObject json;
		try {
			json = new JSONObject(html);
			if (json.getBoolean("error") == true) {
				n.setMessage(json.getString("description"));
				throw n;
			}
		} catch (JSONException e) {
			n.setMessage(e.getMessage());
			throw n;
		}
	}

	public static void getNews() throws NetException2 {
		List<BasicNameValuePair> getValues = new ArrayList<BasicNameValuePair>();
		String html = get(GET_NEWS, getValues);
		NetException2 n = new NetException2();
		JSONObject json;
		try {
			json = new JSONObject(html);
			if (json.getBoolean("error") == true) {
				n.setMessage(json.getString("description"));
				throw n;
			}
		} catch (JSONException e) {
			n.setMessage(e.getMessage());
			throw n;
		}
	}

	public static void getTimeLine() throws NetException2 {
		List<BasicNameValuePair> getValues = new ArrayList<BasicNameValuePair>();
		String html = get("/timeline/", getValues);
		NetException2 n = new NetException2();
		try {
			JSONObject json = new JSONObject(html);
			if (json.getBoolean("error") == true) {
				n.setMessage(json.getString("description"));
				throw n;
			}
		} catch (JSONException e) {
			n.setMessage(e.getMessage());
			throw n;
		}

	}

	public static void getTimeLineVoucher(int id) throws NetException2 {
		List<BasicNameValuePair> postValues = new ArrayList<BasicNameValuePair>();
		String html = post("/timeline/voucher/" + id, postValues);
		NetException2 n = new NetException2();
		try {
			JSONObject json = new JSONObject(html);
			if (json.getBoolean("error") == true) {
				n.setMessage(json.getString("description"));
				throw n;
			}
		} catch (JSONException e) {
			n.setMessage(e.getMessage());
			throw n;
		}
	}

	public static boolean markReservationAsUsed(String id) {
		String response = postRedeemed(id.trim());
		return response.equals("success");
	}

	// Attempts to verify the payment, and if successful, returns the certificate information
	public static Voucher verifyPayment(String token,int priceValue, String offerId, String payKey, int charity) throws NetException2 {
		List<BasicNameValuePair> putValues = new ArrayList<BasicNameValuePair>();
		putValues.add(new BasicNameValuePair("auth_token", token));
		putValues.add(new BasicNameValuePair("payment[amount]", priceValue + ""));
		putValues.add(new BasicNameValuePair("payment[offer_id]", offerId));
		putValues.add(new BasicNameValuePair("payment[paypal_charge_token]", payKey));
		putValues.add(new BasicNameValuePair("payment[charity_id]",""+ charity));
		String html = post("/payments", putValues);
		NetException2 n = new NetException2();
		try {
			return Voucher.parseVoucher(html);
		} catch (JSONException e) {
			n.setMessage(e.getMessage());
			throw n;
		}

	}

	public static String getMailChimp() throws NetException2 {
		List<BasicNameValuePair> postValues = new ArrayList<BasicNameValuePair>();
		String html = get("/general/users", postValues);
		try {
			JSONObject json = new JSONObject(html);
			if (json.getBoolean("error") == true) {
				return "Many";
			}
			return (json.getInt("content") + "");
		} catch (JSONException e) {
			return "Many";
		}
	}
}
