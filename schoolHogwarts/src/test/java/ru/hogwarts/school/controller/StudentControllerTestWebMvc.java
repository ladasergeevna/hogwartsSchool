package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(StudentController.class)
public class StudentControllerTestWebMvc {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private StudentService studentService;
    @MockBean
    private AvatarService avatarService;
    @MockBean
    private FacultyController facultyController;
    @InjectMocks
    private StudentController studentController;

    private ObjectMapper objectMapper;

    // Получение списка всех студентов
    @Test
    public void getAllStudents() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student("TestStudent", 25));

        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("TestStudent"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(25));
    }

    // Получение студента по id
    @Test
    public void getStudentInfo_ExistingId_ReturnsStudent() throws Exception {
        Long studentId = 1L;
        Student student = new Student("TestStudent", 25);
        student.setId(studentId);

        Mockito.when(studentService.findStudent(studentId)).thenReturn(student);

        mockMvc.perform(get("/student/find/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(equalTo("TestStudent")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(25));
    }

    // Создание записи о студенте
    @Test
    public void createStudent() throws Exception {
        Student student = new Student("TestStudent", 25);

        when(studentService.addStudent(any(Student.class))).thenReturn(student);

        ObjectMapper objectMapper = new ObjectMapper();
        String studentJson = objectMapper.writeValueAsString(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestStudent"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(25));
    }

    // Удаление записи о студенте по id
    @Test
    public void deleteStudent() throws Exception {
        Long studentId = 1L;
        doNothing().when(studentService).deleteStudent(studentId);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/remove/{id}", studentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // Получение записи о студенте по возрасту
    @Test
    public void findStudentsByAge() throws Exception {
        int age = 25;
        Student testStudent = new Student("testStudent", age);
        Collection<Student> students = Arrays.asList(testStudent);
        when(studentService.findByAge(age)).thenReturn((List<Student>) students);

        mockMvc.perform(get("/student/findByAge")
                        .param("age", String.valueOf(age))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("testStudent"));
    }

    @Test
    public void getStudentsByAgeRange() throws Exception {
        int minAge = 18;
        int maxAge = 25;
        Student student1 = new Student("Student1", 20);
        Student student2 = new Student("Student2", 22);
        List<Student> students = Arrays.asList(student1, student2);

        when(studentService.findAllByAgeBetween(minAge, maxAge)).thenReturn(students);

        mockMvc.perform(get("/student/findByAgeBetween")
                        .param("min", String.valueOf(minAge))
                        .param("max", String.valueOf(maxAge))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Student1"))
                .andExpect(jsonPath("$[1].name").value("Student2"));
    }

    @Test
    public void getFacultyByStudentId() throws Exception {
        Long studentId = 1L;

        Faculty faculty = new Faculty();
        faculty.setName("Engineering");

        when(studentService.getFacultyByStudentId(studentId)).thenReturn(faculty);

        mockMvc.perform(get("/student/{studentId}/faculty", studentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Engineering"));
    }
}

