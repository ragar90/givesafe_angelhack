package co.foodcircles.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.json.Venue;
import co.foodcircles.net.Net;
import co.foodcircles.util.C;
import co.foodcircles.util.FoodCirclesApplication;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class RestaurantGridFragment extends Fragment
{
	private List<Venue> venues = new ArrayList<Venue>();
	private VenueAdapter adapter;
	private GridView gridView;
	private ProgressDialog progressDialog;
	private String TAG = "RestaurantGridFragment";
	private DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.polaroid_grid, null);
		C.overrideFonts(getActivity(), view);

		options = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity().getApplicationContext()).threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(2 * 1024 * 1024).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
				.imageDownloader(new BaseImageDownloader(getActivity().getApplicationContext())).enableLogging().build();
		ImageLoader.getInstance().init(config);

		gridView = (GridView) view.findViewById(R.id.gridView);
		adapter = new VenueAdapter(venues);
		gridView.setAdapter(adapter);

		progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Loading venues...");
		new AsyncTask<Object, Void, Boolean>()
		{
			protected Boolean doInBackground(Object... param)
			{
				try
				{
					venues.addAll(Net.getVenuesList(null));
					return true;
				}
				catch (Exception e)
				{
					Log.v(TAG, "Error loading venues", e);
					return false;
				}
			}

			protected void onPostExecute(Boolean success)
			{
				adapter.notifyDataSetChanged();
				progressDialog.dismiss();
				if (!success)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage("Could not load venues.").setTitle("Network Error");
					builder.setPositiveButton("OK", new OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int id)
						{
							RestaurantGridFragment.this.getActivity().finish();
						}
					});
					builder.create().show();
				}
			}
		}.execute();

		gridView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id)
			{
				FoodCirclesApplication app = (FoodCirclesApplication) RestaurantGridFragment.this.getActivity().getApplicationContext();
				app.selectedVenue = venues.get(position);
				Intent intent = new Intent(RestaurantGridFragment.this.getActivity(), RestaurantActivity.class);
				startActivity(intent);
			}
		});

		return view;
	}

	private class VenueAdapter extends BaseAdapter
	{
		List<Venue> venues;

		private class ViewHolder
		{
			public ImageView logo;
			public TextView name;
			public TextView cuisine;
			public TextView distance;
		}

		public VenueAdapter(List<Venue> venues)
		{
			this.venues = venues;
		}

		@Override
		public int getCount()
		{
			return venues.size();
		}

		@Override
		public Object getItem(int index)
		{
			return venues.get(index);
		}

		@Override
		public long getItemId(int index)
		{
			return index;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null)
			{
				view = getActivity().getLayoutInflater().inflate(R.layout.polaroid, parent, false);
				C.overrideFonts(parent.getContext(), view);
				holder = new ViewHolder();
				holder.logo = (ImageView) view.findViewById(R.id.imageViewLogo);
				holder.name = (TextView) view.findViewById(R.id.textViewName);
				holder.cuisine = (TextView) view.findViewById(R.id.textViewCuisine);
				holder.distance = (TextView) view.findViewById(R.id.textViewDistance);
				view.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) view.getTag();
			}

			Venue venue = venues.get(position);
			imageLoader.displayImage("http://foodcircles.net" + venues.get(position).getImageUrl(), holder.logo, options);
			holder.name.setText(venue.getName());
			holder.cuisine.setText(venue.getTagsString());
			holder.distance.setText("10.0 mi");

			return view;
		}

	}
}
