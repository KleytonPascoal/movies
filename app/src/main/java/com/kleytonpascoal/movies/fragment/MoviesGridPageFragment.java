package com.kleytonpascoal.movies.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.stmt.PreparedQuery;
import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.activity.MoviesActivity;
import com.kleytonpascoal.movies.adapter.MovieGridRecyclerViewAdapter;
import com.kleytonpascoal.movies.adapter.MoviesViewsPagerAdapter;
import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.widget.BaseRecyclerView;

/**
 * Created by kleyton on 10/05/17.
 */

public class MoviesGridPageFragment extends Fragment
        implements MoviesViewsPagerAdapter.MoviesViewsSwappableCursor {

    private BaseRecyclerView mMoviesGridRecyclerView;
    private MovieGridRecyclerViewAdapter mMoviesGridAdapter;

    public MoviesGridPageFragment() {
        mMoviesGridAdapter = new MovieGridRecyclerViewAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_movies_grid_view, container, false);
        onCreateMovieGridView(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        mMoviesGridAdapter.setContext(context);
        mMoviesGridAdapter.setItemClickAtPositionListener((MoviesActivity) context);
        mMoviesGridAdapter.setItemLongClickAtPositionListener((MoviesActivity) context);
        super.onAttach(context);
    }

    private void onCreateMovieGridView(@NonNull ViewGroup rootView) {
        mMoviesGridRecyclerView = (BaseRecyclerView) rootView.findViewById(R.id.movies_grid);
        mMoviesGridRecyclerView.setHasFixedSize(true);
        mMoviesGridRecyclerView.setAdapter(mMoviesGridAdapter);

        GridLayoutManager moviesGridLayoutManager = new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.movies_grid_view_columns),
                GridLayoutManager.VERTICAL, false);
        mMoviesGridRecyclerView.setLayoutManager(moviesGridLayoutManager);
    }

    @Override
    public void swapCursor(Cursor cursor, PreparedQuery<Movie> moviePreparedQuery) {
        mMoviesGridAdapter.swapCursor(cursor, moviePreparedQuery);
    }
}
