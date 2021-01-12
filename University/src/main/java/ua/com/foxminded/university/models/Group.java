package ua.com.foxminded.university.models;

import java.util.List;

public class Group {
    private int groupId;
    private String groupName;
    private List<Student> students;
    private List<Lesson> lessons;
    
    public Group(int groupId, String groupName, List<Student> students, List<Lesson> lessons) {
        super();
        this.groupId = groupId;
        this.groupName = groupName;
        this.students = students;
        this.lessons = lessons;
    }
    
    public int getGroupId() {
        return groupId;
    }
    public String getGroupName() {
        return groupName;
    }
    public List<Student> getStudents() {
        return students;
    }
    public List<Lesson> getLessons() {
        return lessons;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + groupId;
        result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
        result = prime * result + ((lessons == null) ? 0 : lessons.hashCode());
        result = prime * result + ((students == null) ? 0 : students.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Group other = (Group) obj;
        if (groupId != other.groupId)
            return false;
        if (groupName == null) {
            if (other.groupName != null)
                return false;
        } else if (!groupName.equals(other.groupName))
            return false;
        if (lessons == null) {
            if (other.lessons != null)
                return false;
        } else if (!lessons.equals(other.lessons))
            return false;
        if (students == null) {
            if (other.students != null)
                return false;
        } else if (!students.equals(other.students))
            return false;
        return true;
    }

    public String toString() {
        return "Group [groupId=" + groupId + ", groupName=" + groupName + ", students=" + students + ", lessons="
                + lessons + "]";
    }
}