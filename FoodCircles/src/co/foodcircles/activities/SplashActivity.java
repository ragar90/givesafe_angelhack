package co.foodcircles.activities;

import java.util.Timer;
import java.util.TimerTask;

import co.foodcircles.R;
import co.foodcircles.util.C;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashActivity extends Activity
{
	private int splashDelay = 1000;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		C.overrideFonts(this, findViewById(R.id.root));

		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				//If user is already signed in, show Main activity
				Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
			}
		};

		Timer timer = new Timer();
		timer.schedule(task, splashDelay);
	}
}
