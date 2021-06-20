package ua.com.foxminded.university.api.rest_controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.model_dto.StudentDto;
import ua.com.foxminded.university.service.StudentService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:testApplication.properties")
public class StudentRestControllerTest {

    private final String jsonStudent = new ObjectMapper().writeValueAsString(getTestStudentDto());
    private final String jsonListOfStudent = new ObjectMapper().writeValueAsString(getTestListOfStudentsDto());

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentRestController studentRestController;

    private MockMvc mockMvc;

    public StudentRestControllerTest() throws JsonProcessingException {
    }

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentRestController).build();
    }

    @Test
    void findByIdShouldReturnCorrectJson() throws Exception {
        when(studentService.findDtoById(anyInt())).thenReturn(getTestStudentDto());

        mockMvc.perform(get("/api/students/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonStudent));

        verify(studentService, only()).findDtoById(anyInt());
    }

    @Test
    void findAllShouldReturnCorrectJson() throws Exception {
        when(studentService.findAllDto()).thenReturn(getTestListOfStudentsDto());

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonListOfStudent));

        verify(studentService, only()).findAllDto();
    }

    @Test
    void saveShouldParseJsonIntoObjectAndInvokeService() throws Exception {
        mockMvc.perform(post("/api/students")
                .content(jsonStudent)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated());

        verify(studentService, only()).save(getTestStudentDto());
    }

    @Test
    void updateShouldParseJsonIntoObjectAndInvokeService() throws Exception {
        int expectedStudentId = 1;

        mockMvc.perform(put("/api/students/{id}", expectedStudentId)
                .content(jsonStudent)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());

        verify(studentService, only()).save(getTestStudentDto());
    }

    @Test
    void deleteShouldInvokeService() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", 1))
                .andExpect(status().isOk());

        verify(studentService, only()).deactivate(anyInt());
    }

    @Test
    void showStudentsDtoInGroupShouldReturnCorrectJson() throws Exception {
        when(studentService.getStudentsDtoByGroupId(anyInt())).thenReturn(getTestListOfStudentsDto());

        mockMvc.perform(get("/api/groups/{id}/students", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonListOfStudent));

        verify(studentService, only()).getStudentsDtoByGroupId(anyInt());
    }

    private List<StudentDto> getTestListOfStudentsDto() {
        List<StudentDto> students = new ArrayList<>();
        students.add(new StudentDto(1, "one", "one", 1, false));
        students.add(new StudentDto(2, "two", "two", 2, false));
        students.add(new StudentDto(3, "three", "three", 1, false));
        return students;
    }

    private StudentDto getTestStudentDto() {
        return new StudentDto(1, "one", "one", 1, false);
    }
}