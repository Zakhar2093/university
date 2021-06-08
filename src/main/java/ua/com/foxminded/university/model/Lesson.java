package ua.com.foxminded.university.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="lessons", schema = "university")
public class Lesson {

    @Id
    @Column(name="lesson_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int lessonId;

    @Column(name = "lesson_name")
    private String lessonName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "lesson_date")
    private LocalDate date;

    @Column(name = "lesson_number")
    private int lessonNumber;

    @Column(name = "lesson_inactive")
    private boolean lessonInactive;
    
    public Lesson() {
        super();
    }

    public Lesson(int lessonId,
                  String lessonName,
                  Teacher teacher,
                  Group group,
                  Room room,
                  LocalDate date,
                  int lessonNumber) {
        super();
        this.lessonId = lessonId;
        this.lessonName = lessonName;
        this.teacher = teacher;
        this.group = group;
        this.room = room;
        this.date = date;
        this.lessonNumber = lessonNumber;
    }

    @Override
    public String toString() {
        String teacherId = teacher == null ? "Without teacher" : String.valueOf(teacher.getTeacherId());
        String groupId = group == null ? "Without group" : String.valueOf(group.getGroupId());
        String roomId = room == null ? "Without room" : String.valueOf(room.getRoomId());
        return "Lesson{" +
                "lessonId=" + lessonId +
                ", lessonName='" + lessonName + '\'' +
                ", teacher=" + teacherId +
                ", group=" + groupId +
                ", room=" + roomId +
                ", date=" + date +
                ", lessonNumber=" + lessonNumber +
                ", lessonInactive=" + lessonInactive +
                '}';
    }
    @Override
    public int hashCode() {
        int result = lessonId;
        result = 31 * result + (lessonName != null ? lessonName.hashCode() : 0);
        result = 31 * result + (teacher != null ? teacher.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (room != null ? room.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + lessonNumber;
        result = 31 * result + (lessonInactive ? 1 : 0);
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
        Lesson other = (Lesson) obj;
        if (lessonNumber != other.lessonNumber) return false;
        if (lessonId != other.lessonId) return false;
        if (lessonInactive != other.lessonInactive) return false;
        if (lessonName != null ? !lessonName.equals(other.lessonName) : other.lessonName != null) return false;

        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;

        if (group == null) {
            if (other.group != null)
                return false;
        } else if (group != null && other.group != null){
            if (group.getGroupId() != other.group.getGroupId())
                return false;
        }

        if (room == null) {
            if (other.room != null)
                return false;
        } else if (room != null && other.room != null){
            if (room.getRoomId() != other.room.getRoomId())
                return false;
        }

        if (teacher == null) {
            if (other.teacher != null)
                return false;
        } else if (teacher != null && other.teacher != null){
            if (teacher.getTeacherId() != other.teacher.getTeacherId())
                return false;
        }
        return true;
    }

    public boolean isLessonInactive() {
        return lessonInactive;
    }

    public void setLessonInactive(boolean lessonInactive) {
        this.lessonInactive = lessonInactive;
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
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(int lessonNumber) {
        this.lessonNumber = lessonNumber;
    }
}