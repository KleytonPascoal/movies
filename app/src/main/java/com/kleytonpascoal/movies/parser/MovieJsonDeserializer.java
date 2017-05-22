package com.kleytonpascoal.movies.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.kleytonpascoal.movies.model.Movie;

import java.lang.reflect.Type;

/**
 * Created by kleyton on 22/05/17.
 */

public class MovieJsonDeserializer implements JsonDeserializer<Movie> {

    @Override
    public Movie deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final JsonElement jsonId = jsonObject.get("id");
        final JsonElement jsonTitle = jsonObject.get("title");
        final JsonElement jsonReleaseDate = jsonObject.get("release_date");
        final JsonElement jsonStatus = jsonObject.get("status");
        final JsonElement jsonRuntime = jsonObject.get("runtime");
        final JsonElement jsonGenre = jsonObject.get("genres");
        final JsonElement jsonOverview = jsonObject.get("overview");
        final JsonElement jsonOriginalLanguage = jsonObject.get("original_language");
        final JsonElement jsonPosterPath = jsonObject.get("poster_path");
        final JsonElement jsonVoteAverage = jsonObject.get("vote_average");
        final JsonElement jsonVoteCount = jsonObject.get("vote_count");
        final JsonElement jsonBudget = jsonObject.get("budget");
        final JsonElement jsonProductionCompanies = jsonObject.get("production_companies");
        final JsonElement jsonHomepage = jsonObject.get("homepage");

        final Movie movie = new Movie();
        movie.id = jsonId.getAsLong();
        movie.title = jsonTitle.getAsString();
        movie.releaseDate = jsonReleaseDate.getAsString();
        movie.status = jsonStatus.getAsString();
        movie.runtime = jsonRuntime.getAsString();
        movie.genre = parserListToString(jsonGenre, "name");
        movie.overview = jsonOverview.getAsString();
        movie.language = jsonOriginalLanguage.getAsString();
        movie.posterPath = jsonPosterPath.getAsString();
        movie.voteAverage = jsonVoteAverage.getAsString();
        movie.voteCount = jsonVoteCount.getAsString();
        movie.budget = jsonBudget.getAsString();
        movie.production = parserListToString(jsonProductionCompanies, "name");
        movie.homepage = jsonHomepage.getAsString();

        return movie;
    }

    private String parserListToString(JsonElement jsonElement, String propName) {
        String value = "";
        if (jsonElement != null) {
            final JsonArray jsonAuthorsArray = jsonElement.getAsJsonArray();
            final int size = jsonAuthorsArray.size();
            if (size > 0) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < size; i++) {
                    final JsonElement json = jsonAuthorsArray.get(i);
                    builder.append(json.getAsJsonObject().get(propName).getAsString());
                    if (i != size - 1) {
                        builder.append(", ");
                    }
                }
                value = builder.toString();
            }
        }
        return value;
    }
}
