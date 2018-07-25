F :=
C := $(shell basename $(F) .java).class
RMDIR := rmdir

all: $(C)

$(C): $(F)
	javac $<

CrcPk07.class: CrcPk07.java XmlConfLoader.class

clean:
	$(RM) *.class *.jar
	$(RM) package-list

cleandoc:
	$(RM) *.html *.css resources/*.gif
	$(RMDIR) resources

.PHONY: all test clean cleandoc
