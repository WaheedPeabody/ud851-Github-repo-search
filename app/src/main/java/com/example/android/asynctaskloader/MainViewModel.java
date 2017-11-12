package com.example.android.asynctaskloader;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.example.android.asynctaskloader.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Waheed on 12-Nov-17.
 */

public class MainViewModel extends ViewModel {

    private String searchQuery;
    private String queryUrl;
    private MutableLiveData<String> searchResults = new MutableLiveData<>();

    public void makeGithubSearchQuery(final String searchQuery) {

        this.searchQuery = searchQuery;
        this.queryUrl = NetworkUtils.buildUrl(searchQuery).toString();

        AsyncTask<String, Void, String> asyncTask
                = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    URL url = NetworkUtils.buildUrl(searchQuery);
                    String responseFromHttpUrl
                            = NetworkUtils.getResponseFromHttpUrl(url);
                    return responseFromHttpUrl;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                searchResults.setValue(s);
            }
        };
        asyncTask.execute();
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getQueryUrl() {
        return queryUrl;
    }

    public void setQueryUrl(String queryUrl) {
        this.queryUrl = queryUrl;
    }

    public LiveData<String> getSearchResults() {
        return searchResults;
    }
}
