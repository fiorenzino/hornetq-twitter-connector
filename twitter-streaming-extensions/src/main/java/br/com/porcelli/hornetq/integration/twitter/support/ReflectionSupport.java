package br.com.porcelli.hornetq.integration.twitter.support;

import java.lang.reflect.Constructor;

import org.hornetq.core.logging.Logger;

public final class ReflectionSupport {
    private static final Logger log = Logger.getLogger(ReflectionSupport.class);

    private ReflectionSupport() {}

    public static <T> T buildInstance(final Class<T> clazz, final Class<?>[] constructorArgs, final Object[] args) {
        T reclaimer = null;
        Constructor<T> constructor;

        try {
            constructor = clazz.getConstructor(constructorArgs);
            reclaimer = constructor.newInstance(args);
        } catch (final Exception e) {
            log.error("Can't create instance of '" + clazz.getName() + "'.");
        }
        return reclaimer;
    }
}
