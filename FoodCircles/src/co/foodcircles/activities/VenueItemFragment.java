package co.foodcircles.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import co.foodcircles.R;
import co.foodcircles.json.Offer;
import co.foodcircles.json.Venue;
import co.foodcircles.net.Net;
import co.foodcircles.util.FontSetter;
import co.foodcircles.util.FoodCirclesApplication;

public class VenueItemFragment extends Fragment
{
	ImageView itemImage;
	TextView itemName;
	TextView itemFlavorText;
	TextView itemOriginalPrice;
	Button button;

	//This fragment is the view that gives detailed information about the deal, including a picture and venue info 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = getActivity().getLayoutInflater().inflate(R.layout.venue_profile, null);
		FontSetter.overrideFonts(getActivity(), view);

		itemImage = (ImageView) view.findViewById(R.id.imageView);

		itemName = (TextView) view.findViewById(R.id.textViewItemName);
		itemOriginalPrice = (TextView) view.findViewById(R.id.textViewPrice);
		itemFlavorText = (TextView) view.findViewById(R.id.textViewItemFlavorText);
		button = (Button) view.findViewById(R.id.button);

		final FoodCirclesApplication app = (FoodCirclesApplication) getActivity().getApplicationContext();
		Venue venue = app.selectedVenue;
		
		Offer offer = venue.getOffers().get(0);
		itemImage.setTag(Net.HOST + venue.getImageUrl());
		new DownloadImagesTask().execute(itemImage);
		itemName.setText(offer.getTitle());
		itemFlavorText.setText(offer.getDetails());
		try {
		itemOriginalPrice.setText("" + offer.getFullPrice());
		} catch (Exception e) {			
			itemOriginalPrice.setText("9");
		}
		
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().startActivity(new Intent(getActivity(), BuyOptionsActivity.class));
				app.addPoppableActivity(getActivity());
			}
		});

		return view;
	}

	
	public class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {
		ImageView imageView = null;

		@Override
		protected Bitmap doInBackground(ImageView... imageViews) {
		    this.imageView = imageViews[0];
		    return download_Image((String)imageView.getTag());
		}

		@Override
		protected void onPostExecute(Bitmap result) {
		    imageView.setImageBitmap(result);
		}

		private Bitmap download_Image(String src) {
	        try {
	            URL url = new URL(src);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setDoInput(true);
	            connection.connect();
	            InputStream input = connection.getInputStream();
	            Bitmap myBitmap = BitmapFactory.decodeStream(input);
	            return myBitmap;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
		}
	}
}
