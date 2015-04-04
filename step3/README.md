Use of [Polyglot for Maven](https://github.com/takari/maven-polyglot) with [Groovy](http://groovy-lang.org/).

The release 3.3.1 or above of [Maven](https://maven.apache.org/) is required.

Under [Eclipse Luna](https://projects.eclipse.org/releases/luna), [M2E](http://eclipse.org/m2e/) is not aware of [Polyglot](https://github.com/takari/maven-polyglot) yet. At the moment (M2E v. 1.5.1), the only workaround is to use JBoss Tools [m2e-polyglot-poc](https://github.com/jbosstools/m2e-polyglot-poc).

Configuring
-------

With an existing Maven project, the most straightforward way to start is to get a groovy pom from the pom.xml file using the tool provided by [Takari](http://takari.io/), i.e. :

         mvn io.takari.polyglot:polyglot-translate-plugin:translate -Dinput=pom.xml -Doutput=pom.groovy

Note. Don't forget to add the .mvn/extensions.xml file!

To work under Eclipse, m2e-polyglot-poc must be installed, e.g. with the update site:
         
         http://dl.bintray.com/jbosstools/m2e-polyglot-poc/

Then each Maven Build launch configuration has to specify:

* on the tab "Main", Maven Runtime: `MAVEN (External {your maven path} 3.3.1)`
* on the tab "JRE", VM Arguments: `-Dmaven.multiModuleProjectDirectory=`

Building
-------

        mvn package


Running
-------

        java -jar target/step3-helloworld-0.0.1-SNAPSHOT-jar-standalone.jar

will display something like "Greeting: Hello World (5564df04-e93d-48cd-800c-c2b19ae969e5)" on the console.


