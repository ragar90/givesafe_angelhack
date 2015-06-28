package co.foodcircles.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;

import co.foodcircles.R;
import co.foodcircles.json.Charity;
import co.foodcircles.json.Venue;
import co.foodcircles.net.Net;
import co.foodcircles.services.HomelessDiscoveryService;
import co.foodcircles.util.AndroidUtils;
import co.foodcircles.util.FoodCirclesUtils;

public class DonationActivity extends Activity {

    public static final String PROFILE_UID = "profile_uid";
    ImageView profileImageView;
    TextView profileDescriptionTxt;
    TextView homelessNameTxt;
    Button give1DolarBtn;
    Button giveMore;
    Venue profile;
    String deviceId = "";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        Intent triggeredIntent = getIntent();
        String decvieJSONData = triggeredIntent != null ? triggeredIntent.getStringExtra(HomelessDiscoveryService.BECON_DEVICE_ID) : "";
        if(decvieJSONData != null && decvieJSONData != ""){
            try {
                JSONObject deviceData = new JSONObject(decvieJSONData);
                deviceId = AndroidUtils.safelyGetJsonString(deviceData, "device_id");
                Log.d("DonationActivity", "deviceId: " + deviceId);
                loadVenueWithDeviceId();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadVenueWithDeviceId(){
        try{
            String msg = "Loding Homeless";
            showProgressDialog(msg);

            new AsyncTask<Object, Void, Boolean>(){
                protected Boolean doInBackground(Object... param){
                    try
                    {
                        Venue v = Net.getHomeless(deviceId);
                        profile = v;
                        return true;
                    }
                    catch (Exception e)
                    {
                        Log.v("Donation Activity", "Error loading venues", e);
                        return false;
                    }
                }

                protected void onPostExecute(Boolean success){
                    dismissProgressDialog();
                    if (success){
                        loadProfileInUI();
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showAletDialog();
                            }
                        });
                    }
                }
            }.execute();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showProgressDialog(String Message){
        progressDialog = ProgressDialog.show(this, "Please wait", Message);
    }

    public void dismissProgressDialog(){
        progressDialog.dismiss();
    }

    public void showAletDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DonationActivity.this);
        builder.setMessage("Sorry!  We Couldn't retrieve that Homeless data due to network problems").setTitle("No Restaurants!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
    }

    private void loadProfileInUI() {
        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        profileDescriptionTxt = (TextView) findViewById(R.id.profileDescriptionTxt);
        homelessNameTxt = (TextView) findViewById(R.id.homelessNameTxt);
        give1DolarBtn = (Button) findViewById(R.id.give1DolarBtn);
        homelessNameTxt.setText(profile.getName());
        profileDescriptionTxt.setText(profile.getDescription());
        Bitmap image = getBitmapFromURL(profile.getImageUrl());
        profileImageView.setImageBitmap(image);
        profileDescriptionTxt.setMovementMethod(new ScrollingMovementMethod());
        give1DolarBtn.setOnClickListener(give1DolarBtnClickListener);
        giveMore.setOnClickListener(giveMoreOnClickListener);
    }

    private View.OnClickListener give1DolarBtnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            proceedPayPalCheckout();
            finish();
        }
    };

    private View.OnClickListener giveMoreOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent giveMoreIntent = new Intent(DonationActivity.this, BuyOptionsActivity.class);
            startActivity(giveMoreIntent);
        }
    };

    private void proceedPayPalCheckout(){
        int priceValue = 1;
        String paypalOffer = "Donation for " + profile.getName() + " Through Givesafe";
        PayPalPayment voucherPayment = new PayPalPayment(new BigDecimal(priceValue), "USD", paypalOffer);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_PRODUCTION);
        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "ATtEOxB-eX60pOi_fHSv3K2PvAX8LRme-eyngA9l6LRSTIr9SeJHtmpaJL4M");
        intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, "jtkumario@gmail.com");
        intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, FoodCirclesUtils.getToken(this));
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, voucherPayment);
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_donation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Bitmap getBitmapFromAsset(String strName)
    {
        AssetManager assetManager = getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }

    private Bitmap getBitmapFromURL(String stringUrl){
        try{
            URL url = new URL(Net.HOST + stringUrl);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return bmp;

        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
