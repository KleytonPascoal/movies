package com.kleytonpascoal.movies.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.j256.ormlite.stmt.PreparedQuery;
import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.adapter.BaseCursorRecyclerViewAdapter;
import com.kleytonpascoal.movies.adapter.MoviesViewsPagerAdapter;
import com.kleytonpascoal.movies.loader.MovieOrmCursorLoader;
import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.persistence.DatabaseHelper;
import com.kleytonpascoal.movies.service.MoviePersistenceService;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class MoviesActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        ViewPager.OnPageChangeListener,
        ActionMode.Callback,
        BaseCursorRecyclerViewAdapter.OnItemClickAtPositionListener<Movie>,
        BaseCursorRecyclerViewAdapter.OnItemLongClickAtPositionListener<Movie>{

    private static final String TAG = MoviesActivity.class.getSimpleName();

    private static final int MOVIES_LOADER = 10;
    private static final String MOVIES_VIEW_TYPE = TAG + ".MOVIES_VIEW_TYPE";
    private static final String MOVIES_CURRENT_FILTER = TAG + ".MOVIES_CURRENT_FILTER";

    /**
     * The pager widget, which handles animation and allows swiping horizontally to movies views.
     */
    private ViewPager mMoviesViewsPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private MoviesViewsPagerAdapter mMoviesViewsPagerAdapter;

    private boolean isLoadingMovieDone;

    private Toolbar mToolbar;

    private BottomNavigationView mMoviesViewsButtonNavigation;

    private Cursor mMoviesCursor;
    private MovieOrmCursorLoader mMovieOrmCursorLoader;
    private PreparedQuery<Movie> mPreparedQuery;
    private DatabaseHelper mDbHelper;

    private String mCurrentFilter;

    private SaveOrUpdateMovieLocalBroadcastReceiver mMovieEditedLocalBroadcastReceiver =
            new SaveOrUpdateMovieLocalBroadcastReceiver();

    private int mCurrentBottomNavigationItemId;

    private int[] mMoviesViewsBottomNavigationViewOrder = {
            R.id.movies_views_navigation_item_carousel_view,
            R.id.movies_views_navigation_item_list_view,
            R.id.movies_views_navigation_item_grid_view
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnBottomNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == mCurrentBottomNavigationItemId)
                return true;

            mCurrentBottomNavigationItemId = item.getItemId();
            switch (mCurrentBottomNavigationItemId) {
                case R.id.movies_views_navigation_item_carousel_view:
                    mMoviesViewsPager.setCurrentItem(MoviesViewsPagerAdapter.MOVIES_CAROUSEL_PAGE_INDEX ,false);
                    break;
                case R.id.movies_views_navigation_item_list_view:
                    mMoviesViewsPager.setCurrentItem(MoviesViewsPagerAdapter.MOVIES_LIST_PAGE_INDEX ,false);
                    break;
                case R.id.movies_views_navigation_item_grid_view:
                    mMoviesViewsPager.setCurrentItem(MoviesViewsPagerAdapter.MOVIES_GRID_PAGE_INDEX ,false);
                    break;
            }
            swapCursorMoviesViewPage(mMoviesCursor, mPreparedQuery);

            return true;
        }
    };

    private ActionMode mActionMode;
    private FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        setupInitialValues(savedInstanceState);
        createOrmLiteDatabaseHelper();

        onCreateTopToolbar();
        onCreateMoviesViewsViewPager();
        onCreateButtonNavigationView();
        onCreateAddMovieFloatingButton();

        onCreateSupportLoaderManager();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveValuesToRestore(outState);
        //super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        registerLocalBroadcastReceiverForMovieEdited();
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterLocalBroadcastReceiverForMovieEdited();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isLoadingMovieDone) {
            mMovieOrmCursorLoader.cancelLoadInBackground();
        }

        releaseOrmLiteDatabaseHelper();
    }

    @Override
    public void onBackPressed() {

        if (!tryOnBackPressedMoviesViewsViewPager())
            super.onBackPressed();

    }

    private boolean tryOnBackPressedMoviesViewsViewPager() {
        if (mMoviesViewsPager.getCurrentItem() != 0) {
            mMoviesViewsPager.setCurrentItem(mMoviesViewsPager.getCurrentItem() - 1);
            return true;
        }
        return false;
    }


    /* menus methods */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies_list, menu);

        final MenuItem searchMenuItem = menu.findItem(R.id.menu_search_movie_action);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint(getString(R.string.search_movie_hint));
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_context_movies_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        Movie selectedMovie = (Movie) mode.getTag();

        switch (item.getItemId()) {

            case R.id.menu_context_edit_movie_action:
                startActivity(new Intent(this, MovieEditActivity.class).
                        putExtra(MovieEditActivity.PARAM_MOVIE, selectedMovie));
                mode.finish();
                return true;

            case R.id.menu_context_delete_movie_action:
                MoviePersistenceService.startActionDeleteMovie(this, selectedMovie);
                mode.finish();
                return true;

            default:
                return false;
        }
    }

    // Called when the user exits the action mode
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
    }


    /* search action listener */

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mCurrentFilter = !TextUtils.isEmpty(newText) ? newText : null;
        restartCursorLoader();
        return true;
    }


    /* page scroll events listeners */

    @Override
    public void onPageSelected(int position) {
        mCurrentBottomNavigationItemId = mMoviesViewsBottomNavigationViewOrder[position];
        mMoviesViewsButtonNavigation.setSelectedItemId(mCurrentBottomNavigationItemId);
        swapCursorMoviesViewPage(mMoviesCursor, mPreparedQuery);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
    @Override
    public void onPageScrollStateChanged(int state) {}


    /* loader callback methods */

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (mCurrentFilter == null) {
            mPreparedQuery = mDbHelper.prepareQueryAllMovies();
        } else {
            mPreparedQuery = mDbHelper.prepareQueryMoviesTitleContains(mCurrentFilter);
        }
        mMovieOrmCursorLoader = new MovieOrmCursorLoader(this, mDbHelper.getMovieDao(), mPreparedQuery);

        isLoadingMovieDone = false;

        return mMovieOrmCursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMoviesCursor = data;
        swapCursorMoviesViewPage(mMoviesCursor, mPreparedQuery);
        isLoadingMovieDone = true;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swapCursorMoviesViewPage(null, null);

        isLoadingMovieDone = true;
    }


    /* click list item listener */

    @Override
    public void onClickAtPosition(View view, int position, Movie item) {
        showClickedMovieItemOnMovieDetailActivity(view, item);
    }

    @Override
    public void onLongClickAtPosition(View view, int position, Movie item) {
        showActionModeContextMenu(view, item);
    }


    /* ORM database helper methods */

    private void createOrmLiteDatabaseHelper() {
        mDbHelper = DatabaseHelper.getHelper(this);
    }

    private void releaseOrmLiteDatabaseHelper() {
        mDbHelper.releaseHelper();
    }


    /* onCreate views methods */

    private void onCreateTopToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        mToolbar.setTitle(getTitle());
    }

    private void onCreateMoviesViewsViewPager() {
        mMoviesViewsPager = (ViewPager) findViewById(R.id.movies_views_pager);
        mMoviesViewsPagerAdapter = new MoviesViewsPagerAdapter(getSupportFragmentManager());
        mMoviesViewsPager.setAdapter(mMoviesViewsPagerAdapter);
        mMoviesViewsPager.addOnPageChangeListener(this);
    }

    private void onCreateAddMovieFloatingButton() {
        mFab = (FloatingActionButton) findViewById(R.id.fab_add_new_movie);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MoviesActivity.this, MovieSearchingActivity.class));
            }
        });
    }

    private void onCreateButtonNavigationView() {
        mMoviesViewsButtonNavigation = (BottomNavigationView) findViewById(R.id.movies_views_navigation_view);
        mMoviesViewsButtonNavigation.setOnNavigationItemSelectedListener(mOnBottomNavigationItemSelectedListener);
    }

    private void onCreateSupportLoaderManager() {
        if (getSupportLoaderManager().getLoader(MOVIES_LOADER) == null)
            getSupportLoaderManager().initLoader(MOVIES_LOADER, null, this);
        else
            restartCursorLoader();
    }

    private void restartCursorLoader() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }


    /* private methods */

    private void setupInitialValues(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentBottomNavigationItemId = savedInstanceState.getInt(MOVIES_VIEW_TYPE);
            mCurrentFilter = savedInstanceState.getString(MOVIES_CURRENT_FILTER);
        } else {
            mCurrentBottomNavigationItemId = R.id.movies_views_navigation_item_carousel_view;
            mCurrentFilter = null;
        }
    }

    private void saveValuesToRestore(@NonNull Bundle outState) {
        outState.putInt(MOVIES_VIEW_TYPE, mCurrentBottomNavigationItemId);
        if (mCurrentFilter != null)
            outState.putString(MOVIES_CURRENT_FILTER, mCurrentFilter);
    }

    private void swapCursorMoviesViewPage(@Nullable Cursor moviesCursor,
                                          @Nullable PreparedQuery<Movie> preparedQuery) {
        for (int i = 0; i < mMoviesViewsBottomNavigationViewOrder.length; i++) {
            ((MoviesViewsPagerAdapter.MoviesViewsSwappableCursor) mMoviesViewsPagerAdapter.
                    getItem(i)).
                    swapCursor(moviesCursor, preparedQuery);
        }
    }

    private void showClickedMovieItemOnMovieDetailActivity(View view, Movie item) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, item);

        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(this,
                            Pair.create(view.findViewById(R.id.movie_poster_view),
                                    "movie_poster_view_transition"));

            ActivityCompat.startActivity(this, intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private boolean showActionModeContextMenu(View view, Movie item) {
        if (mActionMode != null) {
            return false;
        }
        mActionMode = startSupportActionMode(this);
        if (mActionMode != null) {
            mActionMode.setTitle(item.title);
            mActionMode.setTag(item);
        }

        view.setSelected(true);

        return true;
    }


    /* local broadcasts receivers methods */

    private void registerLocalBroadcastReceiverForMovieEdited() {
        IntentFilter intentFilter = new IntentFilter(MoviePersistenceService.ACTION_SAVE_OR_UPDATE_MOVIE_DONE);
        intentFilter.addAction(MoviePersistenceService.ACTION_DELETE_MOVIE_DONE);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMovieEditedLocalBroadcastReceiver, intentFilter);
    }

    private void unregisterLocalBroadcastReceiverForMovieEdited() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMovieEditedLocalBroadcastReceiver);
    }

    private void saveOrUpdateMovie(Movie deletedMovie) {
        MoviePersistenceService.startActionSaveOrUpdateMovie(this, deletedMovie);
    }

    class SaveOrUpdateMovieLocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(MoviePersistenceService.EXTRA_DELETED_MOVIE_STATUS, false)) {
                createUndoActionForDeletedMovie(intent);
                restartCursorLoader();
            } else if (intent.getBooleanExtra(MoviePersistenceService.EXTRA_SAVED_OR_UPDATED_MOVIE_STATUS, false)) {
                createEditActionForMovie(intent);
                restartCursorLoader();
            } else {
                Toast.makeText(MoviesActivity.this, R.string.problem_message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void createUndoActionForDeletedMovie(Intent intent) {
        final Movie movie = intent.getParcelableExtra(MoviePersistenceService.EXTRA_MOVIE_PARAM);
        if (movie != null) {
            Snackbar.make(findViewById(R.id.movies_layout),
                getString(R.string.deleted_movie_with_title, movie.title), Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveOrUpdateMovie(movie);
                    }
                }).show();
        } else {
            Toast.makeText(MoviesActivity.this, R.string.problem_message, Toast.LENGTH_LONG).show();
        }
    }

    private void createEditActionForMovie(Intent intent) {
        Movie movie = intent.getParcelableExtra(MoviePersistenceService.EXTRA_MOVIE_PARAM);
        if (movie != null) {
            Toast.makeText(MoviesActivity.this,
                    getString(R.string.movie_saved_successfully_with_title, movie.title),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MoviesActivity.this, R.string.problem_message, Toast.LENGTH_LONG).show();
        }
    }

}
