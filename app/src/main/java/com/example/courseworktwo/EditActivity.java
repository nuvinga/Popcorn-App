package com.example.courseworktwo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    public static final String LOG_TAG = "FAVOURITE_ACTIVITY: ";
    public static final String EXTRA_TAG = "MOVIES_EXTRA";

    private ListView listView;
    private TextView textView;
    private DatabaseController dbController;
    private SQLiteDatabase sqlConnection;
    private ArrayList<String> titleList = new ArrayList<>();
    private SparseBooleanArray booleanArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dbController = new DatabaseController(this.getApplicationContext());
        sqlConnection = dbController.getWritableDatabase();

        listView = (ListView) findViewById(R.id.edit_list);
        textView = (TextView) findViewById(R.id.edit_noData);

        textView.setVisibility(View.INVISIBLE);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        initializeListView();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            TextView clickedText = (TextView) view;
            Intent intent = new Intent(this, DataEditorActivity.class);
            intent.putExtra(EXTRA_TAG, clickedText.getText().toString());
            startActivity(intent);
        });

    }

    private void initializeListView() {
        try {
            titleList = dbController.getEditTitleList();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, titleList);
            listView.setAdapter(arrayAdapter);
        } catch (NullPointerException npe) {
            listView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "EXCEPTION: ");
            e.printStackTrace();
        }
    }
}