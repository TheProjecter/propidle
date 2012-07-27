package com.googlecode.propidle.client.logging;

import java.io.PrintStream;

public class PrintStreamLogger implements Logger {

    private final PrintStream printStream;

    private PrintStreamLogger(PrintStream printStream) {
        this.printStream = printStream;
    }

    public static PrintStreamLogger printStreamLogger(PrintStream printStream) {
        return new PrintStreamLogger(printStream);
    }

    @Override
    public void log(Message message) {
        printStream.println(message);
    }
}
