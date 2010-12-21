/**
 * Copyright (c) 2010 Alexandre Porcelli <alexandre.porcelli@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
