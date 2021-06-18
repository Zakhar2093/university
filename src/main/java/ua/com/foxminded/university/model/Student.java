package ua.com.foxminded.university.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity
@Table(name="students", schema = "university")
public class Student {
    @Id
    @Column(name="student_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int studentId;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @JsonIgnore
    @Column(name="student_inactive")
    private boolean studentInactive;

    public Student() {
    }
   
    public Student(int studentId, String firstName, String lastName, Group group, boolean studentInactive) {
        super();
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        this.studentInactive = studentInactive;
    }

    public boolean isStudentInactive() {
        return studentInactive;
    }

    public void setStudentInactive(boolean studentInactive) {
        this.studentInactive = studentInactive;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Group getGroup() {
        return group;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + (studentInactive ? 1231 : 1237);
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + studentId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (group != null && other.group != null){
            if (group.getGroupId() != other.group.getGroupId())
                return false;
        }
        if (studentInactive != other.studentInactive)
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (studentId != other.studentId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        String groupId = group == null ? "Without group" : String.valueOf(group.getGroupId());
        return "Student{" +
                "studentId=" + studentId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", group=" + groupId +
                ", studentInactive=" + studentInactive +
                '}';
    }
}