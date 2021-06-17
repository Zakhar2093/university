package ua.com.foxminded.university.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.api.controler.StudentController;
import ua.com.foxminded.university.model.model_dto.StudentDto;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@TestPropertySource(locations = "classpath:testApplication.properties")
public class StudentControllerTest {

    @Autowired
    private TestData testData;

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
        when(studentService.findAll()).thenReturn(testData.getTestStudent());
        when(groupService.findAll()).thenReturn(testData.getTestGroups());

        mockMvc.perform(get("/students/"))
                .andExpect(view().name("students/index"))
                .andExpect(model().attribute("groups", testData.getTestGroups()))
                .andExpect(model().attribute("students", testData.getTestStudent()));
    }

    @Test
    void createShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        when(groupService.findAll()).thenReturn(testData.getTestGroups());

        StudentDto studentDto = createDto();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/students/add").flashAttr("studentDto", studentDto);
        mockMvc.perform(request)
                .andExpect(view().name("students/add"))
                .andExpect(model().attribute("groups", testData.getTestGroups()));
    }

    @Test
    void submitCreateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        StudentDto studentDto = createDto();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/students/").flashAttr("studentDto", studentDto);
        mockMvc.perform(request)
                .andExpect(view().name("redirect:/students"));

        verify(studentService, only()).save(studentDto);
    }

    @Test
    void updateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        StudentDto studentDto = createDto();
        when(studentService.findDtoById(anyInt())).thenReturn(studentDto);
        when(groupService.findAll()).thenReturn(testData.getTestGroups());

        mockMvc.perform(get("/students/{id}/update", 2))
                .andExpect(view().name("students/update"))
                .andExpect(model().attribute("studentDto", studentDto))
                .andExpect(model().attribute("groups", testData.getTestGroups()));
    }

    @Test
    void submitUpdateShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        int expectedStudentDtoId = 2;
        StudentDto studentDto = createDto();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/students/{id}", expectedStudentDtoId).flashAttr("studentDto", studentDto);
        mockMvc.perform(request)
                .andExpect(view().name("redirect:/students"));

        verify(studentService, only()).save(studentDto);
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
    void showStudentsByGroupShouldReturnCorrectPageAndModelWithCorrectAttributes() throws Exception {
        int id = 1;
        when(studentService.getStudentsByGroupId(id)).thenReturn(testData.getTestStudent());
        when(groupService.findById(id)).thenReturn(testData.getTestGroups().get(0));

        mockMvc.perform(get("/groups/{id}/students", id))
                .andExpect(view().name("students/index"))
                .andExpect(model().attribute("students", testData.getTestStudent()))
                .andExpect(model().attribute("group", testData.getTestGroups().get(0)));
    }

    private StudentDto createDto(){
        return new StudentDto(1, "Jack", "Smith", 1,false);
    }
}