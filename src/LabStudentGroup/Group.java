package LabStudentGroup;

import com.alibaba.fastjson.annotation.*;
import javax.xml.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@XmlRootElement
@XmlType(propOrder = {"title", "mentor", "yearOfGraduation", "students"})
public class Group {

    // fields
    @JSONField(ordinal = 1)
    private String title;
    @JSONField(ordinal = 2)
    private String mentor;
    @JSONField(ordinal = 3)
    private int yearOfGraduation;
    @JSONField(ordinal = 4)
    private TreeSet<Student> students = new TreeSet<>();

    // constructor
    public Group(){ }

    // setters
    public void setTitle(String title) {
        this.title = title;
    }
    public void setMentor(String mentor) {
        this.mentor = mentor;
    }
    public void setYearOfGraduation(int yearOfGraduation) {
        this.yearOfGraduation = yearOfGraduation;
    }
    @XmlElementWrapper(name = "students")
    @XmlElement(name = "student")
    public void setStudents(TreeSet<Student> students){
        this.students = students;
    }

    // getters
    public String getTitle() {
        return title;
    }
    public String getMentor() {
        return mentor;
    }
    public int getYearOfGraduation() {
        return yearOfGraduation;
    }
    public TreeSet<Student> getStudents() {
        return students;
    }

    // methods
    public void addStudentToGroup(Student st){
        students.add(st);
    }
    public int getCourse(){
        return 5 - (yearOfGraduation - LocalDate.now().getYear());
    }
    public void transferToNextGroup(Student st, Group g){
        g.students.add(st);
        students.remove(st);
    }
    public TreeSet<Student> outputGrantApplicants(int pc){

        int count = (int)(Math.round(students.size() / 100.0 * pc));

        TreeSet<Student> res = new TreeSet<Student>(new ComparatorDescByAvgMark());
        Student min = students.stream().sorted(new ComparatorDescByAvgMark()).limit(count+1).min(new ComparatorDescByAvgMark().reversed()).orElse(null);
        System.out.println("Min: " + min);

        students.stream().sorted(new ComparatorDescByAvgMark()).limit(count+1).filter(s->s.getAverageMark() != min.getAverageMark()).forEach(res::add);
        return res;
    }
    public void outputGroup(){
        students.stream().forEach(s->System.out.println(s));
    }

    @Override
    public String toString() {
        String res = "Title: " + title +
                "\nMentor: " + mentor +
                "\nYearOfGraduation: " + yearOfGraduation +
                "\nStudents: " + "\n";
        for (Student st : students){
            res += st.toString();
        }
        return res;
    }
    @Override
    public boolean equals(Object obj){
        return  (title.equals(((Group) obj).title) &&
                mentor.equals(((Group) obj).mentor) &&
                yearOfGraduation == (((Group) obj).yearOfGraduation));
    }
}


class ComparatorDescByAvgMark implements Comparator<Student>{

    @Override
    public int compare(Student o1, Student o2) {
        if (o1.getAverageMark() > o2.getAverageMark())
            return -1;
        else if (o1.getAverageMark() < o2.getAverageMark())
            return 1;
        else
            return (o1.getFirstName()).compareTo(o2.getFirstName());
    }
}
