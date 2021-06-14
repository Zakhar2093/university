package ua.com.foxminded.university.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name="groups", schema = "university")
public class Group {
    @Id
    @Column(name="group_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int groupId;

    @Size(min = 2, max = 12, message = "Group name size must be between 2 and 12")
    @NotBlank(message = "Group name is mandatory")
    @Column(name="group_name")
    private String groupName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group", fetch = FetchType.LAZY)
    private List<Student> students;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group", fetch = FetchType.LAZY)
    private List<Lesson> lessons;

    @Column(name="group_inactive")
    private boolean groupInactive;

    public Group() {
    }

    public Group(int groupId, String groupName, boolean groupInactive) {
        super();
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupInactive = groupInactive;
    }

    public Group(int groupId, String groupName, List<Student> students, List<Lesson> lessons, boolean groupInactive) {
        super();
        this.groupId = groupId;
        this.groupName = groupName;
        this.students = students;
        this.lessons = lessons;
        this.groupInactive = groupInactive;
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

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public boolean isGroupInactive() {
        return groupInactive;
    }

    public void setGroupInactive(boolean groupInactive) {
        this.groupInactive = groupInactive;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + groupId;
        result = prime * result + (groupInactive ? 1231 : 1237);
        result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
        result = prime * result + ((lessons == null) ? 0 : lessons.hashCode());
        result = prime * result + ((students == null) ? 0 : students.hashCode());
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
        Group other = (Group) obj;
        if (groupId != other.groupId)
            return false;
        if (groupInactive != other.groupInactive)
            return false;
        if (groupName == null) {
            if (other.groupName != null)
                return false;
        } else if (!groupName.equals(other.groupName))
            return false;
        if (lessons == null) {
            if (other.lessons != null)
                return false;
        }
        if (students == null) {
            if (other.students != null)
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Group [groupId=" + groupId + ", groupName=" + groupName + ", students=" + students + ", lessons="
                + lessons + ", groupInactive=" + groupInactive + "]";
    }
}