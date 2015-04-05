PROCDEMO=main
ICSDKHOME=/home/opt/instantclient_12_1

CFLAGS=-I$(ICSDKHOME)/sdk/include
LDFLAGS=-L $(ICSDKHOME)
LDLIBS=-l clntshcore -l clntsh -l nnz12 -l ons -l mql1 -l ipc1

PROC=proc
GCC=gcc

.SUFFIXES: .pc .c .o 

all: $(PROCDEMO)

main.o : ../main.c ../utillib.h
	$(CC) $(CPPFLAGS) $(CFLAGS) -c ../main.c

$(PROCDEMO) : main.o utillib.o

.pc.c :
	$(PROC) $*
	