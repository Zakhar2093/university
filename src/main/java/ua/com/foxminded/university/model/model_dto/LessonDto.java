package ua.com.foxminded.university.model.model_dto;

import ua.com.foxminded.university.annotation.Free;
import ua.com.foxminded.university.annotation.NameConstraint;
import ua.com.foxminded.university.annotation.RoomCapacityConstraint;

@RoomCapacityConstraint(
        groupId = "groupId",
        roomId = "roomId")
@Free.List({
        @Free(
                id = "groupId",
                date = "date",
                number = "lessonNumber",
                message = "The group has already been busy in another lesson. Please choose another day or lesson number."),
        @Free(
                id = "groupId",
                date = "date",
                number = "lessonNumber",
                message = "The group has already been busy in another lesson. Please choose another day or lesson number."),
        @Free(
                id = "groupId",
                date = "date",
                number = "lessonNumber",
                message = "The group has already been busy in another lesson. Please choose another day or lesson number.")
})
public class LessonDto {
    private int lessonId;

    @NameConstraint
    private String lessonName;

    private Integer teacherId;

    private Integer groupId;

    private Integer roomId;

//    @FutureOrPresent(message = "Lesson date can not be past")
    private String date;

    private int lessonNumber;

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
}


