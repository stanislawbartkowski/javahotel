LOAD FROM /tmp/data/dbo_customers.txt
OF DEL
MODIFIED BY LOBSINFILE  CODEPAGE=1208  COLDEL~ ANYORDER  USEDEFAULTS CHARDEL"" DELPRIORITYCHAR
DUMPFILE=/tmp/data/dump/dbo_customers.txt
MESSAGES /tmp/data/msg/dbo_customers.txt
REPLACE INTO dbo.Customers
;

LOAD FROM /tmp/data/dbo_employees.txt
OF DEL
MODIFIED BY LOBSINFILE  CODEPAGE=1208  COLDEL~ ANYORDER  USEDEFAULTS CHARDEL"" DELPRIORITYCHAR
DUMPFILE=/tmp/data/dump/dbo_employees.txt
MESSAGES /tmp/data/msg/dbo_employees.txt
REPLACE INTO dbo.Employees
;

LOAD FROM /tmp/data/dbo_products.txt
OF DEL
MODIFIED BY LOBSINFILE  CODEPAGE=1208  COLDEL~ ANYORDER  USEDEFAULTS CHARDEL"" DELPRIORITYCHAR
DUMPFILE=/tmp/data/dump/dbo_products.txt
MESSAGES /tmp/data/msg/dbo_products.txt
REPLACE INTO dbo.Products
;

LOAD FROM /tmp/data/dbo_sales.txt
OF DEL
MODIFIED BY LOBSINFILE  CODEPAGE=1208  COLDEL~ ANYORDER  USEDEFAULTS CHARDEL"" DELPRIORITYCHAR
DUMPFILE=/tmp/data/dump/dbo_sales.txt
MESSAGES /tmp/data/msg/dbo_sales.txt
REPLACE INTO dbo.Sales
;

