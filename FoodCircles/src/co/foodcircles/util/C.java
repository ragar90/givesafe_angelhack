package co.foodcircles.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class C
{
	public static String fontName = "neutrafaceslabtextbook.ttf";
	public static String fontItalicName = "neutrafaceslabtextbook";

	public static void overrideFonts(final Context context, final View v)
	{
		try
		{
			if (v instanceof ViewGroup)
			{
				ViewGroup vg = (ViewGroup) v;
				for (int i = 0; i < vg.getChildCount(); i++)
				{
					View child = vg.getChildAt(i);
					overrideFonts(context, child);
				}
			}
			else if (v instanceof TextView)
			{
				TextView textView = (TextView) v;
				String font = "neutrafaceslabtextbook";
				if (textView.getTypeface() != null && textView.getTypeface().isBold())
					font = "neutrafaceslabtextbold";
				if (textView.getTypeface() != null && textView.getTypeface().isItalic())
					font += "italic";
				font += ".ttf";
				textView.setTypeface(Typeface.createFromAsset(context.getAssets(), font));
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
