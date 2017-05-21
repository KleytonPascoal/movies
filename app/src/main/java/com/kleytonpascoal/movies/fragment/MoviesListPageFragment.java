package com.kleytonpascoal.movies.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.stmt.PreparedQuery;
import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.activity.MoviesActivity;
import com.kleytonpascoal.movies.adapter.MovieCollectionRecyclerViewAdapter;
import com.kleytonpascoal.movies.adapter.MoviesViewsPagerAdapter;
import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.widget.BaseRecyclerView;

/**
 * Created by kleyton on 10/05/17.
 */

public class MoviesListPageFragment extends Fragment
        implements MoviesViewsPagerAdapter.MoviesViewsSwappableCursor {

    private BaseRecyclerView mMoviesListRecyclerView;
    private MovieCollectionRecyclerViewAdapter mMoviesListAdapter;

    public MoviesListPageFragment() {
        mMoviesListAdapter = new MovieCollectionRecyclerViewAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_movies_list_view, container, false);
        onCreateMovieListView(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        mMoviesListAdapter.setContext(context);
        mMoviesListAdapter.setItemClickAtPositionListener((MoviesActivity) context);
        mMoviesListAdapter.setItemLongClickAtPositionListener((MoviesActivity) context);
        super.onAttach(context);
    }

    private void onCreateMovieListView(@NonNull ViewGroup rootView) {
        mMoviesListRecyclerView = (BaseRecyclerView) rootView.findViewById(R.id.movies_list);
        mMoviesListRecyclerView.setHasFixedSize(true);
        mMoviesListRecyclerView.setAdapter(mMoviesListAdapter);

        LinearLayoutManager moviesListLayoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false);
        mMoviesListRecyclerView.setLayoutManager(moviesListLayoutManager);
    }

    @Override
    public void swapCursor(Cursor cursor, PreparedQuery<Movie> moviePreparedQuery) {
        mMoviesListAdapter.swapCursor(cursor, moviePreparedQuery);
    }
}
