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

    public void create(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    public void create(LessonDto lessonDto) {
        Lesson lesson = mapDtoToLesson(lessonDto);
        lessonRepository.save(lesson);
    }

    public List<Lesson> getAll(){
        return lessonRepository.findAll();
    }

    public List<Lesson> getAllActivated(){
        List<Lesson> lessons = lessonRepository.findAll();
        lessons.removeIf(p -> (p.isLessonInactive()));
        return lessons;
    }

    public Lesson getById(Integer lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ServiceException(
                        String.format("Lesson with such id %d does not exist", lessonId)));
    }

    public LessonDto getDtoById(Integer lessonId) {
        return mapLessonToDto(getById(lessonId));
    }

    public void update(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    public void update(LessonDto lessonDto) {
        Lesson lesson = mapDtoToLesson(lessonDto);
        lessonRepository.save(lesson);
    }

    public void deactivate(Integer lessonId) {
        Lesson lesson = getById(lessonId);
        lesson.setRoom(null);
        lesson.setTeacher(null);
        lesson.setGroup(null);
        lesson.setLessonInactive(true);
        lessonRepository.save(lesson);
    }

    public void activate(Integer lessonId) {
        Lesson lesson = getById(lessonId);
        lesson.setLessonInactive(false);
        lessonRepository.save(lesson);
    }

    //todo fix it
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

    private Lesson mapDtoToLesson(LessonDto dto){
        Lesson lesson = new Lesson();
        lesson.setLessonId(dto.getLessonId());
        lesson.setLessonName(dto.getLessonName());

        Group group = dto.getGroupId() == null ? null : groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new ServiceException(
                    String.format("Group with such id %d does not exist", dto.getGroupId())));
        lesson.setGroup(group);

        Teacher teacher = dto.getTeacherId() == null ? null : teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new ServiceException(
                    String.format("Teacher with such id %d does not exist", dto.getTeacherId())));
        lesson.setTeacher(teacher);

        Room room = dto.getRoomId() == null ? null : roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ServiceException(
                    String.format("Room with such id %d does not exist", dto.getRoomId())));
        lesson.setRoom(room);

        lesson.setLessonInactive(dto.isLessonInactive());
        lesson.setDate(LocalDate.parse(dto.getDate(), FORMATTER));
        lesson.setLessonNumber(dto.getLessonNumber());
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
}