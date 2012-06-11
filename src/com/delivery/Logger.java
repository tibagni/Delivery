package com.delivery;

public class Logger {

    public static void error(String msg, Throwable t) {
        error(msg);
        t.printStackTrace();
    }

    public static void error(String msg) {
        stdOut("!!! " + msg);
    }

    public static void warning(String msg) {
        stdOut("! " + msg);
    }

    public static void debug(String msg) {
        stdOut("[debug] " + msg);
    }

    public static void info(String msg) {
        stdOut("[info] " + msg);
    }

    private static void stdOut(String s, Object... args) {
        System.out.printf("\n" + s, args);
    }
}
