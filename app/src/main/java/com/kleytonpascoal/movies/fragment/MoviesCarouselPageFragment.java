package com.kleytonpascoal.movies.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.stmt.PreparedQuery;
import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.activity.MovieEditActivity;
import com.kleytonpascoal.movies.activity.MoviesActivity;
import com.kleytonpascoal.movies.adapter.CoverFlowCarouselViewAdapter;
import com.kleytonpascoal.movies.adapter.MoviesViewsPagerAdapter;
import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.service.MoviePersistenceService;
import com.kleytonpascoal.movies.widget.BaseRecyclerView;
import com.kleytonpascoal.movies.widget.CoverFlowCarouselView;

/**
 * Created by kleyton on 10/05/17.
 */

public class MoviesCarouselPageFragment extends Fragment
        implements MoviesViewsPagerAdapter.MoviesViewsSwappableCursor {

    private CoverFlowCarouselView mMoviesCarouselRecyclerView;
    private CoverFlowCarouselViewAdapter mMoviesCarouselAdapter;

    public MoviesCarouselPageFragment() {
        mMoviesCarouselAdapter = new CoverFlowCarouselViewAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_movies_carousel_view, container, false);
        onCreateMovieCarouselView(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        mMoviesCarouselAdapter.setContext(context);
        mMoviesCarouselAdapter.setItemClickAtPositionListener((MoviesActivity) context);
        mMoviesCarouselAdapter.setItemLongClickAtPositionListener((MoviesActivity) context);
        super.onAttach(context);
    }

    private void onCreateMovieCarouselView(@NonNull ViewGroup rootView) {
        mMoviesCarouselRecyclerView = (CoverFlowCarouselView) rootView.findViewById(R.id.movies_carousel);
        mMoviesCarouselRecyclerView.setHasFixedSize(true);
        mMoviesCarouselRecyclerView.setAdapter(mMoviesCarouselAdapter);

        LinearLayoutManager carouselLayoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mMoviesCarouselRecyclerView.setLayoutManager(carouselLayoutManager);

        getActivity().registerForContextMenu(mMoviesCarouselRecyclerView);
    }

    @Override
    public void swapCursor(Cursor cursor, PreparedQuery<Movie> moviePreparedQuery) {
        mMoviesCarouselAdapter.swapCursor(cursor, moviePreparedQuery);
    }
}
