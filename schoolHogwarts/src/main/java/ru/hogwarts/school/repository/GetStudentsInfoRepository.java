package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.List;

@Repository
public interface GetStudentsInfoRepository extends JpaRepository<Student, Integer> {

    @Query(value = "SELECT COUNT(id) as number FROM student", nativeQuery = true)
    Integer getNumberAllStudents();

    @Query(value = "SELECT AVG(age) as averageAge FROM student", nativeQuery = true)
    Integer getAverageAgeOfStudents();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getTopFiveStudents();
}
