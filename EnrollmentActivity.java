package com.example.final_exam;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class EnrollmentActivity extends AppCompatActivity {

    private ListView classListView;

    private String[] classes = {"Cyber Security Class", "Game Development Class"};
    private String selectedClass;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        classListView = findViewById(R.id.classListView);


        selectedClass = sharedPreferences.getString("SELECTED_CLASS", null);
        if (selectedClass != null) {
            // Highlight the previously selected class
            int position = getClassPosition(selectedClass);
            if (position != -1) {
                classListView.setItemChecked(position, true);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, classes);
        classListView.setAdapter(adapter);
        classListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        classListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedClass = classes[position];
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("SELECTED_CLASS", selectedClass);
                editor.apply();

                showSubjects(selectedClass);
            }
        });

    }

    private int getClassPosition(String className) {
        for (int i = 0; i < classes.length; i++) {
            if (classes[i].equals(className)) {
                return i;
            }
        }
        return -1; // Not found
    }

    private void showSubjects(String selectedClass) {
        Intent intent = new Intent(this, SubjectListActivity.class);
        intent.putExtra("SELECTED_CLASS", selectedClass);
        startActivity(intent);
    }
}
