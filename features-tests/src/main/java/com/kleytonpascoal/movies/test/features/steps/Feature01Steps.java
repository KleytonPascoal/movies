package com.kleytonpascoal.movies.test.features.steps;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.kleytonpascoal.movies.activity.MoviesActivity;
import com.kleytonpascoal.movies.test.features.pages.BasePage;
import com.kleytonpascoal.movies.test.features.pages.MoviesPage;
import com.kleytonpascoal.movies.test.features.util.ActivityFinisher;

import org.junit.Rule;
import org.junit.runner.RunWith;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static com.kleytonpascoal.movies.test.features.util.DatabaseUtil.insertMoviesInDatabase;
import static com.kleytonpascoal.movies.test.features.util.DatabaseUtil.removeMoviesFromDatabase;

/**
 * Created by kleyton on 18/05/17.
 */

@SuppressWarnings("JUnitTestCaseWithNoTests")
@RunWith(AndroidJUnit4.class)
public class Feature01Steps {

    @Rule
    public ActivityTestRule<MoviesActivity> mActivityTestRule =
            new ActivityTestRule<>(MoviesActivity.class, true, false);

    private BasePage mCurrentPage;

    @Before
    public void setUp() throws Exception {
        removeMoviesFromDatabase();
        insertMoviesInDatabase();
        mActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        ActivityFinisher.finishOpenActivities();
        removeMoviesFromDatabase();
    }

    @Given("^I see My Movies page - F01$")
    public void i_see_my_movies_page() throws Exception {
        mCurrentPage = new MoviesPage();
    }

    @When("^I select navigation bottom to show movie as carousel")
    public void i_select_navigation_bottom_to_show_movie_as_carousel() {
        mCurrentPage.is(MoviesPage.class).actionShowMoviesAsCarousel();
    }

    @Then("^I want to see my movies saved as carousel")
    public void i_want_to_see_my_movies_saved_as_carousel() {
        mCurrentPage.is(MoviesPage.class).checkMoviesShowAsCarousel();
    }

    @When("^I select navigation bottom to show movie as list")
    public void i_select_navigation_bottom_to_show_movie_as_list() {
        mCurrentPage.is(MoviesPage.class).actionShowMoviesAsList();
    }

    @Then("^I want to see my movies saved as list")
    public void i_want_to_see_my_movies_saved_as_list() {
        mCurrentPage.is(MoviesPage.class).checkMoviesShowAsList();
    }

    @When("^I select navigation bottom to show movie as grid")
    public void i_select_navigation_bottom_to_show_movie_as_grid() {
        mCurrentPage.is(MoviesPage.class).actionShowMoviesAsGrid();
    }

    @Then("^I want to see my movies saved as grid")
    public void i_want_to_see_my_movies_saved_as_grid() {
        mCurrentPage.is(MoviesPage.class).checkMoviesShowAsGrid();
    }

    @When("^I swipe to right my movies grid")
    public void i_swipe_to_right_my_movies_grid() {
        mCurrentPage.is(MoviesPage.class).actionSwipeToRight();
    }

    @Then("^I want to see my movies as list again")
    public void i_want_to_see_my_movies_saved_as_list_again() {
        mCurrentPage.is(MoviesPage.class).checkMoviesShowAsList();
    }

    @When("^I swipe to right my movies list")
    public void i_swipe_to_right_my_movies_list() {
        mCurrentPage.is(MoviesPage.class).actionSwipeToRight();
    }

    @Then("^I want to see my movies as carousel again")
    public void i_want_to_see_my_movies_saved_as_carousel_again() throws Exception {
        mCurrentPage.is(MoviesPage.class).checkMoviesShowAsCarousel();
    }
}
