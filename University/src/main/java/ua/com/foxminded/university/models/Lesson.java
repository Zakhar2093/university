package ua.com.foxminded.university.models;

import java.util.Date;

public class Lesson {
    private int lessonId;
    private String lessonName;
    private int teacherId;
    private Group group;
    private Date date;
    
    public Lesson(int lessonId, String lessonName, int teacherId, Group group, Date date) {
        super();
        this.lessonId = lessonId;
        this.lessonName = lessonName;
        this.teacherId = teacherId;
        this.group = group;
        this.date = date;
    }

    public int getLessonId() {
        return lessonId;
    }

    public String getLessonName() {
        return lessonName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public Group getGroup() {
        return group;
    }

    public Date getDate() {
        return date;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + lessonId;
        result = prime * result + ((lessonName == null) ? 0 : lessonName.hashCode());
        result = prime * result + teacherId;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Lesson other = (Lesson) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
            return false;
        if (lessonId != other.lessonId)
            return false;
        if (lessonName == null) {
            if (other.lessonName != null)
                return false;
        } else if (!lessonName.equals(other.lessonName))
            return false;
        if (teacherId != other.teacherId)
            return false;
        return true;
    }

    public String toString() {
        return "Lesson [lessonId=" + lessonId + ", lessonName=" + lessonName + ", teacherId=" + teacherId + ", group="
                + group + ", date=" + date + "]";
    } 
}