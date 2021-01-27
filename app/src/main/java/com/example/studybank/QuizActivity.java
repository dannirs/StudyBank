package com.example.studybank;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.util.*;

public class QuizActivity extends AppCompatActivity {

    static String ACTIVITY_NAME = "QuizActivity";

    ArrayList<Button> buttonsList;

    TextView testname_textview;
    TextView percent;
    TextView question_textview;

    Random random;

    ProgressBar progress;

    String current_question;
    int question_amount;
    int question_count;
    int correct_count = 0;
    int percent_score = 0;

    QuestionBank bank;
    ArrayList<Questions.mc_question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        Intent intent = getIntent();;
        if (intent.hasExtra("qb")) {
            Log.w("readzz","extras");
            bank = intent.getParcelableExtra("qb");
        }else{
            Log.w("readzz","no extras");
            // Initialize sample questions
            bank = sampleQuestions();
            questions = new ArrayList<Questions.mc_question>(bank.getQuestionList());
        }

        question_amount=bank.num_questions;
        questions = bank.questionList;

        random = new Random();

        // Initialize percent textview
        percent = (TextView) findViewById(R.id.percent);

        // Initialize question textview
        question_textview = (TextView) findViewById(R.id.questionView);

        // Initialize buttons list
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);

        buttonsList = new ArrayList<Button>();
        buttonsList.add(button1);
        buttonsList.add(button2);
        buttonsList.add(button3);
        buttonsList.add(button4);

        // Initialize sample questions
//        bank = sampleQuestions();
//        question_amount = bank.getNum_questions();
//        questions = new ArrayList<Questions.mc_question>(bank.getQuestionList());

        // Initialize test name textview
        testname_textview = (TextView) findViewById(R.id.testNameDisplay);
        testname_textview.setText(Integer.toString(bank.getNum_questions())+" questions");

        // Initialize progress bar
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setMax(bank.getNum_questions());

        setQuestion(questions.get(0));


    }

    protected void setQuestion(Questions.mc_question new_question) {
        // Display question
        question_textview.setText(new_question.getQuestion_dialog());

        // Set correct button
        Button correctButton;
        int correct = random.nextInt(4);
        correctButton = buttonsList.get(correct);

        int ans = new_question.getAnswer_index();
        ArrayList<String> inAns = new ArrayList<String>();

        switch(ans) {
            case(0):
                correctButton.setText(new_question.getAns1());
                inAns.add(new_question.getAns2());
                inAns.add(new_question.getAns3());
                inAns.add(new_question.getAns4());
                break;
            case(1):
                correctButton.setText(new_question.getAns2());
                inAns.add(new_question.getAns1());
                inAns.add(new_question.getAns3());
                inAns.add(new_question.getAns4());
                break;
            case(2):
                correctButton.setText(new_question.getAns3());
                inAns.add(new_question.getAns2());
                inAns.add(new_question.getAns1());
                inAns.add(new_question.getAns4());
                break;
            case(3):
                correctButton.setText(new_question.getAns4());
                inAns.add(new_question.getAns2());
                inAns.add(new_question.getAns3());
                inAns.add(new_question.getAns1());
                break;
        }

        correctButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                correctAnswer();
            }
        });

        // Set incorrect buttons
        Button incorrectButton;

        int rem = 3;
        int ic;

        for(Button button : buttonsList){
            if (button != correctButton){
                ic = random.nextInt(rem);
                button.setText(inAns.get(ic));
                inAns.remove(ic);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        incorrectAnswer();
                    }
                });

                rem--;
            }

        }
    }

    protected void nextQuestion() {
        setQuestion(questions.get(question_count));
    }

    protected void correctAnswer() {

        question_count++;
        progress.setProgress(question_count);

        correct_count++;
        percent_score = Math.toIntExact(Math.round(100*((double)correct_count/question_count)));
        percent.setText(Integer.toString(percent_score)+"%");

        if(question_count < question_amount) {
            nextQuestion();
        } else endQuiz();
    }

    protected void incorrectAnswer() {

        question_count++;
        progress.setProgress(question_count);

        percent_score = Math.toIntExact(Math.round(100*((double)correct_count/question_count)));
        percent.setText(Integer.toString(percent_score)+"%");

        if(question_count < question_amount) {
            nextQuestion();
        } else endQuiz();
    }

    protected void endQuiz() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);

        builder.setCancelable(false);

        if(percent_score > 80) {
            builder.setTitle("Great work!");
            builder.setMessage("Wow! You got a score of "+percent_score+"%! You must have studied hard!");
        } else if(percent_score > 60) {
            builder.setTitle("Keep up the good work!");
            builder.setMessage("You got a score of "+percent_score+"%! That's pretty good, but you can do even better!");
        } else if(percent_score >= 50) {
            builder.setTitle("Alright then.");
            builder.setMessage("You got a score of "+percent_score+"%. Well, at least you passed...");
        } else if(percent_score > 10) {
            builder.setTitle("So close!");
            builder.setMessage("You got a score of "+percent_score+"%. Just a little more studying, and you'll pass.");
        } else {
            builder.setTitle("Oh... Oh no...");
            builder.setMessage("You got a score of "+percent_score+"%... Maybe, uh, try again?");
        }

        builder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Returns percentage score with result intent to previous activity
                Intent result = new Intent();
                result.putExtra("score", percent_score);
                setResult(Activity.RESULT_OK, result);
                Toast completeToast = Toast.makeText(QuizActivity.this, "Quiz completed!", Toast.LENGTH_SHORT);
                completeToast.show();
                finish();
            }
        });

        builder.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
        builder.setTitle("Are you sure you want to leave?");
        builder.setMessage("You're not done your quiz yet!");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast incompleteToast = Toast.makeText(QuizActivity.this, "Quiz left incomplete.", Toast.LENGTH_SHORT);
                incompleteToast.show();
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });

        builder.show();
    }

    /////////// Sample question bank maker
    protected QuestionBank sampleQuestions() {
        QuestionBank sample = new QuestionBank();

        for(int i = 1; i < 6; i++) {
            Questions.mc_question question = new Questions.mc_question();
            question.setQuestion_dialog("Sample question #"+i);

            question.setAns1("Correct answer (1)");
            question.setAns2("Incorrect answer (2)");
            question.setAns3("Incorrect answer (3)");
            question.setAns4("Incorrect answer (4)");

            question.setAnswer_index(0);

            sample.add_to_bank(question);
        }

        //Q6
        Questions.mc_question qu6 = new Questions.mc_question();
        qu6.setQuestion_dialog("Which was the first smartphone available that ran Android?");

        qu6.setAns1("HTC Dream");
        qu6.setAns2("Nexus One");
        qu6.setAns3("Motorola Droid");
        qu6.setAns4("iPhone 3G");

        qu6.setAnswer_index(0);

        sample.add_to_bank(qu6);

        //Q7
        Questions.mc_question qu7 = new Questions.mc_question();
        qu7.setQuestion_dialog("What operating system is Android based on?");

        qu7.setAns1("Linux");
        qu7.setAns2("Unix");
        qu7.setAns3("Windows");
        qu7.setAns4("Something else??");

        sample.add_to_bank(qu7);

        //Q8
        Questions.mc_question qu8 = new Questions.mc_question();
        qu8.setQuestion_dialog("From the following list, which method is called first when an Activity starts?");

        qu8.setAns1("onCreate()");
        qu8.setAns2("onStart()");
        qu8.setAns3("setContentView()");
        qu8.setAns4("findViewById()");

        sample.add_to_bank(qu8);

        //Q9
        Questions.mc_question qu9 = new Questions.mc_question();
        qu9.setQuestion_dialog("If you want to navigate from one activity to another then Android provides you which class?");

        qu9.setAns1("Intent");
        qu9.setAns2("Object");
        qu9.setAns3("StartActivity");
        qu9.setAns4("None of these");

        sample.add_to_bank(qu9);

        //Q10
        Questions.mc_question qu10 = new Questions.mc_question();
        qu10.setQuestion_dialog("What do Activity objects represent in Android?");

        qu10.setAns1("Pages in an application");
        qu10.setAns2("Transitions between pages");
        qu10.setAns3("Visible items on your screen");
        qu10.setAns4("Database");

        sample.add_to_bank(qu10);



        return sample;

    }

}




