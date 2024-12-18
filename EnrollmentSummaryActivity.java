package com.example.final_exam;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class EnrollmentSummaryActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment_summary);

        tableLayout = findViewById(R.id.subjectTableLayout);
        logoutButton = findViewById(R.id.logoutButton);

        // Get the selected subjects and class from the intent
        ArrayList<String[]> selectedSubjects = (ArrayList<String[]>) getIntent().getSerializableExtra("SELECTED_SUBJECTS");
        String selectedClass = getIntent().getStringExtra("SELECTED_CLASS");

        // Populate the table with selected subjects
        populateTable(selectedSubjects);

        // Display the selected class
        TextView classTextView = new TextView(this);
        classTextView.setText("Selected Class: " + selectedClass);
        classTextView.setTextSize(18);
        classTextView.setPadding(16, 16, 16, 16);
        tableLayout.addView(classTextView, 0); // Add at the top of the table

        // Set up the logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EnrollmentSummaryActivity.this, Login_page.class));
                finish();
            }
        });
    }


    private void populateTable(ArrayList<String[]> subjects) {
        // Add header row
        TableRow headerRow = new TableRow(this);
        headerRow.addView(createTextView("Number"));
        headerRow.addView(createTextView("Subject"));
        headerRow.addView(createTextView("Credits"));
        tableLayout.addView(headerRow);

        // Add each subject to the table
        for (String[] subject : subjects) {
            TableRow tableRow = new TableRow(this);
            for (int j = 0; j < subject.length; j++) {
                tableRow.addView(createTextView(subject[j]));
            }
            tableLayout.addView(tableRow);
        }
    }


    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        return textView;
    }
}
