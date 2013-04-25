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

public class SignUpActivity extends Activity
{
	TextView signIn;
	EditText email;
	EditText password;
	Button signUpButton;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);

		email = (EditText) findViewById(R.id.editTextEmail);
		password = (EditText) findViewById(R.id.editTextPassword);
		signUpButton = (Button) findViewById(R.id.buttonSignUp);
		signIn = (TextView) findViewById(R.id.textViewSignIn);

		signUpButton.setOnClickListener(new View.OnClickListener()
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
						startActivity(intent);
					}
				}.execute("");
			}
		});

		signIn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
				SignUpActivity.this.finish();
			}
		});
	}
}
