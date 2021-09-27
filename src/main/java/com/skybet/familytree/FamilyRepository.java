package com.skybet.familytree;

import org.springframework.data.repository.CrudRepository;

public interface FamilyRepository extends CrudRepository<Person, String> {
    Person findByName(String name) throws RepositoryException;
}
