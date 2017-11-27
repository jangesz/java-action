package org.tic.guice.vertxdemocommon.module;

import com.google.inject.AbstractModule;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tic.guice.vertxdemo02.action.VertxDemo02Action;
import org.tic.guice.vertxdemocommon.annotation.Action;

import java.util.Set;

public abstract class BaseAbstractModule extends AbstractModule {

    private static final Logger logger = LoggerFactory.getLogger(BaseAbstractModule.class);

    public void bindActions(String actionPkg) {
        System.out.println(actionPkg);
        try {
//            Reflections reflections = new Reflections(actionPkg);
//            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Action.class, true);
//            classes.forEach(clazz -> {
//                System.out.println("guice bidn class: " + clazz.getName());
//                if (logger.isInfoEnabled()) {
//                    logger.info("guice bind class: " + clazz.getName());
//                }
//                binder().bind(clazz);
//            });
//            binder().bind(VertxDemo02Action.class);
        } catch (Exception e) {
            throw new UnsupportedOperationException("can't add action class");
        }
    }

}
