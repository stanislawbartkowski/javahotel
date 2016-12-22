# Simple solution to ease Oracle to DB2 SQL objects migration

The solution prepares migration of SQL objects: like tables, packages, stored procedures etc. from Oracle to DB2. The solution is available as Java application callable from command line or can imported as Eclipse project to evaluate or adjust to a particular needs.

Functionality implemented.

* Statistics, list of objetct. Browse through the Oracle source code and enumerate the number of particular SQL objects. The output can be imported to Excel or Libre Office and for migration progress monitoring.

* Extract objects and performs some simple transformation. Every object is stored as a single file and all objects of the same type, like UDF, packages, triggers are saved in a separate directory.

* Comparison. When migration is finished compares the list of objects from Oracle source code with the DB2 deployed objects. Allows doublecheck that nothing  has been missed during migration.

# Eclipse project




