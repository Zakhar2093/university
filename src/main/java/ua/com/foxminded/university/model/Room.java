package ua.com.foxminded.university.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.List;

@Entity
@Table(name="rooms", schema = "university")
public class Room {
    @Id
    @Column(name="room_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int roomId;

    @Positive(message = "Room number must be positive")
    @Column(name="room_number")
    private int roomNumber;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room", fetch = FetchType.LAZY)
    private List<Lesson> lessons;

    @JsonIgnore
    @Column(name="room_inactive")
    private boolean roomInactive;

    @Positive(message = "Room capacity must be positive")
    @Column(name="room_capacity")
    private int roomCapacity;
    
    public Room() {
        super();
    }

    public Room(int roomId, int roomNumber, int roomCapacity, boolean roomInactive) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomInactive = roomInactive;
        this.roomCapacity = roomCapacity;
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

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    @Override
    public int hashCode() {
        int result = roomId;
        result = 31 * result + roomNumber;
        result = 31 * result + (lessons != null ? lessons.hashCode() : 0);
        result = 31 * result + (roomInactive ? 1 : 0);
        result = 31 * result + roomCapacity;
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
        if (roomCapacity != other.roomCapacity) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomNumber=" + roomNumber +
                ", lessons=" + lessons +
                ", roomInactive=" + roomInactive +
                ", roomCapacity=" + roomCapacity +
                '}';
    }
}