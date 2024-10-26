build:
	mvn compile

unit-test:
	mvn test -P test

integration-test:
	mvn test -P integration-test

system-test:
	mvn test -P system-test