package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {

    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;

    private final int VIEW_TYPE_COUNT = 2;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT; // We have 2 types of view that can be used on the adapter
    }

    /*
      Remember that these views are reused as needed.
    */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int position = getItemViewType(cursor.getPosition());
        int layoutId;

        if (position == VIEW_TYPE_TODAY)
            layoutId = R.layout.list_item_forecast_today;
        else
            layoutId = R.layout.list_item_forecast;

        return LayoutInflater.from(context).inflate(layoutId, parent, false);
    }

    /*
        This is where we fill-in the view (layout_item_forecast) with the contents of the cursor.
        The view parameter comes from newView method
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        TextView descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
        descriptionView.setText(description);

        boolean isMetric = Utility.isMetric(context);

        double maxTemp = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        TextView maxTempView = (TextView)view.findViewById(R.id.list_item_high_textview);
        maxTempView.setText(Utility.formatTemperature(maxTemp, isMetric));

        double minTemp = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        TextView minTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
        minTempView.setText(Utility.formatTemperature(minTemp, isMetric));

        long date = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        TextView dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
        dateView.setText(Utility.getFriendlyDayString(context, date));

        ImageView iconView = (ImageView) view.findViewById(R.id.list_item_icon);
        iconView.setImageResource(R.drawable.ic_launcher);
    }
}