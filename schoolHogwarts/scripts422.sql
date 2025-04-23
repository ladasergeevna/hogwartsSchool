-- Таблица автомобилей
CREATE TABLE public.cars (
	id_car int4 NOT NULL,
	brand varchar NULL,
	model varchar NULL,
	price money NULL,
	CONSTRAINT cars_unique UNIQUE (id_car)
);

-- Таблица владельцев
CREATE TABLE public.owners (
	id_owner int4 NOT NULL,
	first_name varchar NULL,
	last_name varchar NULL,
	age int4 NULL,
	b_license bool NULL,
	CONSTRAINT car_owners_unique UNIQUE (id_owner)
);

-- Связь многие-ко-многим между таблицами
CREATE TABLE public.cars_owners (
	id int4 NOT NULL,
	id_car int4 NULL,
	id_owner int4 NULL,
	CONSTRAINT cars_owners_unique UNIQUE (id),
	CONSTRAINT cars_owners_cars_fk FOREIGN KEY (id_car) REFERENCES public.cars(id_car) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT cars_owners_owners_fk FOREIGN KEY (id_owner) REFERENCES public.owners(id_owner) ON DELETE CASCADE
);