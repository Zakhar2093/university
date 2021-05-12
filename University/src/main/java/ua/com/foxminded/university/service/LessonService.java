package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.RoomRepository;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.model.model_dto.LessonDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class LessonService implements GenericService<Lesson, Integer>{

    private static final String FORMAT = "dd MM yyyy hh:mm a";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);

    private LessonRepository lessonDao;
    private GroupRepository groupDao;
    private TeacherRepository teacherDao;
    private RoomRepository roomDao;

    @Autowired
    public LessonService(LessonRepository lessonDao, GroupRepository groupDao, TeacherRepository teacherDao, RoomRepository roomDao) {
        this.lessonDao = lessonDao;
        this.groupDao = groupDao;
        this.teacherDao = teacherDao;
        this.roomDao = roomDao;
    }

    public void create(Lesson lesson) {
        try {
            lessonDao.create(lesson);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void create(LessonDto lessonDto) {
        try {
            Lesson lesson = mapDtoToLesson(lessonDto);
            lessonDao.create(lesson);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getAll() {
        try {
            return lessonDao.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getAllActivated(){
        try {
            List<Lesson> lessons = lessonDao.getAll();
            lessons.removeIf(p -> (p.isLessonInactive()));
            return lessons;
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public Lesson getById(Integer lessonId) {
        try {
            return lessonDao.getById(lessonId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public LessonDto getDtoById(Integer lessonId) {
        try {
            return mapLessonToDto(lessonDao.getById(lessonId));
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Lesson lesson) {
        try {
            lessonDao.update(lesson);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void update(LessonDto lessonDto) {
        try {
            Lesson lesson = mapDtoToLesson(lessonDto);
            lessonDao.update(lesson);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void deactivate(Integer lessonId) {
        try {
            lessonDao.deactivate(lessonId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void activate(Integer lessonId) {
        try {
            lessonDao.activate(lessonId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonByTeacherIdForDay(int teacherId, LocalDateTime date) {
        try {
            return lessonDao.getLessonByTeacherIdForDay(teacherId, date);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonByTeacherIdForMonth(int teacherId, LocalDateTime date) {
        try {
            return lessonDao.getLessonByTeacherIdForMonth(teacherId, date);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonByStudentIdForDay(int studentId, LocalDateTime date) {
        try {
            return lessonDao.getLessonByStudentIdForDay(studentId, date);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonByStudentIdForMonth(int studentId, LocalDateTime date) {
        try {
            return lessonDao.getLessonByStudentIdForMonth(studentId, date);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonsByGroupId(Integer groupId) {
        try {
            return lessonDao.getLessonsByGroupId(groupId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonsByTeacherId(Integer teacherId) {
        try {
            return lessonDao.getLessonsByTeacherId(teacherId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonsByRoomId(Integer roomId) {
        try {
            return lessonDao.getLessonsByRoomId(roomId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    private Lesson mapDtoToLesson(LessonDto dto){
        Lesson lesson = new Lesson();
        lesson.setLessonId(dto.getLessonId());
        lesson.setLessonName(dto.getLessonName());
        Group group = groupDao.getById(dto.getGroupId());
        lesson.setGroup(group);
        Teacher teacher = teacherDao.getById(dto.getTeacherId());
        lesson.setTeacher(teacher);
        Room room = roomDao.getById(dto.getRoomId());
        lesson.setRoom(room);
        lesson.setLessonInactive(dto.isLessonInactive());
        lesson.setDate(LocalDateTime.parse(dto.getDate(), FORMATTER));
        return lesson;
    }

    private LessonDto mapLessonToDto(Lesson lesson){
        LessonDto dto = new LessonDto();
        dto.setLessonId(lesson.getLessonId());
        dto.setLessonName(lesson.getLessonName());
        dto.setLessonInactive(lesson.isLessonInactive());
        dto.setGroupId(lesson.getGroup() == null ? null : lesson.getGroup().getGroupId());
        dto.setRoomId(lesson.getRoom() == null ? null : lesson.getRoom().getRoomId());
        dto.setTeacherId(lesson.getTeacher() == null ? null : lesson.getTeacher().getTeacherId());
        dto.setDate(lesson.getDate().toString());
        return dto;
    }
}