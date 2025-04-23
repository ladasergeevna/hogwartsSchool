ALTER TABLE public.student ADD CONSTRAINT student_check CHECK (age >=16);
ALTER TABLE public.student ALTER COLUMN "name" SET NOT NULL;
ALTER TABLE public.student ALTER COLUMN age SET DEFAULT 20;
ALTER TABLE public.faculty ADD CONSTRAINT faculty_unique UNIQUE (color,"name");