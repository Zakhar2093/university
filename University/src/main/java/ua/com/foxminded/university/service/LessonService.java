package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.model_dto.LessonDto;
import ua.com.foxminded.university.repository.LessonRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class LessonService implements GenericService<Lesson, Integer>{

    private static final String FORMAT = "dd MM yyyy hh:mm a";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);

    private LessonRepository lessonRepository;
    private GroupService groupService;
    private TeacherService teacherService;
    private RoomService roomService;
    private StudentService studentService;

    @Autowired
    public LessonService(LessonRepository lessonRepository,
                         GroupService groupService,
                         TeacherService teacherService,
                         RoomService roomService,
                         StudentService studentService) {
        this.lessonRepository = lessonRepository;
        this.groupService = groupService;
        this.teacherService = teacherService;
        this.roomService = roomService;
        this.studentService = studentService;
    }

    public void create(Lesson lesson) {
        try {
            lessonRepository.save(lesson);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void create(LessonDto lessonDto) {
        try {
            Lesson lesson = mapDtoToLesson(lessonDto);
            lessonRepository.save(lesson);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getAll(){
        try {
            return lessonRepository.findAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getAllActivated(){
        try {
            List<Lesson> lessons = lessonRepository.findAll();
            lessons.removeIf(p -> (p.isLessonInactive()));
            return lessons;
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public Lesson getById(Integer lessonId) {
        try {
            return lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new ServiceException(
                            String.format("Lesson with such id %d does not exist", lessonId)
                    ));
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public LessonDto getDtoById(Integer lessonId) {
        try {
            return mapLessonToDto(getById(lessonId));
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Lesson lesson) {
        try {
            lessonRepository.save(lesson);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void update(LessonDto lessonDto) {
        try {
            Lesson lesson = mapDtoToLesson(lessonDto);
            lessonRepository.save(lesson);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void deactivate(Integer lessonId) {
        try {
            Lesson lesson = getById(lessonId);
            lessonRepository.save(lesson);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void activate(Integer lessonId) {
        try {
            Lesson lesson = getById(lessonId);
            lesson.setLessonInactive(false);
            lessonRepository.save(lesson);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonByTeacherIdForDay(int teacherId, LocalDateTime date) {
        try {
            return lessonRepository.getLessonByTeacherIdForDay(
                    teacherId,
                    date.getYear(),
                    date.getMonthValue(),
                    date.getDayOfMonth());
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonByTeacherIdForMonth(int teacherId, LocalDateTime date) {
        try {
            return lessonRepository.getLessonByTeacherIdForMonth(
                    teacherId,
                    date.getYear(),
                    date.getMonthValue());
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonByStudentIdForDay(int studentId, LocalDateTime date) {
        try {
            Group group = studentService.getById(studentId).getGroup();
            return lessonRepository.getLessonByStudentIdForDay(
                    group,
                    date.getYear(),
                    date.getMonthValue(),
                    date.getDayOfMonth());
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonByStudentIdForMonth(int studentId, LocalDateTime date) {
        try {
            Group group = studentService.getById(studentId).getGroup();
            return lessonRepository.getLessonByStudentIdForMonth(
                    group,
                    date.getYear(),
                    date.getMonthValue());
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonsByGroupId(Integer groupId) {
        try {
            return lessonRepository.findByGroupGroupId(groupId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonsByTeacherId(Integer teacherId) {
        try {
            return lessonRepository.findByTeacherTeacherId(teacherId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonsByRoomId(Integer roomId) {
        try {
            return lessonRepository.findByRoomRoomId(roomId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    private Lesson mapDtoToLesson(LessonDto dto){
        Lesson lesson = new Lesson();
        lesson.setLessonId(dto.getLessonId());
        lesson.setLessonName(dto.getLessonName());
        Group group = groupService.getById(dto.getGroupId());
        lesson.setGroup(group);
        Teacher teacher = teacherService.getById(dto.getTeacherId());
        lesson.setTeacher(teacher);
        Room room = roomService.getById(dto.getRoomId());
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