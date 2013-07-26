#JAVAPATH=/research/java/jdk1.5.0_05/bin/
#CLASSPATH=.:/research/java/j2sdk1.4.2/lib/classes.zip
JOPTIONS=-g -cp .\:junit-3.8.1.jar
USERHOME=$(HOME)

.SUFFIXES:
.SUFFIXES: .class .java
.java.class: $(JAVAPATH)javac $(JOPTIONS) $*.java

#
# As you define new classes, add them to the following list.
# It may not be absolutely necessary, but it will help guarantee that
# necessary recompilation gets done.
#
TOPPACKAGE=.
CLASSES:=$(patsubst %.java,%.class,$(wildcard SpreadSheetJ/Model/*.java))

TARGET=TestExpressions
TARGETGUI=TestExpressions
ASSIGNMENT=sstimej

%.class: %.java
	$(JAVAPATH)javac  $(JOPTIONS) $*.java


all: TestExpressions.class $(ASSIGNMENT).jar


run: $(ASSIGNMENT).jar
	$(JAVAPATH)java  -cp $(ASSIGNMENT).jar TestExpressions

test: $(ASSIGNMENT).jar
	$(JAVAPATH)java  -cp $(ASSIGNMENT).jar:junit-3.8.1.jar TestExpressionEval

testgui: $(ASSIGNMENT).jar
	$(JAVAPATH)java  -cp .:$(ASSIGNMENT).jar:junit-3.8.1.jar junit.swingui.TestRunner TestExpressionEval



runsample:
	$(JAVAPATH)java  -cp /home/zeil/cs330/Assignments/sstimej/$(ASSIGNMENT).jar TestExpressions

jar: $(ASSIGNMENT).jar


$(ASSIGNMENT).jar: $(CLASSES) TestExpressions.class TestExpressionEval.class
	$(JAVAPATH)jar cf $(ASSIGNMENT).jar SpreadSheetJ/Model/*.class *.class


retroguard: $(ASSIGNMENT).jar retroguard.dat
	mv $(ASSIGNMENT).jar temp.jar
	$(JAVAPATH)java -cp .:junit-3.8.1.jar:/home/zeil/src/retroguard-v2.2.0/retroguard.jar RetroGuard temp.jar $(ASSIGNMENT).jar
	rm temp.jar 


clean:
	-rm -f *.class
	-rm -f $(ASSIGNMENT).jar


grading: 
	unzip assignment.zip
	-cp -f *Node.java SpreadSheetJ/Model/
	-cp -f TimeValue.java SpreadSheetJ/Model/
	-rm -f TimeValue.java *Node.java
	make $(ASSIGNMENT).jar

zip: 
	-rm -f assignment.zip
	zip -9 -r assignment.zip Test*.java SpreadSheetJ




