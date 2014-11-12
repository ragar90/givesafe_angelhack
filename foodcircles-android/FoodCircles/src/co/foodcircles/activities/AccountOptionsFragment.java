package co.foodcircles.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.util.FontSetter;
import co.foodcircles.util.FoodCirclesUtils;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.sromku.simple.fb.SimpleFacebook;

public class AccountOptionsFragment extends Fragment
{
	MixpanelAPI mixpanel;

	@Override
	public void onStart()
	{
		super.onStart();
		mixpanel = MixpanelAPI.getInstance(getActivity(), getResources().getString(R.string.mixpanel_token));
	}

	@Override
	public void onDestroy()
	{
		mixpanel.flush();
		super.onDestroy();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.options, null);
		FontSetter.overrideFonts(getActivity(), view);
		TextView textViewName = (TextView) view.findViewById(R.id.textViewName);
		TextView textViewEmail = (TextView) view.findViewById(R.id.textViewEmail);
		TextView textViewPassword = (TextView) view.findViewById(R.id.textViewPassword);
		CheckBox checkBoxFacebook=(CheckBox) view.findViewById(R.id.checkBoxFacebook);
		CheckBox checkBoxTwitter=(CheckBox) view.findViewById(R.id.checkBoxTwitter);
		textViewName.setText(FoodCirclesUtils.getName(view.getContext()));
		textViewEmail.setText(FoodCirclesUtils.getEmail(view.getContext()));
		textViewPassword.setText(FoodCirclesUtils.getPassword(view.getContext()));
		checkBoxFacebook.setOnCheckedChangeListener(new OnCheckedChangeListener(){
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
	        	FoodCirclesUtils.saveIsFacebookConnected(AccountOptionsFragment.this.getActivity(), isChecked);
		    }
		});
		checkBoxTwitter.setOnCheckedChangeListener(new OnCheckedChangeListener(){
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
	        	FoodCirclesUtils.saveIsFacebookConnected(AccountOptionsFragment.this.getActivity(), isChecked);
		    }
		});
		
		textViewEmail.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MP.track(mixpanel, "Options", "Clicked email");
				FragmentManager fm = getActivity().getSupportFragmentManager();
				EmailDialogFragment emailDialog = new EmailDialogFragment();
				emailDialog.show(fm, "email_dialog");
			}
		});

		textViewPassword.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MP.track(mixpanel, "Options", "Clicked password");
				FragmentManager fm = getActivity().getSupportFragmentManager();
				PasswordDialogFragment passwordDialog = new PasswordDialogFragment();
				passwordDialog.show(fm, "password_dialog");
			}
		});

		ImageView contactTwitterButton = (ImageView) view.findViewById(R.id.twitterContactUs);
		contactTwitterButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW,
					    Uri.parse("twitter://user?screen_name=FoodCircles"));
					startActivity(intent);

					}catch (Exception e) {
					    startActivity(new Intent(Intent.ACTION_VIEW,
					         Uri.parse("https://twitter.com/#!/FoodCircles"))); 
					} 
			}
			
		});
		
		
		ImageView contactFacebookButton = (ImageView) view.findViewById(R.id.facebookContactUs);
		contactFacebookButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				getOpenFacebookIntent(v.getContext());
			}
			
		});
		ImageView contactUsButton = (ImageView) view.findViewById(R.id.emailContactUs);
		contactUsButton.setOnClickListener(new OnClickListener()
		{
		
			@Override
			public void onClick(View v)
			{
			//Creates an email intent, fills it with data, and sends it to the activity chooser
			final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"support@FoodCircles.net"});
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "About the FoodCircles App...");
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		}
	});
		//This removes the user's password and disables automatic login.
		Button logoutButton = (Button) view.findViewById(R.id.buttonLogout);
		logoutButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MP.track(mixpanel, "Options", "Logged out");
				FoodCirclesUtils.savePassword(AccountOptionsFragment.this.getActivity(), null);
				FoodCirclesUtils.saveToken(AccountOptionsFragment.this.getActivity(), null);
				FoodCirclesUtils.saveEmail(AccountOptionsFragment.this.getActivity(), null);
				Intent intent = new Intent(AccountOptionsFragment.this.getActivity(), SignUpActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				MainActivity.mActivity.finish();
				AccountOptionsFragment.this.getActivity().finish();
			}
		});

		return view;
	}
	
	public static Intent getOpenFacebookIntent(Context context) {
		   try {
		    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
		    return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/<id_here>"));
		   } catch (Exception e) {
		    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/<user_name_here>"));
		   }
		}
}
