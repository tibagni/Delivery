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
public final class PagSeguroSystemOutLogger implements Logger {

    public PagSeguroSystemOutLogger() {

    }

    /**
     * Prints an info line in the log file
     * 
     * @param object
     */
    public void info(Object object) {
    	System.out.println("[INFO]"+object);
    }

    /**
     * Prints an error line in the log file
     * 
     * @param message
     * @param e
     */
    public void error(String message, Exception e) {
    	System.out.println("[ERROR] " + message + e.getMessage());
    }

    /**
     * Prints an error line in the log file
     * 
     * @param message
     */
    public void error(String message) {
    	System.out.println("[ERROR] "+message);
    }

    /**
     * Prints a warning line in the log file
     * 
     * @param message
     */
    public void warning(String message) {
    	System.out.println("[WARN] " + message);
    }

    /**
     * Prints a warning line in the log file
     * 
     * @param print
     */
    public void debug(String message) {
    	System.out.println("[DEBUG] " + message);

    }

    /**
     * Prints an info line in the log file
     * 
     * @param print
     */
    public void info(String message) {
    	System.out.println("[INFO]" + message);

    }

    /**
     * Prints an warning line in the log file
     * 
     * @param print
     */
    public void warn(String message) {
    	System.out.println("[WARN] "+message);
    }

    /**
     * Prints a warning line in the log file
     * 
     * @param print
     */
    public void warn(String message, Throwable t) {
    	System.out.println("[WARN] " + message + t.getMessage());
    }

    /**
     * Prints an error line in the log file
     * 
     * @param print
     */
    public void error(String message, Throwable t) {
    	System.out.println("[ERROR] " + message + t.getMessage());
    }

}
