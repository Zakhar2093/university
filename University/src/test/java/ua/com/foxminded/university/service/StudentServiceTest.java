package ua.com.foxminded.university.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ua.com.foxminded.university.dao.DatabaseInitialization;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.model.Student;

class StudentServiceTest {

    private DatabaseInitialization dbInit = new DatabaseInitialization();

    private StudentService studentService;
    
    @Mock
    private StudentDao studentDao;


    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        studentService = new StudentService(studentDao);
        dbInit.initialization();
    }

    @Test
    void createShouldInvokeOnlyOnce() {
        studentService.create(new Student());
        verify(studentDao, only()).create(any(Student.class));
    }
    
    @Test
    void getAllShouldInvokeOnlyOnce() {
        studentService.getAll();
        verify(studentDao, only()).getAll();
    }
    
    @Test
    void getByIdShouldInvokeOnlyOnce() {
        studentService.getById(1);
        verify(studentDao, only()).getById(anyInt());
    }
    
    @Test
    void updateShouldInvokeOnlyOnce() {
        studentService.update(new Student());
        verify(studentDao, only()).update(any(Student.class));
    }
    
    @Test
    void deactivateShouldInvokeOnlyOnce() {
        studentService.deactivate(1);
        verify(studentDao, times(1)).removeStudentFromGroup(anyInt());
        verify(studentDao, times(1)).deactivate(anyInt());
    }
    
    @Test
    void activateShouldInvokeOnlyOnce() {
        studentService.activate(1);
        verify(studentDao, only()).activate(anyInt());
    }
    
    @Test
    void addStudentToGroupShouldInvokeOnlyOnce() {
        studentService.addStudentToGroup(1, 1);
        verify(studentDao, only()).addStudentToGroup(anyInt(), anyInt());
    }
    
    @Test
    void removeStudentFromGroupShouldInvokeOnlyOnce() {
        studentService.removeStudentFromGroup(1);
        verify(studentDao, only()).removeStudentFromGroup(anyInt());
    }
}


