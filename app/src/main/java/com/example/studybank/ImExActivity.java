package com.example.studybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;


public class ImExActivity extends AppCompatActivity {
    QuestionBank qb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imex);

        // Temp main menu button function for homepage
        Button mm = (Button) findViewById(R.id.button3);
        mm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //


    }

    public void exportFile(View view){
        Intent myIntent = getIntent();
        qb = myIntent.getParcelableExtra("qb");
        if(qb.num_questions>0) {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/*");
//            intent.putExtra("qb", qb);
            Log.w("readzz","exp trying");
            startActivityForResult(intent, 1);
        }
    }

    public void importFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        Log.w("readzz","import trying");
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        QuestionBank qb;
        String line="";
        Log.w("readzz","activity result");

        //import chosen
        if(requestCode ==2){
            if(data != null){
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":")+1);
                qb=parse_file(uri);


                Intent myIntent = new Intent(this, QuizActivity.class);
                myIntent.putExtra("qb",qb);
                this.startActivity(myIntent);

            }
        }

        //export chosen
        if(requestCode==1){
            Intent myIntent = getIntent();
            qb = myIntent.getParcelableExtra("qb");
            Log.w("readzz", String.valueOf(qb.num_questions));
            for(int i=0;i<qb.num_questions;i++){
                line=qb.getQuestionList().get(i).getQuestion_dialog();
                line+="&";
                line+=qb.getQuestionList().get(i).getAns1();
                line+="&";
                line+=qb.getQuestionList().get(i).getAns2();
                line+="&";
                line+=qb.getQuestionList().get(i).getAns3();
                line+="&";
                line+=qb.getQuestionList().get(i).getAns4();
                line+="&";
                line+=qb.getQuestionList().get(i).getAnswer_index();

                Log.w("readzz",line);

                line+= "\n";



            }
            if (data != null && data.getData() != null && line.length()>1) {
                writeInFile(data.getData(),line);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void writeInFile(@NonNull Uri uri, @NonNull String text){
        OutputStream outputStream;
        try {
            outputStream = getContentResolver().openOutputStream(uri);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(text);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public QuestionBank parse_file(Uri uri){
        BufferedReader br;
        QuestionBank qb = new QuestionBank();
        try {
            br = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri)));
            String line = null;
            while ((line = br.readLine()) != null) {
                Log.w("readzz",line);
                Questions.mc_question new_q = Questions.mc_question.parse_line_to_mc(line);
                qb.add_to_bank(new_q);
            }
            br.close();
            qb.setNum_questions(qb.questionList.size());


        } catch (IOException e) {
            e.printStackTrace();
        }
        return qb;
    }

}
