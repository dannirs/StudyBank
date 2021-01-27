package com.example.studybank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MultipleChoiceActivity extends AppCompatActivity {
    RadioButton rButton1, rButton2, rButton3, rButton4;
    EditText questionText, option1Text, option2Text, option3Text, option4Text ;
    RadioGroup rGroup;

    QuestionBank qb = new QuestionBank();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);

        option1Text = findViewById(R.id.optionText1);
        option2Text = findViewById(R.id.optionText2);
        option3Text = findViewById(R.id.optionText3);
        option4Text = findViewById(R.id.optionText4);
        rButton1 = findViewById(R.id.option1);
        rButton2 = findViewById(R.id.option2);
        rButton3 = findViewById(R.id.option3);
        rButton4 = findViewById(R.id.option4);
        rGroup = findViewById(R.id.rGroup);
        questionText=findViewById(R.id.questionEditText);
    }

    public void onSubmit(View view) {
        if (option1Text.getText().toString().length() > 0 && option2Text.getText().toString().length() > 0 && option3Text.getText().toString().length() > 0 && option4Text.getText().toString().length() > 0) {
            if (rGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(MultipleChoiceActivity.this, "This submission is invalid, please select an option", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MultipleChoiceActivity.this, "This question has been submitted", Toast.LENGTH_LONG).show();
                Questions.mc_question question = new Questions.mc_question();

                if (rButton1.isChecked()) {
                    question.setAnswer_index(0);
                }
                question.setAns1(option1Text.getText().toString());

                if (rButton2.isChecked()) {
                    question.setAnswer_index(1);
                }
                question.setAns2(option2Text.getText().toString());

                if (rButton3.isChecked()) {
                    question.setAnswer_index(2);
                }
                question.setAns3(option3Text.getText().toString());

                if (rButton4.isChecked()) {
                    question.setAnswer_index(3);
                }
                question.setAns4(option4Text.getText().toString());

                question.setQuestion_dialog(questionText.getText().toString());

                question.print();
                qb.add_to_bank(question);

                rButton1.setChecked(false);
                option1Text.setText(null);
                rButton2.setChecked(false);
                option2Text.setText(null);
                rButton3.setChecked(false);
                option3Text.setText(null);
                rButton4.setChecked(false);
                option4Text.setText(null);
                questionText.setText(null);
            }
        } else {
            Toast.makeText(MultipleChoiceActivity.this, "This submission is invalid, please ensure everything is filled out", Toast.LENGTH_LONG).show();
        }
    }

    public void onReset(View view) {
        rButton1.setChecked(false);
        option1Text.setText(null);
        rButton2.setChecked(false);
        option2Text.setText(null);
        rButton3.setChecked(false);
        option3Text.setText(null);
        rButton4.setChecked(false);
        option4Text.setText(null);
        questionText.setText(null);
    }

    public void onBack(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MultipleChoiceActivity.this);
        alert.setTitle("Do you want to leave this activity?");
        alert.setPositiveButton(R.string.okButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MultipleChoiceActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });
        alert.setNegativeButton(R.string.noButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    public void onExportMC(View view) {
        Intent myIntent = new Intent(MultipleChoiceActivity.this, ImExActivity.class);
        myIntent.putExtra("qb",qb);
        this.startActivity(myIntent);
    }

    public void onCreateTF(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MultipleChoiceActivity.this);
        alert.setTitle("Do you want to switch to True/False Question?");
        alert.setPositiveButton(R.string.okButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MultipleChoiceActivity.this, TrueFalseActivity.class);
                startActivity(intent);
            }
        });
        alert.setNegativeButton(R.string.noButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }


}