Use JPA persistence with [Hibernate](http://hibernate.org/) and [Hsqldb](http://hsqldb.org/).

Require [Spring](http://projects.spring.io/spring-framework/) as the only available implementation of Axon TransactionManager is SpringTransactionManager.

Configuring
-------

See [README.md of step3](https://github.com/atao60/axon-todo-sample-step-by-step/blob/master/step3) about configuring Polyglot for Maven.

Building
-------

        mvn package


Running
-------

        java -jar target/step4-helloworld-0.0.1-SNAPSHOT-standalone.jar

will display something like "Greeting: Hello World (5564df04-e93d-48cd-800c-c2b19ae969e5)" on the console.


