# AssertJ Migrator

AssertJ migrator to migrate complex codebase based on JavaParser.

## Build

You need Java 11 and Maven.

To build:

```
mvn package
```

## Before running

Make sure you use Git or some other VCS tool before running the tool, and clean your working directory, as the tool
might break your code.

In order to resolve types, the tool will scan and load all the JARs available in the directory tree of the project path.

Before running, build the project you want to migrate, and copy the dependencies somewhere in the project directory
tree.

For example, with maven, you can do:

```
mvn package
```

To build all the JARs of your project and then:

```
mvn dependency:copy-dependencies
```

To copy all the dependencies in the target directory

## Run

To see all the options:

```
java -jar target/assertj-migrator-1.0-SNAPSHOT.jar
```

To run:

```
java -jar target/assertj-migrator-1.0-SNAPSHOT.jar <project_path>
```
