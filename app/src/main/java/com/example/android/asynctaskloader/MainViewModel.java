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
    private MutableLiveData<String> queryUrl = new MutableLiveData<>();
    private MutableLiveData<String> searchResults = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public MainViewModel() {
        super();
        loading.setValue(false);
    }

    public void makeGithubSearchQuery(final String searchQuery) {
        loading.setValue(true);

        // if the same search query as before
        // and there exist search results for that query then do nothing
        if (searchQuery.equals(this.searchQuery)
                && (searchResults.getValue() != null)) {
            loading.setValue(false);
            return;
        }

        this.searchQuery = searchQuery;
        String queryUrlStr = NetworkUtils.buildUrl(searchQuery).toString();
        this.queryUrl.setValue(queryUrlStr);

        AsyncTask<String, Void, String> asyncTask
                = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    URL url = NetworkUtils.buildUrl(searchQuery);
                    return NetworkUtils.getResponseFromHttpUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                searchResults.setValue(result);
                loading.setValue(false);
            }
        };
        asyncTask.execute();
    }

    public LiveData<String> getSearchResults() {
        return searchResults;
    }

    public LiveData<String> getQueryUrl() {
        return queryUrl;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }
}
