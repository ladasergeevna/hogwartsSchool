--Получить всех студентов, возраст которых находится между... и ...
select * from student s
where s.age between 20 and 30

--Получить всех студентов, но отобразить только список их имен
select s.name from student s

--Получить всех студентов, у которых в имени присутствует буква О (или любая другая)
select * from student s
where name like '%o%'

--Получить всех студентов, у которых возраст меньше идентификатора
select * from student s
where s.age < s.id

--Получить всех студентов упорядоченных по возрасту
select * from student s
order by s.age ASC