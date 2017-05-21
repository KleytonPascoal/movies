package com.kleytonpascoal.movies.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.model.Movie;

public class WebPageActivity extends AppCompatActivity {

    public static final String PKG = WebPageActivity.class.getPackage().getName();
    public static final String TAG = WebPageActivity.class.getName();

    public static final String EXTRA_MOVIE = PKG + ".EXTRA_MOVIE";

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getIntent().getExtras();
        if (arguments.containsKey(EXTRA_MOVIE)) {

            setContentView(R.layout.activity_web_page);

            onCreateToolbar();
            onCreateProgressBar();
            onCreateWebView();

            loadValuesFromIntent(arguments);

        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            sendResultIntent();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        sendResultIntent();
    }

    private void sendResultIntent() {
        Intent returnIntent = getIntent();
        returnIntent.putExtra(EXTRA_MOVIE, mMovie);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void loadValuesFromIntent(Bundle arguments) {
        mMovie = arguments.getParcelable(EXTRA_MOVIE);
        setTitle(mMovie.title);
        mToolbar.setSubtitle(mMovie.website);
        mWebView.loadUrl(mMovie.website);
    }

    private void onCreateWebView() {
        mWebView = (WebView) findViewById(R.id.web_page);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.requestFocus();

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                mProgressBar.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url){
                mWebView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);

                // For Espresso UI Tests
                decrementIdlingResource();
            }

        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress){
                mProgressBar.setProgress(newProgress);
                if(newProgress == 100){
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        // For Espresso UI Tests
        incrementIdlingResource();
    }

    private void onCreateProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.web_page_progress);
    }

    private void onCreateToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    /* Espresso UI Tests */

    private void decrementIdlingResource() {
        if (mIdlingResource != null)
            mIdlingResource.decrement();
    }

    private void incrementIdlingResource() {
        if (mIdlingResource != null)
            mIdlingResource.increment();
    }

    private CountingIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public CountingIdlingResource getIdlingResource() {
        if (mIdlingResource == null)
            mIdlingResource = new CountingIdlingResource("MOVIE_WEB_PAGE_LOADER");
        return mIdlingResource;
    }
}
