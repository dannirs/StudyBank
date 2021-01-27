package com.example.studybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

public class ProfileChangeActivity extends AppCompatActivity {

    private EditText editProgram;
    private EditText editName;
    private Spinner editYear;
    //  private EditText editEmail;
    private Spinner editCountry;
    private EditText editClasses;
    private EditText editBio;

    private Button confirm;
    private Firebase rootRef;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final Button backButton = findViewById(R.id.back);
        // when the user clicks the button in MainActivity:
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileChangeActivity.this, ProfileMainActivity.class);
                startActivity(intent);
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();

        editProgram = findViewById(R.id.inputProgram);

        editName = findViewById(R.id.inputName);

        final Spinner spinner = (Spinner) findViewById(R.id.inputYear);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileChangeActivity.this,
                R.array.years, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        editYear = findViewById(R.id.inputYear);

        final Spinner countryList = (Spinner)findViewById(R.id.inputCountry);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, getCountryListByLocale().toArray(new String[0]));
        countryList.setAdapter(adapter2);

        editClasses = findViewById(R.id.inputClasses);

        editBio = findViewById(R.id.inputBio);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userInfo = snapshot.getValue(User.class);
                if (userInfo != null) {
                    String strName = userInfo.name;
                    Log.d("name", strName);
                    editName.setText(strName);
                }

                if (snapshot.child("program").exists()) {
                    String strProgram = snapshot.child("program").getValue().toString();
                    Log.d("program", strProgram);
                    editProgram.setText(strProgram);
                }

                if (snapshot.child("bio").exists()) {
                    String strBio = snapshot.child("bio").getValue().toString();
                    Log.d("bio", strBio);
                    editBio.setText(strBio);
                }

                if (snapshot.child("classes").exists()) {
                    String strClasses = snapshot.child("classes").getValue().toString();
                    Log.d("classes", strClasses);
                    editClasses.setText(strClasses);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        confirm = findViewById(R.id.confirm);
        rootRef = new Firebase("https://studybank-43992.firebaseio.com/");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String strCountry = countryList.getSelectedItem().toString();
                if (strCountry.length() > 0) {
                    Firebase childRef = rootRef.child("Users").child(userId).child("country");
                    childRef.setValue(strCountry);
                }
                else if (strCountry.equalsIgnoreCase("N/A")) {
                    Firebase childRef = rootRef.child("Users").child(userId).child("country");
                    childRef.removeValue();
                }

                String strProgram = editProgram.getText().toString().trim();
                if (strProgram.length() > 0) {
                    Firebase childRef = rootRef.child("Users").child(userId).child("program");
                }

                String strName = editName.getText().toString().trim();
                if (strName.length() > 0) {
                    Firebase childRef = rootRef.child("Users").child(userId).child("name");
                    childRef.setValue(strName);
                }


                String strBio = editBio.getText().toString().trim();
                if (strBio.length() > 0) {
                    Firebase childRef = rootRef.child("Users").child(userId).child("bio");
                    childRef.setValue(strBio);
                }


                String strClasses = editClasses.getText().toString().trim();
                if (strClasses.length() > 0) {
                    Firebase childRef = rootRef.child("Users").child(userId).child("classes");
                    childRef.setValue(strClasses);
                }


                Intent intent = new Intent(ProfileChangeActivity.this, ProfileMainActivity.class);
                startActivity(intent);

            }

        });

    }

    private SortedSet<String> getCountryListByLocale() {
        SortedSet<String> countries = new TreeSet<>();
        countries.add("");
        for (Locale locale : Locale.getAvailableLocales()) {
            if (!TextUtils.isEmpty(locale.getDisplayCountry())) {
                countries.add(locale.getDisplayCountry());
            }
        }
        return countries;
    }


}
