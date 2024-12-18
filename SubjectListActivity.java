package com.example.final_exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubjectListActivity extends AppCompatActivity {

    private LinearLayout subjectContainer;
    private TextView totalCreditsTextView;
    private Button submitButton;
    private int totalCredits = 0;
    private final int CREDITS_PER_SUBJECT = 4;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        subjectContainer = findViewById(R.id.subjectContainer);
        totalCreditsTextView = findViewById(R.id.totalCreditsTextView);
        submitButton = findViewById(R.id.submitButton);
        db = FirebaseFirestore.getInstance();

        String selectedClass = getIntent().getStringExtra("SELECTED_CLASS");
        String[][] subjects = getSubjectsForClass(selectedClass);

        populateSubjectList(subjects);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSelectedSubjects();
            }
        });
    }

    private String[][] getSubjectsForClass(String selectedClass) {
        switch (selectedClass) {
            case "Cyber Security Class":
                return new String[][]{
                        {"Network Security"},
                        {"Cryptography"},
                        {"Ethical Hacking"},
                        {"Malware Analysis"},
                        {"Incident Response"},
                        {"Security Policies"},
                        {"Risk Management"}
                };
            case "Game Development Class":
                return new String[][]{
                        {"Advanced Game Programming"},
                        {"Extended Reality"},
                        {"Game Asset and Design"},
                        {"Game Intelligence"},
                        {"Game Programming"},
                        {"Game Project"}
                };
            default:
                return new String[][]{};
        }
    }

    private void populateSubjectList(String[][] subjects) {
        for (String[] subject : subjects) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(subject[0] + " (" + CREDITS_PER_SUBJECT + " credits)");

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (totalCredits + CREDITS_PER_SUBJECT > 24) {
                        Toast.makeText(this, "You can only select up to 24 credits.", Toast.LENGTH_SHORT).show();
                        checkBox.setChecked(false); // Uncheck the checkbox
                    } else {
                        totalCredits += CREDITS_PER_SUBJECT;
                    }
                } else {
                    totalCredits -= CREDITS_PER_SUBJECT;
                }
                totalCreditsTextView.setText("Total Credits: " + totalCredits);
            });

            subjectContainer.addView(checkBox);
        }
    }


    private void submitSelectedSubjects() {
        ArrayList<Map<String, Object>> selectedSubjects = new ArrayList<>();

        for (int i = 0; i < subjectContainer.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) subjectContainer.getChildAt(i);
            if (checkBox.isChecked()) {
                String subjectName = checkBox.getText().toString().split(" \\$")[0];
                Map<String, Object> subjectData = new HashMap<>();
                subjectData.put("subjectName", subjectName);
                subjectData.put("credits", CREDITS_PER_SUBJECT);
                selectedSubjects.add(subjectData);
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selectedClass = sharedPreferences.getString("SELECTED_CLASS", null);

        Map<String, Object> enrollmentData = new HashMap<>();
        enrollmentData.put("class", selectedClass);
        enrollmentData.put("subjects", selectedSubjects);

        db.collection("enrollments")
                .add(enrollmentData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(SubjectListActivity.this, "Enrollment data submitted!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SubjectListActivity.this, EnrollmentSummaryActivity.class);
                    intent.putExtra("SELECTED_SUBJECTS", selectedSubjects);
                    intent.putExtra("SELECTED_CLASS", selectedClass);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SubjectListActivity.this, "Error submitting enrollment data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }




}
