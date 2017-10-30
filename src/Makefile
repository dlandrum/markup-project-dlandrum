# This file written by Don Landrum on October 29th, 2017

JCC = javac

JFLAGS = -classpath .:../vendor/*

default: Solution.class MarkupThread.class Interaction.class

Solution.class: Solution.java
	$(JCC) $(JFLAGS) Solution.java

MarkupThread.class: MarkupThread.java
	$(JCC) $(JFLAGS) MarkupThread.java

Interaction.class: Interaction.java
	$(JCC) $(JFLAGS) Interaction.java

clean:
	rm *.class
