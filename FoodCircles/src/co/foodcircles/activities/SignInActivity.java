package co.foodcircles.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import co.foodcircles.R;

public class SignInActivity extends Activity
{
	TextView signUp;
	EditText email;
	EditText password;
	Button signInButton;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);

		email = (EditText) findViewById(R.id.editTextEmail);
		password = (EditText) findViewById(R.id.editTextPassword);
		signInButton = (Button) findViewById(R.id.buttonSignIn);
		signUp = (TextView) findViewById(R.id.textViewSignUp);

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
						startActivity(intent);
					}
				}.execute("");
			}
		});

		signUp.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
				SignInActivity.this.finish();
			}
		});
	}
}
