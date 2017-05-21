package com.kleytonpascoal.movies.test.features.pages;

import com.kleytonpascoal.movies.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by kleyton on 18/05/17.
 */

public class MovieEditPage extends BasePage {

    public MovieEditPage() {
        onView(withId(R.id.activity_movie_edit)).check(matches(isDisplayed()));
    }

    public MoviesPage saveNewMovie() {
        onView(withId(R.id.movie_edit_fab_save_movie)).perform(click());
        return new MoviesPage();
    }

    public MoviesPage menuActionDeleteMovie() {
        onView(withId(R.id.menu_movie_edit_delete_action)).perform(click());
        return new MoviesPage();
    }

    public MoviesPage menuActionSaveMovie() {
        onView(withId(R.id.menu_movie_edit_save_action)).perform(click());
        return new MoviesPage();
    }
}
