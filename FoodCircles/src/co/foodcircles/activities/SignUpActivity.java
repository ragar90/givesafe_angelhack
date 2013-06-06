package co.foodcircles.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import co.foodcircles.R;
import co.foodcircles.util.C;

public class SignUpActivity extends Activity
{
	EditText email;
	EditText password;
	Button signUpButton;
	Button signInButton;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup);
		C.overrideFonts(this, findViewById(R.id.root));

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
		
		signInButton = (Button) findViewById(R.id.buttonSignIn);
		signInButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
				startActivity(intent);
				SignUpActivity.this.finish();
			}
		});
	}
}
