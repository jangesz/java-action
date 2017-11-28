package org.tic.vertx.bp.guice.example01.service;

import org.tic.vertx.bp.guice.example01.entity.Dog;

public interface Example01PetStoreService {
    Dog findDogByName(String name);
}
