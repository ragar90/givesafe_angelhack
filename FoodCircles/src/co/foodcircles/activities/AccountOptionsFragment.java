package co.foodcircles.activities;

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
import co.foodcircles.util.C;

public class AccountOptionsFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.options, null);
		C.overrideFonts(getActivity(), view);

		TextView textViewName = (TextView) view.findViewById(R.id.textViewName);
		textViewName.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
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
				Intent intent = new Intent(AccountOptionsFragment.this.getActivity(), SignInActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				AccountOptionsFragment.this.getActivity().finish();
			}
		});

		return view;
	}
}
