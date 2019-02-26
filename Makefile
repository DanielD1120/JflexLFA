build:
	jflex Hello.flex
	javac *.java

run:
	java Parser

clean:
	rm HelloLexer.*
	rm *.class
	rm arbore output
