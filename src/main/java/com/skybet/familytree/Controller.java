package com.skybet.familytree;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
public class Controller {
    ArrayList<Person> descendants = new ArrayList<>();
    ArrayList<Person> ancestors = new ArrayList<>();

    @Autowired
    FamilyRepository familyRepository;

    // Method to initialise the Hibernate DB with 2 initial parents
    @PostConstruct
    private void initialiseDB() {
        familyRepository.save(new Person("Alice", null, null));
        familyRepository.save(new Person("Bob", null, null));

        /*
        // Test data
        familyRepository.save(new Person("Alice", null, Arrays.asList("Charles", "Dave")));
        familyRepository.save(new Person("Bob", null, Arrays.asList("Charles", "Dave")));
        familyRepository.save(new Person("Charles", Arrays.asList("Alice", "Bob"), Arrays.asList("George")));
        familyRepository.save(new Person("Emily", null, Arrays.asList("George")));
        familyRepository.save(new Person("George", Arrays.asList("Emily", "Charles"), null));
        familyRepository.save(new Person("Dave", Arrays.asList("Alice", "Bob"), Arrays.asList("Harry")));
        familyRepository.save(new Person("Francis", null, Arrays.asList("Harry")));
        familyRepository.save(new Person("Harry", Arrays.asList("Francis", "Dave"), null));*/
    }

    // Method for initial testing to print all entries in the DB
    @GetMapping
    public ResponseEntity<?> listTree() {
        return ResponseEntity.ok(Lists.newArrayList(familyRepository.findAll(), HttpStatus.OK));
    }

    // Method to add a child to the DB
    @PutMapping("/add")
    public ResponseEntity<String> addPerson(@RequestBody Person reqPerson) {
        // Prevent duplicate names from being added to the repository
        if (familyRepository.existsById(reqPerson.getName()))
            return new ResponseEntity<>("Person already exists", HttpStatus.CONFLICT);

        // Add given Person (from the JSON object) to the repository
        familyRepository.save(reqPerson);

        // Then insert the new person's name into the children list for both parents
        // if they have been defined
        Person parent;
        if (reqPerson.getParents() != null) {
            for (String parentStr : reqPerson.getParents()) {
                // Get the parent out of the repository
                parent = familyRepository.findByName(parentStr);
                // Add the child to the parent then save
                parent.addChild(reqPerson.getName());
                familyRepository.save(parent);
            }
        }
        return new ResponseEntity<>("Added person successfully", HttpStatus.OK);
    }

    // Method to list both parents for a given person
    @GetMapping("/parents")
    public ResponseEntity<?> listParents(@RequestBody Person reqPerson) {
        ArrayList<Person> parents = new ArrayList<>(); // Used to store parent person objects
        // Get the requested person out of the repository
        Person person = familyRepository.findByName(reqPerson.getName());
        if (person != null) {
            // Person exists, so get both parents out of the repository
            for (String parent : person.getParents()) {
                parents.add(familyRepository.findByName(parent));
            }
        } else {
            return new ResponseEntity<>("Could not find person in the tree", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(parents);
    }

    // Method to list children for a given person
    @GetMapping("/children")
    public ResponseEntity<?> listChildren(@RequestBody Person reqPerson) {
        ArrayList<Person> children = new ArrayList<>(); // Used to store child person objects
        // Get the requested person out of the repository
        Person person = familyRepository.findByName(reqPerson.getName());
        if (person != null) {
            // Person exists, so get any out of the repository
            for (String child : person.getChildren()) {
                children.add(familyRepository.findByName(child));
            }
        } else {
            return new ResponseEntity<>("Could not find person in the tree", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(children);
    }

    // Method to list all descendants for a given person
    @GetMapping("/descendants")
    public ResponseEntity<?> listDescendants(@RequestBody Person reqPerson) {
        // New request, so clear the array of descendants
        descendants.clear();

        // Get the requested person out of the repository
        Person person = familyRepository.findByName(reqPerson.getName());

        if (person != null) {    // Person exists, so recursively get the descendants
            iterateThroughDescendants(person);
        } else {
            return new ResponseEntity<>("Could not find person in the tree", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(descendants);
    }

    private void iterateThroughDescendants(Person person) {
        // For every person, get their children's names
        for (String childStr : person.getChildren()) {
            Person child = familyRepository.findByName(childStr);
            if (child != null) {
                // Child exists, so add it to the array of descendants
                descendants.add(child);
                if (child.getChildren().isEmpty()) {
                    //  person has no children so stop
                } else {   // Repeat for the current child
                    iterateThroughDescendants(child);
                }
            }
        }
    }

    // Method to list all ancestors for a given person
    @GetMapping("/ancestors")
    public ResponseEntity<?> listAncestors(@RequestBody Person reqPerson) {
        // New request, so clear the array of ancestors
        ancestors.clear();

        // Get the requested person out of the repository
        Person person = familyRepository.findByName(reqPerson.getName());

        if (person != null) {    // Person exists, so recursively get the ancestors
            iterateThroughAncestors(person);
        } else {
            return new ResponseEntity<>("Could not find person in the tree", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ancestors);
    }

    private void iterateThroughAncestors(Person person) {
        // For every person, get their parent's names
        for (String parentStr : person.getParents()) {
            Person parent = familyRepository.findByName(parentStr);
            if (parent != null) {
                // Parent exists, so add it to the array of descendants
                ancestors.add(parent);
                if (parent.getParents().isEmpty()) {
                    //  person has no children so stop
                } else {   // Repeat for the current child
                    iterateThroughAncestors(parent);
                }
            }
        }
    }

}
