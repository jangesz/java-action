package org.tic.vertx.examples.web.ueditor.service;

public interface UeditorService {
    void save(String html);

    String getPostById(Long id);

    void update(String html);
}
