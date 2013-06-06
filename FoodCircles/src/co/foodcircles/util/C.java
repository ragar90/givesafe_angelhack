package co.foodcircles.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class C
{
	public static Typeface font;
	public static Typeface boldFont;
	public static Typeface italicFont;
	public static Typeface boldItalicFont;

	private static void initFonts(final Context context)
	{
		if (font == null)
		{
			font = Typeface.createFromAsset(context.getAssets(), "neutrafaceslabtextbook.ttf");
			boldFont = Typeface.createFromAsset(context.getAssets(), "neutrafaceslabtextbold.ttf");
			italicFont = Typeface.createFromAsset(context.getAssets(), "neutrafaceslabtextbookitalic.ttf");
			boldItalicFont = Typeface.createFromAsset(context.getAssets(), "neutrafaceslabtextbolditalic.ttf");
		}
	}

	public static void overrideFonts(final Context context, final View v)
	{
		initFonts(context);

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
			else if (v instanceof TextView && !(v instanceof Button) && !(v instanceof EditText))
			{
				TextView textView = (TextView) v;
				
				if ((v.getTag() != null) && ((String) v.getTag()).equals("neu"))
				{
					Typeface newFont = font;

					if (textView.getTypeface() != null)
					{
						if (textView.getTypeface().isBold())
						{
							if (textView.getTypeface().isItalic())
								newFont = boldItalicFont;
							else
								newFont = boldFont;
						}
						else
						{
							if (textView.getTypeface().isItalic())
								newFont = italicFont;
						}
					}
					textView.setTypeface(newFont);
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
