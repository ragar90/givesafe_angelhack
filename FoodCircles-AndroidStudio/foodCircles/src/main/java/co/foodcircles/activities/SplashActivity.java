package co.foodcircles.activities;

import com.crashlytics.android.Crashlytics;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;
import co.foodcircles.R;
import co.foodcircles.services.HomelessDiscoveryService;
import co.foodcircles.util.FontSetter;

public class SplashActivity extends Activity
{
	private int splashDelay = 1000;

	//@SuppressLint("NewApi")
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(HomelessDiscoveryService.serviceIsOn){
			stopService(new Intent(this, HomelessDiscoveryService.class));
		}

	}
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(!HomelessDiscoveryService.serviceIsOn){
			startService(new Intent(this, HomelessDiscoveryService.class));
		}
		Crashlytics.start(this);
		int SDK_INT = android.os.Build.VERSION.SDK_INT;
		if (SDK_INT>8){
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy); 
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		FontSetter.overrideFonts(this, findViewById(R.id.root));
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, splashDelay);
//		if(!HomelessDiscoveryService.serviceIsOn){
//			Intent intent = new Intent(this, HomelessDiscoveryService.class);
//			startService(intent);
//		}

	}
}
