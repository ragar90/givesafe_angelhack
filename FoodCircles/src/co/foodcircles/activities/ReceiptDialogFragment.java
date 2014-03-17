package co.foodcircles.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.net.Net;
import co.foodcircles.util.FontSetter;
import co.foodcircles.util.FoodCirclesApplication;

public class ReceiptDialogFragment extends DialogFragment {
	Button markAsUsedButton;
	FoodCirclesApplication app;
	TextView textViewVenue;
	TextView textViewItemName;
	TextView textViewCode;
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setStyle(STYLE_NO_FRAME, R.style.AppBaseTheme);
		this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getDialog().getWindow().setBackgroundDrawable(
			new ColorDrawable(android.graphics.Color.TRANSPARENT));
		View v = inflater.inflate(R.layout.voucher_receipt, container, false);
		FontSetter.overrideFonts(getActivity(), v.findViewById(R.id.root));
		app = (FoodCirclesApplication) getActivity().getApplicationContext();
		View teeth = v.findViewById(R.id.viewTiledTeeth);
		textViewCode = (TextView) v.findViewById(R.id.textViewCode);
		textViewItemName = (TextView) v.findViewById(R.id.textViewItemName);
		textViewVenue = (TextView) v.findViewById(R.id.textViewVenue);
		TextView textViewDonated = (TextView) v.findViewById(R.id.textViewDonated);
		TextView textViewChildrenFed = (TextView) v.findViewById(R.id.textViewChildrenFed);
		TextView textViewSecondLine = (TextView) v.findViewById(R.id.textViewMinGroup);
		// Display the purchased voucher
		if (app.purchasedVoucher == true) {
			try {
				textViewCode.setText(app.newVoucher.getCode());
			} catch (Exception e) {
				textViewCode.setText("Check timeline for code");
			}
			try {
				Log.d("ReceiptDialogFramgnet","The first one: the textViewItemName is set to " + app.purchasedOffer);
				textViewItemName.setText(app.purchasedOffer);
			} catch (Exception e) {
			}
			try {
				textViewVenue.setText(app.selectedVenue.getName());
			} catch (Exception e) {
				Log.d("", "The venue name could not be recieved!");
			}
			try {
				textViewVenue.setText(app.selectedVenue.getName());
			} catch (Exception e) {}
			try {
				Calendar date = Calendar.getInstance();
				date.add(Calendar.MONTH, 1);
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				String formattedDate = formatter.format(app.newVoucher.getExpirationDate());

				String minGroupString = "";
				if (app.currentReservation.getOffer().getMinDiners() > 0) {
					minGroupString = ("Min. Group " + app.purchasedGroupSize + ". ");
				}
				textViewSecondLine.setText(minGroupString + "Use by " + formattedDate);
			} catch (Exception e) { }
			textViewDonated.setText("$" + app.purchasedCost + " donated");
			if (app.purchasedCost == 1) {
				textViewChildrenFed.setText("(" + app.purchasedCost
						+ " kids fed)");
			} else {
				textViewChildrenFed.setText("(" + app.purchasedCost + " kids fed)");
			}
		} else {
			try {
				try {
					textViewCode.setText(app.currentReservation.getCode());
				} catch (Exception e) { }
				try {
					textViewItemName.setText(app.currentReservation.getOffer().getName());
				} catch (Exception e) {}
				try {
					textViewVenue.setText(app.currentReservation.getVenue().getName());
				} catch (Exception e) {}
				try {
					app.selectedVenue = app.currentReservation.getVenue();
					Calendar date = Calendar.getInstance();
					date.add(Calendar.MONTH, 1);
					SimpleDateFormat formatter = new SimpleDateFormat(
							"MM/dd/yyyy");
					String formattedDate = formatter.format(app.currentReservation.getExpirationDate());
					String minGroupString = "";
					if (app.currentReservation.getOffer().getMinDiners() > 0) {
						minGroupString = ("Min. Group "
								+ app.currentReservation.getOffer()
										.getMinDiners() + ". ");
					}
					textViewSecondLine.setText(minGroupString + "Use by "
							+ formattedDate);
				} catch (Exception e) {
					Log.d("", " Something went wrong with the second line!");
				}
				textViewDonated.setText("$"
						+ app.currentReservation.getKidsFed() + " donated");
				if (app.currentReservation.getKidsFed() == 1) {
					textViewChildrenFed.setText("("
							+ app.currentReservation.getKidsFed()
							+ " kids fed)");
				} else {
					textViewChildrenFed.setText("("
							+ app.currentReservation.getKidsFed()
							+ " kids fed)");
				}
				textViewChildrenFed.setText("("
						+ app.currentReservation.getKidsFed() + " kids fed)");
			} catch (Exception e) { }
		}

		BitmapDrawable teethDrawable = new BitmapDrawable(getResources(),
				BitmapFactory.decodeResource(getResources(),
						R.drawable.receipt_tooth));
		teethDrawable.setTileModeX(TileMode.REPEAT);

		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			teeth.setBackgroundDrawable(teethDrawable);
		} else {
			teeth.setBackground(teethDrawable);
		}
		textViewVenue.setPaintFlags(textViewVenue.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		textViewVenue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						VenueProfileActivity.class);
				startActivity(intent);
			}
		});

		markAsUsedButton = (Button) v.findViewById(R.id.buttonMarkAsUsed);
		markAsUsedButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setMessage("Are you sure?  This can only be done once!")
						.setTitle("Confirm Using Certificate");
				builder.setPositiveButton("OK",
						new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								if (app.currentReservation == null) {
									Net.markReservationAsUsed(textViewCode.getText().toString().trim());
									app.needsRestart = true;
									ReceiptDialogFragment.this.getActivity()
											.finish();
									startActivity(ReceiptDialogFragment.this
											.getActivity().getIntent());
								} else {
									Net.markReservationAsUsed(app.currentReservation
											.getCode().trim());
									app.needsRestart = true;
									ReceiptDialogFragment.this.getActivity()
											.finish();
									startActivity(ReceiptDialogFragment.this
											.getActivity().getIntent());

								}
							}
						});
				builder.setNegativeButton("Cancel", null);
				builder.create().show();
			}
		});

		ImageView facebook = (ImageView) v.findViewById(R.id.imageViewFacebook);
		facebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String child="child";
				if(app.currentReservation.getKidsFed()>1)
					child="children";
				//TODO make this a proper Facebook implementation
				String shareText=" I've fed "+app.currentReservation.getKidsFed()+" hungry "+child+" simply by eating at "+app.currentReservation.getVenue().getName()+" #bofo. http://www.joinfoodcircles.org";
				Intent shareIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						shareText);
				PackageManager pm = app.getPackageManager();
				List<ResolveInfo> activityList = pm.queryIntentActivities(
						shareIntent, 0);
				for (final ResolveInfo app : activityList) {
					if ((app.activityInfo.name).contains("facebook")) {
						final ActivityInfo activity = app.activityInfo;
						final ComponentName name = new ComponentName(
								activity.applicationInfo.packageName,
								activity.name);
						shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
						shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
						shareIntent.setComponent(name);
						v.getContext().startActivity(shareIntent);
						break;
					}
				}
			}
		});
		ImageView twitter = (ImageView) v.findViewById(R.id.imageViewTwitter);
		twitter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String child="child";
				if(app.currentReservation.getKidsFed()>1)
					child="children";
				String shareText="I've fed "+app.currentReservation.getKidsFed()+" hungry "+child+" simply by eating at "+app.currentReservation.getVenue().getName()+" #bofo. http://www.joinfoodcircles.org";

				Intent shareIntent = findTwitterClient(); 
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(shareIntent, "Share"));
			}
		});
		app.purchasedVoucher = false;
		
		return v;
	}
	
	public Intent findTwitterClient() {
	    final String[] twitterApps = {
	            "com.twitter.android", 
	            "com.twidroid", 
	            "com.handmark.tweetcaster", 
	            "com.thedeck.android" }; 
	    Intent tweetIntent = new Intent();
	    tweetIntent.setType("text/plain");
	    final PackageManager packageManager = getActivity().getPackageManager();
	    List<ResolveInfo> list = packageManager.queryIntentActivities(
	            tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

	    for (int i = 0, ii = twitterApps.length; i < ii; i++) {
	        for (ResolveInfo resolveInfo : list) {
	            String p = resolveInfo.activityInfo.packageName;
	            if (p != null && p.startsWith(twitterApps[i])) {
	                tweetIntent.setPackage(p);
	                return tweetIntent;
	            }
	        }
	    }
	    return tweetIntent;

	}
}
