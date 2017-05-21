package com.kleytonpascoal.movies.test.suite;

import com.kleytonpascoal.movies.ContextTest;
import com.kleytonpascoal.movies.test.db.DatabaseHelperTest;
import com.kleytonpascoal.movies.test.ui.MovieDetailActivityTest;
import com.kleytonpascoal.movies.test.ui.MovieEditActivityTest;
import com.kleytonpascoal.movies.test.ui.MovieSearchingActivityTest;
import com.kleytonpascoal.movies.test.ui.MoviesActivityTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by kleyton on 21/05/17.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MoviesActivityTest.class,
        MovieDetailActivityTest.class,
        MovieEditActivityTest.class,
        MovieSearchingActivityTest.class,
        ContextTest.class,
        DatabaseHelperTest.class
})
public class UIRegressionTestSuite {
}
