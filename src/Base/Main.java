package Base;

import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.TreeSet;
import LabJDBC.UniversityDB;
import LabStudentGroup.Group;
import LabStudentGroup.Student;
import Lab_XML_JSON.FastJSON;
import Lab_XML_JSON.XmlJson;

public class Main {

    public static void main(String[] args) {

        // deserializing groups from json file

        File file1 = new File("group1.json");
        File file2 = new File("group2.json");
        File file3 = new File("group3.json");

        XmlJson fastjson = new FastJSON();

        Group[] groups = new Group[3];
        groups[0] = (Group) (fastjson.deserialize(Group.class, file1));
        groups[1] = (Group) (fastjson.deserialize(Group.class, file2));
        groups[2] = (Group) (fastjson.deserialize(Group.class, file3));

        Student[] st1 = groups[0].getStudents().toArray(new Student[groups[0].getStudents().size()]);
        Student[] st2 = groups[1].getStudents().toArray(new Student[groups[1].getStudents().size()]);
        Student[] st3 = groups[2].getStudents().toArray(new Student[groups[2].getStudents().size()]);

        // working with database

        UniversityDB.initialize();
        /*
        UniversityDB.createGroupTable();
        UniversityDB.createStudentTable();

        for (int i = 0; i < groups.length; i++)
            UniversityDB.addGroup(groups[i]);

        for(int i = 0; i < st1.length; i++)
            UniversityDB.addStudent(st1[i], 1);
        for(int i = 0; i < st2.length; i++)
            UniversityDB.addStudent(st2[i], 2);
        for(int i = 0; i < st3.length; i++)
            UniversityDB.addStudent(st3[i], 3);
        */

        //UniversityDB.transferToNextGroup(st1[0], "g345");
        //System.out.println("Course: " + UniversityDB.getCourse(st1[0]));

        UniversityDB.outputAllGroups();
        //UniversityDB.outputGrantApplicants(50);

        /*
        try {
            UniversityDB.deleteGroup(groups[0]);
        } catch (Exception e){
            System.out.println("There are students in this group.");
            UniversityDB.deleteGroupWithStudents(groups[0]);
        }
        */

        UniversityDB.close();

    }
    private static Group[] generateGroup(){

        Student s1, s2, s3, s4, s5, s6;

        s1 = new Student.Builder().setFirstName("Damian").setLastName("Johnson").
                setDateOfBirthday(LocalDate.of(1997, Month.JANUARY, 18)).
                setAverageMark(4.7).setPaidOrFreeGroup(Student.PaidFree.FREE).
                setPhoneNumber("380981234567").createStudent();

        s2 = new Student.Builder().setFirstName("Alex").setLastName("Austen").
                setDateOfBirthday(LocalDate.of(1998, Month.MARCH, 12)).
                setAverageMark(4.5).setPaidOrFreeGroup(Student.PaidFree.FREE).
                setPhoneNumber("380984567843").createStudent();

        s3 = new Student.Builder().setFirstName("Kim").setLastName("Loss").
                setDateOfBirthday(LocalDate.of(1993, Month.JULY, 10)).
                setAverageMark(4.2).setPaidOrFreeGroup(Student.PaidFree.FREE).
                setPhoneNumber("380981234521").createStudent();

        s4 = new Student.Builder().setFirstName("Joe").setLastName("Clark").
                setDateOfBirthday(LocalDate.of(1996, Month.JANUARY, 22)).
                setAverageMark(4.5).setPaidOrFreeGroup(Student.PaidFree.PAID).
                setPhoneNumber("380981234561").createStudent();

        s5 = new Student.Builder().setFirstName("Alice").setLastName("Cooper").
                setDateOfBirthday(LocalDate.of(1992, Month.DECEMBER, 13)).
                setAverageMark(4.1).setPaidOrFreeGroup(Student.PaidFree.PAID).
                setPhoneNumber("380981134567").createStudent();

        s6 = new Student.Builder().setFirstName("Linda").setLastName("Tyler").
                setDateOfBirthday(LocalDate.of(1989, Month.JULY, 25)).
                setAverageMark(4.3).setPaidOrFreeGroup(Student.PaidFree.PAID).
                setPhoneNumber("380981214547").createStudent();

        Group[] g = new Group[3];
        g[0] = new Group();
        g[0].setTitle("g345");
        g[0].setMentor("Allie Fox");
        g[0].setYearOfGraduation(2020);
        g[1] = new Group();
        g[1].setTitle("g365");
        g[1].setMentor("Randy Fisher");
        g[1].setYearOfGraduation(2021);
        g[2] = new Group();
        g[2].setTitle("g285");
        g[2].setMentor("James Hilton");
        g[2].setYearOfGraduation(2019);

        TreeSet<Student> st1 = new TreeSet<>();
        st1.add(s3);
        st1.add(s5);
        TreeSet<Student> st2 = new TreeSet<>();
        st2.add(s1);
        st2.add(s6);
        TreeSet<Student> st3 = new TreeSet<>();
        st3.add(s2);
        st3.add(s4);

        g[0].setStudents(st1);
        g[1].setStudents(st2);
        g[2].setStudents(st3);
        return g;
    }
}

