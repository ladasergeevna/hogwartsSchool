package ru.hogwarts.school.repository;

import ru.hogwarts.school.model.Student;

import java.util.List;

public interface GetTopFiveStudents {
    List <Student> topFiveStudents();
}
