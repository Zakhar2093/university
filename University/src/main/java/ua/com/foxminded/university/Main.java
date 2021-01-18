package ua.com.foxminded.university;


import java.sql.Date;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.university.dao.implementations.GroupDaoImpl;
import ua.com.foxminded.university.dao.implementations.LessonDaoImpl;
import ua.com.foxminded.university.dao.implementations.RoomDaoImpl;
import ua.com.foxminded.university.dao.implementations.TeacherDaoImpl;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.models.Group;
import ua.com.foxminded.university.models.Lesson;
import ua.com.foxminded.university.models.Room;
import ua.com.foxminded.university.models.Teacher;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        GroupDao groupDao = context.getBean("groupDaoImpl", GroupDaoImpl.class);
        TeacherDao teacherDao = context.getBean("teacherDaoImpl", TeacherDaoImpl.class);
        RoomDao roomDao = context.getBean("roomDaoImpl", RoomDaoImpl.class);
        LessonDao lessonDao = context.getBean("lessonDaoImpl", LessonDaoImpl.class);
        
        Group group = new Group(1, "any name");
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one");
        teacherDao.create(teacher);
        Room room = new Room(1, 101);
        roomDao.create(room);
        
        Lesson lesson = new Lesson(1, "Math", teacher, group, room, new Date(1));
        lessonDao.create(lesson);
        Lesson expected = lesson;
        Lesson actual = lessonDao.getById(1);
        System.out.println(actual);
    }
}