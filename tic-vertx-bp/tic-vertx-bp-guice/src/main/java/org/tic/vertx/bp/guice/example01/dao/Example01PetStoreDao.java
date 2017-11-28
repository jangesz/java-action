package org.tic.vertx.bp.guice.example01.dao;

import org.tic.vertx.bp.guice.example01.entity.Dog;

public interface Example01PetStoreDao {
    Dog findDogByName(String name);
}
