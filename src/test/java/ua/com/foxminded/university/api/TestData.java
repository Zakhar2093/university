package ua.com.foxminded.university.api;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestData {
    private static final String FORMAT = "yyyy-MM-dd";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);

    protected List<Room> getTestRooms() {
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(1, 101, 10, false));
        rooms.add(new Room(2, 102, 10, false));
        rooms.add(new Room(3, 103, 10, false));
        return rooms;
    }

    protected List<Group> getTestGroups() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(1, "Java", false));
        groups.add(new Group(2, "C++", false));
        groups.add(new Group(3, "PHP", false));
        return groups;
    }

    protected List<Teacher> getTestTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher(1, "one", "one", false));
        teachers.add(new Teacher(2, "two", "two", false));
        teachers.add(new Teacher(3, "Three", "Three", false));
        return teachers;
    }

    protected List<Lesson> getTestLessons() {
        List<Teacher> teachers = getTestTeachers();
        List<Group> groups = getTestGroups();
        List<Room> rooms = getTestRooms();
        LocalDate date = LocalDate.parse("2021-11-04", FORMATTER);

        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson(1, "Math", teachers.get(1), groups.get(0), rooms.get(0), date, 1));
        lessons.add(new Lesson(2, "History", teachers.get(1), groups.get(1), rooms.get(0), date, 1));
        lessons.add(new Lesson(3, "English", teachers.get(2), groups.get(2), rooms.get(2), date, 1));
        lessons.add(new Lesson(4, "Math", teachers.get(0), groups.get(1), rooms.get(1), date, 1));
        lessons.add(new Lesson(5, "Bio", teachers.get(0), groups.get(2), rooms.get(1), date, 1));
        return lessons;
    }

    protected List<Student> getTestStudent() {
        List<Group> groups = getTestGroups();
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "one", "one", groups.get(0), false));
        students.add(new Student(1, "two", "two", groups.get(2), false));
        students.add(new Student(1, "three", "three", groups.get(1), false));
        return students;
    }
}
