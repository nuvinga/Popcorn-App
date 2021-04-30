package com.example.courseworktwo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openRegisterActivity(View view){
        Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    public void openDisplayActivity(View view) {
        Intent displayIntent = new Intent(MainActivity.this, DisplayActivity.class);
        startActivity(displayIntent);
    }

    public void openFavouriteActivity(View view) {
        Intent favouriteActivity = new Intent(MainActivity.this, FavouriteActivity.class);
        startActivity(favouriteActivity);
    }

    public void openEditActivity(View view) {
        Intent editActivity = new Intent(MainActivity.this, EditActivity.class);
        startActivity(editActivity);
    }

    public void openSearchActivity(View view) {
        Intent searchActivity = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(searchActivity);
    }

    public void openRatingsActivity(View view) {
        Intent ratingsActivity = new Intent(MainActivity.this, RatingsActivity.class);
        startActivity(ratingsActivity);
    }
}