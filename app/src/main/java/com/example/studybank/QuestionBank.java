package com.example.studybank;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.studybank.Questions.*;

public class QuestionBank implements Parcelable {
    int num_questions=0;
    ArrayList<mc_question> questionList = new ArrayList<mc_question>();
    String creator;
    String date_created;

    protected QuestionBank(Parcel in) {
        num_questions = in.readInt();
        creator = in.readString();
        date_created = in.readString();
        questionList = in.createTypedArrayList(mc_question.CREATOR);

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(num_questions);
        dest.writeString(creator);
        dest.writeString(date_created);
        dest.writeTypedList(questionList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuestionBank> CREATOR = new Creator<QuestionBank>() {
        @Override
        public QuestionBank createFromParcel(Parcel in) {
            return new QuestionBank(in);
        }

        @Override
        public QuestionBank[] newArray(int size) {
            return new QuestionBank[size];
        }
    };

    public QuestionBank(){

    }


    public void add_to_bank(mc_question e){
        this.questionList.add(e);
        num_questions++;
    }

    public int getNum_questions() {
        return num_questions;
    }

    public void setNum_questions(int num_questions) {
        this.num_questions = num_questions;
    }

    public ArrayList<mc_question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(ArrayList<mc_question> questionList) {
        this.questionList = questionList;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
