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

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    /*
        This is where we fill-in the view (layout_item_forecast) with the contents of the cursor.
        The view parameter comes from newView method
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // For today item view we use the Art resource
        // For other we use the black&white icon res
        if (getItemViewType(cursor.getPosition()) == VIEW_TYPE_TODAY)
            viewHolder.iconIV.setImageResource(Utility
                    .getArtResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
        else
            viewHolder.iconIV.setImageResource(Utility
                    .getIconResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));

        String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        viewHolder.forecastTV.setText(description);

        boolean isMetric = Utility.isMetric(context);

        double maxTemp = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        viewHolder.maxTempTV.setText(Utility.formatTemperature(context, maxTemp, isMetric));

        double minTemp = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        viewHolder.minTempTV.setText(Utility.formatTemperature(context, minTemp, isMetric));

        long date = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        viewHolder.dateTV.setText(Utility.getFriendlyDayString(context, date));
    }

    private static class ViewHolder {
        TextView minTempTV;
        TextView maxTempTV;
        TextView dateTV;
        TextView forecastTV;
        ImageView iconIV;

        private ViewHolder(View view) {
            minTempTV = (TextView) view.findViewById(R.id.list_item_low_textview);
            maxTempTV = (TextView) view.findViewById(R.id.list_item_high_textview);
            dateTV = (TextView) view.findViewById(R.id.list_item_date_textview);
            forecastTV = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            iconIV = (ImageView) view.findViewById(R.id.list_item_icon);
        }
    }
}