package model;

public class Student {
    private Integer id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String major;
    private Float gpa;

    public Student() {
    }

    public Student(Integer id, String name, String email, String phoneNumber, String address, String major, Float gpa) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.major = major;
        this.gpa = gpa;
    }

    // Getter và Setter methods

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", major='" + major + '\'' +
                ", gpa=" + gpa +
                '}';
    }
}