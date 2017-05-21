package com.kleytonpascoal.movies.test.features.pages;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.view.View;

import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.persistence.DatabaseHelper;
import com.kleytonpascoal.movies.test.features.util.RecyclerViewInteraction;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.kleytonpascoal.movies.test.features.util.ViewActionsUtil.swipeRightOnTop;

/**
 * Created by kleyton on 18/05/17.
 */

public class MoviesPage extends BasePage {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    public MoviesPage() {
        onView(withId(R.id.activity_movies)).check(matches(isDisplayed()));
    }

    /* actions */

    public MovieSearchingPage addNewMovie() {
        onView(withId(R.id.fab_add_new_movie)).perform(click());
        return new MovieSearchingPage();
    }

    public MovieEditPage doMenuActionEditMovieAtPosition(int position) {
        onView(withId(R.id.movies_carousel)).perform(actionOnItemAtPosition(position, longClick()));
        onView(withId(R.id.menu_context_edit_movie_action)).perform(click());
        return new MovieEditPage();
    }

    public void doMenuActionDeleteMovieAtPosition(int position) {
        onView(withId(R.id.movies_carousel)).perform(actionOnItemAtPosition(position, longClick()));
        onView(withId(R.id.menu_context_delete_movie_action)).perform(click());
    }

    public void doSearchMovieByTtile(String title) {
        onView(withId(R.id.menu_search_movie_action)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).perform(typeText(title));
        closeSoftKeyboard();
    }

    public void actionShowMoviesAsCarousel() {
        onView(withId(R.id.movies_views_navigation_item_carousel_view)).perform(click());
    }

    public void actionShowMoviesAsList() {
        onView(withId(R.id.movies_views_navigation_item_list_view)).perform(click());
    }

    public void actionShowMoviesAsGrid() {
        onView(withId(R.id.movies_views_navigation_item_grid_view)).perform(click());
    }


    /* checks */

    public void checkMoviesShowAsCarousel() {
        onView(withId(R.id.movies_carousel)).check(matches(isDisplayed()));
    }

    public void checkMoviesShowAsList() {
        onView(withId(R.id.movies_list)).check(matches(isDisplayed()));
    }

    public void checkMoviesShowAsGrid() {
        onView(withId(R.id.movies_grid)).check(matches(isDisplayed()));
    }

    public void checkMovieIsDelete(final Movie movie) throws Exception {
        final Context targetContext = InstrumentationRegistry.getTargetContext();
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(targetContext);

        exception.expect(NoMatchingViewException.class);
        RecyclerViewInteraction.<Movie>onRecyclerView(withId(R.id.movies_carousel))
                .withItems(dbHelper.getMovieDao().queryForAll())
                .check(new RecyclerViewInteraction.ItemViewAssertion<Movie>() {
                    @Override
                    public void check(Movie item, View view, NoMatchingViewException e) {
                        matches(hasDescendant(withText(movie.title))).check(view, e);
                    }
                });
    }

    public void checkMovieIsAdded(final Movie movie) throws Exception {
        final Context targetContext = InstrumentationRegistry.getTargetContext();
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(targetContext);

        RecyclerViewInteraction.<Movie>onRecyclerView(withId(R.id.movies_carousel))
                .withItems(dbHelper.getMovieDao().queryForAll())
                .check(new RecyclerViewInteraction.ItemViewAssertion<Movie>() {
                    @Override
                    public void check(Movie item, View view, NoMatchingViewException e) {
                        matches(hasDescendant(withText(movie.title))).check(view, e);
                    }
                });
    }

    public void actionSwipeToRight() {
        onView(withId(R.id.movies_views_pager)).perform(swipeRightOnTop());
    }

    public void actionClickSearchButton() {
        onView(withId(R.id.menu_search_movie_action)).perform(click());
    }

    public void actionTypeSearchText(String text) {
        onView(withId(android.support.design.R.id.search_src_text)).perform(typeText(text));
        closeSoftKeyboard();
    }

    public void checkHasMovieWithTitle(final String text) throws SQLException {
        final Context targetContext = InstrumentationRegistry.getTargetContext();
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(targetContext);

        RecyclerViewInteraction.<Movie>onRecyclerView(withId(R.id.movies_carousel))
                .withItems(dbHelper.getMovieDao().queryForAll())
                .check(new RecyclerViewInteraction.ItemViewAssertion<Movie>() {
                    @Override
                    public void check(Movie item, View view, NoMatchingViewException e) {
                        matches(hasDescendant(withText(text))).check(view, e);
                    }
                });
    }

    public void actionClickOnMovieAt(int i) {

    }

    public MovieSearchingPage actionClickAddButton() {
        onView(withId(R.id.fab_add_new_movie)).perform(click());
        return new MovieSearchingPage();
    }
}
