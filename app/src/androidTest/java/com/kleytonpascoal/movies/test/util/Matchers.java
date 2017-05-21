package com.kleytonpascoal.movies.test.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by kleyton on 12/05/17.
 */

public class Matchers {

    public static Matcher<View> hasItems() {

        return new TypeSafeMatcher<View>() {

            private RecyclerView mRecyclerView;

            @Override
            protected boolean matchesSafely(View item) {
                mRecyclerView = (RecyclerView) item;
                return mRecyclerView.getAdapter().getItemCount() > 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has items in RecyclerView adapter");
            }
        };
    }

    public static Matcher<View> parentViewWithChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with "+childPosition+" child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
            }
        };
    }
}
