package com.example.demo3;

public class Subject {
    private String name;
    private int coef;
    private Note note;
    public Subject(String name, int coef, Note note) {
        this.name = name;
        this.coef = coef;
        this.note = note;
    }
    public String getName() {
        return name;
    }
    public int getCoef() {
        return this.coef;
    }
    public Note getNote() {
        return this.note;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCoef(int coef) {
        this.coef = coef;
    }
    public void setNote(Note note) {
        this.note = note;
    }
}
