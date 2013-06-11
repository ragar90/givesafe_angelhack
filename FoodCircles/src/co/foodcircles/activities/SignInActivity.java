package co.foodcircles.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.util.C;
import co.foodcircles.util.FoodCirclesApplication;

public class SignInActivity extends Activity
{
	EditText email;
	EditText password;
	Button signInButton;
	TextView signUpButton;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signin);
		C.overrideFonts(this, findViewById(R.id.root));

		email = (EditText) findViewById(R.id.editTextEmail);
		password = (EditText) findViewById(R.id.editTextPassword);
		signInButton = (Button) findViewById(R.id.buttonSignIn);
		signUpButton = (TextView) findViewById(R.id.buttonSignUp);

		signInButton.setOnClickListener(new View.OnClickListener()
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
						Intent intent = new Intent(SignInActivity.this, MainActivity.class);
						intent.putExtra("tab", 1);
						startActivity(intent);
						SignInActivity.this.finish();
						FoodCirclesApplication app = (FoodCirclesApplication) getApplicationContext();
						app.newTop();
					}
				}.execute("");
			}
		});

		signUpButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SignInActivity.this.finish();
			}
		});
	}
}
