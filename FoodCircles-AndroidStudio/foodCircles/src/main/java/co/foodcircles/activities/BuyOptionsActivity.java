package co.foodcircles.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import co.foodcircles.R;
import co.foodcircles.services.HomelessDiscoveryService;
import co.foodcircles.util.AndroidUtils;
import co.foodcircles.util.FoodCirclesApplication;

import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class BuyOptionsActivity extends FragmentActivity
{
	String deviceId = "";
	FoodCirclesApplication app;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buy_options_activity);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Intent triggeredIntent = getIntent();
		String decvieJSONData = triggeredIntent != null ? triggeredIntent.getStringExtra(HomelessDiscoveryService.BECON_DEVICE_ID) : "";
		if(decvieJSONData != null && decvieJSONData != ""){
			try {
				JSONObject deviceData = new JSONObject(decvieJSONData);
				deviceId = AndroidUtils.safelyGetJsonString(deviceData, "device_id");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		loadBuyOptionsFragment();
	}

	public String getDeviceId(){
		return this.deviceId;
	}


	public void showProgressDialog(String Message){
		progressDialog = ProgressDialog.show(this, "Please wait", Message);
	}

	public void dismissProgressDialog(){
		progressDialog.dismiss();
	}

	public void showAletDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(BuyOptionsActivity.this);
		builder.setMessage("Sorry!  We Couldn't retrieve that Homeless data due to network problems").setTitle("No Restaurants!");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		builder.create().show();
	}

	private void loadBuyOptionsFragment(){
		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_SANDBOX);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "AS2vSxDNmRzYZ9qH7hiSWAEbpabaDmpKmLlglqJtOKDh5KLBZ6a4N5Ex-ql4");
		BuyFragment buyFragment = new BuyFragment();
		buyFragment.setArguments(intent.getExtras());
		getSupportFragmentManager().beginTransaction().add(R.id.fragment1, buyFragment).commit();
		Log.i("OnCreate","1");
	}

	@Override
	public void onDestroy()
	{
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
	}
}
