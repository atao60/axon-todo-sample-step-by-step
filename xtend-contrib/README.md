Rationale
-------

Eclipse editor for Xtend doesn't like unused member "serialVersionUID". Unlike Eclipse editor for Java, it always displays a warning message. Adding SuppressWarnings annotation doesn't change anything. 

Xtend is very helpful to reduce the amount of boilerplate code. But no Active Annotation was available on the web to deal with Serializable interface and member "serialVersionUID".

Building
-------

> mvn install

The package must be available from the local m2 repository.

