package com.example.courseworktwo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText titleInput;
    private EditText yearInput;
    private EditText directorInput;
    private EditText castInput;
    private EditText ratingInput;
    private EditText reviewInput;
    private String titleString;
    private int yearInt;
    private String directorString;
    private String castString;
    private int ratingInt;
    private String reviewString;
    private Button saveButton;
    private Button titleError;
    private Button yearError;
    private Button directorError;
    private Button castError;
    private Button ratingError;
    private Button reviewError;
    private TextView errorText;
    private DatabaseController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        titleInput = (EditText) findViewById(R.id.title_input);
        yearInput = (EditText) findViewById(R.id.production_input);
        directorInput = (EditText) findViewById(R.id.director_input);
        castInput = (EditText) findViewById(R.id.cast_input);
        ratingInput = (EditText) findViewById(R.id.rating_input);
        reviewInput = (EditText) findViewById(R.id.review_input);

        saveButton = (Button) findViewById(R.id.save_button);
        titleError = (Button) findViewById(R.id.title_error_button);
        yearError = (Button) findViewById(R.id.year_error_button);
        directorError = (Button) findViewById(R.id.director_error_button);
        castError = (Button) findViewById(R.id.cast_error_button);
        ratingError = (Button) findViewById(R.id.rating_error_button);
        reviewError = (Button) findViewById(R.id.review_error_button);

        errorText = (TextView) findViewById(R.id.error_text);

        dbController = new DatabaseController(this.getApplicationContext());

        titleError.setVisibility(View.INVISIBLE);
        yearError.setVisibility(View.INVISIBLE);
        directorError.setVisibility(View.INVISIBLE);
        castError.setVisibility(View.INVISIBLE);
        ratingError.setVisibility(View.INVISIBLE);
        reviewError.setVisibility(View.INVISIBLE);

        errorText.setVisibility(View.INVISIBLE);

    }

    public void save(View view) {
        try {
            if (!(titleInput.getText().toString().trim().isEmpty() ||
                    yearInput.getText().toString().trim().isEmpty() ||
                    directorInput.getText().toString().trim().isEmpty() ||
                    castInput.getText().toString().trim().isEmpty() ||
                    ratingInput.getText().toString().trim().isEmpty() ||
                    reviewInput.getText().toString().trim().isEmpty())) {
                // checking for all the mandatory fields

                if ((Integer.parseInt(yearInput.getText().toString().trim()) >= 1895) && // validating for the year
                        (Integer.parseInt(yearInput.getText().toString().trim()) <= 2030)) {

                    if ((Integer.parseInt(ratingInput.getText().toString().trim()) >= 0) && // validating got the rate
                            (Integer.parseInt(ratingInput.getText().toString()) <= 10)) {

                        if (!dbController.checkTitleAvailability(titleInput.getText().toString().trim().toLowerCase())) { // validating title availability

                            // getting a loop back inorder to validate save
                            boolean result = dbController.addData(titleInput.getText().toString(),
                                    Integer.parseInt(yearInput.getText().toString()),
                                    directorInput.getText().toString(),
                                    castInput.getText().toString(),
                                    Integer.parseInt(ratingInput.getText().toString()),
                                    reviewInput.getText().toString());
                            if (!result) {
                                Log.e("DB-CONTROLLER:", "FAILED TO SAVE DATA");
                                Toast.makeText(this.getApplicationContext(), "Cannot save!", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                                alert.setTitle("DATA SAVE SUCCESSFUL!");
                                alert.setMessage("Data has been successfully entered to the database!");
                                alert.show();
//                                alert.wait(1000);
                                finish();
                            }
                        } else {
                            Toast.makeText(this.getApplicationContext(), "Title name already exists", Toast.LENGTH_SHORT).show();
                            titleError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(this.getApplicationContext(), "Rating has to be within 0 and 10", Toast.LENGTH_SHORT).show();
                        ratingError.setVisibility(View.VISIBLE);

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
            e.printStackTrace();
        }

    }

    public void titleHints(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Title doesn't meet requirements");
        alert.setMessage("The title has to be unique and is a must to input.\n\nPlease reconsider your inputs.");
        alert.show();
    }

    public void yearHints(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Year unacceptable");
        alert.setMessage("The date has to be limited from 1895 to 2030.\n\nPlease reconsider your inputs.");
        alert.show();
    }


    public void ratingHints(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Rating unacceptable");
        alert.setMessage("The rating has be a whole number within 0 and 10.\n\nPlease reconsider your inputs.");
        alert.show();
    }
}