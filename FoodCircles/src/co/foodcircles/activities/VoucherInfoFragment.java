package co.foodcircles.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import co.foodcircles.R;

public class VoucherInfoFragment extends Fragment
{
	TextView restaurantName;
	TextView kidsFed;
	TextView upgrade;
	TextView partySize;
	TextView expiration;
	Button viewCode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.voucher, null);

		restaurantName = (TextView) view.findViewById(R.id.textViewName);
		kidsFed = (TextView) view.findViewById(R.id.textViewKidsFed);
		upgrade = (TextView) view.findViewById(R.id.textViewUpgrade);
		partySize = (TextView) view.findViewById(R.id.textViewPartySize);
		expiration = (TextView) view.findViewById(R.id.textViewExpiration);
		viewCode = (Button) view.findViewById(R.id.buttonViewCode);

		viewCode.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("Code: 5dig1").setPositiveButton("Mark As Used", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						// TODO: mark as used on server?`
					}
				}).setNegativeButton("Cancel", null);
				// Create the AlertDialog object and return it
				builder.create().show();
			}
		});

		return view;
	}
}
