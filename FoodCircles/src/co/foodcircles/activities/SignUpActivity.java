package co.foodcircles.activities;

import android.app.Activity;
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
import co.foodcircles.util.C;
import co.foodcircles.util.FoodCirclesApplication;

public class SignUpActivity extends Activity
{
	EditText email;
	EditText password;
	Button signUpButton;
	TextView signInButton;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup);
		C.overrideFonts(this, findViewById(R.id.root));
		
		FoodCirclesApplication app = (FoodCirclesApplication) getApplicationContext();
		app.addPoppableActivity(this);

		TextView copyText = (TextView) findViewById(R.id.textViewCopy);
		Spannable spannable = new SpannableString("Buy one appetizer or dessert for $1, feed one child in need.");
		spannable.setSpan(new TextAppearanceSpan(this, R.style.TextAppearanceLargeBold), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new TextAppearanceSpan(this, R.style.TextAppearanceLargeBold), 37, 45, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 37, 45, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new AbsoluteSizeSpan((int) this.getResources().getDimension(R.dimen.signup_small_text)), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new AbsoluteSizeSpan((int) this.getResources().getDimension(R.dimen.signup_small_text)), 37, 45, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		copyText.setText(spannable);

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
	}
}
