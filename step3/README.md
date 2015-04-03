Use of Polyglot for Maven with Groovy.

Editing
-------

ATM it's not possible to edit this project under Eclipse.

The current release 1.5.0 of [M2Eclipse](http://eclipse.org/m2e/) doesn't recognize Polyglot for Maven. Even version 1.6.0 doesn't seem to provide any help.

The only workaround should be to use [JBoss Tools m2e-polyglot-poc](https://github.com/jbosstools/m2e-polyglot-poc) with the update site:


> [http://dl.bintray.com/jbosstools/m2e-polyglot-poc/](http://dl.bintray.com/jbosstools/m2e-polyglot-poc/)

But m2e-polyglot-poc doesn't show up under Eclipse Luna, either SR1 or SR2.

Building
-------

> mvn package


Running
-------

> java -jar target/step3-helloworld-0.0.1-SNAPSHOT-jar-with-dependencies.jar

will display something like "Greeting: Hello World (5564df04-e93d-48cd-800c-c2b19ae969e5)" on the console.


