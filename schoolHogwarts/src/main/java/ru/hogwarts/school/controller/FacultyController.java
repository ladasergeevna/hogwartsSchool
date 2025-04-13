package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    // Получение списка всех факультетов
    @GetMapping
    public Collection<Faculty> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    // Получение факультета по id
    @GetMapping("/find/{id}")
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable Long id) {
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    // Создание записи о факультете
    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    // Удаление записи о факультете по id
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    // Получение записи о студенте по цвету
    @GetMapping("/findByColor")
    public ResponseEntity<Collection<Faculty>> findFaculties(@RequestParam(required = false) String color) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.findByColor(color));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
    @GetMapping("/findByColorOrName")
        public ResponseEntity<Collection<Faculty>> findFacultiesByColorOrName(@RequestParam(required = false) String color,
                                           @RequestParam(required = false) String name)
    {
        if ((color != null && !color.isBlank()) || (name != null && !name.isBlank())) {
            return ResponseEntity.ok(facultyService.findByColorIgnoreCaseOrNameIgnoreCase(color,name));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
   @GetMapping("/{facultyId}/students")
   public List<Student> getStudentsByFacultyId(@PathVariable Long facultyId) {
       return facultyService.getStudentsByFacultyId(facultyId);
   }


}
