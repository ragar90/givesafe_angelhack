package co.foodcircles.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import co.foodcircles.R;
import co.foodcircles.util.C;

public class CarouselFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.carousel, null);
		C.overrideFonts(getActivity(), view);

		((ImageView) view.findViewById(R.id.imageViewFacebook)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String url = "https://twitter.com/intent/tweet?source=webclient&text=TWEET+THIS!";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		((ImageView) view.findViewById(R.id.imageViewTwitter)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String url = "https://twitter.com/intent/tweet?source=webclient&text=Food+Circles+twitter+copy";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		return view;
	}
}
