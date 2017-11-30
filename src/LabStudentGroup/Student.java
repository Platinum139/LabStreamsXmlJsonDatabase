package LabStudentGroup;

import java.lang.Object;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;
import java.time.LocalDate;
import java.util.regex.Pattern;
import com.alibaba.fastjson.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Student implements Comparable{

    // constants
    private static final double MAXMARK = 5.0;
    private static final int CURRENT_YEAR = LocalDate.now().getYear();
    private static final String PATTERN = "^[A-Z][a-z]+";

    public enum PaidFree {
        PAID, FREE
    }

    // fields
    @JSONField(ordinal = 1)
    private String firstName;

    @JSONField(ordinal = 2)
    private String lastName;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    @JSONField(ordinal = 3)
    private LocalDate dateOfBirthday;

    @JSONField(ordinal = 4)
    private double averageMark;

    @JSONField(ordinal = 5)
    private String phoneNumber;

    @JSONField(ordinal = 6)
    private PaidFree paidOrFreeGroup;

    // constructors
    private Student(){}
    private Student(Builder b){
        firstName = b.firstName;
        lastName = b.lastName;
        dateOfBirthday = b.dateOfBirthday;
        averageMark = b.averageMark;
        phoneNumber = b.phoneNumber;
        paidOrFreeGroup = b.paidOrFreeGroup;
    }

    // getters
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public LocalDate getDateOfBirthday() {
        return dateOfBirthday;
    }
    public double getAverageMark() {
        return averageMark;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public PaidFree getPaidOrFreeGroup() {
        return paidOrFreeGroup;
    }

    // setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setDateOfBirthday(LocalDate dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;
    }
    public void setAverageMark(double averageMark) {
        this.averageMark = averageMark;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setPaidOrFreeGroup(PaidFree paidOrFreeGroup) {
        this.paidOrFreeGroup = paidOrFreeGroup;
    }

    // builder
    public static class Builder {

        // fields
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirthday;
        private double averageMark;
        private String phoneNumber;
        private Student.PaidFree paidOrFreeGroup;

        // setters
        public Builder setFirstName(String firstName) {
            Pattern p = Pattern.compile(PATTERN);
            if (p.matcher(firstName).matches())
                this.firstName = firstName;
            else
                throw new IllegalArgumentException("Illegal Argument");
            return this;
        }
        public Builder setLastName(String lastName) {
            Pattern p = Pattern.compile(PATTERN);
            if (p.matcher(lastName).matches())
                this.lastName = lastName;
            else
                throw new IllegalArgumentException("Illegal Argument");
            return this;
        }
        public Builder setDateOfBirthday(LocalDate date) {
            if (date.getYear() > CURRENT_YEAR - 100 && date.getYear() < CURRENT_YEAR - 15)
                this.dateOfBirthday = date;
            else
                throw new IllegalArgumentException("Illegal Argument");
            return this;
        }
        public Builder setAverageMark(double averageMark) {
            if (averageMark >= 0.0 && averageMark <= MAXMARK)
                this.averageMark = averageMark;
            else
                throw new IllegalArgumentException("Illegal Argument");
            return this;
        }
        public Builder setPhoneNumber(String phoneNumber) {
            Pattern p = Pattern.compile("^[\\+]*(380)[0-9]{9}");
            if (p.matcher(phoneNumber).matches())
                this.phoneNumber = phoneNumber;
            else
                throw new IllegalArgumentException("Illegal Argument");
            return this;
        }
        public Builder setPaidOrFreeGroup (Student.PaidFree p) {
            this.paidOrFreeGroup = p;
            return this;
        }
        public Student createStudent(){
            return new Student(this);
        }
    }

    @Override
    public int compareTo (Object o){
        Student s = (Student) o;
        int c = this.getFirstName().compareTo(s.getFirstName());
        return c == 0? this.getLastName().compareTo(s.getLastName()) : c;
    }
    @Override
    public String toString(){
        return "\nFirst name: " + firstName + "\nLast name: " + lastName +
                "\nBirthday: " + dateOfBirthday + "\nAverage mark: " + averageMark +
                "\nPhone number: " + phoneNumber + "\nGroup: " + paidOrFreeGroup + "\n";
    }
    @Override
    public boolean equals (Object obj){

        return  firstName.equals(((Student) obj).firstName) &&
                lastName.equals(((Student) obj).lastName) &&
                phoneNumber.equals(((Student) obj).phoneNumber) &&
                paidOrFreeGroup.equals(((Student) obj).paidOrFreeGroup);
    }
}

class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    public LocalDate unmarshal(String v) throws Exception {
        return LocalDate.parse(v);
    }
    public String marshal(LocalDate v) throws Exception {
        return v.toString();
    }
}

