package com.kleytonpascoal.movies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kleytonpascoal.movies.model.MovieShortInfo;

/**
 * Created by kleyton on 09/05/17.
 */

public class MovieShortInfoListRecyclerViewAdapter extends BaseListRecyclerViewAdapter<MovieShortInfo, MovieShortInfoListRecyclerViewAdapter.ViewHolder> {

    @Override
    protected ViewHolder onCreateCustomViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return android.R.layout.simple_list_item_2;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final MovieShortInfo movie = mValues.get(position);
        if (movie != null) {
            holder.mItem = movie;
            holder.mTitleView.setText(movie.title);
            holder.mYearView.setText(movie.releaseDate);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mTitleView;
        public final TextView mYearView;
        public MovieShortInfo mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(android.R.id.text1);
            mYearView = (TextView) view.findViewById(android.R.id.text2);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
