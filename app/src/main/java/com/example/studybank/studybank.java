package com.example.studybank;

import android.app.Application;

import com.firebase.client.Firebase;

public class studybank extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
