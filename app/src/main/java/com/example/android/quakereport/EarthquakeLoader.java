package com.example.android.quakereport;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 11/30/2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<GetQuake>> {

    String mUrl;

    /**
     *@param context of the activity
     *@param url to load data from the qiven url
     */
    public EarthquakeLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<GetQuake> loadInBackground() {
        //Do not perform the request if there's no url or the first url is null
        if (mUrl == null) {
            return null;
        }

        List<GetQuake> result = QueryUtils.fetchEarthquakes(mUrl);
        return result;
    }
}
