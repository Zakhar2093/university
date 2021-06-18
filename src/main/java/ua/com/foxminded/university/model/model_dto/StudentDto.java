package ua.com.foxminded.university.model.model_dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ua.com.foxminded.university.annotation.NameConstraint;

public class StudentDto {

    private int studentId;

    @NameConstraint
    private String firstName;

    @NameConstraint
    private String lastName;

    private Integer groupId;

    @JsonIgnore
    private boolean studentInactive;

    public StudentDto() {
    }

    public StudentDto(int studentId, String firstName, String lastName, Integer groupId, boolean studentInactive) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupId = groupId;
        this.studentInactive = studentInactive;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public boolean isStudentInactive() {
        return studentInactive;
    }

    public void setStudentInactive(boolean studentInactive) {
        this.studentInactive = studentInactive;
    }

    @Override
    public String toString() {
        return "StudentDto{" +
                "studentId=" + studentId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", groupId=" + groupId +
                ", studentInactive=" + studentInactive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentDto that = (StudentDto) o;

        if (studentId != that.studentId) return false;
        if (studentInactive != that.studentInactive) return false;
        if (!firstName.equals(that.firstName)) return false;
        if (!lastName.equals(that.lastName)) return false;
        return groupId != null ? groupId.equals(that.groupId) : that.groupId == null;
    }

    @Override
    public int hashCode() {
        int result = studentId;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (studentInactive ? 1 : 0);
        return result;
    }
}

