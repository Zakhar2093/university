package ua.com.foxminded.university.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="teachers", schema = "university")
public class Teacher {
    @Id
    @Column(name="teacher_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int teacherId;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Transient
    private List<Lesson> lessons;
    @Column(name="teacher_inactive")
    private boolean teacherInactive;
    
    public Teacher() {
    }

    public Teacher(int teacherId, String firstName, String lastName, boolean teacherInactive) {
        super();
        this.teacherId = teacherId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.teacherInactive = teacherInactive;
    }

    public Teacher(int teacherId, String firstName, String lastName, List<Lesson> lessons, boolean teacherInactive) {
        super();
        this.teacherId = teacherId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lessons = lessons;
        this.teacherInactive = teacherInactive;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }
    
    public boolean isTeacherInactive() {
        return teacherInactive;
    }

    public void setTeacherInactive(boolean teacherInactive) {
        this.teacherInactive = teacherInactive;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((lessons == null) ? 0 : lessons.hashCode());
        result = prime * result + teacherId;
        result = prime * result + (teacherInactive ? 1231 : 1237);
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
        Teacher other = (Teacher) obj;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (lessons == null) {
            if (other.lessons != null)
                return false;
        } else if (!lessons.equals(other.lessons))
            return false;
        if (teacherId != other.teacherId)
            return false;
        if (teacherInactive != other.teacherInactive)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Teacher [teacherId=" + teacherId + ", firstName=" + firstName + ", lastName=" + lastName + ", lessons="
                + lessons + ", teacherInactive=" + teacherInactive + "]";
    }
}