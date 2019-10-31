ALTER TABLE employee ADD dept_id bigint AFTER email;

update employee
set dept_id = 1;

ALTER TABLE employee ALTER COLUMN dept_id SET NOT NULL;
ALTER TABLE employee ADD FOREIGN KEY (dept_id) REFERENCES department(id);