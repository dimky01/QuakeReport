package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 8/29/2017.
 */

public class EarthQuakeAdapter extends ArrayAdapter<GetQuake> {

    private static final String LOCATION_SEPARATOR = "of";

    public EarthQuakeAdapter(Activity context, ArrayList<GetQuake> earthquakes){
        super(context, 0, earthquakes);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //get the current position of the earthquake in the arraylist
        GetQuake currentEarthQuake = getItem(position);

        View listItemView = convertView;
        if(listItemView == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            listItemView = layoutInflater.inflate(R.layout.listview_format, null);
        }


        /**
         * SET THE MAGNITUDE TEXTVIEWS
         */


        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);

        //format the magnitude to show 1 decimal
        String formattedMagnitude = decimalFormat(currentEarthQuake.getMagnitude());

        //set the formatted magnitude on the Magnitude Textview
        magnitudeView.setText(formattedMagnitude);

        //set the proper color on the magnitude circle
        //Fetch the background XML from the magnitude textView which is a gradient drawable
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        int magnitudeColor = getMagnitudeColor(currentEarthQuake.getMagnitude());

        //set the color on the magnitudeCircle
        magnitudeCircle.setColor(magnitudeColor);



        /**
         * SET THE LOCATION TEXTVIEWS
         */

        //Create a new string object from the location of the earthquake
        String originalLocation = currentEarthQuake.getLocation();

        //declare the variable to store the first part of the location
        String location_Offset ="";

        //declare variable to store the second part of the location
        String primary_Location ="";

        if(originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            location_Offset = parts[0] + LOCATION_SEPARATOR;
            primary_Location = parts[1];
        }

        else {
            location_Offset = "Near of";
            primary_Location = originalLocation;
        }
         //set the first part of the splitted location string to the location_offset variable
         TextView locationOffset = (TextView) listItemView.findViewById(R.id.location_offset);
         locationOffset.setText(location_Offset);

         //set the first part of the splitted location string to the primary_location variable
         TextView primaryLocation = (TextView) listItemView.findViewById(R.id.Primary_location);
         primaryLocation.setText(primary_Location.trim());


        /**
         * SET THE DATE AND TIME TEXTVIEWS
         */

        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(currentEarthQuake.getTime());

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        String formattedDate = formatDate(dateObject);
        dateView.setText(formattedDate);

        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        String formattedTime = formatTime(dateObject);
        timeView.setText(formattedTime);

        return listItemView;
    }


    /**
     * Return the formatted date String (i.e. Mar 3, 2014) from the Date Object
     * @param dateObject
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM DD, YYYY");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date String (i.e. 4:30 pm) from the Date Object
     * @param dateObject
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     * Return formatted @param magnitude into a double format of one decimal place
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String decimalFormat(Double magnitude) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(magnitude);
    }


    /**
     * Return the color of the magnitude circle based on the intensity of the earthquake magnitude
     * @param magnitude
     * @return
     */
    private int getMagnitudeColor(Double magnitude) {
        //integer variable to store the ID of the magnitude color
        int magnitudeColorResourceID;

        //Approximate the magnitude value to whole number
        int magnitudeFloor = (int) Math.floor(magnitude);

        switch (magnitudeFloor){

            case 0:

            case 1:
                magnitudeColorResourceID = R.color.magnitude1;
                break;

            case 2:
                magnitudeColorResourceID = R.color.magnitude2;
                break;

            case 3:
                magnitudeColorResourceID = R.color.magnitude3;
                break;

            case 4:
                magnitudeColorResourceID = R.color.magnitude4;
                break;

            case 5:
                magnitudeColorResourceID = R.color.magnitude5;
                break;

            case 6:
                magnitudeColorResourceID = R.color.magnitude6;
                break;

            case 7:
                magnitudeColorResourceID = R.color.magnitude7;
                break;

            case 8:
                magnitudeColorResourceID = R.color.magnitude8;
                break;

            case 9:
                magnitudeColorResourceID = R.color.magnitude9;
                break;

            default:
                magnitudeColorResourceID = R.color.magnitude10plus;
                break;

        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceID);
    }
}
