package com.example.courseworktwo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class RatingsActivity extends AppCompatActivity {

    private ListView listView;
    private Button addButton;
    private TextView textView;
    private DatabaseController dbController;
    private SQLiteDatabase sqlConnection;
    private Cursor data;
    private SparseBooleanArray booleanArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        dbController = new DatabaseController(this.getApplicationContext());
        sqlConnection = dbController.getWritableDatabase();

        listView = (ListView) findViewById(R.id.ratings_list);
        addButton = (Button) findViewById(R.id.view_imdb);
        textView = (TextView) findViewById(R.id.rating_noData);

        textView.setVisibility(View.INVISIBLE);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        addButton.setVisibility(View.INVISIBLE);

        initializeListView();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            addButton.setVisibility(View.VISIBLE);
        });

    }

    private void initializeListView() {
        addButton.setVisibility(View.INVISIBLE);
        ArrayList<Integer> favouriteList = new ArrayList<>();
        try {
            data = dbController.getDisplayMovies();
            ArrayList<String> returnList = new ArrayList<>();
            favouriteList = new ArrayList<>();
            data.moveToNext();
            do {
                returnList.add(data.getString(0));
                favouriteList.add(data.getInt(1));
            } while (data.moveToNext());
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, returnList);
            listView.setAdapter(arrayAdapter);
        } catch (NullPointerException npe) {
            listView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e("OOF", "EXCEPTION: ");
            e.printStackTrace();
        }
    }

    public void openSingleMovie(View view) {
        String checkedItem = null;
        for(int i = 0; i < listView.getCount(); i++){
            if(listView.isItemChecked(i)) {
                checkedItem = (String) listView.getItemAtPosition(i);
            }
        }

        Intent lookupMovie = new Intent(this, imdbActivity.class);
        lookupMovie.putExtra("IMDBMOVIE",  checkedItem);
        startActivity(lookupMovie);
    }

}