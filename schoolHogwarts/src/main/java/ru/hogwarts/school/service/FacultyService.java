package ru.hogwarts.school.service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

@Service
public class FacultyService {
    @Autowired
   private final FacultyRepository facultyRepository;
    @Autowired
    private StudentRepository studentRepository;
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    public Faculty addFaculty(Faculty faculty) {
        List<Faculty> existingFaculties = facultyRepository.findByColor(faculty.getColor());
        if (!existingFaculties.isEmpty()) {
            logger.warn("A faculty with the color '{}' already exists. Faculty: {}", faculty.getColor(), existingFaculties);
        }
        logger.info("Was invoked method addFaculty");
        return facultyRepository.save(faculty);
    }
    public List<Faculty> getAllFaculties() {
        logger.info("Was invoked method addFaculty");
        return facultyRepository.findAll();
    }
    public Faculty findFaculty(Long id) {
        logger.info("Was invoked method addFaculty");
        return facultyRepository.findById(id).get();
    }
    public void deleteFaculty(Long id) {
        if (facultyRepository.existsById(id)) {
            logger.info("Deleting faculty with id: {}", id);
            facultyRepository.deleteById(id);
        } else {
            logger.warn("Attempted to delete non-existing faculty with id: {}", id);
        }
    }
    public List<Faculty> findByColor(String name) {
        logger.debug("Was invoked method findByColor");
        return facultyRepository.findByColor(name);
    }
    public List<Faculty> findByColorIgnoreCaseOrNameIgnoreCase (String color,String name){
        logger.debug("Was invoked method findByColorIgnoreCaseOrNameIgnoreCase");
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(color,name);
    }
  public List<Student> getStudentsByFacultyId(Long facultyId) {
      logger.debug("Was invoked method getStudentsByFacultyId");
        return studentRepository.findByFacultyId(facultyId);
  }

}
