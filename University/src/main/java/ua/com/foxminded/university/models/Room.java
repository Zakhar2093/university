package ua.com.foxminded.university.models;

import java.util.List;

public class Room {
    private int roomId;
    private int roomNumber;
    private List<Lesson> lessons;
    
    public Room(int roomId, int roomNumber, List<Lesson> lessons) {
        super();
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.lessons = lessons;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lessons == null) ? 0 : lessons.hashCode());
        result = prime * result + roomId;
        result = prime * result + roomNumber;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Room other = (Room) obj;
        if (lessons == null) {
            if (other.lessons != null)
                return false;
        } else if (!lessons.equals(other.lessons))
            return false;
        if (roomId != other.roomId)
            return false;
        if (roomNumber != other.roomNumber)
            return false;
        return true;
    }

    public String toString() {
        return "Room [roomId=" + roomId + ", roomNumber=" + roomNumber + ", lessons=" + lessons + "]";
    }
}
