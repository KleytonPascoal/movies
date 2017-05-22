package com.kleytonpascoal.movies.test.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.activity.MovieDetailActivity;
import com.kleytonpascoal.movies.activity.MovieEditActivity;
import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.test.util.ActivityFinisher;
import com.kleytonpascoal.movies.test.util.MovieContentJson;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
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

/**
 * Created by kleyton on 12/05/17.
 */

@RunWith(AndroidJUnit4.class)
public class MovieDetailActivityTest {

    private static Movie mMovie;

    @Rule
    public ActivityTestRule<MovieDetailActivity> movieDetailActivityActivityTestRule =
            new ActivityTestRule<>(MovieDetailActivity.class, true, false);

    @After
    public void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }

    @BeforeClass
    public static void create_movie() throws IOException {
        mMovie = MovieContentJson.createMovie(InstrumentationRegistry.getTargetContext());
    }

    @AfterClass
    public static void static_finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }


    /* tests */

    @Test
    public void check_movie_info_is_showing() {
        start_movie_detail_activity();

        check_initial_toolbar_title_subtitle_are_filled();
        check_initial_toolbar_title_subtitle_are_displayed();

        check_the_main_view_is_displayed();
        check_floating_action_button_to_edit_movie_is_displayed();

        check_all_fields_to_edit_are_displayed();
        check_all_fields_to_edit_are_filled();
    }

    @Test
    public void check_toolbar_title_subtitle_behaviour() throws Exception {
        start_movie_detail_activity();
        check_activity_initial_state();
        check_toolbar_title_subtitle_are_displayed_after_scroll();
    }

    @Test
    public void action_start_movie_edit() {
        Intents.init();

        start_movie_detail_activity();
        perform_action_to_edit_movie();
        check_intent_to_edit_movie_action();

        Intents.release();
    }


    /* activity */

    private void start_movie_detail_activity() {
        movieDetailActivityActivityTestRule.launchActivity(new Intent().putExtra(MovieDetailActivity.EXTRA_MOVIE, mMovie));
    }

    private void check_activity_initial_state() throws Exception {
        check_initial_toolbar_title_subtitle_are_displayed();
        check_the_main_view_is_displayed();
        check_all_fields_to_edit_are_displayed();
    }


    /* toolbar */

    private void check_initial_toolbar_title_subtitle_are_displayed() {
        final int orientation = movieDetailActivityActivityTestRule.getActivity().getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            onView(allOf(withId(R.id.toolbar_title_subtitle_view_title), withEffectiveVisibility(VISIBLE))).check(matches(isDisplayed()));
            onView(allOf(withId(R.id.toolbar_title_subtitle_view_subtitle), withEffectiveVisibility(VISIBLE))).check(matches(isDisplayed()));

            onView(allOf(withId(R.id.toolbar_title_subtitle_view_title), withEffectiveVisibility(GONE))).check(matches(anything()));
            onView(allOf(withId(R.id.toolbar_title_subtitle_view_subtitle), withEffectiveVisibility(GONE))).check(matches(anything()));
        } else {
            onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.movie_detail_toolbar)))).check(matches(isDisplayed()));
            onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.movie_detail_toolbar)))).check(matches(isDisplayed()));
        }
    }

    private void check_initial_toolbar_title_subtitle_are_filled() {
        final int orientation = movieDetailActivityActivityTestRule.getActivity().getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            onView(allOf(withId(R.id.toolbar_title_subtitle_view_title), withEffectiveVisibility(VISIBLE))).check(matches(withText(mMovie.title)));
            onView(allOf(withId(R.id.toolbar_title_subtitle_view_subtitle), withEffectiveVisibility(VISIBLE))).check(matches(withText(mMovie.releaseDate)));

            onView(allOf(withId(R.id.toolbar_title_subtitle_view_title), withEffectiveVisibility(GONE))).check(matches(withText(mMovie.title)));
            onView(allOf(withId(R.id.toolbar_title_subtitle_view_subtitle), withEffectiveVisibility(GONE))).check(matches(withText(mMovie.releaseDate)));
        } else {
            onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.movie_detail_toolbar)))).check(matches(withText(mMovie.title)));
            onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.movie_detail_toolbar)))).check(matches(withText(mMovie.releaseDate)));
        }
    }

    private void check_toolbar_title_subtitle_are_displayed_after_scroll() {
        final int orientation = movieDetailActivityActivityTestRule.getActivity().getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            onView(allOf(withId(R.id.toolbar_title_subtitle), withParent(withId(R.id.toolbar_title_subtitle_view)))).check(matches(isDisplayed()));
            onView(allOf(withId(R.id.toolbar_title_subtitle), withParent(withId(R.id.toolbar_title_subtitle_float_view)))).check(matches(isDisplayed()));
        }
    }


    /* check main view */

    private void check_the_main_view_is_displayed() {
        onView(withId(R.id.movie_detail_container)).check(matches(isDisplayed()));
    }


    /* floating action button */

    private void check_floating_action_button_to_edit_movie_is_displayed() {
        onView(withId(R.id.movie_to_edit_fab)).check(matches(isDisplayed()));
    }

    private void perform_action_to_edit_movie() {
        onView(withId(R.id.movie_to_edit_fab)).perform(click());
    }


    /* movie check fields */

    private void check_all_fields_to_edit_are_displayed() {

        onView(withId(R.id.movie_detail_plot)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_gender)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_runtime)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_rating)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_imdb_votes)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_website)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_production)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_box_office)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
    }

    private void check_all_fields_to_edit_are_filled() {

        onView(withId(R.id.movie_detail_plot)).check(matches(withText(mMovie.overview)));
        onView(withId(R.id.movie_detail_gender)).check(matches(withText(mMovie.genre)));
        onView(withId(R.id.movie_detail_runtime)).check(matches(withText(mMovie.runtime)));
        onView(withId(R.id.movie_detail_rating)).check(matches(withText(mMovie.voteAverage)));
        onView(withId(R.id.movie_detail_imdb_votes)).check(matches(withText(mMovie.voteCount)));
        onView(withId(R.id.movie_detail_website)).check(matches(withText(mMovie.homepage)));
        onView(withId(R.id.movie_detail_production)).check(matches(withText(mMovie.production)));
        onView(withId(R.id.movie_detail_box_office)).check(matches(withText(mMovie.budget)));
    }


    /* check intents */

    private void check_intent_to_edit_movie_action() {
        intended(allOf(hasComponent(MovieEditActivity.class.getName()), hasExtra(MovieEditActivity.PARAM_MOVIE, mMovie)));
    }
}
