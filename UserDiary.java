package com.tech.royal_vee.journalapp.Model;

import java.util.HashMap;
import java.util.Map;

public class UserDiary {
    private String id;
    private String date;
    private String time;
    private String note;

    public UserDiary(){

    }

    public UserDiary(String id, String date, String time, String note) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.note = note;
    }

    public UserDiary(String date, String time, String note) {
        this.date = date;
        this.time = time;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("date", this.date);
        result.put("time", this.time);
        result.put("note", this.note);

        return result;
    }
    public Map<String, Object> toNote() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("date", this.date);
        result.put("time", this.time);
        result.put("note", this.note);

        return result;
    }
}
