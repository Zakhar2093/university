package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.model_dto.LessonDto;
import ua.com.foxminded.university.repository.*;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Transactional
public class LessonService implements GenericService<Lesson, Integer>{

    private static final String FORMAT = "yyyy-MM-dd";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);

    private LessonRepository lessonRepository;
    private GroupRepository groupRepository;
    private TeacherRepository teacherRepository;
    private RoomRepository roomRepository;
    private StudentRepository studentRepository;

    @Autowired
    public LessonService(LessonRepository lessonRepository,
                         GroupRepository groupRepository,
                         TeacherRepository teacherRepository,
                         RoomRepository roomRepository,
                         StudentRepository studentRepository) {
        this.lessonRepository = lessonRepository;
        this.groupRepository = groupRepository;
        this.teacherRepository = teacherRepository;
        this.roomRepository = roomRepository;
        this.studentRepository = studentRepository;
    }

    public void save(Lesson lesson) {
        try {
            validate(lesson);
            lessonRepository.save(lesson);
        } catch (ConstraintViolationException e) {
            throw new ValidationException(e.getConstraintViolations().stream().findFirst().get().getMessage());
        }
    }

    public void save(LessonDto lessonDto) {
        Lesson lesson = mapDtoToLesson(lessonDto);
        save(lesson);
    }

    public List<Lesson> findAll(){
        List<Lesson> lessons = lessonRepository.findAll();
        lessons.removeIf(p -> (p.isLessonInactive()));
        return lessons;
    }

    public Lesson findById(Integer lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ServiceException(
                        String.format("Lesson with such id %d does not exist", lessonId)));
    }

    public LessonDto findDtoById(Integer lessonId) {
        return mapLessonToDto(findById(lessonId));
    }

    public void deactivate(Integer lessonId) {
        Lesson lesson = findById(lessonId);
        lesson.setRoom(null);
        lesson.setTeacher(null);
        lesson.setGroup(null);
        lesson.setLessonInactive(true);
        lessonRepository.save(lesson);
    }

    public void activate(Integer lessonId) {
        Lesson lesson = findById(lessonId);
        lesson.setLessonInactive(false);
        lessonRepository.save(lesson);
    }

    public List<Lesson> getLessonByTeacherIdForDay(int teacherId, LocalDate date) {
        return lessonRepository.getLessonByTeacherIdForDay(
                teacherId,
                date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth());
    }

    public List<Lesson> getLessonByTeacherIdForMonth(int teacherId, LocalDate date) {
        return lessonRepository.getLessonByTeacherIdForMonth(
                teacherId,
                date.getYear(),
                date.getMonthValue());
    }

    public List<Lesson> getLessonByStudentIdForDay(int studentId, LocalDate date) {
        Group group = studentRepository.findById(studentId)
                .orElseThrow(() -> new ServiceException(
                        String.format("Student with such id %d does not exist", studentId))).getGroup();
        return lessonRepository.getLessonByGroupIdForDay(
                group,
                date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth());
    }

    public List<Lesson> getLessonByStudentIdForMonth(int studentId, LocalDate date) {
        Group group = studentRepository.findById(studentId)
                .orElseThrow(() -> new ServiceException(
                        String.format("Student with such id %d does not exist", studentId))).getGroup();
        return lessonRepository.getLessonByGroupIdForMonth(
                group,
                date.getYear(),
                date.getMonthValue());
    }

    public List<Lesson> getLessonsByGroupId(Integer groupId) {
        return lessonRepository.findByGroupGroupId(groupId);
    }

    public List<Lesson> getLessonsByTeacherId(Integer teacherId) {
        return lessonRepository.findByTeacherTeacherId(teacherId);
    }

    public List<Lesson> getLessonsByRoomId(Integer roomId) {
        return lessonRepository.findByRoomRoomId(roomId);
    }

    private Lesson mapDtoToLesson(LessonDto lessonDto){
        Lesson lesson = new Lesson();
        lesson.setLessonId(lessonDto.getLessonId());
        lesson.setLessonName(lessonDto.getLessonName());

        Group group = lessonDto.getGroupId() == null ? null : groupRepository.findById(lessonDto.getGroupId())
                .orElseThrow(() -> new ServiceException(
                    String.format("Group with such id %d does not exist", lessonDto.getGroupId())));
        lesson.setGroup(group);

        Teacher teacher = lessonDto.getTeacherId() == null ? null : teacherRepository.findById(lessonDto.getTeacherId())
                .orElseThrow(() -> new ServiceException(
                    String.format("Teacher with such id %d does not exist", lessonDto.getTeacherId())));
        lesson.setTeacher(teacher);

        Room room = lessonDto.getRoomId() == null ? null : roomRepository.findById(lessonDto.getRoomId())
                .orElseThrow(() -> new ServiceException(
                    String.format("Room with such id %d does not exist", lessonDto.getRoomId())));
        lesson.setRoom(room);

        lesson.setLessonInactive(lessonDto.isLessonInactive());
        lesson.setDate(LocalDate.parse(lessonDto.getDate(), FORMATTER));
        lesson.setLessonNumber(lessonDto.getLessonNumber());
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
        dto.setLessonNumber(lesson.getLessonNumber());
        return dto;
    }

    private void validate(Lesson lesson){
        int roomId = lesson.getRoom() == null ? 0 : lesson.getRoom().getRoomId();
        int groupId = lesson.getGroup() == null ? 0 : lesson.getGroup().getGroupId();
        int teacherId = lesson.getTeacher() == null ? 0 : lesson.getTeacher().getTeacherId();
        int number = lesson.getLessonNumber();
        LocalDate date = lesson.getDate();

        if (!lessonRepository.findByGroupGroupIdAndDateAndLessonNumberAndLessonInactiveFalse(groupId, date, number).isEmpty()){
            throw new ValidationException("The group has already been busy in another lesson. Please choose another day or lesson number.");
        }
        if (!lessonRepository.findByRoomRoomIdAndDateAndLessonNumberAndLessonInactiveFalse(roomId, date, number).isEmpty()){
            throw new ValidationException("The room has already been busy in another lesson. Please choose another day or lesson number.");
        }
        if (!lessonRepository.findByTeacherTeacherIdAndDateAndLessonNumberAndLessonInactiveFalse(teacherId, date, number).isEmpty()){
            throw new ValidationException("The teacher has already been busy in another lesson. Please choose another day or lesson number.");
        }
    }
}