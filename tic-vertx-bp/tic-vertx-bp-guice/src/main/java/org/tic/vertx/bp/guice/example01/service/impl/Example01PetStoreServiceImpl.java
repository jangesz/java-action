package org.tic.vertx.bp.guice.example01.service.impl;

import org.tic.vertx.bp.guice.example01.dao.Example01PetStoreDao;
import org.tic.vertx.bp.guice.example01.entity.Dog;
import org.tic.vertx.bp.guice.example01.service.Example01PetStoreService;

import javax.inject.Inject;

public class Example01PetStoreServiceImpl implements Example01PetStoreService {

    private final Example01PetStoreDao example01PetStoreDao;

    @Inject
    public Example01PetStoreServiceImpl(Example01PetStoreDao example01PetStoreDao) {
        this.example01PetStoreDao = example01PetStoreDao;
    }

    @Override
    public Dog findDogByName(String name) {
        return this.example01PetStoreDao.findDogByName(name);
    }
}
