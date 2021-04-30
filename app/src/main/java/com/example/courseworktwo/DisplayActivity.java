package com.example.courseworktwo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {

    public static final String LOG_TAG = "DISPLAY_ACTIVITY: ";

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
        setContentView(R.layout.activity_display);

        dbController = new DatabaseController(this.getApplicationContext());
        sqlConnection = dbController.getWritableDatabase();

        listView = (ListView) findViewById(R.id.display_list);
        addButton = (Button) findViewById(R.id.addToFavs_button);
        textView = (TextView) findViewById(R.id.display_noData);

        textView.setVisibility(View.INVISIBLE);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        addButton.setVisibility(View.INVISIBLE);

        initializeListView();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            boolean check = false;
            booleanArray = listView.getCheckedItemPositions();
            for (int i = 0; i < booleanArray.size(); i++) {
                if (booleanArray.valueAt(i)) {
                    check = true;
                }
            }
            if (check) {
                addButton.setVisibility(View.VISIBLE);
            } else {
                addButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void initializeListView() {
        addButton.setVisibility(View.INVISIBLE);
        ArrayList<Integer> favouriteList = new ArrayList<>();
        try {
            data = dbController.getDisplayMovies();
            ArrayList<String> returnList = new ArrayList<>();
            favouriteList = new ArrayList<>();
            data.moveToNext();
            do { // the titles list if updated through the cursor along with the favourites list
                returnList.add(data.getString(0));
                favouriteList.add(data.getInt(1));
            } while (data.moveToNext());
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, returnList);
            listView.setAdapter(arrayAdapter);
            Log.e("GET COUNT: ", String.valueOf(arrayAdapter.getCount()));
        } catch (NullPointerException npe) {
            listView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "EXCEPTION: ");
            e.printStackTrace();
        }
    }

    public void addToFavourites(View view) {
        for (int i = 0; i < listView.getCount(); i++) {
            if (listView.isItemChecked(i)) {
                sqlConnection.execSQL("UPDATE MOVIES " +
                        "SET FAVOURITES = 1 " +
                        "WHERE TITLE = '" + listView.getItemAtPosition(i) + "';");
            }
        }

        initializeListView();
    }

}