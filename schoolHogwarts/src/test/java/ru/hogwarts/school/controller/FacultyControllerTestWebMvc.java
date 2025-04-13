package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTestWebMvc {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultyRepository facultyRepository;
    @MockBean
    private FacultyService facultyService;
    @MockBean
    private StudentController studentController;
    @InjectMocks
    private FacultyController facultyController;
    private ObjectMapper objectMapper;

    // Получение списка всех факультетов
    @Test
    public void getAllFaculties() throws Exception {
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty("TestFaculty", "TestColor"));

        when(facultyService.getAllFaculties()).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("TestFaculty"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].color").value("TestColor"));
    }

    // Получение факультета по id
    @Test
    public void getFacultyInfo() throws Exception {
        Long facultyId = 1L;
        Faculty faculty = new Faculty("TestFaculty", "TestColor");
        faculty.setId(facultyId);

        Mockito.when(facultyService.findFaculty(facultyId)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/find/{id}", facultyId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(equalTo("TestFaculty")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(equalTo("TestColor")));
    }

    // Создание записи о факультете
    @Test
    public void createFaculty() throws Exception {
        Long facultyId = 1L;
        Faculty faculty = new Faculty("TestFaculty", "TestColor");
        faculty.setId(facultyId);

        when(facultyService.addFaculty(any(Faculty.class))).thenReturn(faculty);

        ObjectMapper objectMapper = new ObjectMapper();
        String studentJson = objectMapper.writeValueAsString(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(equalTo("TestFaculty")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(equalTo("TestColor")));
    }

    // Удаление записи о факультете по id
    @Test
    public void deleteFaculty() throws Exception {
        Long facultyId = 1L;
        doNothing().when(facultyService).deleteFaculty(facultyId);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/remove/{id}", facultyId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findFacultiesByColor() throws Exception {
        String color = "Red";
        Faculty faculty = new Faculty("TestFaculty", color);
        when(facultyService.findByColor(color)).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty/findByColor")
                        .param("color", color)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("TestFaculty")) // Используйте индекс [0] для доступа к первому элементу массива
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].color").value(color)); // Исправлено на color

    }

    @Test
    public void findFacultiesByColorOrName() throws Exception {

        String color = "Red";
        String name = "TestFaculty";
        Faculty faculty = new Faculty(name, color);
        when(facultyService.findByColorIgnoreCaseOrNameIgnoreCase(color, name))
                .thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty/findByColorOrName")
                        .param("color", color)
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("TestFaculty")) // Используйте индекс [0] для доступа к первому элементу массива
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].color").value(color)); // Исправлено на color

    }

    @Test
    public void getStudentsByFacultyId() throws Exception {
        Long facultyId = 1L;
        Student student = new Student();
        student.setName("TestStudent");

        when(facultyService.getStudentsByFacultyId(facultyId)).thenReturn(Collections.singletonList(student));

        mockMvc.perform(get("/faculty/{facultyId}/students", facultyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("TestStudent")); // Используйте индекс [0] для доступа к первому элементу массива
    }
}

