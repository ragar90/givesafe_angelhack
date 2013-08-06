package co.foodcircles.activities;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.util.FontSetter;

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
		textViewName.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MP.track(mixpanel, "Options", "Clicked name");
				FragmentManager fm = getActivity().getSupportFragmentManager();
				NameDialogFragment nameDialog = new NameDialogFragment();
				nameDialog.show(fm, "name_dialog");
			}
		});

		TextView textViewEmail = (TextView) view.findViewById(R.id.textViewEmail);
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

		TextView textViewPassword = (TextView) view.findViewById(R.id.textViewPassword);
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

		Button logoutButton = (Button) view.findViewById(R.id.buttonLogout);
		logoutButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MP.track(mixpanel, "Options", "Logged out");
				Intent intent = new Intent(AccountOptionsFragment.this.getActivity(), SignInActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				AccountOptionsFragment.this.getActivity().finish();
			}
		});

		return view;
	}
}
