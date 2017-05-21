package com.kleytonpascoal.movies.adapter;

import com.kleytonpascoal.movies.R;

/**
 * Created by kleyton on 09/05/17.
 */

public class MovieGridRecyclerViewAdapter extends MovieCollectionRecyclerViewAdapter {

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.movie_grid_item;
    }
}
