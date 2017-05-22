package com.kleytonpascoal.movies.test.json;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.kleytonpascoal.movies.R;
import com.kleytonpascoal.movies.model.SearchMovieResult;
import com.kleytonpascoal.movies.parser.JsonParserHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.kleytonpascoal.movies.test.util.StreamUtil.getStringFromStream;

/**
 * Created by kleyton on 22/05/17.
 */

@RunWith(AndroidJUnit4.class)
public class SearchMovieResultTest {

    private static final String TAG = SearchMovieResultTest.class.getSimpleName();

    @Test
    public void movieResultParserTest() throws Exception {

        SearchMovieResult result = JsonParserHelper.deserializeSearchResult(getStringFromStream(
                InstrumentationRegistry.getTargetContext().getResources().openRawResource(R.raw.search_reponse_s_batman_page_1)));

        Log.d(TAG, result + "");
    }
}
