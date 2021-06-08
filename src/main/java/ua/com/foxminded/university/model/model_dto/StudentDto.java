package ua.com.foxminded.university.model.model_dto;

import ua.com.foxminded.university.annotation.NameConstraint;

public class StudentDto {

    private int studentId;

    @NameConstraint
    private String firstName;

    @NameConstraint
    private String lastName;

    private Integer groupId;

    private boolean studentInactive;

    public StudentDto() {
    }

    public StudentDto(int studentId, String firstName, String lastName, int groupId, boolean studentInactive) {
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
}

