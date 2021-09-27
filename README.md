# Family Tree Tech Test Project

Java project to model a basic family tree structure manipulated using a REST API

## Technology Choices Made

I have selected Java out of the recommended languages, as it is the most suitable language that I am comfortable with.

The project uses the Spring Boot framework with Spring Web, Spring Data JPA and H2/Hibernate dependencies 
to enable fast development of a RESTful application with support for an in-memory database to store the Family tree data.
Additionally, it was initially created using the Spring Initializr to quickly generate the project structure.

## Limitations

Each person record is keyed on their name in the database, therefore every person must have a unique name in the tree
(duplicate names are rejected when adding a new person).

## Running the project in a Mac environment

1. Clone the project to a local directory (git clone https://github.com/alexdmitchell/family-tree.git)
2. cd to family-tree, then run mvn clean install to build the project
3. Start the project by running mvn spring-boot:run
4. Stop the project using ctrl+c

## Automated testing
For testing, I implemented 3 automated tests to test adding a new child to the tree, check that adding a duplicate 
person is rejected and to add a new child and list the parents for that child, within the given timeframe.

To run the automated tests from the command line, run mvn -Dtest=FamilyTreeApplicationTests test

## Interacting with the application

The family tree can be manipulated by sending JSON, using an application such as Postman. The available endpoints 
provided by the REST API are

* /add - Add a new person to the tree by sending a PUT request to http://localhost:8080/add with JSON in the body, i.e.
```json
{
  "name":"Charles",
  "parents": ["Alice", "Bob"]
}
```

* /parents - Get the parents of a given person by sending a GET request to http://localhost:8080/parents with JSON
in the body, i.e.
```json
{
  "name":"Charles"
}
```

* /children - Return the children of a given person by sending a GET request to http://localhost:8080/children with JSON 
in the same format as /parents
* /descendants - Return the descendants of a given person by sending a GET request to http://localhost:8080/descendants with JSON
in the same format as /parents
* /ancestors - Return the ancestors of a given person by sending a GET request to http://localhost:8080/ancestors with JSON
in the same format as /parents






