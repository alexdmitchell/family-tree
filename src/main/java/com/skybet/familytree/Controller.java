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

@RestController
public class Controller {

    @Autowired
    FamilyRepository familyRepository;

    // Method to initialise the Hibernate DB with 2 initial parents
    @PostConstruct
    private void initialiseDB() {
        familyRepository.save(new Person("Alice", null, null));
        familyRepository.save(new Person("Bob", null, null));
    }

    // TODO: Method for initial testing to print all entries in the DB
    @GetMapping
    public ResponseEntity<?> listTree()
    {
        return ResponseEntity.ok(Lists.newArrayList(familyRepository.findAll(), HttpStatus.OK));
    }

    // TODO: Method to add a child to the DB
    @PutMapping("/add")
    public ResponseEntity<String> addPerson(@RequestBody Person reqPerson)
    {
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    // TODO: Method to list both parents for a given person
    @GetMapping("/parents")
    public ResponseEntity<?> listParents(@RequestBody Person reqPerson)
    {
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    // TODO: Method to list children for a given person
    @GetMapping("/children")
    public ResponseEntity<?> listChildren(@RequestBody Person reqPerson)
    {
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    // TODO: Method to list all descendants for a given person
    @GetMapping("/descendants")
    public ResponseEntity<?> listDescendants(@RequestBody Person reqPerson)
    {
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    // TODO: Method to list all ancestors for a given person
    @GetMapping("/ancestors")
    public ResponseEntity<?> listAncestors(@RequestBody Person reqPerson)
    {
        return new ResponseEntity<>("", HttpStatus.OK);
    }

}
