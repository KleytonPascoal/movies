package com.kleytonpascoal.movies.test.features.pages;

import com.kleytonpascoal.movies.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.kleytonpascoal.movies.test.features.util.ViewActionsUtil.scrollNestedScrollView;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by kleyton on 18/05/17.
 */

public class MovieDetailPage extends BasePage {

    public MovieDetailPage() {
        onView(withId(R.id.activity_movie_edit)).check(matches(isDisplayed()));
    }

    public void checkMovieDetail(String title) {
        onView(allOf(withId(R.id.toolbar_title_subtitle_view_title), withEffectiveVisibility(VISIBLE))).check(matches(withText(title)));
        
        onView(withId(R.id.movie_detail_plot)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_gender)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_runtime)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_rating)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_awards)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_imdb_votes)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_website)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_actors)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_writer)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_director)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_production)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_detail_box_office)).perform(scrollNestedScrollView(true)).check(matches(isDisplayed()));
    }
}
