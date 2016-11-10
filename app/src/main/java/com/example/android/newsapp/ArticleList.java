package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ArticleList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    // globals
    private ProgressBar mProgBar;
    private TextView mEmptyTextView;
    private ArticleAdapter mAdapter;
    private String mURL;

    private static final String apiKey = "c2c3707e-b87e-430e-9b42-aac6641aa664";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        String uriBase = "http://content.guardianapis.com/search?q=";
        String input = MainActivity.search_input.getText().toString();
        String encodedInput = "";
        try {
            encodedInput = URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, "encoding error", e);
        }
        mURL = uriBase + encodedInput;

        mProgBar = (ProgressBar) findViewById(R.id.progress_bar);

        ListView listView = (ListView) findViewById(R.id.list_view);

        mEmptyTextView = (TextView) findViewById(R.id.empty_text_view);
        listView.setEmptyView(mEmptyTextView);

        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article currentArticle = mAdapter.getItem(position);

                Intent website = new Intent(Intent.ACTION_VIEW, Uri.parse(currentArticle.getmURL()));

                startActivity(website);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
            mProgBar.setVisibility(View.VISIBLE);
        } else {
            mEmptyTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String fromDate = sharedPrefs.getString(
                getString(R.string.settings_from_date_key),
                getString(R.string.settings_from_date_default)
        );
        String toDate = sharedPrefs.getString(
                getString(R.string.settings_to_date_key),
                getString(R.string.settings_to_date_default)
        );
        Uri baseURI = Uri.parse(mURL);
        Uri.Builder uriBuilder = baseURI.buildUpon();


        uriBuilder.appendQueryParameter("from-date", fromDate);
        uriBuilder.appendQueryParameter("to-date", toDate);
        uriBuilder.appendQueryParameter("api-key", apiKey);

        Log.e(LOG_TAG, "UUUUUURRRRRRIIIIII ---- BASE : " + uriBuilder.toString());
        return new QueryAsyncTaskLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        mProgBar.setVisibility(View.GONE);
        mAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        } else {
            mEmptyTextView.setText(R.string.empty_text);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
