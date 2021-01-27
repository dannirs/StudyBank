package com.example.studybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.ProgressBar;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

public class ProfileMainActivity extends AppCompatActivity {

    ProgressBar progress;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    private Firebase rootRef;
    private ImageButton imageButton;
    static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int PICK_IMAGE = 1;
    static final String ACTIVITY_NAME = "ProfileMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();
        rootRef = new Firebase("https://studybank-43992.firebaseio.com/");

        final TextView name = findViewById(R.id.name);
        final TextView email = findViewById(R.id.email);
        final TextView program = findViewById(R.id.program);
        final TextView bio = findViewById(R.id.bio);
        final TextView classes = findViewById(R.id.classes);
        final TextView country = findViewById(R.id.country);
        imageButton = findViewById(R.id.imageButton);
        progress = (ProgressBar) findViewById(R.id.progressBar);

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(imageButton);
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }


        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog alert = new AlertDialog.Builder(ProfileMainActivity.this).create();
                alert.setMessage("Change Profile Picture");
                alert.setButton("Take Picture", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        setResult(ProfileMainActivity.RESULT_OK, takePictureIntent);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                });
                alert.setButton2("Select from Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, PICK_IMAGE);
                    }
                });
                alert.show();
            }
        });

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userInfo = snapshot.getValue(User.class);
                if (userInfo != null) {
                    String strName = userInfo.name;
                    Log.d("name", strName);
                    String strEmail = userInfo.email;
                    Log.d("email", strEmail);
                    name.setText(strName);
                    email.setText(strEmail);

                    if (snapshot.child("program").exists()) {
                        String strProgram = snapshot.child("program").getValue().toString();
                        Log.d("program", strProgram);
                        program.setText(strProgram);
                    }

                    if (snapshot.child("bio").exists()) {
                        String strBio = snapshot.child("bio").getValue().toString();
                        Log.d("bio", strBio);
                        bio.setText(strBio);
                    }

                    if (snapshot.child("classes").exists()) {
                        String strClasses = snapshot.child("classes").getValue().toString();
                        Log.d("classes", strClasses);
                        classes.setText(strClasses);
                    }

                    if (snapshot.child("country").exists()) {
                        String strCountry = snapshot.child("country").getValue().toString();
                        Log.d("country", strCountry);
                        country.setText(strCountry);
                    }

                    if (snapshot.child("country").exists()) {
                        String strCountry = snapshot.child("country").getValue().toString();
                        Log.d("country", strCountry);
                        country.setText(strCountry);
                    }

                    if (snapshot.child("year").exists()) {
                        String strYear = snapshot.child("year").getValue().toString();
                        Log.d("year", strYear);
                        program.append("Year "+strYear);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileMainActivity.this, "Error occurred; could not display user profile", Toast.LENGTH_LONG).show();
            }
        });


        final Button button = findViewById(R.id.button);
        // when the user clicks the button in MainActivity:
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMainActivity.this, ProfileChangeActivity.class);
                startActivity(intent);
            }
        });

        final Button scoreButton = findViewById(R.id.score);
        // when the user clicks the button in MainActivity:
        scoreButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMainActivity.this, ScoreListview.class);
                startActivity(intent);
            }
        });

        final Button backButton = findViewById(R.id.back);
        // when the user clicks the button in MainActivity:
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMainActivity.this, HomepageActivity.class);
                startActivity(intent);
            }
        });
    }

    public void userScore(View v){
        Intent intent = new Intent(ProfileMainActivity.this, ProfileChangeActivity.class);
        startActivity(intent);
    }

    public void friendsList(View v) {
        Intent intent = new Intent(ProfileMainActivity.this, ProfileChangeActivity.class);
        startActivity(intent);
    }

    public void library(View v) {
        Intent intent = new Intent(ProfileMainActivity.this, ProfileChangeActivity.class);
        startActivity(intent);
    }

    private SortedSet<String> getCountryListByLocale() {
        SortedSet<String> countries = new TreeSet<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            if (!TextUtils.isEmpty(locale.getDisplayCountry())) {
                countries.add(locale.getDisplayCountry());
            }
        }
        return countries;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("requestCode", Integer.toString(requestCode));
        Log.d("resultCode", Integer.toString(resultCode));
        progress.setVisibility(View.VISIBLE);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                // gets the captured picture and sets it as the imagebutton display
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ImageButton imageButton = findViewById(R.id.imageButton);
                imageButton.setImageBitmap(imageBitmap);
                handleUpload(imageBitmap);
            }
        }
        else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Log.d("image", "Succeeded");
            ImageButton imageButton = findViewById(R.id.imageButton);
            imageButton.setImageURI(data.getData());
            Uri imageUri = data.getData();
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            handleUpload(imageBitmap);
        }
        else if (resultCode != RESULT_OK) {
            progress.setVisibility(View.GONE);
        }

    }

    private void handleUpload(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profilepic")
                .child(uid + ".jpeg");
        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(ACTIVITY_NAME, "onFailure: ", e.getCause());
                    }
                });
    }

    private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(ACTIVITY_NAME, "onSuccess" + uri);
                        setUserProfileUrl(uri);
                    }
                });
    }

    private void setUserProfileUrl(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(ProfileMainActivity.this, "Successfully set profile picture", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progress.setVisibility(View.GONE);
                        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.layout2),
                                "Failed to set profile picture", Snackbar.LENGTH_SHORT);
                        mySnackbar.setAction("Retry", new MyUndoListener());
                        mySnackbar.show();
//                        Toast.makeText(ProfileMainActivity.this, "Failed to set profile picture", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public class MyUndoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            imageButton.performClick();
        }
    }



}