package com.skybet.familytree;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FamilyTreeApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    // Test to add a new child to the tree
    @Test
    public void addPerson() throws Exception {
        // Build up the JSON object
        Person personToAdd = Person.builder()
                .name("Charles")
                .parents(Arrays.asList("Alice", "Bob")) // People already initialised in the DB
                .build();

        // Send it as a MockMvc put request
        this.mockMvc.perform(put("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(personToAdd)))
                // Check that the person was added
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Added person successfully")));
    }

    // Test to attempt to add a duplicate person
    @Test
    public void addDuplicatePerson() throws Exception {
        // Build up the JSON object
        Person person = Person.builder()
                .name("Bob")
                .build();

        // Then send it as a MockMvc put request
        this.mockMvc.perform(put("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(person)))
                // Check the CONFLICT response is returned, so the person has not been added
                .andExpect(status().isConflict());
    }


    // Test to add a new person then list their parents
    @Test
    public void addChildThenListParents() throws Exception {
        // Build up the JSON object
        Person personToAdd = Person.builder()
                .name("Dave")
                .parents(Arrays.asList("Alice", "Bob")) // People already initialised in the DB
                .build();

        // Send it as a MockMvc put request
        this.mockMvc.perform(put("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(personToAdd)))
                // Check that the person was added
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Added person successfully")));

        // Create a new JSON object containing only the person name
        Person personToQuery = Person.builder()
                .name("Dave")
                .build();

        // Send this object using a get request
        this.mockMvc.perform(get("/parents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(personToQuery)))
                // Check that the request is ok and that the parents returned match those defined
                // in the original JSON object
                .andExpect(status().isOk())
                .andDo(response -> {
                    String json = response.getResponse().getContentAsString();
                    Person[] personResponse = objectMapper.readValue(json, Person[].class);
                    for (Person person : personResponse) {
                        Assertions.assertTrue(personToAdd.getParents().contains(person.getName()));
                    }
                });
    }

    // TODO: Add automated tests for listing children, descendants and ancestors
}
