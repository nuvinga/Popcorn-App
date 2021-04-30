package com.example.courseworktwo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {

    public static final String LOG_TAG = "FAVOURITE_ACTIVITY: ";

    private ListView listView;
    private Button saveButton;
    private TextView textView;
    private DatabaseController dbController;
    private SQLiteDatabase sqlConnection;
    private ArrayList<String> titleList = new ArrayList<>();
    private SparseBooleanArray booleanArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        dbController = new DatabaseController(this.getApplicationContext());
        sqlConnection = dbController.getWritableDatabase();

        listView = (ListView) findViewById(R.id.favourites_list);
        saveButton = (Button) findViewById(R.id.favouriteSave_button);
        textView = (TextView) findViewById(R.id.noData_show);

        textView.setVisibility(View.INVISIBLE);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        saveButton.setVisibility(View.INVISIBLE);

        initializeListView();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            boolean check = false;
            booleanArray = listView.getCheckedItemPositions();
            for (int i = 0; i < booleanArray.size(); i++) {
                if (!booleanArray.valueAt(i)) {
                    check = true;
                }
            }
            if (check) {
                saveButton.setVisibility(View.VISIBLE);
            } else {
                saveButton.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void initializeListView() {
        saveButton.setVisibility(View.INVISIBLE);
        try {
            titleList = dbController.getFavouriteMovies();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, titleList);
            listView.setAdapter(arrayAdapter);
            for (int i = 0; i < titleList.size(); i++) {
                listView.setItemChecked(i, true);
            }
        } catch (NullPointerException npe) {
            listView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "EXCEPTION: ");
            e.printStackTrace();
        }
    }

    public void saveChanges(View view) {
        SparseBooleanArray booleanArray = listView.getCheckedItemPositions();
        for (int i = 0; i < booleanArray.size(); i++) {
            if (!booleanArray.valueAt(i)) {
                sqlConnection.execSQL("UPDATE MOVIES SET FAVOURITES = 0 WHERE TITLE = '" + listView.getItemAtPosition(i) + "';");
            }
        }
        initializeListView();
    }
}