package com.tech.royal_vee.journalapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.tech.royal_vee.journalapp.Model.UserDiary;
import com.tech.royal_vee.journalapp.NewDiaryEntry;
import com.tech.royal_vee.journalapp.R;
import com.tech.royal_vee.journalapp.ViewDiary;
import android.widget.ImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.List;

public class JournalEntryAdapter  extends RecyclerView.Adapter<JournalEntryAdapter.ViewHolder> {

    private List<UserDiary> notesList;
    private Context context;
    private FirebaseFirestore firestoreDB;

    public JournalEntryAdapter(List<UserDiary> notesList, Context context, FirebaseFirestore firestoreDB) {
        this.notesList = notesList;
        this.context = context;
        this.firestoreDB = firestoreDB;
    }

    @Override
    public JournalEntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_row, parent, false);

        return new JournalEntryAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(JournalEntryAdapter.ViewHolder holder, int position) {
        final int itemPosition = position;
        final UserDiary note = notesList.get(itemPosition);

        holder.date.setText(note.getDate());
        holder.time.setText(note.getTime());
        holder.note.setText(note.getNote());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote(note);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote(note.getId(), itemPosition);
            }
        });

        holder.note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewNote(note);
            }
        });
        holder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewNote(note);
            }
        });
        holder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewNote(note);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, note;
        ImageView edit;
        ImageView delete;

        ViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.et_date);
            time = view.findViewById(R.id.et_time);
            note = view.findViewById(R.id.et_note);

            edit = view.findViewById(R.id.ivEdit);
            delete = view.findViewById(R.id.ivDelete);
        }
    }

    private void updateNote(UserDiary note) {
        Intent intent = new Intent(context, NewDiaryEntry.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("UpdateNoteId", note.getId());
        intent.putExtra("UpdateNoteDate", note.getDate());
        intent.putExtra("UpdateNoteTime", note.getTime());
        intent.putExtra("UpdateNoteNote", note.getNote());
        context.startActivity(intent);
    }

    private void viewNote(UserDiary note) {
        Intent intent = new Intent(context, ViewDiary.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("UpdateNoteId", note.getId());
        intent.putExtra("UpdateNoteDate", note.getDate());
        intent.putExtra("UpdateNoteTime", note.getTime());
        intent.putExtra("UpdateNoteNote", note.getNote());
        context.startActivity(intent);
    }

    private void deleteNote(String id, final int position) {
        firestoreDB.collection("notes")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        notesList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, notesList.size());
                        Toast.makeText(context, "Note has been deleted!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}