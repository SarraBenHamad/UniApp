package com.example.demo3;


import java.util.ArrayList;

public class Student {
    private int id ;
    private String Fullname ;
    private String birthDate;
    private ArrayList<Subject> grades;
    private double average;
    public Student(int id, String Fullname, String birthDate, ArrayList<Subject> grades, double average) {
        this.id = id;
        this.Fullname = Fullname;
        this.birthDate = birthDate;
        this.grades = grades;
        this.average = average;

    }
    public Student(int id, String Fullname,String birthDate) {
        this.id = id;
        this.Fullname = Fullname;
        this.birthDate = birthDate;
        this.grades= new ArrayList<Subject>();
        this.average = 0;
    }
    public void initGrades(){
        this.grades.clear();
        this.average = 0;
    }
    public void addGrade(Subject grade){
        if (grades.size()<5)
            this.grades.add(grade);
        else System.out.println("Error :overloaded grades");
    }

    public void calculateAverage(){
        float avg= 0;
        int coSum=0;
        for(Subject grade: grades){
            int coef=grade.getCoef();
            coSum+=coef;
            double moyenne=grade.getNote().getMoyenne();
            avg =avg+ (float) (coef*moyenne);
        }
        this.average =avg/coSum;
    }


    public double getAverage(){
        return this.average;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return Fullname;
    }
    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public String getBirthDate() {
        return birthDate;
    }
    public ArrayList <Subject> getGrades() {
        return grades;
    }
}
