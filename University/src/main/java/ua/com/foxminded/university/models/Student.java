package ua.com.foxminded.university.models;

public class Student {
    private int studentId;
    private String studentFirstName;
    private String studentLastName;
    private int groupId;
    
    public Student(int studentId, String studentFirstName, String studentLastName, int groupId) {
        super();
        this.studentId = studentId;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.groupId = groupId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public int getGroupId() {
        return groupId;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + groupId;
        result = prime * result + ((studentFirstName == null) ? 0 : studentFirstName.hashCode());
        result = prime * result + studentId;
        result = prime * result + ((studentLastName == null) ? 0 : studentLastName.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        if (groupId != other.groupId)
            return false;
        if (studentFirstName == null) {
            if (other.studentFirstName != null)
                return false;
        } else if (!studentFirstName.equals(other.studentFirstName))
            return false;
        if (studentId != other.studentId)
            return false;
        if (studentLastName == null) {
            if (other.studentLastName != null)
                return false;
        } else if (!studentLastName.equals(other.studentLastName))
            return false;
        return true;
    }

    public String toString() {
        return "Student [studentId=" + studentId + ", studentFirstName=" + studentFirstName + ", studentLastName="
                + studentLastName + ", groupId=" + groupId + "]";
    }
}
