JFLAGS=-g
JC=javac

%.class: %.java
	$(JC) $(JFLAGS) $<

sources=$(wildcard *.java)
classes=$(sources:.java=.class)

all: $(classes)

clean:
	rm -f *.class
