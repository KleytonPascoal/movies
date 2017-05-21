package com.kleytonpascoal.movies.test.features.pages;

import android.annotation.SuppressLint;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.Espresso;
import android.support.test.filters.Suppress;
import android.widget.EditText;

import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.activity.MovieSearchingActivity;
import com.kleytonpascoal.movies.test.features.util.ActivityUtil;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Created by kleyton on 18/05/17.
 */

public class MovieSearchingPage extends BasePage {

    @VisibleForTesting
    public MovieSearchingPage() {
        onView(withId(R.id.activity_movie_searching)).check(matches(isDisplayed()));
        Espresso.registerIdlingResources(((MovieSearchingActivity) ActivityUtil.getActivityInstance()).getIdlingResource());
    }

    public void searchMovieByTitle(String title) {
        onView(withId(R.id.searching_bt)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText(title), pressImeActionButton());
    }

    @VisibleForTesting
    public MovieEditPage selectMovieToAddAt(int position) {
        onView(withId(R.id.searching_result_list)).perform(actionOnItemAtPosition(position, click()));
        Espresso.unregisterIdlingResources(((MovieSearchingActivity) ActivityUtil.getActivityInstance()).getIdlingResource());
        return new MovieEditPage();
    }

    public void checkShowResult() {
        onView(withId(R.id.movie_list_empty_search_msg)).check(matches(not(isDisplayed())));
        onView(withId(R.id.searching_result_list)).check(matches(isDisplayed()));
    }
}
