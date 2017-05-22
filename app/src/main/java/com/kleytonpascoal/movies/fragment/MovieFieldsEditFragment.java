package com.kleytonpascoal.movies.fragment;

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
    private EditText movieVotes;
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
        moviePlot.setText(mMovie.overview);
        movieGender.setText(mMovie.genre);
        movieRuntime.setText(mMovie.runtime);
        movieRating.setText(String.valueOf(mMovie.voteAverage));
        movieVotes.setText(mMovie.voteCount);
        movieProduction.setText(mMovie.production);
        movieBoxOffice.setText(mMovie.budget);
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
        movieVotes = (EditText) rootView.findViewById(R.id.movie_edit_votes);
        movieProduction = (EditText) rootView.findViewById(R.id.movie_edit_production);
        movieBoxOffice = (EditText) rootView.findViewById(R.id.movie_edit_box_office);
    }

    public Movie getCurrentMovie() {
        mMovie.overview = moviePlot.getText().toString().trim();
        mMovie.genre = movieGender.getText().toString().trim();
        mMovie.runtime = movieRuntime.getText().toString().trim();
        mMovie.voteAverage = movieRating.getText().toString().trim();
        mMovie.voteCount = movieVotes.getText().toString().trim();
        mMovie.production = movieProduction.getText().toString().trim();
        mMovie.budget = movieBoxOffice.getText().toString().trim();
        return mMovie;
    }
}
