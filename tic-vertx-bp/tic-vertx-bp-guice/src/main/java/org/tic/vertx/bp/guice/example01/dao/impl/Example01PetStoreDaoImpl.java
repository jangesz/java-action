package org.tic.vertx.bp.guice.example01.dao.impl;

import org.tic.vertx.bp.guice.example01.dao.Example01PetStoreDao;
import org.tic.vertx.bp.guice.example01.entity.Dog;

import java.util.Random;

public class Example01PetStoreDaoImpl implements Example01PetStoreDao {
    @Override
    public Dog findDogByName(String name) {
        Dog d = new Dog();
        d.setName(name);
        d.setId(new Random().nextLong());
        return d;
    }
}
