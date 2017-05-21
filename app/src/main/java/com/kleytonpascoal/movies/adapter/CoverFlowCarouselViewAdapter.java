package com.kleytonpascoal.movies.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
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

public class CoverFlowCarouselViewAdapter extends BaseCursorRecyclerViewAdapter<Movie, CoverFlowCarouselViewAdapter.ViewHolder> {

    private static final String TAG = CoverFlowCarouselViewAdapter.class.getSimpleName();

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Movie movie = getItemFromCursorAt(position);
        if (movie != null) {
            final String posterUrl = movie.poster;

            if (!TextUtils.isEmpty(posterUrl))
                LoadImageHelper.loadImageFromUrl(mContext, posterUrl, holder.mPosterImageView);

            holder.mItem = movie;
            holder.mTitleView.setText(movie.title);
        }
    }

    @Override
    protected ViewHolder onCreateCustomViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.movie_carousel_item;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mPosterImageView;
        public final TextView mTitleView;
        public Movie mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPosterImageView = (ImageView) view.findViewById(R.id.movie_poster_view);
            mTitleView = (TextView) view.findViewById(R.id.carousel_movie_title);
        }

        @Override
        public String toString() {
            return mItem.toString();
        }
    }
}
