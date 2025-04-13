package ru.hogwarts.school.service;

import java.util.*;

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

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }
    public Faculty findFaculty(Long id) {
        return facultyRepository.findById(id).get();
    }
    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }
    public List<Faculty> findByColor(String name) {
        return facultyRepository.findByColor(name);
    }
    public List<Faculty> findByColorIgnoreCaseOrNameIgnoreCase (String color,String name){
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(color,name);
    }
  public List<Student> getStudentsByFacultyId(Long facultyId) {
      return studentRepository.findByFacultyId(facultyId);
  }

}
