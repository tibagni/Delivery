/**
 * Copyright [2011] [PagSeguro Internet Ltda.]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package br.com.uol.pagseguro.logs;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import br.com.uol.pagseguro.properties.PagSeguroConfig;

public class PagSeguroLoggerFactory {

    private static Map map = new HashMap();

    private static String loggerImplClassName = PagSeguroConfig.getLoggerImplementation();

    public static Logger getLogger(Class clazz) {

        Logger logger = (Logger) map.get(clazz.getName());

        if (logger == null) {

            try {
                Class c = Class.forName(loggerImplClassName);
                Constructor[] constructors = c.getConstructors();
                Constructor defaultConstructor = null;
                Constructor classConstructor = null;

                for (int i = 0; i < constructors.length; i++) {
                    Constructor currConstructor = constructors[i];
                    Class[] inputParamTypes = currConstructor.getParameterTypes();
                    if (inputParamTypes.length == 0) {
                        defaultConstructor = currConstructor;
                    } else if (inputParamTypes.length == 1 && Class.class.equals(inputParamTypes[0])) {
                        classConstructor = currConstructor;
                    }
                }

                if (classConstructor != null) {
                    logger = (Logger) classConstructor.newInstance(new Object[] { clazz });
                    map.put(clazz.getName(), logger);
                } else if (defaultConstructor != null) {
                    
                    if (map.size() > 0) {
                        logger = (Logger) map.values().iterator().next();
                    } else {
                        logger = (Logger) defaultConstructor.newInstance(null);
                        map.put(clazz.getName(), logger);
                    }
                } else {
                    throw new RuntimeException("Error initializing class: " + loggerImplClassName
                            + ". Default constructor or constructor receiving a Class object not found.");
                }

            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Error initializing class: " + loggerImplClassName + ". Class not found.", e);
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new RuntimeException("Error initializing class: " + loggerImplClassName + ".", e);
            }
        }

        return logger;
    }

}
