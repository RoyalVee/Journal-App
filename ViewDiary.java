package com.tech.royal_vee.journalapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewDiary extends AppCompatActivity {

    TextView time;
    String id;
    TextView date;
    TextView note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_diary);

        date = findViewById(R.id.ja_date);
        time = findViewById(R.id.ja_time);
        note = findViewById(R.id.ja_note);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("UpdateNoteId");
          time.setText(bundle.getString("UpdateNoteTime"));
           date.setText( bundle.getString("UpdateNoteDate"));
            note.setText(bundle.getString("UpdateNoteNote"));
        }
    }
}
