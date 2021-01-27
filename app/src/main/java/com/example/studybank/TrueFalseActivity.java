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

public class TrueFalseActivity extends AppCompatActivity {

    RadioButton rButton1, rButton2;
    EditText questionText;
    RadioGroup rGroup;
    QuestionBank qb = new QuestionBank();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_false);

        rButton1 = findViewById(R.id.trueOption);
        rButton2 = findViewById(R.id.falseOption);
        rGroup = findViewById(R.id.trueFalseOptions);
        questionText=findViewById(R.id.questionEditText);
    }

    public void onSubmit(View view) {
        if (questionText.getText().toString().length() > 0){
            if (rGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(TrueFalseActivity.this, "This submission is invalid, please select an option", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(TrueFalseActivity.this, "This question has been submitted", Toast.LENGTH_LONG).show();
                Questions.mc_question question = new Questions.mc_question();

                if (rButton1.isChecked()) {
                    question.setAnswer_index(0);
                }
                if (rButton2.isChecked()) {
                    question.setAnswer_index(1);
                }

                question.setAns1("True");
                question.setAns2("False");
                question.setAns3("-");
                question.setAns4("-");

                question.setQuestion_dialog(questionText.getText().toString());
                question.print();
                qb.add_to_bank(question);
                rButton1.setChecked(false);
                rButton2.setChecked(false);
                questionText.setText(null);
            }
        } else {
            Toast.makeText(TrueFalseActivity.this, "This submission is invalid, please ensure everything is filled out", Toast.LENGTH_LONG).show();
        }
    }

    public void onBack(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(TrueFalseActivity.this);
        alert.setTitle("Do you want to leave this activity?");
        alert.setPositiveButton(R.string.okButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(TrueFalseActivity.this, HomepageActivity.class);
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

    public void onReset(View view) {
        rButton1.setChecked(false);
        rButton2.setChecked(false);
        questionText.setText(null);
    }

    public void onExportTF(View view) {
        Intent myIntent = new Intent(TrueFalseActivity.this, ImExActivity.class);
        myIntent.putExtra("qb",qb);
        this.startActivity(myIntent);
    }

    public void onCreateMC(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(TrueFalseActivity.this);
        alert.setTitle("Do you want to switch to True/False Question?");
        alert.setPositiveButton(R.string.okButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(TrueFalseActivity.this, MultipleChoiceActivity.class);
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