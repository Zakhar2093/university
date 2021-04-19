package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.model_dto.StudentDto;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;
import ua.com.foxminded.university.service.TeacherService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    void getAllShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        when(teacherService.getAllActivated()).thenReturn(getTestTeachers());

        mockMvc.perform(get("/teachers/"))
                .andExpect(view().name("teachers/index"))
                .andExpect(model().attribute("teachers", getTestTeachers()));
    }

    @Test
    void createShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        Teacher teacher = new Teacher();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/teachers/").flashAttr("teacher", teacher);
        mockMvc.perform(request)
                .andExpect(view().name("redirect:/teachers"));

        verify(teacherService, only()).create(teacher);
    }

    @Test
    void updateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        Teacher teacher = new Teacher();
        when(teacherService.getById(anyInt())).thenReturn(teacher);

        mockMvc.perform(get("/teachers/{id}/edit", 2))
                .andExpect(view().name("teachers/update"))
                .andExpect(model().attribute("teacher", teacher));
    }

    @Test
    void submitUpdateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        int expectedTeacherId = 2;
        Teacher teacher = new Teacher();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/teachers/{id}", expectedTeacherId).flashAttr("teacher", teacher);
        mockMvc.perform(request)
                .andExpect(view().name("redirect:/teachers"));

        verify(teacherService, only()).update(teacher);
        int actualTeacherId = teacher.getTeacherId();
        assertEquals(expectedTeacherId, actualTeacherId);
    }

    @Test
    void deleteShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        mockMvc.perform(delete("/teachers/{id}", 2))
                .andExpect(view().name("redirect:/teachers"));

        verify(teacherService, only()).deactivate(anyInt());
    }


    private List<Teacher> getTestTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher(1, "one", "one", false));
        teachers.add(new Teacher(2, "two", "two", false));
        teachers.add(new Teacher(3, "Three", "Three", false));
        return teachers;
    }
}