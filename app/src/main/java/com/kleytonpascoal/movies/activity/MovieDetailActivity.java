package com.kleytonpascoal.movies.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.fragment.MovieFieldsDetailFragment;
import com.kleytonpascoal.movies.helper.LoadImageHelper;
import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.widget.ToolbarTitleSubTitle;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class MovieDetailActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener, MovieFieldsDetailFragment.MovieFieldsDetailListener {

    private static final String PKG = MovieDetailActivity.class.getName();
    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    public static final String EXTRA_MOVIE = PKG + ".extra.EXTRA_MOVIE";
    private Movie mMovie;

    private boolean isHideToolbarView;
    private ToolbarTitleSubTitle mTitleSubTitleView;
    private ToolbarTitleSubTitle mFloatTitleSubTitleView;

    private boolean reCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        extractMovieParameter(savedInstanceState);

        onCreateToolbar();
        onCreateFragment(savedInstanceState);
        onCreateMoviePosterImageView();

        reCreated = savedInstanceState != null;

        setToolbarTitleAndSubtitle();

        Log.d(TAG, "onCreate");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_MOVIE, mMovie);

        Log.d(TAG, "onSaveInstanceState: " + mMovie);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMovie = savedInstanceState.getParcelable(EXTRA_MOVIE);

        Log.d(TAG, "onRestoreInstanceState: " + mMovie);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !reCreated) {
                supportFinishAfterTransition();
            } else {
                finish();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (reCreated)
            finish();
        else
            super.onBackPressed();
    }


    /* fab click listener to edit */

    public void onClickStartEditMovie(View view) {
        final Intent intent = new Intent(this, MovieEditActivity.class).
                putExtra(MovieEditActivity.PARAM_MOVIE, mMovie);
        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(this,
                            Pair.create(findViewById(R.id.movie_detail_poster),
                                    "movie_poster_view_transition"));

            ActivityCompat.startActivity(this, intent, options.toBundle());
        } else {
            startActivity(intent);
        }

        finish();

        Log.d(TAG, "onClickStartEditMovie: " + mMovie);
    }

    private void extractMovieParameter(Bundle savedInstanceState) {
        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        } else if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(EXTRA_MOVIE);
        }

        Log.d(TAG, "extractMovieParameter: " + mMovie);
    }

    private void onCreateFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .add(R.id.movie_detail_container, MovieFieldsDetailFragment.newInstance(mMovie))
                    .commit();
        }

        Log.d(TAG, "onCreateFragment");
    }

    private void onCreateToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.movie_detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        Log.d(TAG, "onCreateToolbar");
    }

    private void setToolbarTitleAndSubtitle() {
        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.movie_detail_toolbar_layout);
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitle(" ");

            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(this);
                mTitleSubTitleView = (ToolbarTitleSubTitle) findViewById(R.id.toolbar_title_subtitle_view);
                mFloatTitleSubTitleView = (ToolbarTitleSubTitle) findViewById(R.id.toolbar_title_subtitle_float_view);
                mTitleSubTitleView.setTitleSubtitle(mMovie.title, mMovie.releaseDate);
                mFloatTitleSubTitleView.setTitleSubtitle(mMovie.title, mMovie.releaseDate);
            }

        } else {
            Toolbar toolbar = (Toolbar) findViewById(R.id.movie_detail_toolbar);
            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
                actionBar.setDisplayShowTitleEnabled(false);
            toolbar.setTitle(mMovie.title);
            toolbar.setSubtitle(mMovie.releaseDate);
        }

        Log.d(TAG, "setToolbarTitleAndSubtitle");
    }

    private void onCreateMoviePosterImageView() {
        LoadImageHelper.loadImageFromUrl(this, mMovie.posterPath, (ImageView) findViewById(R.id.movie_detail_poster));
    }


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

    @Override
    public void showMovieWebsite() {
        startActivityForResult(new Intent(this, WebPageActivity.class).
                        putExtra(WebPageActivity.EXTRA_MOVIE, mMovie), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            mMovie = data.getParcelableExtra(WebPageActivity.EXTRA_MOVIE);
        }
    }
}
