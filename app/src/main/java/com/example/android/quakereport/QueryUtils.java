package com.example.android.quakereport;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    // Tag for the lo messages
    public static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Query the USGS dataset
     */
    public static List<GetQuake> fetchEarthquakes(String requestUrl) {
        //create URL object
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);

        }
        catch (IOException e){
            Log.e(LOG_TAG, "Unable to extract data", e);
        }

        //Extract relevant fields from JSON response and create a list of {@Link GetQuake} Earthquake
        List<GetQuake> earthquakes = extractFeatureFromJson(jsonResponse);

        //return the {@Link GetQuake}
        return earthquakes;
    }


    /**
     * Returns URL object from the given URL String
     */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }

    /**
     * Make HTTP request to the given URL and return a string as the response
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If the URL is null, then return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            //make url Connection
            urlConnection.connect();

            /**
             * If the request was successful (i.e. response code = 200)
             * then read the input stream
             */
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }

            else {
                Log.e(LOG_TAG, "Error Response code: " + urlConnection.getResponseCode());
            }
        }
        catch (IOException e) {
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }


    private static String readFromStream (InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Return an {@Link Event} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */
    private  static List<GetQuake> extractFeatureFromJson(String earthquakeJSON) {
        //If the JSON string is empty or null, the return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        //Create an empty ArrayList that we can start adding earthquakes to
        List<GetQuake> earthquakes = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
            JSONArray featuresArray = baseJsonResponse.getJSONArray("features");

            for(int i = 0; i<featuresArray.length(); i++) {
                JSONObject currentEarthquake = featuresArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");
                Double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                Long time = properties.getLong("time");
                String url = properties.getString("url");

                //Create a new GetQuake Object
                GetQuake earthquake = new GetQuake(magnitude, location, time, url);
                earthquakes.add(earthquake);
            }

        } catch (JSONException e){
            Log.e(LOG_TAG, "Error parsing the earthquake JSON data", e);
        }

        //return the list of earthquakes
        return earthquakes;
    }






















    /** Sample JSON response for a USGS query */


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
  //  private QueryUtils() {
  //  }

    /**
     * Return a list of {@link GetQuake} objects that has been built up from
     * parsing a JSON response.
     */
    /** @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<GetQuake> extractEarthquakes() {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<GetQuake> earthquakes = new ArrayList<>();

        // Try to parse the REQUEST_URL. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the REQUEST_URL string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject baseJsonResponse = new JSONObject(REQUEST_URL);
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            for(int i = 0; i<earthquakeArray.length(); i++)
            {
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");
                Double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                Long time = properties.getLong("time");


                GetQuake earthquake = new GetQuake(magnitude, location, time);
                earthquakes.add(earthquake);
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    */

}