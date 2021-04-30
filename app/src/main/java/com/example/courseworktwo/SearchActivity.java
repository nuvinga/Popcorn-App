package com.example.courseworktwo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyboardShortcutInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private EditText criteria;
    private ListView listView;
    private TextView noResults;
    private DatabaseController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        criteria = (EditText) findViewById(R.id.search_input);
        listView = (ListView) findViewById(R.id.search_list);
        noResults = (TextView) findViewById(R.id.no_search_results);
        dbController = new DatabaseController(this);
        noResults.setVisibility(View.INVISIBLE);

    }

    public void lookup(View view) {
        try {
            noResults.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            ArrayList<String> resultList = dbController.getSearchResults(criteria.getText().toString().toLowerCase().trim());
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultList);
            listView.setAdapter(arrayAdapter);
        } catch (Exception npe) {
            listView.setVisibility(View.INVISIBLE);
            noResults.setVisibility(View.VISIBLE);
            npe.printStackTrace();
        }

    }

}