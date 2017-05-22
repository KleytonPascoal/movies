package com.kleytonpascoal.movies.test.json;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.model.Movie;
import com.kleytonpascoal.movies.parser.JsonParserHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.kleytonpascoal.movies.test.util.StreamUtil.getStringFromStream;
import static org.junit.Assert.assertEquals;

/**
 * Created by kleyton on 22/05/17.
 */

@RunWith(AndroidJUnit4.class)
public class MovieParserTest {


    private static final String TAG = MovieParserTest.class.getSimpleName();

    @Test
    public void movieParserTest() throws Exception {
        Movie movieExpected = new Movie();
        movieExpected.id = 209112;
        movieExpected.budget = "250000000";
        movieExpected.genre = "Action, Adventure, Fantasy";
        movieExpected.homepage = "http://www.batmanvsupermandawnofjustice.com/";
        movieExpected.language = "en";
        movieExpected.title = "Batman v Superman: Dawn of Justice";
        movieExpected.overview = "Fearing the actions of a god-like Super Hero left unchecked, Gotham City’s own formidable, forceful vigilante takes on Metropolis’s most revered, modern-day savior, while the world wrestles with what sort of hero it really needs. And with Batman and Superman at war with one another, a new threat quickly arises, putting mankind in greater danger than it’s ever known before.";
        movieExpected.posterPath = "/cGOPbv9wA5gEejkUN892JrveARt.jpg";
        movieExpected.production = "DC Comics, Atlas Entertainment, Warner Bros., DC Entertainment, Cruel & Unusual Films, RatPac-Dune Entertainment";
        movieExpected.releaseDate = "2016-03-23";
        movieExpected.runtime = "151";
        movieExpected.status = "Released";
        movieExpected.voteAverage = "5.6";
        movieExpected.voteCount = "5685";

        Movie movie = JsonParserHelper.deserializeMovie(getStringFromStream(
                InstrumentationRegistry.getTargetContext().getResources().openRawResource(R.raw.movie_id209112)));

        assertEquals(movieExpected, movie);

        Log.d(TAG, movie.toString());
    }
}
