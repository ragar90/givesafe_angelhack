package co.foodcircles.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PricePickerDialogFragment extends DialogFragment
{
	RestaurantUpgradesFragment innerFragment;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		innerFragment = new RestaurantUpgradesFragment();
		innerFragment.setDialog(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return innerFragment.onCreateView(inflater, container, savedInstanceState);
	}
}