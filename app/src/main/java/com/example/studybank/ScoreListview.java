package com.example.studybank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoreListview extends AppCompatActivity {

    private ListView lvSpinner;
    private Firebase rootRef;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    //ArrayList<String> scoreList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_listview);
        lvSpinner = (ListView) findViewById(R.id.listview_spinner);

        //scoreList = new ArrayList<String>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();
        rootRef = new Firebase("https://studybank-43992.firebaseio.com/");

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference friendsRef = rootRef.child("Users").child(userId).child("Scores");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> scoreList = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String friend = ds.getKey();
                    scoreList.add(friend);
                }
                ArrayList<String> mSpinnerData = new ArrayList<>();

                mSpinnerData.add("1");
                mSpinnerData.add("2");
                mSpinnerData.add("3");
                mSpinnerData.add("4");

                ListviewAdapter adapter = new ListviewAdapter((ArrayList<String>) scoreList, mSpinnerData, ScoreListview.this);
                lvSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        friendsRef.addListenerForSingleValueEvent(eventListener);
//        ArrayList<String> mData = new ArrayList<>();
//        mData.add("Name");
//        mData.add("Program");
//        mData.add("Bio");
//        mData.add("Classes");


    }


}
