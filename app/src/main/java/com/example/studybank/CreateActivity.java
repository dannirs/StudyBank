package com.example.studybank;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateActivity extends AppCompatActivity {

    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }

    public void onCreateFlashCard(View v) {
        AlertDialog.Builder questionChoice = new AlertDialog.Builder(CreateActivity.this);
        questionChoice.setTitle("Which type of flashcard would you like to create?");
        questionChoice.setPositiveButton(R.string.multipleChoiceButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CreateActivity.this, MultipleChoiceActivity.class);
                startActivity(intent);
            }
        });

        questionChoice.setNegativeButton(R.string.trueFalseButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CreateActivity.this, TrueFalseActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = questionChoice.create();
        dialog.show();
    }
}