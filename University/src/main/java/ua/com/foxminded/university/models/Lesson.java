package ua.com.foxminded.university.models;

import java.util.Date;

public class Lesson {
    private int lessonId;
    private String lessonName;
    private Teacher teacher;
    private Group group;
    private Room room;
    private Date date;
    
    public Lesson(int lessonId, String lessonName, Teacher teacher, Group group, Room room, Date date) {
        super();
        this.lessonId = lessonId;
        this.lessonName = lessonName;
        this.teacher = teacher;
        this.group = group;
        this.room = room;
        this.date = date;
    }

    public String toString() {
        return "Lesson [lessonId=" + lessonId + ", lessonName=" + lessonName + ", teacher=" + teacher + ", group="
                + group + ", room=" + room + ", date=" + date + "]";
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + lessonId;
        result = prime * result + ((lessonName == null) ? 0 : lessonName.hashCode());
        result = prime * result + ((room == null) ? 0 : room.hashCode());
        result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
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
        if (room == null) {
            if (other.room != null)
                return false;
        } else if (!room.equals(other.room))
            return false;
        if (teacher == null) {
            if (other.teacher != null)
                return false;
        } else if (!teacher.equals(other.teacher))
            return false;
        return true;
    }

    public int getLessonId() {
        return lessonId;
    }
    
    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }
    
    public String getLessonName() {
        return lessonName;
    }
    
    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }
    
    public Teacher getTeacher() {
        return teacher;
    }
    
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    
    public Group getGroup() {
        return group;
    }
    
    public void setGroup(Group group) {
        this.group = group;
    }
    
    public Room getRoom() {
        return room;
    }
    
    public void setRoom(Room room) {
        this.room = room;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    } 
}