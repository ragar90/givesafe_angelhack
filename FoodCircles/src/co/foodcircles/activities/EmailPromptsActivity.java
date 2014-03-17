package co.foodcircles.activities;

import java.text.NumberFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import co.foodcircles.exception.NetException2;
import co.foodcircles.net.Net;
import co.foodcircles.util.AndroidUtils;
import co.foodcircles.util.FontSetter;
import co.foodcircles.util.FoodCirclesApplication;
import co.foodcircles.util.FoodCirclesUtils;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.sromku.simple.fb.SimpleFacebook;


public class EmailPromptsActivity extends Activity {
	EditText email;
	
	Button signUpButton;
	TextView signInButton;
	String tempkey;
	MixpanelAPI mixpanel;
	long twit_UID;


	@Override
	public void onStart() {
		super.onStart();
		mixpanel = MixpanelAPI.getInstance(this,
				getResources().getString(R.string.mixpanel_token));
	}

	@Override
	public void onDestroy() {
		mixpanel.flush();
		super.onDestroy();
	}

	 Context mcontext;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.email_prompts_screen);

		mcontext  = this;
		FontSetter.overrideFonts(this, findViewById(R.id.root));
		twit_UID = getIntent().getLongExtra("UID", 0);
		System.out.println("TID :"+twit_UID);

		FoodCirclesApplication app = (FoodCirclesApplication) getApplicationContext();
		app.addPoppableActivity(this);

		TextView copyText = (TextView) findViewById(R.id.textViewCopy);
		Spannable spannable = new SpannableString(
				"Buy one appetizer or dessert for $1,\n feed one child in need.");
		spannable.setSpan(new TextAppearanceSpan(this,
				R.style.TextAppearanceLargeBoldItalic), 0, 7,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new TextAppearanceSpan(this,
				R.style.TextAppearanceLargeBoldItalic), 38, 46,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 7,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 38, 46,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new AbsoluteSizeSpan((int) this.getResources()
				.getDimension(R.dimen.signup_small_text)), 0, 7,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new AbsoluteSizeSpan((int) this.getResources()
				.getDimension(R.dimen.signup_small_text)), 38, 46,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		copyText.setText(spannable);

		TextView countText = (TextView) findViewById(R.id.textViewCount);
		float size = countText.getTextSize();
		int numPeople = 1000;
		String numPeopleString = NumberFormat.getNumberInstance(
				Locale.getDefault()).format(numPeople);
		Spannable countSpannable = new SpannableString(
				numPeopleString
						+ " have joined the movement in Grand Rapids.\nToday, it\'s your turn.");
		countSpannable.setSpan(new TextAppearanceSpan(this,
				R.style.TextAppearanceLargeBold), 0, numPeopleString.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		countSpannable.setSpan(new TextAppearanceSpan(this,
				R.style.TextAppearanceLargeBold), countSpannable.length() - 24,
				countSpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		countSpannable.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						R.color.dark_font)), 0, countSpannable.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		countSpannable.setSpan(new AbsoluteSizeSpan((int) size), 0,
				countSpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		countText.setText(countSpannable);
		
		
		TextView success_signin = (TextView) findViewById(R.id.textViewsuccess);
		String signinsucess = "Successfully signed in with Twitter.";
		Spannable countSpannable1 = new SpannableString(signinsucess+"\nAll we need now is an email for your shiny, new account.");
		countSpannable1.setSpan(new TextAppearanceSpan(this,
				R.style.TextAppearanceLargeBold), 0, signinsucess.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		countSpannable1.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						R.color.dark_font)), 0, countSpannable1.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		countSpannable1.setSpan(new AbsoluteSizeSpan((int) size), 0,
				countSpannable1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		success_signin.setText(countSpannable1);
		
		email = (EditText) findViewById(R.id.editTextEmail);
	
		signUpButton = (Button) findViewById(R.id.buttonfinishSignUp);

		signUpButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("TWITTER ID :"+twit_UID);
				String s = String.valueOf(twit_UID);
				signUp(email.getEditableText().toString(),s);
			}
		});

		signInButton = (TextView) findViewById(R.id.buttonSignIn);
		signInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EmailPromptsActivity.this,
						SignInActivity.class);
				startActivity(intent);
			}
		});

	}

	private void signUp(final String email, final String uid) {
		AndroidUtils.showProgress(this);
		new Thread() {
			public void run() {
				try {
					final String token = Net.twitterSignUp(email, uid);
					EmailPromptsActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AndroidUtils.dismissProgress();
							FoodCirclesUtils.saveToken(EmailPromptsActivity.this,token);
							//FoodCirclesUtils.saveUId(EmailPromptsActivity.this,uid);
							//FoodCirclesUtils.saveEmail(EmailPromptsActivity.this,email);
					
							gotoSignedInPage();
						}
					});
				} catch (final NetException2 e) {
					EmailPromptsActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AndroidUtils.showAlertOk(EmailPromptsActivity.this,
									"Sign-up Failed - " + e.getMessage());
							AndroidUtils.dismissProgress();
						}
					});
				}
			};
		}.start();
	}
	
	private void gotoSignedInPage() {
		Intent intent = new Intent(EmailPromptsActivity.this, MainActivity.class);
		intent.putExtra("tab", 1);
		startActivity(intent);
		EmailPromptsActivity.this.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		SimpleFacebook.getInstance().onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}


}
