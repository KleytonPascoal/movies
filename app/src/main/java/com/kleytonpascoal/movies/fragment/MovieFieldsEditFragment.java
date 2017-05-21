package com.kleytonpascoal.movies.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.model.Movie;

import static com.kleytonpascoal.movies.activity.MovieEditActivity.PARAM_MOVIE;

/**
 *
 */
public class MovieFieldsEditFragment extends Fragment {

    private static final String TAG = MovieFieldsEditFragment.class.getSimpleName();

    private EditText moviePlot;
    private EditText movieGender;
    private EditText movieRuntime;
    private EditText movieRating;
    private EditText movieAwards;
    private EditText movieVotes;
    private EditText movieActors;
    private EditText movieWriter;
    private EditText movieDirector;
    private EditText movieProduction;
    private EditText movieBoxOffice;

    private Movie mMovie;

    public static MovieFieldsEditFragment newInstance(Movie mMovie) {
        MovieFieldsEditFragment fragment = new MovieFieldsEditFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(PARAM_MOVIE, mMovie);
        fragment.setArguments(arguments);
        return fragment;
    }

    public MovieFieldsEditFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractMovieParameter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_movie_fields_edit, container, false);
        onCreateViews(rootView);
        setMovieValuesToView();
        return rootView;
    }

    private void setMovieValuesToView() {
        moviePlot.setText(mMovie.plot);
        movieGender.setText(mMovie.genre);
        movieRuntime.setText(mMovie.runtime);
        movieRating.setText(String.valueOf(mMovie.imdbRating));
        movieAwards.setText(mMovie.awards);
        movieVotes.setText(mMovie.imdbVotes);
        movieActors.setText(mMovie.actors);
        movieWriter.setText(mMovie.writer);
        movieDirector.setText(mMovie.director);
        movieProduction.setText(mMovie.production);
        movieBoxOffice.setText(mMovie.boxOffice);
    }

    private void extractMovieParameter() {
        if (getArguments().containsKey(PARAM_MOVIE)) {
            mMovie = getArguments().getParcelable(PARAM_MOVIE);
        }
    }
    
    private void onCreateViews(View rootView) {
        moviePlot = (EditText) rootView.findViewById(R.id.movie_edit_plot);
        movieGender = (EditText) rootView.findViewById(R.id.movie_edit_gender);
        movieRuntime = (EditText) rootView.findViewById(R.id.movie_edit_runtime);
        movieRating = (EditText) rootView.findViewById(R.id.movie_edit_rating);
        movieAwards = (EditText) rootView.findViewById(R.id.movie_edit_awards);
        movieVotes = (EditText) rootView.findViewById(R.id.movie_edit_votes);
        movieActors = (EditText) rootView.findViewById(R.id.movie_edit_actors);
        movieWriter = (EditText) rootView.findViewById(R.id.movie_edit_writer);
        movieDirector = (EditText) rootView.findViewById(R.id.movie_edit_director);
        movieProduction = (EditText) rootView.findViewById(R.id.movie_edit_production);
        movieBoxOffice = (EditText) rootView.findViewById(R.id.movie_edit_box_office);
    }

    public Movie getCurrentMovie() {
        mMovie.plot = moviePlot.getText().toString().trim();
        mMovie.genre = movieGender.getText().toString().trim();
        mMovie.runtime = movieRuntime.getText().toString().trim();
        mMovie.imdbRating = movieRating.getText().toString().trim();
        mMovie.awards = movieAwards.getText().toString().trim();
        mMovie.imdbVotes = movieVotes.getText().toString().trim();
        mMovie.actors = movieActors.getText().toString().trim();
        mMovie.writer = movieWriter.getText().toString().trim();
        mMovie.director = movieDirector.getText().toString().trim();
        mMovie.production = movieProduction.getText().toString().trim();
        mMovie.boxOffice = movieBoxOffice.getText().toString().trim();
        return mMovie;
    }
}
