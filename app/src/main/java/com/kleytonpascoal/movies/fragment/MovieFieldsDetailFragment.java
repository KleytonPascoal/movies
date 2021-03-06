package com.kleytonpascoal.movies.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.activity.MovieDetailActivity;
import com.kleytonpascoal.movies.model.Movie;

import static com.kleytonpascoal.movies.activity.MovieDetailActivity.EXTRA_MOVIE;

/**
 *
 */
public class MovieFieldsDetailFragment extends Fragment {

    public interface MovieFieldsDetailListener {
        void showMovieWebsite();
    }

    private static final String TAG = MovieFieldsDetailFragment.class.getSimpleName();

    private TextView moviePlot;
    private TextView movieGender;
    private TextView movieRuntime;
    private TextView movieAwards;
    private TextView movieImdbVotes;
    private TextView movieWebsite;
    private TextView movieActors;
    private TextView movieRating;
    private TextView movieWriter;
    private TextView movieDirector;
    private TextView movieProduction;
    private TextView movieBoxOffice;

    private Movie mMovie;

    private MovieFieldsDetailListener mFragmentListener;

    public static Fragment newInstance(Movie mMovie) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(EXTRA_MOVIE, mMovie);
        MovieFieldsDetailFragment fragment = new MovieFieldsDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public MovieFieldsDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractMovieParameter();

        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_fields_detail, container, false);
        onCreateViews(rootView);
        setValuesToViews();

        Log.d(TAG, "onCreateView");
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentListener = (MovieDetailActivity) context;
    }

    private void extractMovieParameter() {
        if (getArguments().containsKey(EXTRA_MOVIE)) {
            mMovie = getArguments().getParcelable(EXTRA_MOVIE);
        }

        Log.d(TAG, "extractMovieParameter");
    }

    private void onCreateViews(View rootView) {
        moviePlot = (TextView) rootView.findViewById(R.id.movie_detail_plot);
        movieGender = (TextView) rootView.findViewById(R.id.movie_detail_gender);
        movieRuntime = (TextView) rootView.findViewById(R.id.movie_detail_runtime);
        movieAwards = (TextView) rootView.findViewById(R.id.movie_detail_awards);
        movieImdbVotes = (TextView) rootView.findViewById(R.id.movie_detail_imdb_votes);
        movieWebsite = (TextView) rootView.findViewById(R.id.movie_detail_website);
        movieActors = (TextView) rootView.findViewById(R.id.movie_detail_actors);
        movieRating = (TextView) rootView.findViewById(R.id.movie_detail_rating);
        movieWriter = (TextView) rootView.findViewById(R.id.movie_detail_writer);
        movieDirector = (TextView) rootView.findViewById(R.id.movie_detail_director);
        movieProduction = (TextView) rootView.findViewById(R.id.movie_detail_production);
        movieBoxOffice = (TextView) rootView.findViewById(R.id.movie_detail_box_office);
        movieWebsite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mFragmentListener.showMovieWebsite();
            }
        });

        Log.d(TAG, "onCreateViews");
    }

    private void setValuesToViews() {
        moviePlot.setText(mMovie.plot);
        movieGender.setText(mMovie.genre);
        movieRuntime.setText(mMovie.runtime);
        movieAwards.setText(mMovie.awards);
        movieRating.setText(String.valueOf(mMovie.imdbRating));
        movieImdbVotes.setText(mMovie.imdbVotes);
        movieWebsite.setText(mMovie.website);
        movieActors.setText(mMovie.actors);
        movieWriter.setText(mMovie.writer);
        movieDirector.setText(mMovie.director);
        movieProduction.setText(mMovie.production);
        movieBoxOffice.setText(mMovie.boxOffice);

        Log.d(TAG, "setValuesToViews");
    }
}
