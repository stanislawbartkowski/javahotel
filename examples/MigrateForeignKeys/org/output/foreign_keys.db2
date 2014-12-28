ALTER TABLE dbo.dept_emp ADD CONSTRAINT "FK_dept_emp_departments" FOREIGN KEY
   (dept_no)
   REFERENCES dbo.departments
   (dept_no)
@

ALTER TABLE dbo.dept_emp ADD CONSTRAINT "FK_dept_emp_employees" FOREIGN KEY
   (emp_no)
   REFERENCES dbo.employees
   (emp_no)
@

ALTER TABLE dbo.dept_manager ADD CONSTRAINT "FK_dept_manager_departments" FOREIGN KEY
   (dept_no)
   REFERENCES dbo.departments
   (dept_no)
@

ALTER TABLE dbo.dept_manager ADD CONSTRAINT "FK_dept_manager_employees" FOREIGN KEY
   (emp_no)
   REFERENCES dbo.employees
   (emp_no)
@

ALTER TABLE dbo.salaries ADD CONSTRAINT "FK_salaries_employees" FOREIGN KEY
   (emp_no)
   REFERENCES dbo.employees
   (emp_no)
@

ALTER TABLE dbo.titles ADD CONSTRAINT "FK_titles_employees" FOREIGN KEY
   (emp_no)
   REFERENCES dbo.employees
   (emp_no)
@

