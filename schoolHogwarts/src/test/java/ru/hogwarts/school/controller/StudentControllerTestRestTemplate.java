package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Использовать тестовую базу
public class StudentControllerTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;

    private Student testStudent;
    private Faculty testFaculty;

    @BeforeEach
    public void setUp() {
        // тестовый факультет
        testFaculty = new Faculty("Test Faculty", "Test color");
        testFaculty = facultyRepository.save(testFaculty);

        // тестовый студент
        testStudent = new Student("Test Student", 20);
        testStudent.setFaculty(testFaculty);
        testStudent = studentRepository.save(testStudent);
    }

   // Проверка, что все бины были проинициализированы
    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    // Получение списка всех студентов
    @Test
    public void testGetAllStudents() {
        ResponseEntity<Collection> response = restTemplate.getForEntity("/faculty", Collection.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotEmpty();
    }

    // Получение студента по id
    @Test
    public void testGetStudentInfo() {
        ResponseEntity<Student> response = restTemplate.getForEntity("/student/find/" + testStudent.getId(), Student.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Test Student");
        assertThat(response.getBody().getAge()).isEqualTo(20);
    }

    // Создание записи о студенте
    @Test
    public void testCreateStudent() {
        Student newStudent = new Student("New Student", 22);
        ResponseEntity<Student> response = restTemplate.postForEntity("/student", newStudent, Student.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("New Student");
        assertThat(response.getBody().getAge()).isEqualTo(22);

        Long newStudentId = response.getBody().getId();
        assertThat(studentRepository.findById(newStudentId)).isPresent();
    }

    // Удаление записи о студенте по id
    @Test
    public void testDeleteStudent() {
        Long studentId = testStudent.getId();
        restTemplate.delete("/student/remove/" + studentId);
        assertThat(studentRepository.findById(studentId)).isNotPresent();
    }

    // Получение записи о студенте по возрасту
    @Test
    public void testFindStudentsByAge() {
        ResponseEntity<Collection> response = restTemplate.getForEntity("/student/findByAge?age=20", Collection.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotEmpty();
    }

    // Получение студентов по диапазону возрастов
    @Test
    public void testGetStudentsByAgeRange() {
        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "/student/findByAgeBetween?min=18&max=25",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        List<Student> studentsInRange = response.getBody();
        assertThat(studentsInRange).isNotEmpty();
        boolean allWithinRange = studentsInRange.stream()
                .allMatch(student -> student.getAge() >= 18 && student.getAge() <= 25);
        assertThat(allWithinRange).isTrue();
    }

    // Получение факультета по ID студента
    @Test
    public void testGetFacultyByStudentId() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/student/" + testStudent.getId() + "/faculty", Faculty.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

}