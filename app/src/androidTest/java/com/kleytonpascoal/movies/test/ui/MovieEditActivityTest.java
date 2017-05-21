package com.kleytonpascoal.movies.test.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.activity.MovieEditActivity;
import com.kleytonpascoal.movies.activity.MoviesActivity;
import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.persistence.DatabaseHelper;
import com.kleytonpascoal.movies.test.util.ActivityFinisher;
import com.kleytonpascoal.movies.test.util.IntentServiceIdlingResource;
import com.kleytonpascoal.movies.test.util.MovieContent;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.GONE;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.kleytonpascoal.movies.test.util.ViewActionsUtil.scrollNestedScrollView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsAnything.anything;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by kleyton on 12/05/17.
 */

@RunWith(AndroidJUnit4.class)
public class MovieEditActivityTest {

    private static Movie mMovie;

    @Rule
    public IntentsTestRule<MovieEditActivity> movieEditActivityTestRule =
            new IntentsTestRule<>(MovieEditActivity.class, true, false);

    private IdlingResource idlingResource;

    @BeforeClass
    public static void create_movie() {
        mMovie = MovieContent.createMovie();
    }


    /* tests */

    @Test
    public void check_new_movie_to_add_is_showing() throws Exception {
        start_movie_edit_activity_new_movie();
        check_activity_initial_state();
        check_floating_action_button_to_save_movie_is_displayed();
        finish_all_activities();
    }

    @Test
    public void check_existent_movie_to_edit_is_showing() throws Exception {
        start_movie_edit_activity_existent_movie();
        check_activity_initial_state();
        check_menu_items_save_and_delete_is_displayed();
        finish_all_activities();
    }

    @Test
    public void check_new_movie_fields_are_filled() throws Exception {
        start_movie_edit_activity_new_movie();
        check_initial_toolbar_title_subtitle_are_filled();
        check_activity_initial_state();
        check_all_fields_to_edit_are_filled();
        finish_all_activities();
    }

    @Test
    public void check_existent_movie_fields_are_filled() throws Exception {
        start_movie_edit_activity_existent_movie();
        check_initial_toolbar_title_subtitle_are_filled();
        check_activity_initial_state();
        check_all_fields_to_edit_are_filled();
        finish_all_activities();
    }

    @Test
    public void check_toolbar_title_subtitle_behaviour_when_add_new_movie() throws Exception {
        start_movie_edit_activity_new_movie();
        check_activity_initial_state();
        check_toolbar_title_subtitle_are_displayed_after_scroll();
        finish_all_activities();
    }

    @Test
    public void check_toolbar_title_subtitle_behaviour_when_edit_existent_movie() throws Exception {
        start_movie_edit_activity_existent_movie();
        check_activity_initial_state();
        check_toolbar_title_subtitle_are_displayed_after_scroll();
        finish_all_activities();
    }

    @Test
    public void action_save_new_movie() throws Exception {
        start_movie_edit_activity_new_movie();
        register_espresso_idling_resource();

        check_floating_action_button_to_save_movie_is_displayed();
        click_floating_action_button_to_save_movie();
        check_intent_to_show_movies_activity();

        check_exist_movie_in_database(mMovie.imdbID);

        unregister_espresso_idling_resource();
        delete_movie_if_exists_in_database();

        finish_all_activities();
    }

    @Test
    public void action_edit_new_movie_and_save() throws Exception {
        start_movie_edit_activity_new_movie();
        register_espresso_idling_resource();

        String newPlot = type_new_plot_in_movie();

        check_floating_action_button_to_save_movie_is_displayed();
        click_floating_action_button_to_save_movie();
        check_intent_to_show_movies_activity();

        final Movie movieSaved = check_exist_movie_in_database(mMovie.imdbID);
        assertEquals(newPlot, movieSaved.plot);

        unregister_espresso_idling_resource();
        delete_movie_if_exists_in_database();

        finish_all_activities();
    }

    @Test
    public void action_edit_existent_movie_and_save() throws Exception {
        start_movie_edit_activity_existent_movie();
        register_espresso_idling_resource();

        String newPlot = type_new_plot_in_movie();

        check_menu_items_save_and_delete_is_displayed();
        click_menu_save_item_to_save_movie();
        check_intent_to_show_movies_activity();

        final Movie movieSaved = check_exist_movie_in_database(mMovie.imdbID);
        assertEquals(newPlot, movieSaved.plot);

        unregister_espresso_idling_resource();
        delete_movie_if_exists_in_database();

        finish_all_activities();
    }

    @Test
    public void action_delete_existent_movie() throws SQLException {
        delete_movie_if_exists_in_database();
        insert_movie_in_database();

        start_movie_edit_activity_existent_movie();
        register_espresso_idling_resource();

        check_menu_items_save_and_delete_is_displayed();
        click_menu_delete_item_to_delete_movie();
        check_not_exist_movie_in_database(mMovie.imdbID);
        check_intent_to_show_movies_activity();

        unregister_espresso_idling_resource();

        finish_all_activities();
    }


    /* activity */

    private void start_movie_edit_activity_existent_movie() {
        movieEditActivityTestRule.launchActivity(new Intent().
                putExtra(MovieEditActivity.PARAM_MOVIE, mMovie));
    }

    private void start_movie_edit_activity_new_movie() {
        movieEditActivityTestRule.launchActivity(new Intent().
                putExtra(MovieEditActivity.PARAM_MOVIE, mMovie).
                putExtra(MovieEditActivity.PARAM_MOVIE_IS_NEW, true));
    }

    private void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }


    /* type new plot */

    @NonNull
    private String type_new_plot_in_movie() {
        String newPlot = "bla bla bla bla bla bla bla bla bla bla";
        onView(withId(R.id.movie_edit_plot)).perform(clearText(), typeText(newPlot));

        Espresso.closeSoftKeyboard();
        return newPlot;
    }


    /* activity initial state */

    private void check_activity_initial_state() throws Exception {
        check_initial_toolbar_title_subtitle_are_displayed();
        check_the_main_view_is_displayed();
        check_all_fields_to_edit_are_displayed();
    }


    /* menu */

    private void check_menu_items_save_and_delete_is_displayed() {
        onView(withId(R.id.menu_movie_edit_save_action)).check(matches(isDisplayed()));
        onView(withId(R.id.menu_movie_edit_delete_action)).check(matches(isDisplayed()));
    }

    private void click_menu_save_item_to_save_movie() {
        onView(withId(R.id.menu_movie_edit_save_action)).perform(click());
    }

    private void click_menu_delete_item_to_delete_movie() {
        onView(withId(R.id.menu_movie_edit_delete_action)).perform(click());
    }


    /* toolbar */

    private void check_initial_toolbar_title_subtitle_are_displayed() {
        final int orientation = movieEditActivityTestRule.getActivity().getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            onView(allOf(withId(R.id.toolbar_title_subtitle_view_title), withEffectiveVisibility(VISIBLE))).check(matches(isDisplayed()));
            onView(allOf(withId(R.id.toolbar_title_subtitle_view_subtitle), withEffectiveVisibility(VISIBLE))).check(matches(isDisplayed()));

            onView(withId(R.id.movie_edit_plot)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));

            onView(allOf(withId(R.id.toolbar_title_subtitle_view_title), withEffectiveVisibility(VISIBLE))).check(matches(anything()));
            onView(allOf(withId(R.id.toolbar_title_subtitle_view_subtitle), withEffectiveVisibility(VISIBLE))).check(matches(anything()));
        } else {
            onView(withId(R.id.movie_edit_toolbar)).check(matches(isDisplayed()));
        }
    }

    private void check_initial_toolbar_title_subtitle_are_filled() {
        final int orientation = movieEditActivityTestRule.getActivity().getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            onView(allOf(withId(R.id.toolbar_title_subtitle_view_title), withEffectiveVisibility(VISIBLE))).check(matches(withText(mMovie.title)));
            onView(allOf(withId(R.id.toolbar_title_subtitle_view_subtitle), withEffectiveVisibility(VISIBLE))).check(matches(withText(mMovie.year)));
            onView(allOf(withId(R.id.toolbar_title_subtitle_view_title), withEffectiveVisibility(GONE))).check(matches(withText(mMovie.title)));
            onView(allOf(withId(R.id.toolbar_title_subtitle_view_subtitle), withEffectiveVisibility(GONE))).check(matches(withText(mMovie.year)));
        } else {
            onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.movie_edit_toolbar)))).check(matches(withText(mMovie.title)));
            onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.movie_edit_toolbar)))).check(matches(withText(mMovie.year)));
        }
    }

    private void check_toolbar_title_subtitle_are_displayed_after_scroll() {
        final int orientation = movieEditActivityTestRule.getActivity().getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            onView(allOf(withId(R.id.toolbar_title_subtitle), withParent(withId(R.id.toolbar_title_subtitle_view)))).check(matches(isDisplayed()));
            onView(allOf(withId(R.id.toolbar_title_subtitle), withParent(withId(R.id.toolbar_title_subtitle_float_view)))).check(matches(isDisplayed()));
        }
    }


    /* check main view */

    private void check_the_main_view_is_displayed() {
        onView(withId(R.id.movie_edit_container)).check(matches(isDisplayed()));
    }


    /* floating action button */

    private void check_floating_action_button_to_save_movie_is_displayed() {
        onView(withId(R.id.movie_edit_fab_save_movie)).check(matches(isDisplayed()));
    }

    private void click_floating_action_button_to_save_movie() {
        onView(withId(R.id.movie_edit_fab_save_movie)).perform(click());
    }


    /* movie check fields */

    private void check_all_fields_to_edit_are_displayed() throws Exception {
        onView(withId(R.id.movie_edit_plot)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_edit_gender)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_edit_runtime)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_edit_rating)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_edit_awards)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_edit_votes)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_edit_actors)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_edit_writer)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_edit_director)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_edit_production)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_edit_box_office)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
    }

    private void check_all_fields_to_edit_are_filled() {
        onView(withId(R.id.movie_edit_plot)).check(matches(withText(mMovie.plot)));
        onView(withId(R.id.movie_edit_gender)).check(matches(withText(mMovie.genre)));
        onView(withId(R.id.movie_edit_runtime)).check(matches(withText(mMovie.runtime)));
        onView(withId(R.id.movie_edit_rating)).check(matches(withText(mMovie.imdbRating)));
        onView(withId(R.id.movie_edit_awards)).check(matches(withText(mMovie.awards)));
        onView(withId(R.id.movie_edit_votes)).check(matches(withText(mMovie.imdbVotes)));
        onView(withId(R.id.movie_edit_actors)).check(matches(withText(mMovie.actors)));
        onView(withId(R.id.movie_edit_writer)).check(matches(withText(mMovie.writer)));
        onView(withId(R.id.movie_edit_director)).check(matches(withText(mMovie.director)));
        onView(withId(R.id.movie_edit_production)).check(matches(withText(mMovie.production)));
        onView(withId(R.id.movie_edit_box_office)).check(matches(withText(mMovie.boxOffice)));
        // website not show to edit
    }


    /* check intents */

    private void check_intent_to_show_movies_activity() {
        intended(hasComponent(new ComponentName(InstrumentationRegistry.getTargetContext(), MoviesActivity.class.getName())));
    }


    /* espresso idling resource */

    private void register_espresso_idling_resource() {
        idlingResource =  new IntentServiceIdlingResource(
                InstrumentationRegistry.getInstrumentation().getTargetContext());
        Espresso.registerIdlingResources(idlingResource);
    }

    private void unregister_espresso_idling_resource() {
        if (idlingResource != null)
            Espresso.unregisterIdlingResources(idlingResource);
    }


    /* database util methods */

    private void delete_movie_if_exists_in_database() throws SQLException {
        final DatabaseHelper dbHelper = DatabaseHelper.getHelper(InstrumentationRegistry.getTargetContext());
        dbHelper.getWritableDatabase().beginTransaction();

        final Movie movie = dbHelper.getMovieDao().queryForId(mMovie.imdbID);
        if (movie != null)
            dbHelper.getMovieDao().deleteById(mMovie.imdbID);

        dbHelper.getWritableDatabase().setTransactionSuccessful();
        dbHelper.getWritableDatabase().endTransaction();
        dbHelper.releaseHelper();
    }

    private Movie check_exist_movie_in_database(String id) throws SQLException {
        final Movie movie = retrieve_movie_from_database(id);
        assertNotNull(movie);
        return movie;
    }

    private void check_not_exist_movie_in_database(String id) throws SQLException {
        assertNull(retrieve_movie_from_database(id));
    }

    private void insert_movie_in_database() throws SQLException {
        final DatabaseHelper dbHelper = DatabaseHelper.getHelper(InstrumentationRegistry.getTargetContext());

        dbHelper.getWritableDatabase().beginTransaction();
        dbHelper.getMovieDao().create(mMovie);
        dbHelper.getWritableDatabase().setTransactionSuccessful();
        dbHelper.getWritableDatabase().endTransaction();

        dbHelper.releaseHelper();
    }

    private Movie retrieve_movie_from_database(String id) throws SQLException {
        final DatabaseHelper dbHelper = DatabaseHelper.getHelper(InstrumentationRegistry.getTargetContext());
        final Movie movie = dbHelper.getMovieDao().queryForId(id);
        dbHelper.releaseHelper();
        return movie;
    }

}