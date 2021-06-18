package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.model_dto.StudentDto;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class StudentService implements GenericService<Student, Integer>{
    
    private StudentRepository studentRepository;
    private GroupRepository groupRepository;
    
    @Autowired
    public StudentService(StudentRepository studentRepository, GroupRepository groupRepository) {
        super();
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }
    
    public void save(Student student) {
        studentRepository.save(student);
    }

    public void save(StudentDto studentDto) {
        Student student = mapDtoToStudent(studentDto);
        save(student);
    }

    public List<Student> findAll(){
        List<Student> students = studentRepository.findAll();
        students.removeIf(p -> (p.isStudentInactive()));
        return students;
    }

    public List<StudentDto> findAllDto(){
        return mapListOfStudentsToListOfStudentsDto(findAll());
    }

    public Student findById(Integer studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ServiceException(
                        String.format("Student with such id %d does not exist", studentId)));
    }

    public StudentDto findDtoById(Integer studentId) {
        return mapStudentToDto(findById(studentId));
    }

    public void deactivate(Integer studentId) {
        Student student = findById(studentId);
        student.setGroup(null);
        student.setStudentInactive(true);
        studentRepository.save(student);
    }

    public void activate(Integer studentId) {
        Student student = findById(studentId);
        student.setStudentInactive(false);
        studentRepository.save(student);
    }

    public List<Student> getStudentsByGroupId(Integer groupId) {
        return studentRepository.findByGroupGroupId(groupId);
    }

    public List<StudentDto> getStudentsDtoByGroupId(Integer groupId) {
        List<Student> students = studentRepository.findByGroupGroupId(groupId);
        return mapListOfStudentsToListOfStudentsDto(students);
    }

    private Student mapDtoToStudent(StudentDto dto){
        Student student = new Student();
        student.setStudentId(dto.getStudentId());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        Group group = dto.getGroupId() == null ? null : groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new ServiceException(
                    String.format("Group with such id %d does not exist", dto.getGroupId())));
        student.setGroup(group);
        student.setStudentInactive(dto.isStudentInactive());
        return student;
    }

    private StudentDto mapStudentToDto(Student student){
        StudentDto dto = new StudentDto();
        dto.setStudentId(student.getStudentId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setStudentInactive(student.isStudentInactive());
        if (student.getGroup() == null) {
            dto.setGroupId(null);
        } else {
            dto.setGroupId(student.getGroup().getGroupId());
        }
        return dto;
    }

    private List<StudentDto> mapListOfStudentsToListOfStudentsDto(List<Student> students){
        List<StudentDto> studentsDto = new ArrayList<>();
        for (Student student: students) {
            studentsDto.add(mapStudentToDto(student));
        }
        return studentsDto;
    }
}
