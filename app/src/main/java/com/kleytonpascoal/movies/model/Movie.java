package com.kleytonpascoal.movies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.kleytonpascoal.movies.persistence.TablesNames;

import static android.text.TextUtils.isEmpty;

/**
 * Created by kleyton on 09/05/17.
 */

@DatabaseTable(tableName = TablesNames.Movie.TABLE)
public class Movie implements Parcelable {

    @SerializedName("id")
    @DatabaseField(id = true, columnName = TablesNames.Movie.ID)
    public long id;

    @SerializedName("title")
    @DatabaseField(columnName = TablesNames.Movie.TITLE)
    public String title;

    @SerializedName("release_date")
    @DatabaseField(columnName = TablesNames.Movie.RELEASE_DATE)
    public String releaseDate;

    @SerializedName("status")
    @DatabaseField(columnName = TablesNames.Movie.STATUS)
    public String status;

    @SerializedName("runtime")
    @DatabaseField(columnName = TablesNames.Movie.RUNTIME)
    public String runtime;

    @SerializedName("Genre")
    @DatabaseField(columnName = TablesNames.Movie.GENRE)
    public String genre;

    @SerializedName("overview")
    @DatabaseField(columnName = TablesNames.Movie.OVERVIEW)
    public String overview;

    @SerializedName("original_language")
    @DatabaseField(columnName = TablesNames.Movie.LANGUAGE)
    public String language;

    @SerializedName("poster_path")
    @DatabaseField(columnName = TablesNames.Movie.POSTER_PATH)
    public String posterPath;

    @SerializedName("vote_average")
    @DatabaseField(columnName = TablesNames.Movie.VOTE_AVERAGE)
    public String voteAverage;

    @SerializedName("vote_count")
    @DatabaseField(columnName = TablesNames.Movie.IMDBVOTES)
    public String voteCount;

    @SerializedName("budget")
    @DatabaseField(columnName = TablesNames.Movie.BUDGET)
    public String budget;

    @SerializedName("production_companies")
    @DatabaseField(columnName = TablesNames.Movie.PRODUCTION)
    public String production;

    @SerializedName("homepage")
    @DatabaseField(columnName = TablesNames.Movie.HOMEPAGE)
    public String homepage;

    public Movie() {
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || !(obj instanceof Movie))
            return false;

        Movie another = (Movie) obj;

        return  id == another.id &&
                TextUtils.equals(title, another.title) &&
                TextUtils.equals(releaseDate, another.releaseDate) &&
                TextUtils.equals(status, another.status) &&
                TextUtils.equals(runtime, another.runtime) &&
                TextUtils.equals(genre, another.genre) &&
                TextUtils.equals(overview, another.overview) &&
                TextUtils.equals(language, another.language) &&
                TextUtils.equals(posterPath, another.posterPath) &&
                TextUtils.equals(voteAverage, another.voteAverage) &&
                TextUtils.equals(voteCount, another.voteCount) &&
                TextUtils.equals(budget, another.budget) &&
                TextUtils.equals(production, another.production) &&
                TextUtils.equals(homepage, another.homepage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(isEmpty(title)     ? "N/A" : title);
        dest.writeString(isEmpty(releaseDate)      ? "N/A" : releaseDate);
        dest.writeString(isEmpty(status)   ? "N/A" : status);
        dest.writeString(isEmpty(runtime)   ? "N/A" : runtime);
        dest.writeString(isEmpty(genre)     ? "N/A" : genre);
        dest.writeString(isEmpty(overview)      ? "N/A" : overview);
        dest.writeString(isEmpty(language)  ? "N/A" : language);
        dest.writeString(isEmpty(posterPath)    ? "N/A" : posterPath);
        dest.writeString(isEmpty(voteAverage)? "N/A" : voteAverage);
        dest.writeString(isEmpty(voteCount) ? "N/A" : voteCount);
        dest.writeString(isEmpty(budget) ? "N/A" : budget);
        dest.writeString(isEmpty(production)? "N/A" : production);
        dest.writeString(isEmpty(homepage)   ? "N/A" : homepage);
    }

    protected Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        releaseDate = in.readString();
        status = in.readString();
        runtime = in.readString();
        genre = in.readString();
        overview = in.readString();
        language = in.readString();
        posterPath = in.readString();
        voteAverage = in.readString();
        voteCount = in.readString();
        budget = in.readString();
        production = in.readString();
        homepage = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
