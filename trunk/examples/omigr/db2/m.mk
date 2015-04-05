PROCDEMO=main
DB2PATH=/home/db2inst1/sqllib

CFLAGS=-I$(DB2PATH)/include
LDFLAGS=-L $(DB2PATH)/lib
LDLIBS=-l db2

GCC=gcc

.SUFFIXES: .sqc .c .o 

all: $(PROCDEMO)

main.o : ../main.c ../utillib.h
	$(CC) $(CPPFLAGS) $(CFLAGS) -c ../main.c
	
$(PROCDEMO) : main.o utillib.o	

.sqc.c :
	./prep $*