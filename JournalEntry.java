package com.tech.royal_vee.journalapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.tech.royal_vee.journalapp.Adapter.JournalEntryAdapter;
import com.tech.royal_vee.journalapp.Model.UserDiary;

import java.util.ArrayList;
import java.util.List;

public class JournalEntry extends AppCompatActivity {
    private static final String TAG = "JournalEntryActivity";

    private RecyclerView recyclerView;
    private JournalEntryAdapter mAdapter;

    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;

    private FirebaseUser currentFirebaseUser;
    String currentUserID;

    // Creating FirebaseAuth object.
    FirebaseAuth firebaseAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry);


            currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
            currentUserID = currentFirebaseUser.getUid();




        recyclerView = findViewById(R.id.journalentry);
        firestoreDB = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        loadNotesList();

        firestoreListener = firestoreDB.collection("users").document(currentUserID).collection("mydiary")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }

                        List<UserDiary> notesList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            UserDiary note = doc.toObject(UserDiary.class);
                            note.setId(doc.getId());
                            notesList.add(note);
                        }

                        mAdapter = new JournalEntryAdapter(notesList, getApplicationContext(), firestoreDB);
                        recyclerView.setAdapter(mAdapter);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        firestoreListener.remove();
    }

    private void loadNotesList() {
        firestoreDB.collection("users").document(currentUserID).collection("mydiary")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<UserDiary> notesList = new ArrayList<>();

                            for (DocumentSnapshot doc : task.getResult()) {
                                UserDiary note = doc.toObject(UserDiary.class);
                                note.setId(doc.getId());
                                notesList.add(note);
                            }

                            mAdapter = new JournalEntryAdapter(notesList, getApplicationContext(), firestoreDB);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            if (item.getItemId() == R.id.addNote) {
                Intent intent = new Intent(this, NewDiaryEntry.class);
                startActivity(intent);
            }
            if (item.getItemId() == R.id.logout) {
                firebaseAuth.signOut();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
