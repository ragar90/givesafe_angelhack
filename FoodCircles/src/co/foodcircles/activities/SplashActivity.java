package co.foodcircles.activities;

import java.util.Timer;
import java.util.TimerTask;

import co.foodcircles.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity
{
	private int splashDelay = 1000;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				//TODO: If user has logged in before, show sign in screen instead
				Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
			}
		};

		Timer timer = new Timer();
		timer.schedule(task, splashDelay);
	}
}
