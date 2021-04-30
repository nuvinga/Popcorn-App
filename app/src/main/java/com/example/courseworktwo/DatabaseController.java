package com.example.courseworktwo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.security.spec.ECField;
import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public class DatabaseController extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "MOVIES";
    private static final String TITLE_CLM = "TITLE";
    private static final String YEAR_CLM = "YEAR";
    private static final String DIRECTOR_CLM = "DIRECTOR";
    private static final String CAST_CLM = "CASTING";
    private static final String RATING_CLM = "RATING";
    private static final String REVIEW_CLM = "REVIEW";
    private static final String FAVOURITE_CLM = "FAVOURITES";
    private SQLiteDatabase database = getWritableDatabase();


    public DatabaseController(Context context) {
        super(context, "movies.db", null, 7);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE_CLM + " VARCHAR NOT NULL," +
                YEAR_CLM + " INTEGER NOT NULL," +
                DIRECTOR_CLM + " VARCHAR NOT NULL," +
                CAST_CLM + " LONGTEXT NOT NULL," +
                RATING_CLM + " TINYINT NOT NULL," +
                REVIEW_CLM + " LONGTEXT NOT NULL," +
                FAVOURITE_CLM + " INTEGER DEFAULT 0 )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String title, int year, String director, String cast, int rating, String review) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_CLM, title);
        contentValues.put(YEAR_CLM, year);
        contentValues.put(DIRECTOR_CLM, director);
        contentValues.put(CAST_CLM, cast);
        contentValues.put(RATING_CLM, rating);
        contentValues.put(REVIEW_CLM, review);
        contentValues.put(FAVOURITE_CLM, false);
        long result = database.insert(TABLE_NAME,null, contentValues);
        return result != -1;
    }

    public boolean checkTitleAvailability(String title) {
        database = this.getReadableDatabase();
        boolean check = false;
        Cursor titles = database.rawQuery("SELECT " + TITLE_CLM + ", " + FAVOURITE_CLM + " FROM " + TABLE_NAME, null);
        if (titles.getCount() > 0) {
            titles.moveToNext();
            do { // checking if name is present in the databse or if it s new database where a null pointer exception maybe thrown
                if (titles.getString(0).toLowerCase().equals(title)) check = true;
            } while (titles.moveToNext());
            return check;
        } else {
            return false;
        }
    }

    public boolean checkTitleAvailabilityForEditing(String originalTitle, String title) {
        database = this.getReadableDatabase();
        boolean check = false;
        Cursor titles = database.rawQuery("SELECT " + TITLE_CLM + ", " + FAVOURITE_CLM + " FROM " + TABLE_NAME, null);
        if (titles.getCount() > 0) {
            titles.moveToNext();
            do { // used for editing, where the name will alreayd exist int he database
                if ((titles.getString(0).toLowerCase().equals(title))) {
                    if (!(titles.getString(0).toLowerCase().equals(originalTitle.toLowerCase()))) {
                        check = true;
                    }
                }
            } while (titles.moveToNext());
            return check;
        } else {
            return false;
        }
    }

    public boolean updateData(String originalTitle, String title, int year, String director, String cast, int rating, String review, int favourite) {
        try {
            database = this.getWritableDatabase();
            database.execSQL("UPDATE " + TABLE_NAME +
                    " SET " + TITLE_CLM + " = '" + title + "', " +
                    YEAR_CLM + " = '" + year + "', " +
                    DIRECTOR_CLM + " = '" + director + "', " +
                    CAST_CLM + " = '" + cast + "', " +
                    RATING_CLM + " = '" + rating + "', " +
                    REVIEW_CLM + " = '" + review + "', " +
                    FAVOURITE_CLM + " = '" + favourite + "' " +
                    "WHERE " + TITLE_CLM + " = '" + originalTitle + "';");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Cursor getDisplayMovies() throws NullPointerException {
        database = this.getReadableDatabase();
        Cursor titles = database.rawQuery("SELECT " + TITLE_CLM + ", " + FAVOURITE_CLM + " FROM " + TABLE_NAME, null);
        if (titles.getCount() > 0) {
            return titles;
        } else {
            throw new NullPointerException();
        }
    }

    public ArrayList<String> getFavouriteMovies() throws NullPointerException {
        database = this.getReadableDatabase();
        ArrayList<String> returnList = new ArrayList<>();
        Cursor data = database.rawQuery("SELECT " + TITLE_CLM + ", " + FAVOURITE_CLM + " FROM " + TABLE_NAME, null);
        data.moveToNext();
        do {
            if (data.getInt(1) == 1) {
                returnList.add(data.getString(0));
            }
        } while (data.moveToNext());
        if (returnList.size() > 0) {
            return returnList;
        } else {
            throw new NullPointerException();
        }
    }

    public ArrayList<String> getEditTitleList() throws NullPointerException {
        database = this.getReadableDatabase();
        ArrayList<String> returnList = new ArrayList<>();
        Cursor data = database.rawQuery("SELECT " + TITLE_CLM + " FROM " + TABLE_NAME, null);
        data.moveToNext();
        do {
            returnList.add(data.getString(0));
        } while (data.moveToNext());
        if (returnList.size() > 0) {
            return returnList;
        } else {
            throw new NullPointerException();
        }
    }

    public Cursor getMovieData(String movieName) {
        database = this.getReadableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + TITLE_CLM + " = '" + movieName + "'", null);
        return data;
    }

    public ArrayList<String> getSearchResults(String criteria) {
        database = this.getWritableDatabase();
        ArrayList<String> returnList = new ArrayList<>();
        Cursor data = database.rawQuery("SELECT " + TITLE_CLM + " FROM " + TABLE_NAME + " WHERE " +
                TITLE_CLM + " || " + DIRECTOR_CLM + " || " + CAST_CLM + " LIKE '%"+ criteria +"%'", null );
        if (data.getCount() > 0) {
            data.moveToNext();
            do {
                returnList.add(data.getString(0));
            } while (data.moveToNext());
            return returnList;
        } else {
            throw new NullPointerException();
        }
    }



}
