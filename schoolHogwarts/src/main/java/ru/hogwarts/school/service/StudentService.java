package ru.hogwarts.school.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.GetStudentsInfoRepository;
import ru.hogwarts.school.repository.StudentRepository;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final GetStudentsInfoRepository getStudentsInfoRepository;
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);


    public StudentService(StudentRepository studentRepository, GetStudentsInfoRepository getStudentsInfoRepository) {
        this.studentRepository = studentRepository;
        this.getStudentsInfoRepository = getStudentsInfoRepository;
    }

    public Student addStudent(Student student) {
        logger.info("Was invoked method addStudent");
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        logger.info("Was invoked method getAllStudents");
        return studentRepository.findAll();
    }

    public Student findStudent(Long id) {
        logger.info("Was invoked method findStudent");
        return studentRepository.findById(id).get();
    }

    public void deleteStudent(Long id) {
        logger.info("Was invoked method deleteStudent");
        studentRepository.deleteById(id);
    }

    public List<Student> findByAge(Integer age) {
        logger.info("Was invoked method findByAge");
        return studentRepository.findByAge(age);
    }
    public List<Student> findAllByAgeBetween(Integer ageMin, Integer ageMax){
        logger.info("Was invoked method findAllByAgeBetween");
        return studentRepository.findAllByAgeBetween(ageMin,ageMax);
    }
    public Faculty getFacultyByStudentId(Long studentId) {
        logger.info("Was invoked method getFacultyByStudentId");
        return studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("There is not faculty for student with id = {}", studentId);
                    return new RuntimeException("Student not found");
                })
                .getFaculty();
    }

    public Integer getNumberAllStudents () {
        logger.debug("Was invoked method getNumberAllStudents");
        return getStudentsInfoRepository.getNumberAllStudents();}
    public Integer getAverageAgeOfStudents () {
        logger.debug("Was invoked method getAverageAgeOfStudents");
        return getStudentsInfoRepository.getAverageAgeOfStudents();}
    public List<Student> getTopFiveStudents () {
        logger.debug("Was invoked method getTopFiveStudents");
        return  getStudentsInfoRepository.getTopFiveStudents();}

}