JR=jar
JRFLAGS=cfe

JC=javac

PACKAGE=prj1

JA=java
JAFLAGS=-jar

TARGET=Project1.jar

CLASS1=$(PACKAGE)/Project1.class
CLASS2=$(PACKAGE)/Node.class
CLASS3=$(PACKAGE)/SocketThread.class

CA=confA.txt
CB=confB.txt
CC=confC.txt

all: compile jar
	@echo
	@echo use make run_ appended with 1-6 to see different outputs or use:
	@echo java -jar Project1.jar first_configuration second_configuration third_configuration
	@echo
	@echo eg: java -jar Project1.jar confA.txt confB.txt confC.txt
	@echo

compile:
	javac $(PACKAGE)/SocketThread.java
	javac $(PACKAGE)/Node.java
	javac $(PACKAGE)/Project1.java

jar:
	$(JR) $(JRFLAGS) $(TARGET) $(PACKAGE).Project1 $(PACKAGE)/Project1.class $(PACKAGE)/Node.class $(PACKAGE)/SocketThread.class

run_all: run_1 run_2 run_3 run_4 run_5 run_6

run_1:
	@echo Starting nodes in order A B C
	$(JA) $(JAFLAGS) $(TARGET) $(CA) $(CB) $(CC)

run_2:
	@echo Starting nodes in order A C B
	$(JA) $(JAFLAGS) $(TARGET) $(CA) $(CC) $(CB)

run_3:
	@echo Starting nodes in order B A C
	$(JA) $(JAFLAGS) $(TARGET) $(CB) $(CA) $(CC)

run_4:
	@echo Starting nodes in order B C A
	$(JA) $(JAFLAGS) $(TARGET) $(CB) $(CC) $(CA)

run_5:
	@echo Starting nodes in order C A B
	$(JA) $(JAFLAGS) $(TARGET) $(CC) $(CA) $(CB)

run_6:
	@echo Starting nodes in order C B A
	$(JA) $(JAFLAGS) $(TARGET) $(CC) $(CB) $(CA)

clean:
	rm $(TARGET) $(CLASS1) $(CLASS2) $(CLASS3)