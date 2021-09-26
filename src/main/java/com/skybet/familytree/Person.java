package com.skybet.familytree;

import lombok.Builder;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
@Builder
public class Person {
    @Id
    private String name;
    @ElementCollection
    private List<String> parents;
    @ElementCollection
    private List<String> children;

    public Person(){}

    public Person(String name, List<String> parents, List<String> children)
    {
        this.name = name;
        this.parents = parents;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getParents() {
        return parents;
    }

    public void addChild(String childName)
    {
        children.add(childName);
    }

    public void setParents(List<String> parents) {
        this.parents = parents;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }
}
