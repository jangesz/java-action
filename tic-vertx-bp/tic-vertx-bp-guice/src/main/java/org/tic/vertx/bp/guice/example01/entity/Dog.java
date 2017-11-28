package org.tic.vertx.bp.guice.example01.entity;

public class Dog {
    private Long id;
    private String name;

    @Override
    public String toString() {
        return "name = " + this.name + " , id = " + this.id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
