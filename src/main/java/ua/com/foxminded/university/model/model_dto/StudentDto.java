package ua.com.foxminded.university.model.model_dto;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class StudentDto {

    private int studentId;

    @NotBlank(message = "Student first name is mandatory")
    @Size(min = 2, max = 12, message = "Student first name size must be between 2 and 12")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Student first name must not contain special characters")
    @Column(name="first_name")
    private String firstName;

    @NotBlank(message = "Student last name is mandatory")
    @Size(min = 2, max = 12, message = "Student last name size must be between 2 and 12")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Student last name must not contain special characters")
    @Column(name="last_name")
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

