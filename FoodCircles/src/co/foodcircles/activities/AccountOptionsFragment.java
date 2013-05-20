package co.foodcircles.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.foodcircles.R;
import co.foodcircles.util.C;

public class AccountOptionsFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.options, null);
		C.overrideFonts(getActivity(), view);
		return view;
	}
}
