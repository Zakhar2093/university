package ua.com.foxminded.university.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.api.controler.HelloPageController;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.StudentService;
import ua.com.foxminded.university.service.TeacherService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@TestPropertySource(locations = "classpath:testApplication.properties")
public class HelloPageControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private HelloPageController helloPageController;

    private MockMvc mockMvc;


    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(helloPageController).build();
    }

    @Test
    void getAllShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        when(studentService.findAll()).thenReturn(getTestStudent());
        when(teacherService.findAll()).thenReturn(getTestTeachers());

        mockMvc.perform(get("/"))
                .andExpect(view().name("homePage"))
                .andExpect(model().attribute("students", getTestStudent()))
                .andExpect(model().attribute("teachers", getTestTeachers()));
    }

    private List<Teacher> getTestTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher(1, "one", "one", false));
        teachers.add(new Teacher(2, "two", "two", false));
        teachers.add(new Teacher(3, "Three", "Three", false));
        return teachers;
    }

    private List<Student> getTestStudent() {
        Group group = new Group(1, "Java", false);
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "one", "one", group, false));
        students.add(new Student(1, "two", "two", group, false));
        students.add(new Student(1, "three", "three", group, false));
        return students;
    }
}
