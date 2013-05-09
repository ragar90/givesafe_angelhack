package co.foodcircles.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import co.foodcircles.R;

public class UpgradePurchasedActivity extends Activity
{
	Button viewVoucher;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upgrade_purchased);

		viewVoucher = (Button) findViewById(R.id.buttonVoucher);

		viewVoucher.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				TaskStackBuilder stackBuilder = TaskStackBuilder.from(UpgradePurchasedActivity.this);
				stackBuilder.addNextIntent(new Intent(UpgradePurchasedActivity.this, MainActivity.class).putExtra("tab", 0));
				stackBuilder.addNextIntent(new Intent(UpgradePurchasedActivity.this, ViewVoucherActivity.class));
				
				stackBuilder.startActivities();
				UpgradePurchasedActivity.this.finish();
			}
		});
	}
}
