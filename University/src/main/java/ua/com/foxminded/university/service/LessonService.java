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

    private LessonRepository lessonRepository;
    private GroupRepository groupRepository;
    private TeacherRepository teacherRepository;
    private RoomRepository roomRepository;

    @Autowired
    public LessonService(LessonRepository lessonRepository, GroupRepository groupRepository, TeacherRepository teacherRepository, RoomRepository roomRepository) {
        this.lessonRepository = lessonRepository;
        this.groupRepository = groupRepository;
        this.teacherRepository = teacherRepository;
        this.roomRepository = roomRepository;
    }

    public void create(Lesson lesson) {
        try {
            lessonRepository.create(lesson);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void create(LessonDto lessonDto) {
        try {
            Lesson lesson = mapDtoToLesson(lessonDto);
            lessonRepository.create(lesson);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getAll() {
        try {
            return lessonRepository.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getAllActivated(){
        try {
            List<Lesson> lessons = lessonRepository.getAll();
            lessons.removeIf(p -> (p.isLessonInactive()));
            return lessons;
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public Lesson getById(Integer lessonId) {
        try {
            return lessonRepository.getById(lessonId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public LessonDto getDtoById(Integer lessonId) {
        try {
            return mapLessonToDto(lessonRepository.getById(lessonId));
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Lesson lesson) {
        try {
            lessonRepository.update(lesson);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void update(LessonDto lessonDto) {
        try {
            Lesson lesson = mapDtoToLesson(lessonDto);
            lessonRepository.update(lesson);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void deactivate(Integer lessonId) {
        try {
            lessonRepository.deactivate(lessonId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void activate(Integer lessonId) {
        try {
            lessonRepository.activate(lessonId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonByTeacherIdForDay(int teacherId, LocalDateTime date) {
        try {
            return lessonRepository.getLessonByTeacherIdForDay(teacherId, date);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonByTeacherIdForMonth(int teacherId, LocalDateTime date) {
        try {
            return lessonRepository.getLessonByTeacherIdForMonth(teacherId, date);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonByStudentIdForDay(int studentId, LocalDateTime date) {
        try {
            return lessonRepository.getLessonByStudentIdForDay(studentId, date);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonByStudentIdForMonth(int studentId, LocalDateTime date) {
        try {
            return lessonRepository.getLessonByStudentIdForMonth(studentId, date);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonsByGroupId(Integer groupId) {
        try {
            return lessonRepository.getLessonsByGroupId(groupId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonsByTeacherId(Integer teacherId) {
        try {
            return lessonRepository.getLessonsByTeacherId(teacherId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonsByRoomId(Integer roomId) {
        try {
            return lessonRepository.getLessonsByRoomId(roomId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    private Lesson mapDtoToLesson(LessonDto dto){
        Lesson lesson = new Lesson();
        lesson.setLessonId(dto.getLessonId());
        lesson.setLessonName(dto.getLessonName());
        Group group = groupRepository.getById(dto.getGroupId());
        lesson.setGroup(group);
        Teacher teacher = teacherRepository.getById(dto.getTeacherId());
        lesson.setTeacher(teacher);
        Room room = roomRepository.getById(dto.getRoomId());
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