package co.foodcircles.activities;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.services.AlarmReceiver;
import co.foodcircles.util.C;
import co.foodcircles.util.FoodCirclesApplication;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class SignUpActivity extends Activity
{
	EditText email;
	EditText password;
	Button signUpButton;
	Button buttonFacebook;
	Button buttonTwitter;
	TextView signInButton;

	MixpanelAPI mixpanel;

	@Override
	public void onStart()
	{
		super.onStart();
		mixpanel = MixpanelAPI.getInstance(this, getResources().getString(R.string.mixpanel_token));
	}

	@Override
	public void onDestroy()
	{
		mixpanel.flush();
		super.onDestroy();
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup);
		C.overrideFonts(this, findViewById(R.id.root));

		FoodCirclesApplication app = (FoodCirclesApplication) getApplicationContext();
		app.addPoppableActivity(this);

		TextView copyText = (TextView) findViewById(R.id.textViewCopy);
		Spannable spannable = new SpannableString("Buy one appetizer or dessert for $1,\n feed one child in need.");
		spannable.setSpan(new TextAppearanceSpan(this, R.style.TextAppearanceLargeBoldItalic), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new TextAppearanceSpan(this, R.style.TextAppearanceLargeBoldItalic), 38, 46, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 38, 46, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new AbsoluteSizeSpan((int) this.getResources().getDimension(R.dimen.signup_small_text)), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new AbsoluteSizeSpan((int) this.getResources().getDimension(R.dimen.signup_small_text)), 38, 46, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		copyText.setText(spannable);

		TextView countText = (TextView) findViewById(R.id.textViewCount);
		float size = countText.getTextSize();
		int numPeople = 865;
		String numPeopleString = NumberFormat.getNumberInstance(Locale.getDefault()).format(numPeople);
		Spannable countSpannable = new SpannableString(numPeopleString + " people repurpose their everyday dining.\nToday, it\'s your turn.");
		countSpannable.setSpan(new TextAppearanceSpan(this, R.style.TextAppearanceLargeBold), 0, numPeopleString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		countSpannable.setSpan(new TextAppearanceSpan(this, R.style.TextAppearanceLargeBold), countSpannable.length() - 24, countSpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		countSpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dark_font)), 0, countSpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		countSpannable.setSpan(new AbsoluteSizeSpan((int) size), 0, countSpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		countText.setText(countSpannable);

		email = (EditText) findViewById(R.id.editTextEmail);
		password = (EditText) findViewById(R.id.editTextPassword);
		signUpButton = (Button) findViewById(R.id.buttonSignUp);

		signUpButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new AsyncTask<String, Void, Boolean>()
				{
					@Override
					protected Boolean doInBackground(String... params)
					{
						// TODO: do some net stuff
						return true;
					}

					@Override
					protected void onPostExecute(Boolean success)
					{
						Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
						intent.putExtra("tab", 1);
						startActivity(intent);
						SignUpActivity.this.finish();
					}
				}.execute("");
			}
		});

		signInButton = (TextView) findViewById(R.id.buttonSignIn);
		signInButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
				startActivity(intent);
			}
		});

		buttonFacebook = (Button) findViewById(R.id.buttonFacebook);
		buttonFacebook.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
			}
		});

		buttonTwitter = (Button) findViewById(R.id.buttonTwitter);
		buttonTwitter.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
			}
		});

		startupNotifications();
	}

	private void startupNotifications()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 16);
		calendar.set(Calendar.MINUTE, 30);
		calendar.set(Calendar.SECOND, 00);

		Intent intent = new Intent(this, AlarmReceiver.class);
		intent.setAction("co.foodcircles.geonotification");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pendingIntent);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
	}
}
