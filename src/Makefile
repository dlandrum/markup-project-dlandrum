JCC = javac

JFLAGS = -classpath .:../vendor/*

default: Solution.class MarkupThread.class

Solution.class: Solution.java
	$(JCC) $(JFLAGS) Solution.java

MarkupThread.class: MarkupThread.java
	$(JCC) $(JFLAGS) MarkupThread.java

clean:
	rm *.class
