/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import android.content.AsyncTaskLoader;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<GetQuake>> {

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    /**
     * Adapter for the list of earthquakes
     */
    EarthQuakeAdapter mAdapter;

    /**
     * Tag for log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Url to fetch data from
     */
    private static final String REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    TextView locationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new EarthQuakeAdapter(this, new ArrayList<GetQuake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);


        /**
         * //Set EarthquakeAsyncTask to update the UI using
         * EarthquakeAsyncTask task = new EarthquakeAsyncTask();
         * task.execute(REQUEST_URL);
         */


        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Find the current earthquake that was clicked on
                GetQuake currentEarthquake = mAdapter.getItem(position);

                //Convert the String URL into a URI object (to pass into the Intent constant)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                //Create new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                //Start intent to launch a new activity
                startActivity(websiteIntent);
            }
        });


        //Get a reference to the loader manager in order to interact with the loaders
        android.app.LoaderManager loaderManager = getLoaderManager();

        /**
         * Initialize the loader. Pass in the int ID constant defined above
         * pass in null for the bundle
         * Pass in this activity for the LoaderCallbacks parameter (which is valid
         * because this activity implements the LoaderCallbacks interface).
         */
        getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);

    }

    @Override
    public Loader<List<GetQuake>> onCreateLoader(int i, Bundle bundle) {
        //creates a new loader for the given url
        return new EarthquakeLoader(this, REQUEST_URL);
    }


    @Override
    public void onLoadFinished(Loader<List<GetQuake>> loader, List<GetQuake> earthquakes) {
        //clear the adapter of previous earthquake data
        mAdapter.clear();

        //if there's a valid list of {@link earthquakes} then add them to the adapter's
        //data set. This will trigger the listview to update
        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<GetQuake>> loader) {
        mAdapter.clear();
    }
}


    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the list of earthquakes in the response.
     * <p>
     * AsyncTask has three generic parameters: the input type, a type used for progress updates, and
     * an output type. Our task will take a String URL, and return an Earthquake. We won't do
     * progress updates, so the second generic is just Void.
     * <p>
     * We'll only override two of the methods of AsyncTask: doInBackground() and onPostExecute().
     * The doInBackground() method runs on a background thread, so it can run long-running code
     * (like network activity), without interfering with the responsiveness of the app.
     * Then onPostExecute() is passed the result of doInBackground() method, but runs on the
     * UI thread, so it can use the produced data to update the UI.
     */
    /**

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<GetQuake>> {

        /**
         * This method runs the network request on the background thread
         * and returns a list of Earthquakes
     *

        @Override
        protected List<GetQuake> doInBackground(String... urls) {
            //Do not perform the request if there's no url or the first url is null
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<GetQuake> result = QueryUtils.fetchEarthquakes(urls[0]);
            return result;

        }

        @Override
        protected void onPostExecute (List<GetQuake> data) {
            // Clear the adapter of previous earthquake data
            mAdapter.clear();

            /**
             * If there's a valid list of Earthquake then add them to the set.
             * This will trigger the listview to update


            if (data != null && !data.isEmpty()){
                mAdapter.addAll(data);
            }
        }
    }*/

