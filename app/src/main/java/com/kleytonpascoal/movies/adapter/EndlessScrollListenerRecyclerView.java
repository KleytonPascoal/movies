package com.kleytonpascoal.movies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by kleyton on 12/05/17.
 */

public abstract class EndlessScrollListenerRecyclerView extends RecyclerView.OnScrollListener {

    private final LinearLayoutManager mLayoutManager;
    private int currentPageIndex;
    private boolean isOnLoad = false;
    private int mMaxPageIndex;

    protected EndlessScrollListenerRecyclerView(@NonNull LinearLayoutManager layoutManager,
                                                @NonNull int startPageIndex) {
        mLayoutManager = layoutManager;
        currentPageIndex = startPageIndex;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int totalItemCount = mLayoutManager.getItemCount();
        int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

        if (!isOnLoad && totalItemCount - 1 == lastVisibleItem) {

            currentPageIndex++;

            if (currentPageIndex <= mMaxPageIndex) {
                isOnLoad = true;
                onLoading(currentPageIndex);
                isOnLoad = false;
            }
        }
    }


    public void setMaxPageIndex(int totalResults, int numItemReturned) {
        if (currentPageIndex == 1)
            mMaxPageIndex = (totalResults / numItemReturned) + (totalResults % numItemReturned != 0 ? 1 : 0);
    }

    /**
     * This method should always be called after be received items to the adapter.
     * Otherwise new loading don't happen.
     */
    public void loadingIsDone() {
        isOnLoad = false;
    }

    /**
     * Dispatch the operation to load data to be add in your adapter.
     * <p>
     * After be received items to the adapter, call the method {@link #loadingIsDone() loadingIsDone()},
     * otherwise new loading don't happen.
     *
     * @param currentPageIndex
     */
    public abstract void onLoading(int currentPageIndex);
}
