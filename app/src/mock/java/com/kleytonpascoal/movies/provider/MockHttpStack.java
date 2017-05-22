package com.kleytonpascoal.movies.provider;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpStack;
import com.kleytonpascoal.movies.R;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by kleyton on 12/05/17.
 */

class MockHttpStack implements HttpStack {

    private static final String TAG = MockHttpStack.class.getSimpleName();

    private static final long SIMULATE_NETWORK_DELAY_MS = 600;

    final String URL_FIND_MOVIES_BY_TITLE = "https://api.themoviedb.org/3/search/movie";
    final String URL_FIND_MOVIE_BY_ID = "https://api.themoviedb.org/3/movie/";

    /*final static int[] SEARCH_RESULTS = new int[] {
            R.raw.search_reponse_s_gameofthrones_page_1,
            R.raw.search_reponse_s_gameofthrones_page_2,
            R.raw.search_reponse_s_gameofthrones_page_3,
            R.raw.search_reponse_s_gameofthrones_page_4,
            R.raw.search_reponse_s_gameofthrones_page_5
    };*/

    private final Context mContext;

    public MockHttpStack(Context context) {
        mContext = context;
    }

    @Override
    public org.apache.http.HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        try {
            Thread.sleep(SIMULATE_NETWORK_DELAY_MS);
        } catch (InterruptedException e) {
        }
        HttpResponse response
                = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "OK"));
        List<Header> headers = defaultHeaders();
        response.setHeaders(headers.toArray(new Header[0]));
        response.setLocale(Locale.getDefault());
        response.setEntity(createEntity(request));
        return response;
    }

    private HttpEntity createEntity(Request<?> request) {

        final String requestUrl = request.getUrl();
        Log.d(TAG, "Request URL: " + requestUrl);

        String response = null;
        if (requestUrl.startsWith(URL_FIND_MOVIES_BY_TITLE)) {
            response = createResponseMoviesSearchList(requestUrl);

        } else if (requestUrl.startsWith(URL_FIND_MOVIE_BY_ID)) {
            response = createResponseMovieById(requestUrl);
        }

        Log.d(TAG, "Response: " + response);

        return createStringEntity(response);
    }

    private String createResponseMovieById(String requestUrl) {
        final String movieId = requestUrl.substring(URL_FIND_MOVIE_BY_ID.length(), requestUrl.length());
        return parser(movieId);
    }

    private String createResponseMoviesSearchList(String requestUrl) {

        final String params = requestUrl.substring(URL_FIND_MOVIES_BY_TITLE.length(), requestUrl.length());
        final String[] paramsSplited = params.split("&page=");
        final String titleToSearch = paramsSplited[0];
        final Integer pageIndex = Integer.valueOf(paramsSplited[1]);

        String resourceName = "search_reponse_s_" + titleToSearch.replace("+", "").toLowerCase() + "_page_" + pageIndex;

        String response = parser(resourceName);
        if (response == null) {
            response = parser("search_reponse_s_not_found");
        }

        return response;
    }

    private StringEntity createStringEntity(String response) {
        if (response != null) {
            try {
                return new StringEntity(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private List<Header> defaultHeaders() {
        //DateFormat dateFormat = new SimpleDateFormat("EEE, dd mmm yyyy HH:mm:ss zzz");

        final List<Header> basicHeaders = new ArrayList<>();
        basicHeaders.add(new BasicHeader("accept", "application/json"));
        basicHeaders.add(new BasicHeader("accept-encoding", "gzip, deflate"));
        basicHeaders.add(new BasicHeader("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36"));
        basicHeaders.add(new BasicHeader("accept-language", "en-US,en;q=0.8"));

        return basicHeaders;
    }

    private String parser(String resourceName) {
        try {
            InputStream stream = mContext.getResources().openRawResource(
                    mContext.getResources().getIdentifier(resourceName, "raw", mContext.getPackageName()));

            final BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int result = bufferedInputStream.read();
            while (result != -1) {
                outputStream.write(result);
                result = bufferedInputStream.read();
            }
            return outputStream.toString("UTF-8");
        } catch (Exception exc) {}
        return null;
    }
}
