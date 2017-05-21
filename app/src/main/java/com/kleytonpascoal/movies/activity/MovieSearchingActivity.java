package com.kleytonpascoal.movies.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.adapter.EndlessScrollListenerRecyclerView;
import com.kleytonpascoal.movies.adapter.MovieShortInfoListRecyclerViewAdapter;
import com.kleytonpascoal.movies.helper.VolleyHelper;
import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.model.MovieShortInfo;
import com.kleytonpascoal.movies.model.SearchMovieListResult;
import com.kleytonpascoal.movies.provider.NetworkStatusProvider;

import java.util.List;

public class MovieSearchingActivity extends AppCompatActivity
        implements MovieShortInfoListRecyclerViewAdapter.OnItemClickAtPositionListener<MovieShortInfo>,
        TextWatcher, TextView.OnEditorActionListener {

    private final static String TAG = MovieSearchingActivity.class.getSimpleName();

    public static final String BASE_URL = "http://www.omdbapi.com/?apikey=d4403af7&";

    private RecyclerView mMoviesResultRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private MovieShortInfoListRecyclerViewAdapter mMoviesResultListRecyclerViewAdapter;

    private ProgressBar mMainProgressBar;
    private ProgressBar mEndlessScrollProgressBar;
    private TextView mEmptySearchResponseTextMsg;
    private ProgressDialog mProgressBarGetMovieInfo;
    private EditText mSearchEditText;
    private ImageButton mClearSearchTextButton;

    private Gson gson;
    private RequestQueue mQueue;
    private EndlessScrollListenerRecyclerView mEndlessScrollListenerRecyclerView;
    private int mCurrentPageIndex;

    private ConnectivityStatusBroadcastReceiver mConnectivityStatusBroadcastReceiver;
    private Snackbar mNetworkConnectivitySnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_searching);

        onCreateVolleyQueue();
        onCreateGsonBuilderToParserJson();
        onSetupInitialValues();
        onCreateConnectivityBroadcastReceiver();

        onCreateTopToolbar();
        onCreateSearchEditText();
        onCreateEmptySearchResponse();
        onCreateClearSearchTextButton();
        onCreateMoviesViewToShowSearchResponse();
        onCreateProgressBarToCenterOfActivity();
        onCreateEndlessScrollOfMoviesSearchResponse();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerConnectivityBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hasInternetConnection();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterConnectivityBroadcastReceiver();
    }


    /* on create methods */

    private void onSetupInitialValues() {
        mCurrentPageIndex = 1;
    }

    private void onCreateTopToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.movie_searching_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void onCreateClearSearchTextButton() {
        mClearSearchTextButton = (ImageButton) findViewById(R.id.clear_search_bt);
    }

    private void onCreateEmptySearchResponse() {
        mEmptySearchResponseTextMsg = (TextView) findViewById(R.id.movie_list_empty_search_msg);
    }

    private void onCreateEndlessScrollOfMoviesSearchResponse() {
        mEndlessScrollProgressBar = (ProgressBar) findViewById(R.id.movie_list_endless_scroll_progress_bar);

        mEndlessScrollListenerRecyclerView = new EndlessScrollListenerRecyclerView(mLinearLayoutManager, 1) {
            @Override
            public void onLoading(int currentPageIndex) {
                mCurrentPageIndex = currentPageIndex;
                executeSearchMoviesByTitlePerPage();
            }
        };
        mMoviesResultRecyclerView.addOnScrollListener(mEndlessScrollListenerRecyclerView);
    }

    private void onCreateVolleyQueue() {
        mQueue = VolleyHelper.newRequestQueue(this);
    }

    private void onCreateSearchEditText() {
        mSearchEditText = (EditText) findViewById(R.id.searching_text);
        mSearchEditText.setOnEditorActionListener(this);
        mSearchEditText.addTextChangedListener(this);
        mSearchEditText.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void onCreateProgressBarToCenterOfActivity() {
        mMainProgressBar = (ProgressBar) findViewById(R.id.movie_list_start_search_progress_bar);
    }

    private void onCreateMoviesViewToShowSearchResponse() {
        mMoviesResultRecyclerView = (RecyclerView) findViewById(R.id.searching_result_list);
        mMoviesResultRecyclerView.setHasFixedSize(true);

        mMoviesResultListRecyclerViewAdapter = new MovieShortInfoListRecyclerViewAdapter();
        mMoviesResultListRecyclerViewAdapter.setItemClickAtPositionListener(this);
        mMoviesResultRecyclerView.setAdapter(mMoviesResultListRecyclerViewAdapter);

        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMoviesResultRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    private void onCreateGsonBuilderToParserJson() {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        gson = gsonBuilder.create();
    }

    private void onCreateConnectivityBroadcastReceiver() {
        mConnectivityStatusBroadcastReceiver = new ConnectivityStatusBroadcastReceiver();
    }


    /* View onClickListener methods in XML */

    public void onClickSearchButton(View view) {
        executeSearchMoviesByTitlePerPage();
    }

    public void onClickClearSearchTextButton(View view) {
        mSearchEditText.setText("");
    }


    /* private methods */

    private void executeSearchMoviesByTitlePerPage() {

        String title = mSearchEditText.getText().toString().trim();
        if (title.length() == 0) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);

        hideEmptyResultMessage();

        requestOmdbApiSearchMovieByTitle(title);
    }

    private boolean requestOmdbApiMovieById(MovieShortInfo item) {
        if (!hasInternetConnection()) {
            return false;
        }
        final String url = BASE_URL + "i=" + item.imdbID;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // for tests
                        decrementIdlingResource();

                        if (!TextUtils.isEmpty(response)) {
                            final Movie movie = gson.fromJson(response, Movie.class);

                            mProgressBarGetMovieInfo.cancel();

                            startActivity(new Intent(MovieSearchingActivity.this, MovieEditActivity.class).
                                    putExtra(MovieEditActivity.PARAM_MOVIE, movie).
                                    putExtra(MovieEditActivity.PARAM_MOVIE_IS_NEW, true));

                        } else {
                            hideMainProgressBar();
                            showEmptyResultMessage();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // for tests
                decrementIdlingResource();

                handleRequestOmdpApiWithError(error);
            }
        });

        // for tests
        incrementIdlingResource();

        mQueue.add(stringRequest);

        return true;
    }

    private void requestOmdbApiSearchMovieByTitle(String title) {
        if (!hasInternetConnection()) {
            return;
        }
        final String url = BASE_URL + "s=" + title.replace(" ", "+") + "&page=" + mCurrentPageIndex;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // for tests
                        decrementIdlingResource();

                        Log.d(TAG, response);

                        if (!TextUtils.isEmpty(response)) {

                            final SearchMovieListResult result = gson.fromJson(response, SearchMovieListResult.class);

                            if (result.response) {
                                setNumberOfPageIndexes(result);
                                setResponseToRecyclerView(result.movies);
                            } else {
                                showEmptyResultMessage();
                            }

                        } else {
                            showEmptyResultMessage();
                        }

                        mEndlessScrollListenerRecyclerView.loadingIsDone();
                        chooseProgressBarToHide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // for tests
                decrementIdlingResource();

                handleRequestOmdpApiWithError(error);
                chooseProgressBarToHide();
            }
        });

        chooseProgressBarToShow();

        // for tests
        incrementIdlingResource();

        mQueue.add(stringRequest);
    }

    private boolean hasInternetConnection() {
        if (mConnectivityStatusBroadcastReceiver.isConnected()) {
            return true;
        } else {
            showNoConnectivitySnackbarMessage();
            return false;
        }
    }

    private void showNoConnectivitySnackbarMessage() {
        if (isShowingNoConnectivitySnackbar())
            return;

        mNetworkConnectivitySnackbar = Snackbar.make(findViewById(R.id.movie_searching_toolbar_layout),
                R.string.no_network_connection, Snackbar.LENGTH_INDEFINITE);
        mNetworkConnectivitySnackbar.setAction(android.R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNetworkConnectivitySnackbar.dismiss();
            }
        }).show();
    }

    private boolean isShowingNoConnectivitySnackbar() {
        return mNetworkConnectivitySnackbar != null && mNetworkConnectivitySnackbar.isShown();
    }

    private void hideNoConnectivitySnackbarMessage() {
        if (isShowingNoConnectivitySnackbar())
            mNetworkConnectivitySnackbar.dismiss();
    }

    private void handleRequestOmdpApiWithError(VolleyError error) {
        Toast.makeText(this, R.string.problem_message, Toast.LENGTH_LONG).show();
        Log.e(TAG, "" + error);
        hideMainProgressBar();
        showEmptyResultMessage();
    }

    private void showEmptyResultMessage() {

        if (mEmptySearchResponseTextMsg.getVisibility() != View.VISIBLE)
            mEmptySearchResponseTextMsg.setVisibility(View.VISIBLE);

        if (mMoviesResultRecyclerView.getVisibility() != View.GONE)
            mMoviesResultRecyclerView.setVisibility(View.GONE);
    }

    private void hideEmptyResultMessage() {

        if (mEmptySearchResponseTextMsg.getVisibility() != View.GONE)
            mEmptySearchResponseTextMsg.setVisibility(View.GONE);

        if (mMoviesResultRecyclerView.getVisibility() != View.VISIBLE)
            mMoviesResultRecyclerView.setVisibility(View.VISIBLE);
    }

    private void setNumberOfPageIndexes(SearchMovieListResult result) {
        mEndlessScrollListenerRecyclerView.setMaxPageIndex(result.totalResults, result.movies.size());
    }

    private void setResponseToRecyclerView(List<MovieShortInfo> movies) {
        mMoviesResultListRecyclerViewAdapter.addAll(movies);
        mMoviesResultListRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void chooseProgressBarToShow() {
        if (mCurrentPageIndex == 1)
            showMainProgressBar();
        else
            showEndlessScrollProgressBar();
    }

    private void chooseProgressBarToHide() {
        if (mMainProgressBar.getVisibility() == View.VISIBLE)
            hideMainProgressBar();
        else
            hideEndlessScrollProgressBar();
    }

    private void showMainProgressBar() {

        if (mMainProgressBar.getVisibility() != View.VISIBLE)
            mMainProgressBar.setVisibility(View.VISIBLE);

        if (mMoviesResultRecyclerView.getVisibility() != View.GONE)
            mMoviesResultRecyclerView.setVisibility(View.GONE);
    }

    private void hideMainProgressBar() {

        if (mMainProgressBar.getVisibility() != View.GONE)
            mMainProgressBar.setVisibility(View.GONE);

        if (mMoviesResultRecyclerView.getVisibility() != View.VISIBLE)
            mMoviesResultRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showEndlessScrollProgressBar() {
        if (mEndlessScrollProgressBar.getVisibility() != View.VISIBLE)
            mEndlessScrollProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideEndlessScrollProgressBar() {
        if (mEndlessScrollProgressBar.getVisibility() != View.GONE)
            mEndlessScrollProgressBar.setVisibility(View.GONE);
    }

    private void showProgressDialogToGetMovieById() {
        mProgressBarGetMovieInfo = new ProgressDialog(this);
        mProgressBarGetMovieInfo.setTitle(R.string.app_name);
        mProgressBarGetMovieInfo.setMessage(getString(R.string.search_movie_get_full_info));
        mProgressBarGetMovieInfo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBarGetMovieInfo.show();
    }


    /* item click listener */

    @Override
    public void onClickAtPosition(View view, int position, MovieShortInfo item) {
        if (requestOmdbApiMovieById(item))
            showProgressDialogToGetMovieById();
    }


    /* text watcher */

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
            mCurrentPageIndex = 1;
            mClearSearchTextButton.setVisibility(View.GONE);
        } else {
            mClearSearchTextButton.setVisibility(View.VISIBLE);
        }
    }


    /* keyboard enter button click action */

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            executeSearchMoviesByTitlePerPage();
            return true;
        }
        return false;
    }


    /* connectivity broadcast receiver */

    private void registerConnectivityBroadcastReceiver() {
        registerReceiver(mConnectivityStatusBroadcastReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void unregisterConnectivityBroadcastReceiver() {
        unregisterReceiver(mConnectivityStatusBroadcastReceiver);
    }

    public class ConnectivityStatusBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceived();
        }

        public void onBroadcastReceived() {
            if (isConnected())
                hideNoConnectivitySnackbarMessage();
            else
                showNoConnectivitySnackbarMessage();
        }

        public boolean isConnected() {
            return NetworkStatusProvider.isConnected(MovieSearchingActivity.this);
        }
    }


    /* Espresso UI Tests */
    private CountingIdlingResource mIdlingResource;

    private void decrementIdlingResource() {
        if (mIdlingResource != null)
            mIdlingResource.decrement();
    }

    private void incrementIdlingResource() {
        if (mIdlingResource != null)
            mIdlingResource.increment();
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new CountingIdlingResource("MOVIES_SEARCH_LIST_LOADER");
        }
        return mIdlingResource;
    }

    @VisibleForTesting
    @NonNull
    public ConnectivityStatusBroadcastReceiver getNetworkStatusBroadcastReceiver() {
        return mConnectivityStatusBroadcastReceiver;
    }
}
