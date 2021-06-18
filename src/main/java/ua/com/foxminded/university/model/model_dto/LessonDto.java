package ua.com.foxminded.university.model.model_dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ua.com.foxminded.university.annotation.NameConstraint;

import javax.validation.constraints.Positive;


public class LessonDto {
    private int lessonId;

    @NameConstraint
    private String lessonName;

    private Integer teacherId;

    private Integer groupId;

    private Integer roomId;

    private String date;

    @Positive(message = "Lesson number must be positive")
    private int lessonNumber;

    @JsonIgnore
    private boolean lessonInactive;

    public LessonDto() {
    }

    public LessonDto(int lessonId, String lessonName, int teacherId, int groupId, int roomId, String date, int lessonNumber) {
        this.lessonId = lessonId;
        this.lessonName = lessonName;
        this.teacherId = teacherId;
        this.groupId = groupId;
        this.roomId = roomId;
        this.date = date;
        this.lessonNumber = lessonNumber;
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

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isLessonInactive() {
        return lessonInactive;
    }

    public void setLessonInactive(boolean lessonInactive) {
        this.lessonInactive = lessonInactive;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(int lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    @Override
    public String toString() {
        return "LessonDto{" +
                "lessonId=" + lessonId +
                ", lessonName='" + lessonName + '\'' +
                ", teacherId=" + teacherId +
                ", groupId=" + groupId +
                ", roomId=" + roomId +
                ", date='" + date + '\'' +
                ", lessonNumber=" + lessonNumber +
                ", lessonInactive=" + lessonInactive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LessonDto lessonDto = (LessonDto) o;

        if (lessonId != lessonDto.lessonId) return false;
        if (lessonNumber != lessonDto.lessonNumber) return false;
        if (lessonInactive != lessonDto.lessonInactive) return false;
        if (!lessonName.equals(lessonDto.lessonName)) return false;
        if (teacherId != null ? !teacherId.equals(lessonDto.teacherId) : lessonDto.teacherId != null) return false;
        if (groupId != null ? !groupId.equals(lessonDto.groupId) : lessonDto.groupId != null) return false;
        if (roomId != null ? !roomId.equals(lessonDto.roomId) : lessonDto.roomId != null) return false;
        return date.equals(lessonDto.date);
    }

    @Override
    public int hashCode() {
        int result = lessonId;
        result = 31 * result + lessonName.hashCode();
        result = 31 * result + (teacherId != null ? teacherId.hashCode() : 0);
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (roomId != null ? roomId.hashCode() : 0);
        result = 31 * result + date.hashCode();
        result = 31 * result + lessonNumber;
        result = 31 * result + (lessonInactive ? 1 : 0);
        return result;
    }
}


