package com.kleytonpascoal.movies.test.ui;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.activity.MovieEditActivity;
import com.kleytonpascoal.movies.activity.MovieSearchingActivity;
import com.kleytonpascoal.movies.provider.NetworkStatusProvider;
import com.kleytonpascoal.movies.test.util.ActivityFinisher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Created by kleyton on 12/05/17.
 */

@RunWith(AndroidJUnit4.class)
public class MovieSearchingActivityTest {

    @Rule
    public ActivityTestRule<MovieSearchingActivity> mActivityTestRule =
            new ActivityTestRule<>(MovieSearchingActivity.class, true, false);
    private IdlingResource idlingResource;

    @Test
    public void start_no_network_and_dont_allow_search() {
        set_network_off();

        start_movie_searching_activity();
        register_idling_resource();

        check_show_snackbar_with_no_network_connection_message();
        click_on_snackbar_action_to_close_network_connection_message();

        perform_search_movie_by_title("batman");
        check_show_snackbar_with_no_network_connection_message();

        unregister_idling_resource();
        finish_all_activities();
    }

    @Test
    public void start_no_network_after_receive_network_connection_event() {
        set_network_off();

        start_movie_searching_activity();
        register_idling_resource();

        check_show_snackbar_with_no_network_connection_message();

        set_network_on();
        simulate_broadcast_network_status_change();
        //check_hide_snackbar_with_no_network_connection_message();

        perform_search_movie_by_title("batman");
        check_show_result_and_hide_empty_message();

        unregister_idling_resource();
        finish_all_activities();
    }

    @Test
    public void search_movie_by_title_with_empty_result() {
        set_network_on();

        start_movie_searching_activity();
        register_idling_resource();

        perform_search_movie_by_title("abcdefghij");
        onView(withId(R.id.movie_list_empty_search_msg)).check(matches(isDisplayed()));

        unregister_idling_resource();
        finish_all_activities();
    }

    @Test
    public void search_movie_by_title_with_result() {
        set_network_on();

        start_movie_searching_activity();
        register_idling_resource();

        perform_search_movie_by_title("batman");
        check_show_result_and_hide_empty_message();

        unregister_idling_resource();
        finish_all_activities();
    }

    @Test
    public void verify_endless_scroll_is_functional() {
        set_network_on();

        start_movie_searching_activity();
        register_idling_resource();

        perform_search_movie_by_title("batman");
        check_show_result_and_hide_empty_message();

        onView(withId(R.id.searching_result_list)).perform(scrollToPosition(19));
        onView(withId(R.id.searching_result_list)).perform(scrollToPosition(39));
        onView(withId(R.id.searching_result_list)).perform(scrollToPosition(59));
        onView(withId(R.id.searching_result_list)).perform(scrollToPosition(79));
        onView(withId(R.id.searching_result_list)).perform(scrollToPosition(92));

        unregister_idling_resource();
        finish_all_activities();
    }

    @Test
    public void show_movie_complete_info() {
        Intents.init();
        set_network_on();
        start_movie_searching_activity();
        register_idling_resource();

        perform_search_movie_by_title("batman");
        check_show_result_and_hide_empty_message();

        click_on_first_movie_in_result_list();

        intent_to_show_new_movie_info();

        unregister_idling_resource();
        Intents.release();
        finish_all_activities();
    }


    /* activity */

    private void start_movie_searching_activity() {
        mActivityTestRule.launchActivity(new Intent());
    }

    private void finish_all_activities() {
        ActivityFinisher.finishOpenActivities();
    }


    /* search */

    private void perform_search_movie_by_title(String title) {
        onView(withId(R.id.searching_bt)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText(title), pressImeActionButton());
    }


    /* search result */

    private void check_show_result_and_hide_empty_message() {
        onView(withId(R.id.movie_list_empty_search_msg)).check(matches(not(isDisplayed())));
        onView(withId(R.id.searching_result_list)).check(matches(isDisplayed()));
    }

    public void click_on_first_movie_in_result_list() {
        onView(withId(R.id.searching_result_list)).perform(actionOnItemAtPosition(0, click()));
    }


    /* snack bar */

    private void check_show_snackbar_with_no_network_connection_message() {
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.no_network_connection)))
                .check(matches(isDisplayed()));
    }

    private void click_on_snackbar_action_to_close_network_connection_message() {
        onView(withId(android.support.design.R.id.snackbar_action)).perform(click());
    }


    /* idling resource */

    private void register_idling_resource() {
        idlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    private void unregister_idling_resource() {
        if (idlingResource != null)
            Espresso.unregisterIdlingResources(idlingResource);
    }


    /* broadcast */

    private void simulate_broadcast_network_status_change() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mActivityTestRule.getActivity().getNetworkStatusBroadcastReceiver().onBroadcastReceived();
            }
        });
    }


    /* network status */

    private void set_network_on() {
        NetworkStatusProvider.HAS_NETWORK_CONNECTION = true;
    }

    private void set_network_off() {
        NetworkStatusProvider.HAS_NETWORK_CONNECTION = false;
    }


    /* intent */

    private void intent_to_show_new_movie_info() {
        intended(allOf(hasComponent(MovieEditActivity.class.getName()),
                hasExtra(MovieEditActivity.PARAM_MOVIE_IS_NEW, true),
                hasExtraWithKey(MovieEditActivity.PARAM_MOVIE)));
    }
}
