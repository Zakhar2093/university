package ua.com.foxminded.university.model.model_dto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LessonDto {
    private int lessonId;

    @NotBlank(message = "Lesson name is mandatory")
    @Size(min = 2, max = 12, message = "Lesson name size must be between 2 and 12")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Lesson name must not contain special characters")
    private String lessonName;

    private Integer teacherId;

    private Integer groupId;

    private Integer roomId;

    @FutureOrPresent(message = "Lesson date can not be past")
    private String date;

    private boolean lessonInactive;

    public LessonDto() {
    }

    public LessonDto(int lessonId, String lessonName, int teacherId, int groupId, int roomId, String date, boolean lessonInactive) {
        this.lessonId = lessonId;
        this.lessonName = lessonName;
        this.teacherId = teacherId;
        this.groupId = groupId;
        this.roomId = roomId;
        this.date = date;
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

}


