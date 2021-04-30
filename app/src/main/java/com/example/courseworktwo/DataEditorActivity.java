package com.example.courseworktwo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class DataEditorActivity extends AppCompatActivity {

    public static final String EXTRA_TAG = "MOVIES_EXTRA";
    private String intentString;
    private EditText titleInput;
    private EditText yearInput;
    private EditText directorInput;
    private EditText castInput;
    private RatingBar ratingBar;
    private EditText reviewInput;
    private CheckBox favouriteCheckBox;
    private Button saveButton;
    private Button titleError;
    private Button yearError;
    private Button directorError;
    private Button castError;
    private Button reviewError;
    private TextView errorText;
    private String titleString;
    private int yearInt;
    private String directorString;
    private String castString;
    private int ratingInt;
    private String reviewString;
    private boolean favourite;
    private DatabaseController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        intentString = intent.getStringExtra(EXTRA_TAG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_editor);

        titleInput = (EditText) findViewById(R.id.title_input_editor);
        yearInput = (EditText) findViewById(R.id.production_input_editor);
        directorInput = (EditText) findViewById(R.id.director_input_editor);
        castInput = (EditText) findViewById(R.id.cast_input_editor);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar_editor);
        reviewInput = (EditText) findViewById(R.id.review_input_editor);
        favouriteCheckBox = (CheckBox) findViewById(R.id.checkedTextView_editor);

        saveButton = (Button) findViewById(R.id.save_button_editor);
        titleError = (Button) findViewById(R.id.title_error_button_editor);
        yearError = (Button) findViewById(R.id.year_error_button_editor);
        directorError = (Button) findViewById(R.id.director_error_button_editor);
        castError = (Button) findViewById(R.id.cast_error_button_editor);
        reviewError = (Button) findViewById(R.id.review_error_button_editor);

        errorText = (TextView) findViewById(R.id.error_text_editor);

        dbController = new DatabaseController(this.getApplicationContext());

        titleError.setVisibility(View.INVISIBLE);
        yearError.setVisibility(View.INVISIBLE);
        directorError.setVisibility(View.INVISIBLE);
        castError.setVisibility(View.INVISIBLE);
        reviewError.setVisibility(View.INVISIBLE);

        errorText.setVisibility(View.INVISIBLE);

        // extracting data from the cursor to the the current text feilds in the editor
        Cursor data = dbController.getMovieData(intentString);
        data.moveToNext();
        titleString = data.getString(1);
        yearInt = data.getInt(2);
        directorString = data.getString(3);
        castString = data.getString(4);
        ratingInt = data.getInt(5);
        reviewString = data.getString(6);
        favourite = data.getInt(7) != 0;

        titleInput.setText(titleString);
        yearInput.setText(String.valueOf(yearInt));
        directorInput.setText(directorString);
        castInput.setText(castString);
        ratingBar.setRating(ratingInt);
        reviewInput.setText(reviewString);
        favouriteCheckBox.setChecked(favourite);
    }

    public void saveUpdates(View view) {
        try {
            if (!(titleInput.getText().toString().trim().isEmpty() ||
                    yearInput.getText().toString().trim().isEmpty() ||
                    directorInput.getText().toString().trim().isEmpty() ||
                    castInput.getText().toString().trim().isEmpty() ||
                    reviewInput.getText().toString().trim().isEmpty())) {

                if ((Integer.parseInt(yearInput.getText().toString().trim()) >= 1895) &&
                        (Integer.parseInt(yearInput.getText().toString().trim()) <= 2030)) {

                    if (!dbController.checkTitleAvailabilityForEditing(intentString, titleInput.getText().toString().trim().toLowerCase())) {
                        int favInt;
                        if (favouriteCheckBox.isChecked()) favInt = 1; else favInt = 0;
                        dbController.updateData(intentString, titleInput.getText().toString(),
                                Integer.parseInt(yearInput.getText().toString()),
                                directorInput.getText().toString(),
                                castInput.getText().toString(),
                                (int) ratingBar.getRating(),
                                reviewInput.getText().toString(),
                                favInt);
                        finish();
                    } else {
                        Toast.makeText(this.getApplicationContext(), "Title name already exists", Toast.LENGTH_SHORT).show();
                        titleError.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(this.getApplicationContext(), "Year has to be within 1895 and 2030", Toast.LENGTH_SHORT).show();
                    yearError.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this.getApplicationContext(), "Please fill all the fields!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(this.getApplicationContext(), "Cannot save! Please re-check", Toast.LENGTH_SHORT).show();
        }
    }
}