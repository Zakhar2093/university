package ua.com.foxminded.university.models;

public class Teacher {
    private int teacherId;
    private String teacherFirstName;
    private String teacherLastName;
    
    public Teacher(int teacherId, String teacherFirstName, String teacherLastName) {
        super();
        this.teacherId = teacherId;
        this.teacherFirstName = teacherFirstName;
        this.teacherLastName = teacherLastName;
    }
    
    public int getTeacherId() {
        return teacherId;
    }
    
    public String getTeacherFirstName() {
        return teacherFirstName;
    }
    
    public String getTeacherLastName() {
        return teacherLastName;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((teacherFirstName == null) ? 0 : teacherFirstName.hashCode());
        result = prime * result + teacherId;
        result = prime * result + ((teacherLastName == null) ? 0 : teacherLastName.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Teacher other = (Teacher) obj;
        if (teacherFirstName == null) {
            if (other.teacherFirstName != null)
                return false;
        } else if (!teacherFirstName.equals(other.teacherFirstName))
            return false;
        if (teacherId != other.teacherId)
            return false;
        if (teacherLastName == null) {
            if (other.teacherLastName != null)
                return false;
        } else if (!teacherLastName.equals(other.teacherLastName))
            return false;
        return true;
    }

    public String toString() {
        return "Teacher [teacherId=" + teacherId + ", teacherFirstName=" + teacherFirstName + ", teacherLastName="
                + teacherLastName + "]";
    }
}
