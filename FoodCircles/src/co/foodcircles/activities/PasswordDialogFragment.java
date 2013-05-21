package co.foodcircles.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import co.foodcircles.R;

public class PasswordDialogFragment extends DialogFragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View v = inflater.inflate(R.layout.change_password, container, false);
		EditText password1 = (EditText) v.findViewById(R.id.editTextPassword);
		EditText password2 = (EditText) v.findViewById(R.id.editTextConfirm);

		// Watch for button clicks.
		Button button = (Button) v.findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				PasswordDialogFragment.this.dismiss();
			}
		});

		return v;
	}
}
