-- liquibase formatted sql

-- changeset kls:1
CREATE INDEX student_name_idx ON student (name)
-- changeset kls:2
CREATE INDEX faculty_color_idx ON public.faculty (color,name)