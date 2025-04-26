package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Использовать тестовую базу
public class FacultyControllerTestRestTemplate {
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

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
        Assertions.assertThat(facultyController).isNotNull();
    }

    // Получение списка всех факультетов
    @Test
    public void getAllFaculties() {
        ResponseEntity<Collection> response = restTemplate.getForEntity("/faculty", Collection.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotEmpty();
    }

    // Получение факультета по id
    @Test
    public void testGetFacultyInfo() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/find/" + testFaculty.getId(), Faculty.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Test Faculty");
        assertThat(response.getBody().getColor()).isEqualTo("Test color");
    }

    // Создание записи о факультете
    @Test
    public void testCreateFaculty() {
        Faculty newFaculty = new Faculty("New Faculty", "RGB");
        ResponseEntity<Faculty> response = restTemplate.postForEntity("/faculty", newFaculty, Faculty.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("New Faculty");
        assertThat(response.getBody().getColor()).isEqualTo("RGB");

        Long newFacultyId = response.getBody().getId();
        assertThat(facultyRepository.findById(newFacultyId)).isPresent();
    }

    // Удаление записи о факультете по id
    @Test
    public void testDeleteFaculty() {
        Long facultyId = testFaculty.getId();
        studentRepository.delete(testStudent);
        restTemplate.delete("/faculty/remove/" + facultyId);
        assertThat(facultyRepository.findById(facultyId)).isNotPresent();
    }

    // Получение записи о факультете по цвету
    @Test
    public void testFindFacultiesByColor() {
        Faculty anotherFaculty = new Faculty("Grey Faculty", "Grey");
        facultyRepository.save(anotherFaculty);

        ResponseEntity<Collection> response = restTemplate.getForEntity("/faculty/findByColor?color=Test color", Collection.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(((Collection<?>) response.getBody()).size()).isGreaterThan(0);
    }

    // Получение записи о факультете по цвету или имени
    @Test
    public void testFindFacultiesByColorOrName() {
        Faculty anotherFaculty = new Faculty("Black Faculty", "Black");
        facultyRepository.save(anotherFaculty);

        ResponseEntity<Collection> response = restTemplate.getForEntity("/faculty/findByColorOrName?color=Green&name=Test Faculty", Collection.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotEmpty();
    }

    // Получение студентов по ID факультета
    @Test
    public void testGetStudentsByFacultyId() {
        ResponseEntity<List> response = restTemplate.getForEntity("/faculty/" + testFaculty.getId() + "/students", List.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(((List<?>) response.getBody()).size()).isGreaterThan(0);
    }
}
