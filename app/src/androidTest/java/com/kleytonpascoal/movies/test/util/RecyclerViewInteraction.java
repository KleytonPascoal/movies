package com.kleytonpascoal.movies.test.util;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.util.HumanReadables;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Matcher;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;

/**
 * Created by kleyton on 18/05/17.
 */

public class RecyclerViewInteraction<T> {

    private Matcher<View> viewMatcher;
    private List<T> items;

    private RecyclerViewInteraction(Matcher<View> viewMatcher) {
        this.viewMatcher = viewMatcher;
    }

    public static <T> RecyclerViewInteraction<T> onRecyclerView(Matcher<View> viewMatcher) {
        return new RecyclerViewInteraction<>(viewMatcher);
    }

    public RecyclerViewInteraction<T> withItems(List<T> items) {
        this.items = items;
        return this;
    }

    public RecyclerViewInteraction<T> check(ItemViewAssertion<T> itemViewAssertion) {
        for (int i = 0; i < items.size(); i++) {
            onView(viewMatcher)
                    .perform(scrollToPosition(i))
                    .check(new RecyclerItemViewAssertion<>(i, items.get(i), itemViewAssertion));
        }
        return this;
    }

    public interface ItemViewAssertion<T> {
        void check(T item, View view, NoMatchingViewException e);
    }

    public class RecyclerItemViewAssertion<T> implements ViewAssertion {

        private int position;
        private T item;
        private ItemViewAssertion<T> itemViewAssertion;

        public RecyclerItemViewAssertion(int position, T item, ItemViewAssertion<T> itemViewAssertion) {
            this.position = position;
            this.item = item;
            this.itemViewAssertion = itemViewAssertion;
        }

        @Override
        public final void check(View view, NoMatchingViewException e) {
            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.ViewHolder viewHolderForPosition = recyclerView.findViewHolderForLayoutPosition(position);
            if (viewHolderForPosition == null) {
                throw (new PerformException.Builder())
                        .withActionDescription(toString())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new IllegalStateException("No view holder at position: " + position))
                        .build();
            } else {
                View viewAtPosition = viewHolderForPosition.itemView;
                itemViewAssertion.check(item, viewAtPosition, e);
            }
        }
    }
}
