package com.example.demo3;
public class Note{
    private double ds;
    private double tp;
    private double exam;
    private double moyenne;
    Note( double exam, double ds, double tp) {
        this.ds = ds;
        this.tp = tp;
        this.exam = exam;
        this.moyenne = ds*0.25 + tp*0.25 + exam*0.5;
    }
    Note(double exam, double ds) {
        this.ds = ds;
        this.exam = exam;
        this.moyenne = ds*0.3 + exam*0.7;
    }
    Note(double exam) {
        this.exam = exam;
        this.moyenne = exam;
    }


    public Note() {
        this(0,0,0);
    }

    public double getMoyenne(){
        return this.moyenne;
    }
    public double getDs() {
        return ds;
    }
    public void setDs(double ds) {
        this.ds = ds;
    }
    public double getTp() {
        return tp;
    }
    public void setTp(double tp) {
        this.tp = tp;
    }
    public double getExam() {
        return exam;
    }
    public void setExam(double exam) {
        this.exam = exam;
    }
}
