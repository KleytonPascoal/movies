package com.kleytonpascoal.movies.test.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.activity.MovieDetailActivity;
import com.kleytonpascoal.movies.activity.MovieEditActivity;
import com.kleytonpascoal.movies.activity.MovieSearchingActivity;
import com.kleytonpascoal.movies.activity.MoviesActivity;
import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.persistence.DatabaseHelper;
import com.kleytonpascoal.movies.test.util.ActivityFinisher;
import com.kleytonpascoal.movies.test.util.MovieContentJson;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;
import java.util.List;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.kleytonpascoal.movies.test.util.ViewActionsUtil.swipeLeftOnTop;
import static com.kleytonpascoal.movies.test.util.ViewActionsUtil.swipeRightOnTop;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by kleyton on 12/05/17.
 */

@RunWith(AndroidJUnit4.class)
public class MoviesActivityTest {

    private static List<Movie> mMovies;

    @Rule
    public IntentsTestRule<MoviesActivity> mTestRule = new IntentsTestRule<>(MoviesActivity.class, true, false);

    @BeforeClass
    public static void insert_movies_in_database() throws SQLException {
        final Context targetContext = InstrumentationRegistry.getTargetContext();
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(targetContext);
        mMovies = MovieContentJson.createMovieList(targetContext);
        dbHelper.getWritableDatabase().beginTransaction();
        dbHelper.getMovieDao().create(mMovies);
        dbHelper.getWritableDatabase().setTransactionSuccessful();
        dbHelper.getWritableDatabase().endTransaction();
        dbHelper.releaseHelper();

    }

    @AfterClass
    public static void remove_movies_from_database() throws SQLException {
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(InstrumentationRegistry.getTargetContext());
        dbHelper.getWritableDatabase().beginTransaction();
        dbHelper.getMovieDao().delete(mMovies);
        dbHelper.getWritableDatabase().setTransactionSuccessful();
        dbHelper.getWritableDatabase().endTransaction();
        dbHelper.releaseHelper();
    }

    @Test
    public void check_movies_activity_show_as_carousel_by_click_navigation_item() {
        start_movies_activity();
        check_my_movies_showed_as(R.id.movies_carousel);
        finish_all_activities();
    }

    @Test
    public void check_movies_activity_show_as_list_by_click_navigation_item() {
        start_movies_activity();
        click_bottom_navigation_item_list();
        check_my_movies_showed_as(R.id.movies_list);
        finish_all_activities();
    }

    @Test
    public void check_movies_activity_show_as_grid_by_click_navigation_item() {
        start_movies_activity();
        click_bottom_navigation_item_grid();
        check_my_movies_showed_as(R.id.movies_grid);
        finish_all_activities();
    }

    @Test
    public void check_movies_activity_show_as_carousel_by_swipe_page() {
        start_movies_activity();
        swipe_left_my_movies_view_pager();
        swipe_right_my_movies_view_pager();
        check_my_movies_showed_as(R.id.movies_carousel);
        finish_all_activities();
    }

    @Test
    public void check_movies_activity_show_as_list_by_swipe_page() {
        start_movies_activity();
        swipe_left_my_movies_view_pager();
        check_my_movies_showed_as(R.id.movies_list);
        finish_all_activities();
    }

    @Test
    public void check_movies_activity_show_as_grid_by_swipe_page() {
        start_movies_activity();
        swipe_left_my_movies_view_pager();
        swipe_left_my_movies_view_pager();
        check_my_movies_showed_as(R.id.movies_list);
        finish_all_activities();
    }

    @Test
    public void show_movie_detail_from_my_movies_carousel() {
        start_movies_activity();
        click_first_movie_in_my_movies_carousel();
        check_intent_to_start_movie_detail_activity();
        finish_all_activities();
    }

    @Test
    public void show_movie_detail_from_my_movies_list() {
        start_movies_activity();
        click_bottom_navigation_item_list();
        click_first_movie_in_my_movies_list();
        check_intent_to_start_movie_detail_activity();
        finish_all_activities();
    }

    @Test
    public void show_movie_detail_from_my_movies_grid() {
        start_movies_activity();
        click_bottom_navigation_item_grid();
        click_first_movie_in_my_movies_grid();
        check_intent_to_start_movie_detail_activity();
        finish_all_activities();
    }

    @Test
    public void action_filter_movie_by_title_in_my_movies_as_carousel() {
        start_movies_activity();

        final String movieTitle = "The Dark Knight";
        action_search_movie_by_title_in_my_movies(movieTitle);
        check_has_movie_item_in_my_movies_as(R.id.movies_carousel, movieTitle);
        finish_all_activities();
    }

    @Test
    public void action_filter_movie_by_title_in_my_movies_as_list() {
        start_movies_activity();
        click_bottom_navigation_item_list();
        final String movieTitle = "The Dark Knight";
        action_search_movie_by_title_in_my_movies(movieTitle);
        check_has_movie_item_in_my_movies_as(R.id.movies_list, movieTitle);
        finish_all_activities();
    }

    @Test
    public void action_filter_movie_by_title_in_my_movies_as_grid() {
        start_movies_activity();
        click_bottom_navigation_item_grid();
        final String movieTitle = "The Dark Knight";
        action_search_movie_by_title_in_my_movies(movieTitle);
        check_has_movie_item_in_my_movies_as(R.id.movies_grid, movieTitle);
        finish_all_activities();
    }

    @Test
    public void action_add_new_movie() {
        start_movies_activity();
        click_button_action_add_new_movie();
        check_intent_to_start_add_new_movie_action();
        finish_all_activities();
    }

    @Test
    public void context_menu_action_edit_movie_from_my_movies() {
        start_movies_activity();
        click_context_menu_edit_movie();
        check_intent_to_start_edit_movie_activity();
        finish_all_activities();
    }

    @Test
    public void context_menu_action_delete_movie_from_my_movies() {
        start_movies_activity();
        click_context_menu_delete_movie();
        check_show_snackbar_with_undo_action();
        finish_all_activities();
    }


    /* activity */

    private void start_movies_activity() {
        mTestRule.launchActivity(new Intent());
        onView(withId(R.id.movies_carousel)).check(matches(isDisplayed()));
    }

    private void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }


    /* bottom navigation */

    private void click_bottom_navigation_item_carousel() {
        onView(withId(R.id.movies_views_navigation_item_carousel_view)).perform(click());
        onView(withId(R.id.movies_carousel)).check(matches(isDisplayed()));
    }

    private void click_bottom_navigation_item_list() {
        onView(withId(R.id.movies_views_navigation_item_list_view)).perform(click());
        onView(withId(R.id.movies_list)).check(matches(isDisplayed()));
    }

    private void click_bottom_navigation_item_grid() {
        onView(withId(R.id.movies_views_navigation_item_grid_view)).perform(click());
        onView(withId(R.id.movies_grid)).check(matches(isDisplayed()));
    }


    /* movies items */

    private void click_first_movie_in_my_movies_carousel() {
        onView(withId(R.id.movies_carousel)).perform(actionOnItemAtPosition(0, click()));
    }

    private void click_first_movie_in_my_movies_list() {
        onView(withId(R.id.movies_list)).perform(actionOnItemAtPosition(0, click()));
    }

    private void click_first_movie_in_my_movies_grid() {
        onView(withId(R.id.movies_grid)).perform(actionOnItemAtPosition(0, click()));
    }

    private void check_has_movie_item_in_my_movies_as(int resId, String movieTitle) {
        onView(withId(resId)).check(matches(hasDescendant(withText(containsString(movieTitle)))));
    }

    private void check_my_movies_showed_as(@IdRes int resId) {
        onView(withId(resId)).check(matches(isDisplayed()));
    }

    private void swipe_left_my_movies_view_pager() {
        onView(withId(R.id.movies_views_pager)).perform(swipeLeftOnTop());
    }

    private void swipe_right_my_movies_view_pager() {
        onView(withId(R.id.movies_views_pager)).perform(swipeRightOnTop());
    }


    /* check intents */

    private void check_intent_to_start_movie_detail_activity() {
        intended(allOf(hasComponent(MovieDetailActivity.class.getName()),
                hasExtraWithKey(MovieDetailActivity.EXTRA_MOVIE)));
    }

    private void check_intent_to_start_edit_movie_activity() {
        intended(allOf(hasComponent(MovieEditActivity.class.getName()),
                hasExtraWithKey(MovieEditActivity.PARAM_MOVIE)));
    }

    private void check_intent_to_start_add_new_movie_action() {
        intended(hasComponent(MovieSearchingActivity.class.getName()));
        closeSoftKeyboard();
    }


    /* actions */

    private void click_button_action_add_new_movie() {
        onView(withId(R.id.fab_add_new_movie)).perform(click());
    }

    private void click_context_menu_edit_movie() {
        onView(withId(R.id.movies_carousel)).perform(actionOnItemAtPosition(0, longClick()));
        onView(withId(R.id.menu_context_edit_movie_action)).perform(click());
    }

    private void click_context_menu_delete_movie() {
        onView(withId(R.id.movies_carousel)).perform(actionOnItemAtPosition(0, longClick()));
        onView(withId(R.id.menu_context_delete_movie_action)).perform(click());
    }

    private void action_search_movie_by_title_in_my_movies(String movieTitle) {
        onView(withId(R.id.menu_search_movie_action)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).perform(typeText(movieTitle));
        closeSoftKeyboard();
    }


    /* snack bar */

    private void check_show_snackbar_with_undo_action() {
        onView(allOf(withId(android.support.design.R.id.snackbar_action), withText(R.string.undo_action)))
                .check(matches(isDisplayed()));
    }
}
