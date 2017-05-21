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

    @SerializedName("imdbID")
    @DatabaseField(id = true, columnName = TablesNames.Movie.IMDBID)
    public String imdbID;

    @SerializedName("Title")
    @DatabaseField(columnName = TablesNames.Movie.TITLE)
    public String title;

    @SerializedName("Year")
    @DatabaseField(columnName = TablesNames.Movie.YEAR)
    public String year;

    @SerializedName("Rated")
    @DatabaseField(columnName = TablesNames.Movie.RATED)
    public String rated;

    @SerializedName("Released")
    @DatabaseField(columnName = TablesNames.Movie.RELEASED)
    public String released;

    @SerializedName("Runtime")
    @DatabaseField(columnName = TablesNames.Movie.RUNTIME)
    public String runtime;

    @SerializedName("Genre")
    @DatabaseField(columnName = TablesNames.Movie.GENRE)
    public String genre;

    @SerializedName("Director")
    @DatabaseField(columnName = TablesNames.Movie.DIRECTOR)
    public String director;

    @SerializedName("Writer")
    @DatabaseField(columnName = TablesNames.Movie.WRITER)
    public String writer;

    @SerializedName("Actors")
    @DatabaseField(columnName = TablesNames.Movie.ACTORS)
    public String actors;

    @SerializedName("Plot")
    @DatabaseField(columnName = TablesNames.Movie.PLOT)
    public String plot;

    @SerializedName("Language")
    @DatabaseField(columnName = TablesNames.Movie.LANGUAGE)
    public String language;

    @SerializedName("Country")
    @DatabaseField(columnName = TablesNames.Movie.COUNTRY)
    public String country;

    @SerializedName("Awards")
    @DatabaseField(columnName = TablesNames.Movie.AWARDS)
    public String awards;

    @SerializedName("Poster")
    @DatabaseField(columnName = TablesNames.Movie.POSTER)
    public String poster;

    @SerializedName("Metascore")
    @DatabaseField(columnName = TablesNames.Movie.METASCORE)
    public String metascore;

    @SerializedName("imdbRating")
    @DatabaseField(columnName = TablesNames.Movie.IMDBRATING)
    public String imdbRating;

    @SerializedName("imdbVotes")
    @DatabaseField(columnName = TablesNames.Movie.IMDBVOTES)
    public String imdbVotes;

    @SerializedName("Type")
    @DatabaseField(columnName = TablesNames.Movie.TYPE)
    public String type;

    @SerializedName("DVD")
    @DatabaseField(columnName = TablesNames.Movie.DVD)
    public String dvd;

    @SerializedName("BoxOffice")
    @DatabaseField(columnName = TablesNames.Movie.BOXOFFICE)
    public String boxOffice;

    @SerializedName("Production")
    @DatabaseField(columnName = TablesNames.Movie.PRODUCTION)
    public String production;

    @SerializedName("Website")
    @DatabaseField(columnName = TablesNames.Movie.WEBSITE)
    public String website;

    @SerializedName("Response")
    @DatabaseField(columnName = TablesNames.Movie.RESPONSE)
    public boolean response;

    @DatabaseField(columnName = TablesNames.Movie.FAVORITE)
    public boolean favorite;

    public Movie() {
        response = false;
        favorite = false;
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

        return  TextUtils.equals(imdbID, another.imdbID) &&
                TextUtils.equals(title, another.title) &&
                TextUtils.equals(year, another.year) &&
                TextUtils.equals(rated, another.rated) &&
                TextUtils.equals(released, another.released) &&
                TextUtils.equals(runtime, another.runtime) &&
                TextUtils.equals(genre, another.genre) &&
                TextUtils.equals(director, another.director) &&
                TextUtils.equals(writer, another.writer) &&
                TextUtils.equals(actors, another.actors) &&
                TextUtils.equals(plot, another.plot) &&
                TextUtils.equals(language, another.language) &&
                TextUtils.equals(country, another.country) &&
                TextUtils.equals(awards, another.awards) &&
                TextUtils.equals(poster, another.poster) &&
                TextUtils.equals(metascore, another.metascore) &&
                TextUtils.equals(imdbRating, another.imdbRating) &&
                TextUtils.equals(imdbVotes, another.imdbVotes) &&
                TextUtils.equals(type, another.type) &&
                TextUtils.equals(dvd, another.dvd) &&
                TextUtils.equals(boxOffice, another.boxOffice) &&
                TextUtils.equals(production, another.production) &&
                TextUtils.equals(website, another.website) &&
                response == another.response &&
                favorite == another.favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imdbID);
        dest.writeString(isEmpty(title)     ? "N/A" : title);
        dest.writeString(isEmpty(year)      ? "N/A" : year);
        dest.writeString(isEmpty(rated)     ? "N/A" : rated);
        dest.writeString(isEmpty(released)   ? "N/A" : released);
        dest.writeString(isEmpty(runtime)   ? "N/A" : runtime);
        dest.writeString(isEmpty(genre)     ? "N/A" : genre);
        dest.writeString(isEmpty(director)  ? "N/A" : director);
        dest.writeString(isEmpty(writer)    ? "N/A" : writer);
        dest.writeString(isEmpty(actors)    ? "N/A" : actors);
        dest.writeString(isEmpty(plot)      ? "N/A" : plot);
        dest.writeString(isEmpty(language)  ? "N/A" : language);
        dest.writeString(isEmpty(country)   ? "N/A" : country);
        dest.writeString(isEmpty(awards)    ? "N/A" : awards);
        dest.writeString(isEmpty(poster)    ? "N/A" : poster);
        dest.writeString(isEmpty(metascore) ? "N/A" : metascore);
        dest.writeString(isEmpty(imdbRating)? "N/A" : imdbRating);
        dest.writeString(isEmpty(imdbVotes) ? "N/A" : imdbVotes);
        dest.writeString(isEmpty(type)      ? "N/A" : type);
        dest.writeString(isEmpty(dvd)       ? "N/A" : dvd);
        dest.writeString(isEmpty(boxOffice) ? "N/A" : boxOffice);
        dest.writeString(isEmpty(production)? "N/A" : production);
        dest.writeString(isEmpty(website)   ? "N/A" : website);
        dest.writeInt(response ? 1 : 0);
        dest.writeInt(favorite ? 1 : 0);
    }

    protected Movie(Parcel in) {
        imdbID = in.readString();
        title = in.readString();
        year = in.readString();
        rated = in.readString();
        released = in.readString();
        runtime = in.readString();
        genre = in.readString();
        director = in.readString();
        writer = in.readString();
        actors = in.readString();
        plot = in.readString();
        language = in.readString();
        country = in.readString();
        awards = in.readString();
        poster = in.readString();
        metascore = in.readString();
        imdbRating = in.readString();
        imdbVotes = in.readString();
        type = in.readString();
        dvd = in.readString();
        boxOffice = in.readString();
        production = in.readString();
        website = in.readString();
        response = in.readInt() == 1;
        favorite = in.readInt() == 1;
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
