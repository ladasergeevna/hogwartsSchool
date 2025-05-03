--Информация обо всех студентах вместе с названиями факультетов
SELECT
	s.name,
	s.age,
	f.name
FROM student s
INNER JOIN faculty f
	ON f.id = s.faculty_id

--  Студенты, у которых есть аватарки
SELECT
	s.name,
	s.age
FROM student s
INNER JOIN avatar a
	ON a.student_id = s.id

SELECT
	s.name,
	s.age
FROM student s
LEFT JOIN avatar a
	ON a.student_id = s.id
WHERE
	a.id IS NOT NULL