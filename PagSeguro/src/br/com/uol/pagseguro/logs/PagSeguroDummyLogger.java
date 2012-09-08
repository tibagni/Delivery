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

/**
 * Logging class
 */
public final class PagSeguroDummyLogger implements Logger {

    public PagSeguroDummyLogger() {

    }

    /**
     * Prints an info line in the log file
     * 
     * @param object
     */
    public void info(Object object) {

    }

    /**
     * Prints an error line in the log file
     * 
     * @param message
     * @param e
     */
    public void error(String message, Exception e) {

    }

    /**
     * Prints an error line in the log file
     * 
     * @param message
     */
    public void error(String message) {

    }

    /**
     * Prints a warning line in the log file
     * 
     * @param message
     */
    public void warning(String message) {

    }

    /**
     * Prints a warning line in the log file
     * 
     * @param print
     */
    public void debug(String message) {

    }

    /**
     * Prints an info line in the log file
     * 
     * @param print
     */
    public void info(String message) {

    }

    /**
     * Prints a warning line in the log file
     * 
     * @param print
     */
    public void warn(String message) {

    }

    /**
     * Prints a warning line in the log file
     * 
     * @param print
     */
    public void warn(String message, Throwable t) {

    }

    /**
     * Prints an error line in the log file
     * 
     * @param print
     */
    public void error(String message, Throwable t) {

    }

}
