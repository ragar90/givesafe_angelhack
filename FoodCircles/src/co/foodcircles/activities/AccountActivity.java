package co.foodcircles.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import co.foodcircles.R;

public class AccountActivity extends Activity
{
	EditText nameEditText;
	ListView list;
	AboutYouAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		List<String> selectionStrings = new ArrayList<String>();
		selectionStrings.add("Purchase History");
		selectionStrings.add("Saved Payments");
		selectionStrings.add("Change Password");
		selectionStrings.add("Social Networks");
		selectionStrings.add("ToS / Privace");
		selectionStrings.add("Log Out");

		List<Integer> selectionIcons = new ArrayList<Integer>();
		selectionIcons.add(R.drawable.logo);
		selectionIcons.add(R.drawable.logo);
		selectionIcons.add(R.drawable.logo);
		selectionIcons.add(R.drawable.logo);
		selectionIcons.add(R.drawable.logo);
		selectionIcons.add(R.drawable.logo);

		nameEditText = (EditText) findViewById(R.id.editText);
		list = (ListView) findViewById(R.id.listView);
		adapter = new AboutYouAdapter(selectionStrings, selectionIcons);
		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		list.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				//TODO: do something when they click an item
			}
		});
	}

	private class AboutYouAdapter extends BaseAdapter
	{
		List<String> strings;
		List<Integer> icons;

		private class ViewHolder
		{
			public ImageView icon;
			public TextView string;
		}

		public AboutYouAdapter(List<String> strings, List<Integer> icons)
		{
			this.strings = strings;
			this.icons = icons;
		}

		@Override
		public int getCount()
		{
			return strings.size();
		}

		@Override
		public Object getItem(int position)
		{
			return strings.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null)
			{
				view = getLayoutInflater().inflate(R.layout.about_you_row, parent, false);
				holder = new ViewHolder();
				holder.icon = (ImageView) view.findViewById(R.id.imageViewIcon);
				holder.string = (TextView) view.findViewById(R.id.textView);
				view.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) view.getTag();
			}

			holder.string.setText(strings.get(position));
			holder.icon.setImageResource(icons.get(position));

			return view;
		}

	}

}
