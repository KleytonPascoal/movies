package com.kleytonpascoal.movies.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.fragment.MovieFieldsEditFragment;
import com.kleytonpascoal.movies.helper.LoadImageHelper;
import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.service.MoviePersistenceService;
import com.kleytonpascoal.movies.widget.ToolbarTitleSubTitle;

public class MovieEditActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    private static final String CLASS_NAME = MovieEditActivity.class.getName();
    private static final String TAG = MovieEditActivity.class.getSimpleName();

    public static final String PARAM_MOVIE = CLASS_NAME + ".MOVIE_PARAM";
    public static final String PARAM_MOVIE_IS_NEW = CLASS_NAME + ".MOVIE_PARAM_IS_NEW";

    private boolean isHideToolbarView;
    private ToolbarTitleSubTitle mTitleSubTitleView;
    private ToolbarTitleSubTitle mFloatTitleSubTitleView;

    private boolean showMenuEditMovieInToolbar;

    private Movie mMovie;
    private MovieFieldsEditFragment mMovieFieldsFrag;

    private MovieEditedStatusBroadcastReceiver mMovieEditedStatusBroadcastReceiver =
            new MovieEditedStatusBroadcastReceiver();
    private boolean reCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_edit);

        extractInitialValuesForIntent(savedInstanceState);

        onCreateToolbar();
        onCreateViews();

        setToolbarTitleAndSubtitle();
        onCreateMoviePosterImageView();

        reCreated = savedInstanceState != null;

        onCreateFragment(savedInstanceState);
    }

    @Override
    protected void onStart() {
        registerLocalBroadcastReceiverToEditedMovie();
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterLocalBroadcastReceiverToEditedMovie();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (reCreated)
            finish();
        else
            super.onBackPressed();
    }


    private void extractInitialValuesForIntent(@Nullable Bundle savedInstanceState) {
        showMenuEditMovieInToolbar = !getIntent().getBooleanExtra(PARAM_MOVIE_IS_NEW, false);

        if (getIntent().hasExtra(PARAM_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(PARAM_MOVIE);
        } else if (savedInstanceState != null && savedInstanceState.containsKey(PARAM_MOVIE)) {
            mMovie = savedInstanceState.getParcelable(PARAM_MOVIE);
        } else {
            Toast.makeText(this, getString(R.string.msg_error_movie_desnt_load), Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    /* on create views */

    private void onCreateToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.movie_edit_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        Log.d(TAG, "onCreateToolbar");
    }

    private void onCreateViews() {
        FloatingActionButton saveMovieFAB = (FloatingActionButton) findViewById(R.id.movie_edit_fab_save_movie);
        saveMovieFAB.setVisibility(showMenuEditMovieInToolbar ? View.GONE : View.VISIBLE);
    }

    private void onCreateFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mMovieFieldsFrag = MovieFieldsEditFragment.newInstance(mMovie);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .add(R.id.movie_edit_container, mMovieFieldsFrag)
                    .commit();
        } else {
            mMovieFieldsFrag = (MovieFieldsEditFragment) getSupportFragmentManager().
                    findFragmentById(R.id.movie_edit_container);
        }
    }

    private void setToolbarTitleAndSubtitle() {
        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.movie_edit_toolbar_layout);
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitle(" ");

            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(this);
                mTitleSubTitleView = (ToolbarTitleSubTitle) findViewById(R.id.toolbar_title_subtitle_view);
                mFloatTitleSubTitleView = (ToolbarTitleSubTitle) findViewById(R.id.toolbar_title_subtitle_float_view);
                mTitleSubTitleView.setTitleSubtitle(mMovie.title, mMovie.year);
                mFloatTitleSubTitleView.setTitleSubtitle(mMovie.title, mMovie.year);
            }

        } else {
            Toolbar toolbar = (Toolbar) findViewById(R.id.movie_edit_toolbar);
            setTitle(mMovie.title);
            toolbar.setSubtitle(mMovie.year);
        }

        Log.d(TAG, "setToolbarTitleAndSubtitle");
    }

    private void onCreateMoviePosterImageView() {
        LoadImageHelper.loadImageFromUrl(this, mMovie.poster, (ImageView) findViewById(R.id.edit_movie_poster));
    }


    /* save fab action listener in activity_movie_edit.xml */

    public void onClickSaveMovieFAB(View view) {
        startSaveMovieService();
    }


    /* menu methods */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_edit, menu);
        menu.setGroupVisible(R.id.group_edit_actions, showMenuEditMovieInToolbar);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !reCreated) {
                    supportFinishAfterTransition();
                } else {
                    finish();
                }
                return true;
            case R.id.menu_movie_edit_save_action:
                startSaveMovieService();
                return true;
            case R.id.menu_movie_edit_delete_action:
                startDeleteMovieService();
                return true;
            default:
                break;
        }
        return false;
    }


    /* start persistence service methods */

    private void startSaveMovieService() {
        mMovie = mMovieFieldsFrag.getCurrentMovie();
        MoviePersistenceService.startActionSaveOrUpdateMovie(this, mMovie);
    }

    private void startDeleteMovieService() {
        MoviePersistenceService.startActionDeleteMovie(this, mMovie);
    }


    /* title and subtitle offset position change */

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {

        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            mTitleSubTitleView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;
        } else if (percentage < 1f && !isHideToolbarView) {
            mTitleSubTitleView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }


    /* local broadcast receiver to movie edited*/

    private void registerLocalBroadcastReceiverToEditedMovie() {
        final IntentFilter intentFilter = new IntentFilter(MoviePersistenceService.ACTION_SAVE_OR_UPDATE_MOVIE_DONE);
        intentFilter.addAction(MoviePersistenceService.ACTION_DELETE_MOVIE_DONE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMovieEditedStatusBroadcastReceiver,
                intentFilter);
    }

    private void unregisterLocalBroadcastReceiverToEditedMovie() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMovieEditedStatusBroadcastReceiver);
    }

    class MovieEditedStatusBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            startActivity(new Intent(MovieEditActivity.this, MoviesActivity.class));
            finish();
        }
    }
}
