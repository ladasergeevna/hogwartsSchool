package ru.hogwarts.school.service;

import java.util.*;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

@Service
public class FacultyService {

   /* private final HashMap<Long, Faculty> faculties = new HashMap<>();
    private long count = 0;*/
   private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

  /*  public Faculty addFaculty(Faculty faculty) {
        faculty.setId(count++);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }*/
    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }
/*
    public Faculty findFaculty(long id) {
        return faculties.get(id);
    } //

    public Faculty editFaculty(Faculty faculty) {
        if (!faculties.containsKey(faculty.getId())) {
            return null;
        }
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty deleteFaculty(long id) {
        return faculties.remove(id);
    }//

    public Collection<Faculty> findByColor(String color) {
        ArrayList<Faculty> result = new ArrayList<>();
        for (Faculty faculty : faculties.values()) {
            if (Objects.equals(faculty.getColor(), color)) {
                result.add(faculty);
            }
        }
        return result;
    }
    public List<Faculty> getAllFaculties() {
        return new ArrayList<>(faculties.values());
    }*/

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
}
