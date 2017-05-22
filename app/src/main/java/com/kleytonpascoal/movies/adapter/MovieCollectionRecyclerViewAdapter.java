package com.kleytonpascoal.movies.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.helper.LoadImageHelper;
import com.kleytonpascoal.movies.model.Movie;

/**
 * Created by kleyton on 09/05/17.
 */

public class MovieCollectionRecyclerViewAdapter extends BaseCursorRecyclerViewAdapter<Movie, MovieCollectionRecyclerViewAdapter.ViewHolder> {

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Movie movie = getItemFromCursorAt(position);
        if (movie != null) {
            final String posterUrl = movie.posterPath;

            if (!TextUtils.isEmpty(posterUrl))
                LoadImageHelper.loadImageFromUrl(mContext, posterUrl, holder.mPosterImageView);

            holder.mItem = movie;
            holder.mTitleView.setText(movie.title);
            holder.mYearView.setText(movie.releaseDate);
        }
    }

    @Override
    protected MovieCollectionRecyclerViewAdapter.ViewHolder onCreateCustomViewHolder(final View view) {
        return new ViewHolder(view);
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.movie_list_item;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mPosterImageView;
        public final TextView mTitleView;
        public final TextView mYearView;
        public Movie mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPosterImageView = (ImageView) view.findViewById(R.id.movie_poster_view);
            mTitleView = (TextView) view.findViewById(R.id.movie_list_item_title);
            mYearView = (TextView) view.findViewById(R.id.movie_list_item_year);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem + "'";
        }
    }
}
