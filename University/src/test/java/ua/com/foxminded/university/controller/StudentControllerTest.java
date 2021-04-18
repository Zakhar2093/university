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
import ua.com.foxminded.university.model.model_dto.StudentDto;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @Mock
    private GroupService groupService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void getAllShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        when(studentService.getAllActivated()).thenReturn(getTestStudent());
        when(groupService.getAllActivated()).thenReturn(getTestGroups());

        mockMvc.perform(get("/students/"))
                .andExpect(view().name("students/index"))
                .andExpect(model().attribute("groups", getTestGroups()))
                .andExpect(model().attribute("students", getTestStudent()));
    }

    @Test
    void createShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        when(groupService.getAllActivated()).thenReturn(getTestGroups());

        StudentDto studentDto = new StudentDto();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/students/add").flashAttr("studentDto", studentDto);
        mockMvc.perform(request)
                .andExpect(view().name("students/add"))
                .andExpect(model().attribute("groups", getTestGroups()));
    }

    @Test
    void submitCreateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        StudentDto studentDto = new StudentDto();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/students/").flashAttr("studentDto", studentDto);
        mockMvc.perform(request)
                .andExpect(view().name("redirect:/students"));

        verify(studentService, only()).create(studentDto);
    }

    @Test
    void updateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        StudentDto studentDto = new StudentDto();
        when(studentService.getDtoById(anyInt())).thenReturn(studentDto);
        when(groupService.getAllActivated()).thenReturn(getTestGroups());

        mockMvc.perform(get("/students/{id}/update", 2))
                .andExpect(view().name("students/update"))
                .andExpect(model().attribute("studentDto", studentDto))
                .andExpect(model().attribute("groups", getTestGroups()));
    }

    @Test
    void submitUpdateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        int expectedStudentDtoId = 2;
        StudentDto studentDto = new StudentDto();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/students/{id}", expectedStudentDtoId).flashAttr("studentDto", studentDto);
        mockMvc.perform(request)
                .andExpect(view().name("redirect:/students"));

        verify(studentService, only()).update(studentDto);
        int actualStudentDtoId = studentDto.getStudentId();
        assertEquals(expectedStudentDtoId, actualStudentDtoId);
    }

    @Test
    void deleteShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        mockMvc.perform(delete("/students/{id}", 2))
                .andExpect(view().name("redirect:/students"));

        verify(studentService, only()).deactivate(anyInt());
    }

    @Test
    void showLessonsByGroupShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        int id = 1;
        when(studentService.getStudentsByGroupId(id)).thenReturn(getTestStudent());
        when(groupService.getById(id)).thenReturn(getTestGroups().get(0));

        mockMvc.perform(get("/students/byGroup/{id}", id))
                .andExpect(view().name("students/index"))
                .andExpect(model().attribute("students", getTestStudent()))
                .andExpect(model().attribute("group", getTestGroups().get(0)));
    }

    private List<Student> getTestStudent() {
        List<Group> groups = getTestGroups();
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "one", "one", groups.get(0), false));
        students.add(new Student(1, "two", "two", groups.get(2), false));
        students.add(new Student(1, "three", "three", groups.get(1), false));
        return students;
    }

    private List<Group> getTestGroups() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(1, "Java", false));
        groups.add(new Group(2, "C++", false));
        groups.add(new Group(3, "PHP", false));
        return groups;
    }
}