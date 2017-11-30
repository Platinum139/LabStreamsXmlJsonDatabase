package LabJDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

import LabStudentGroup.*;

public class UniversityDB {

    private static MySqlManager sql;

    public static void initialize() {

        sql = new MySqlManager();
        sql.connectDatabase();
    }

    public static void createStudentTable() {

        String query = "CREATE TABLE student(" +
                "student_id INT AUTO_INCREMENT," +
                "firstName VARCHAR(20)," +
                "lastName VARCHAR(20)," +
                "dateOfBirthday DATE," +
                "averageMark DOUBLE," +
                "phoneNumber VARCHAR(20)," +
                "paidOrFreeGroup VARCHAR(4)," +
                "group_id INT," +
                "PRIMARY KEY(student_id)," +
                "FOREIGN KEY(group_id) REFERENCES groups(group_id));";
        sql.executeUpdate(query);
    }

    public static void createGroupTable() {

        String query = "CREATE TABLE groups(" +
                "group_id INT AUTO_INCREMENT," +
                "title VARCHAR(10)," +
                "mentor VARCHAR(20)," +
                "yearOfGraduation INT," +
                "PRIMARY KEY(group_id));";
        sql.executeUpdate(query);
    }

    public static void addStudent(Student s, int group) {

        String query = "INSERT INTO student(firstname, lastname, " +
                "dateOfBirthday, averageMark, phonenumber, paidOrFreeGroup, group_id) VALUES (?,?,?,?,?,?,?);";
        PreparedStatement prepStmt = sql.createPreparedStatement(query);
        try {
            prepStmt.setString(1, s.getFirstName());
            prepStmt.setString(2, s.getLastName());
            prepStmt.setDate(3,java.sql.Date.valueOf(s.getDateOfBirthday()));
            prepStmt.setDouble(4, s.getAverageMark());
            prepStmt.setString(5, s.getPhoneNumber());
            prepStmt.setString(6, s.getPaidOrFreeGroup().toString());
            prepStmt.setInt(7, group);
            prepStmt.executeUpdate();
        } catch (SQLException e){
            System.out.println(e);
        }
    }

    public static void addGroup(Group g) {

        String query = "INSERT INTO groups(title, mentor, yearOfGraduation) VALUES (?,?,?);";
        PreparedStatement prepStmt = sql.createPreparedStatement(query);
        try{
        prepStmt.setString(1, g.getTitle());
        prepStmt.setString(2, g.getMentor());
        prepStmt.setInt(3, g.getYearOfGraduation());
        prepStmt.executeUpdate();
        } catch (SQLException e){
            System.out.println(e);
        }
    }

    public static void transferToNextGroup(Student s, String newGroup) {

        String query = "UPDATE student " +
                "SET group_id = (SELECT group_id FROM groups WHERE title = ?)" +
                "WHERE phonenumber = ?;";

        PreparedStatement prepStmt = sql.createPreparedStatement(query);
        try{
            prepStmt.setString(1, newGroup);
            prepStmt.setString(2, s.getPhoneNumber());
            prepStmt.executeUpdate();
        } catch (SQLException e){
            System.out.println(e);
        }
    }

    /**
     * return course by phonenumber
     *
     * @param s
     * @return course
     */
    public static int getCourse(Student s) {

        int course = 0;
        String query = "SELECT (5 - yearOfGraduation + YEAR(CURDATE())) AS course " +
                "FROM student,groups WHERE groups.group_id = " +
                "(SELECT group_id FROM student WHERE phonenumber=" + "'" + s.getPhoneNumber() + "');";
        ResultSet res = sql.executeQuery(query);

        try {
            while (res.next()) {
                course = res.getInt("course");
            }
        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            return course;
        }
    }

    // output group
    public static void outputAllGroups() {

        String query = "SELECT firstname, lastname, dateofbirthday, averagemark, phonenumber, paidorfreegroup, title " +
                "FROM student s,groups g WHERE s.group_id = g.group_id;";
        ResultSet res = sql.executeQuery(query);
        try {
            while (res.next()) {
                String firstname = res.getString("firstname");
                String lastname = res.getString("lastname");
                String dateOfBirthday = res.getString("dateofbirthday");
                Double avgMark = res.getDouble("averagemark");
                String phonenum = res.getString("phonenumber");
                String paidOrFree = res.getString("paidorfreegroup");
                String title = res.getString("title");

                System.out.println("\nFirst name: " + firstname + "\nLast name: " + lastname +
                        "\nBirthday: " + dateOfBirthday + "\nAverage mark: " + avgMark +
                        "\nPhone number: " + phonenum + "\nGroup: " + paidOrFree +
                        "\nGroup: " + title + "\n");
            }
        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public static void outputGrantApplicants(int percent) {

        String addQuery = "SELECT COUNT(*) AS count FROM student;";
        int count = 0;
        try {
            ResultSet r = sql.executeQuery(addQuery);
            if (r.next())
                count = r.getInt("count");
        } catch (SQLException e) {
            System.out.println("Exception:" + e.getMessage());
        }

        int part = (int) (Math.round(count / 100.0 * percent));
        part++;

        String query = "SELECT * FROM " +
                "(SELECT * FROM student " +
                "ORDER BY averagemark DESC " +
                "LIMIT " + part + ") AS temp " +
                "WHERE temp.averagemark != " +
                "(SELECT MIN(temp.averagemark) " +
                "FROM " +
                "(SELECT * FROM student " +
                "ORDER BY averagemark DESC " +
                "LIMIT " + part + ") AS temp);";

        ResultSet res = sql.executeQuery(query);
        try {
            while (res.next()) {
                String firstname = res.getString("firstname");
                String lastname = res.getString("lastname");
                String dateOfBirthday = res.getString("dateofbirthday");
                Double avgMark = res.getDouble("averagemark");
                String phonenum = res.getString("phonenumber");
                String paidOrFree = res.getString("paidorfreegroup");

                System.out.println("\nFirst name: " + firstname + "\nLast name: " + lastname +
                        "\nBirthday: " + dateOfBirthday + "\nAverage mark: " + avgMark +
                        "\nPhone number: " + phonenum + "\nGroup: " + paidOrFree + "\n");
            }
        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public static void deleteGroup(Group group) throws Exception {

        String queryDelGroups = "DELETE FROM groups WHERE title='" + group.getTitle() + "';";
        int affectedRows = sql.executeUpdate(queryDelGroups);
        if (affectedRows == 0) {
            throw new Exception("You cannot delete from 'Groups' table because of dependencies.");
        }
    }

    public static void deleteGroupWithStudents(Group group) {

        String tmp = "SELECT COUNT(*) FROM groups g, student s WHERE g.group_id=s.group_id AND g.title='" + group.getTitle() + "';";
        ResultSet res = sql.executeQuery(tmp);
        int count = 0;
        try {
            if (res.next())
                count = res.getInt("COUNT(*)");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        if (count != 0){
            String query = "DELETE FROM student WHERE group_id = (SELECT group_id FROM groups WHERE title='" + group.getTitle() + "');";
            sql.executeUpdate(query);
            String queryDelGroups = "DELETE FROM groups WHERE title='" + group.getTitle() + "';";
            sql.executeUpdate(queryDelGroups);
        }
        else {
            String queryDelGroups = "DELETE FROM groups WHERE title='" + group.getTitle() + "';";
            sql.executeUpdate(queryDelGroups);
        }
    }

    public static void close() {
        sql.disconnectDatabase();
    }
}
