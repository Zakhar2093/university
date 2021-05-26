package ua.com.foxminded.university.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="rooms", schema = "university")
public class Room {
    @Id
    @Column(name="room_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int roomId;

    @Column(name="room_number")
    private int roomNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room", fetch = FetchType.LAZY)
    private List<Lesson> lessons;

    @Column(name="room_inactive")
    private boolean roomInactive;
    
    public Room() {
        super();
    }

    public Room(int roomId, int roomNumber) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
    }

    public Room(int roomId, int roomNumber, boolean roomInactive) {
        super();
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomInactive = roomInactive;
    }

    public Room(int roomId, int roomNumber, List<Lesson> lessons, boolean roomInactive) {
        super();
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.lessons = lessons;
        this.roomInactive = roomInactive;
    }

    public boolean isRoomInactive() {
        return roomInactive;
    }

    public void setRoomInactive(boolean roomInactive) {
        this.roomInactive = roomInactive;
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

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lessons == null) ? 0 : lessons.hashCode());
        result = prime * result + roomId;
        result = prime * result + (roomInactive ? 1231 : 1237);
        result = prime * result + roomNumber;
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
        Room other = (Room) obj;
        if (lessons == null) {
            if (other.lessons != null)
                return false;
        }
        if (roomId != other.roomId)
            return false;
        if (roomInactive != other.roomInactive)
            return false;
        if (roomNumber != other.roomNumber)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Room [roomId=" + roomId + ", roomNumber=" + roomNumber + ", lessons=" + lessons + ", roomInactive="
                + roomInactive + "]";
    }
}