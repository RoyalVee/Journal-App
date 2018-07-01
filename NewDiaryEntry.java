package com.tech.royal_vee.journalapp;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tech.royal_vee.journalapp.Model.UserDiary;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class NewDiaryEntry extends AppCompatActivity {

    private static final String TAG = "AddNoteActivity";

    Calendar calendar;

    SimpleDateFormat mtimeformat;
    SimpleDateFormat mdateformat;

    // Creating FirebaseAuth object.
    FirebaseAuth firebaseAuth;

    private FirebaseUser currentFirebaseUser;
    String currentUserID;

    TextView edtTitle;
    TextView edtContent;
    Button btAdd;

    private FirebaseFirestore firestoreDB;
    String id = "";
    String gentime;
    String gendate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diary_entry);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        currentUserID = currentFirebaseUser.getUid();

        edtContent = findViewById(R.id.ja_diarynote);
        btAdd = findViewById(R.id.ja_addentry);

        calendar = Calendar.getInstance();
        mtimeformat = new SimpleDateFormat("hh:mm a");
        mdateformat = new SimpleDateFormat("dd/MM/yyyy");


        firestoreDB = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("UpdateNoteId");

            gentime = bundle.getString("UpdateNoteTime");

            gendate = bundle.getString("UpdateNoteDate");


            edtContent.setText(bundle.getString("UpdateNoteNote"));
        }

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = mdateformat.format(calendar.getTime());
                String time = mtimeformat.format(calendar.getTime());
                String note = edtContent.getText().toString();

                if (note.length() > 0) {
                    if (id.length() > 0) {
                        updateNote(id, gendate, gentime, note);
                    } else {
                        addNote(date, time, note);
                    }
                }

                finish();
            }
        });
    }

    private void updateNote(String id, String date, String time, String content) {
        Map<String, Object> note = (new UserDiary(id, date, time, content)).toNote();

        firestoreDB.collection("users").document(currentUserID).collection("mydiary")
                .document(id)
                .set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "Note document update successful!");
                        Toast.makeText(getApplicationContext(), "Note has been updated!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding Note document", e);
                        Toast.makeText(getApplicationContext(), "Note could not be updated!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addNote(String date, String time, String content) {
        Map<String, Object> note = new UserDiary(date, time, content).toMap();

        firestoreDB.collection("users").document(currentUserID).collection("mydiary")
                .add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "Note has been added!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding Note document", e);
                        Toast.makeText(getApplicationContext(), "Note could not be added!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}