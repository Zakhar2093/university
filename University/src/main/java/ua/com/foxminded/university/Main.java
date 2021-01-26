package ua.com.foxminded.university;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.joda.LocalDateParser;

import ua.com.foxminded.university.dao.implementation.GroupDaoImpl;
import ua.com.foxminded.university.dao.implementation.LessonDaoImpl;
import ua.com.foxminded.university.dao.implementation.RoomDaoImpl;
import ua.com.foxminded.university.dao.implementation.StudentDaoImpl;
import ua.com.foxminded.university.dao.implementation.TeacherDaoImpl;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;

public class Main {

    public static void main(String[] args) throws ParseException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
//        LessonDaoImpl lessonDao = context.getBean("lessonDaoImpl", LessonDaoImpl.class);
//        GroupDaoImpl groupDao = context.getBean("groupDaoImpl", GroupDaoImpl.class);
//        TeacherDao teacherDao = context.getBean("teacherDaoImpl", TeacherDaoImpl.class);
//        RoomDao roomDao = context.getBean("roomDaoImpl", RoomDaoImpl.class);
        StudentDaoImpl studentDao = context.getBean("studentDaoImpl", StudentDaoImpl.class);

//        Teacher teacher = new Teacher(1, "one", "one");
//        teacherDao.create(teacher);
//        Room room = new Room(1, 101);
//        roomDao.create(room);     
//        Group group = new Group(1, "any name");
//        groupDao.create(group);
//        Student student = new Student(1, "sda", "asd", group);
//        studentDao.create(student);
        studentDao.activate(5);
        
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm.ss");
//        LocalDateTime date1 = LocalDateTime.parse("2021.01.20-23.55.11", formatter);
//        LocalDateTime date2 = LocalDateTime.parse("2021.01.21-03.00.00", formatter);
//        LocalDateTime date3 = LocalDateTime.parse("2021.01.21-23.55.11", formatter);
//        LocalDateTime date4 = LocalDateTime.parse("2021.02.11-23.55.11", formatter);
        
//        Lesson lesson1 = new Lesson(1, "Math", teacher, group, room, date1);
//        Lesson lesson2 = new Lesson(1, "bio", teacher, group, room, date2);
//        Lesson lesson3 = new Lesson(1, "history", teacher, group, room, date3);
//        Lesson lesson4 = new Lesson(1, "english", teacher, group, room, date4);
//        
//        lessonDao.create(lesson1);
//        lessonDao.create(lesson2);
//        lessonDao.create(lesson3);
//        lessonDao.create(lesson4);

        System.out.println(studentDao.getAll());
        context.close();
    }

}
