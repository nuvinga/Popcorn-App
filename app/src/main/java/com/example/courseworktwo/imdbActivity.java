package com.example.courseworktwo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class imdbActivity extends AppCompatActivity {

    private String intentString;
    private ListView imdbList;
    private String privateKey = "k_hogmxuh4";
    private String[] movieNames;
    private String[] movieURL;
    private String[] movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imdb);

        Intent intent = getIntent();
        intentString = intent.getStringExtra("IMDBMOVIE");
        Log.d("output", intentString);

        imdbList = findViewById(R.id.imdbList);
        searchIMDB(intentString);

    }

    private void searchIMDB(String movieName) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                StringBuilder connectionResult = new StringBuilder();

                try {
                    URL imdbUrl = new URL("https://imdb-api.com/en/API/SearchTitle/" + privateKey + "/" + movieName);
                    HttpURLConnection connection = (HttpURLConnection) imdbUrl.openConnection();

                    BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = bf.readLine()) != null) {
                        connectionResult.append(line);
                    }

                    String resultString = connectionResult.toString();

                    JSONObject jsonResult = new JSONObject(resultString);
                    JSONArray movies = jsonResult.getJSONArray("results");

                    Log.d("OUTPUT", jsonResult.toString());
                    movieNames = new String[movies.length()];
                    movieURL = new String[movies.length()];
                    movieID = new String[movies.length()];

                    for(int i = 0; i < movies.length(); i++){

                        JSONObject movie = movies.getJSONObject(i);
                        String movieName = movie.getString("title");
                        String movieUrl = movie.getString("image");
                        String movieId = movie.getString("id");

                        String rating = ratingMovie(movieId);

                        Log.d("OUTPUT", movieName + " " + movieUrl + " " + movieId);
                        movieID[i] = movieId;
                        movieNames[i] = movieName + "\t\t" + rating;
                        movieURL[i] = movieUrl;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, movieNames);
                        imdbList.setAdapter(arrayAdapter);
                    }
                });
            }
        });

        thread.start();

    }

    private String ratingMovie(String movieID) {

        StringBuilder connectionResult = new StringBuilder();
        String movieRating = "";

        try {
            URL imdbUrl = new URL("https://imdb-api.com/en/API/UserRatings/" + privateKey + "/" + movieID);
            HttpURLConnection connection = (HttpURLConnection) imdbUrl.openConnection();

            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bf.readLine()) != null) {
                connectionResult.append(line);
            }

            String resultString = connectionResult.toString();

            JSONObject jsonResult = new JSONObject(resultString);
            movieRating = jsonResult.getString("totalRating");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return movieRating;
    }


}