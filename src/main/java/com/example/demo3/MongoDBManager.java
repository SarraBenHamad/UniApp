package com.example.demo3;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MongoDBManager {
    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    private static final String DATABASE_NAME = "Etudiants";
    private static final String COLLECTION_NAME = "liste des etudiants";

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public MongoDBManager() {
        mongoClient = new MongoClient(HOST, PORT);
        database = mongoClient.getDatabase(DATABASE_NAME);
        collection = database.getCollection(COLLECTION_NAME);
    }

    public boolean exists(int id) {
        return collection.find(Filters.eq("id", id)).first() != null;
    }

    public void deleteStudent(int id) {
        collection.deleteOne(Filters.eq("id", id));
    }

    public void insertStudent(Student student) {
        Document document = new Document();
        document.append("id", student.getId())
                .append("name", student.getName())
                .append("birthdate", student.getBirthDate())
                .append("grades", student.getGrades().stream()
                        .map(grade -> new Document("subject", grade.getName())
                                .append("coef", grade.getCoef())
                                .append("ds", grade.getNote().getDs())
                                .append("tp", grade.getNote().getTp())
                                .append("exam", grade.getNote().getExam()))
                        .collect(Collectors.toList()))
                .append("average", student.getAverage());
        collection.insertOne(document);
    }

    public long studentsCount() {
        return collection.countDocuments();
    }

    public ArrayList<Student> getStudents(double limite, int order) {
        int nb = (int) Math.floor(studentsCount() * limite);
        FindIterable<Document> studentsIterable = collection.find().sort(new BasicDBObject("average", order)).limit(nb);
        ArrayList<Student> students = new ArrayList<>();
        studentsIterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                int id = document.getInteger("id");
                String name = document.getString("name");
                String birthdate = document.getString("birthdate");
                double average = document.getDouble("average");
                ArrayList<Subject> grades = new ArrayList<>();
                ArrayList<Document> gradesDocuments = (ArrayList<Document>) document.get("grades");
                for (Document gradeDoc : gradesDocuments) {
                    String subjectName = gradeDoc.getString("subject");
                    int coef = gradeDoc.getInteger("coef");
                    double ds = gradeDoc.getDouble("ds");
                    double tp = gradeDoc.getDouble("tp");
                    double exam = gradeDoc.getDouble("exam");
                    Note note = new Note(exam, ds, tp); // Create a Note object
                    Subject subject = new Subject(subjectName, coef, note); // Create a Subject object
                    grades.add(subject);
                }

                Student student = new Student(id, name, birthdate, grades, average);
                students.add(student);
            }
        });

        return students;
    }
    public void updateStudent(Student student) {
        Document updatedDocument= new Document();
        updatedDocument.append("name", student.getName())
                .append("birthdate", student.getBirthDate())
                .append("grades", student.getGrades().stream()
                        .map(grade -> new Document("subject", grade.getName())
                                .append("coef", grade.getCoef())
                                .append("ds", grade.getNote().getDs())
                                .append("tp", grade.getNote().getTp())
                                .append("exam", grade.getNote().getExam()))
                        .collect(Collectors.toList()))
                .append("average", student.getAverage());

        collection.updateOne(Filters.eq("id", student.getId()), new Document("$set", updatedDocument));
    }


    public long betweenStudentsCount(int minAverage, int maxAverage) {
        return collection.countDocuments(
                Filters.and(
                        Filters.gte("average", minAverage),
                        Filters.lte("average", maxAverage)
                )
        );
    }

    public Map<String, Double[]> getStudentGrades(int studentId) {
        Document studentDoc = collection.find(Filters.eq("id", studentId)).first();
        if (studentDoc == null) {
            return new HashMap<>();
        }

        Map<String, Double[]> grades = new HashMap<>();
        ArrayList<Document> gradesDocuments = (ArrayList<Document>) studentDoc.get("grades");
        for (Document gradeDoc : gradesDocuments) {
            String subjectName = gradeDoc.getString("subject");
            Double exam = gradeDoc.getDouble("exam");
            Double ds = gradeDoc.getDouble("ds");
            Double tp = gradeDoc.getDouble("tp");

            grades.put(subjectName, new Double[]{exam, ds, tp});
        }

        return grades;
    }
    public void close() {
        mongoClient.close();
    }
}
