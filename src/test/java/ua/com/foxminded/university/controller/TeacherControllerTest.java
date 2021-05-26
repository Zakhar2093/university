package ua.com.foxminded.university.controller;

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
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@TestPropertySource(locations = "classpath:testApplication.properties")
public class TeacherControllerTest {

    @Autowired
    private TestData testData;

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
        when(teacherService.getAllActivated()).thenReturn(testData.getTestTeachers());

        mockMvc.perform(get("/teachers/"))
                .andExpect(view().name("teachers/index"))
                .andExpect(model().attribute("teachers", testData.getTestTeachers()));
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
}