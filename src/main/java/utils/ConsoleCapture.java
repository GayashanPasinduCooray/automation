package utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ConsoleCapture {

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final PrintStream oldOut = System.out;
    private PrintStream teeStream;

    // Start capturing System.out
    public void start() {
        teeStream = new PrintStream(baos) {
            @Override
            public void println(String x) {
                super.println(x);       // store in buffer
                oldOut.println(x);      // print to original console
            }

            @Override
            public void println(Object x) {
                super.println(x);
                oldOut.println(x);
            }

            @Override
            public void println() {
                super.println();
                oldOut.println();
            }
        };
        System.setOut(teeStream);
    }

    // Stop capturing and restore original System.out
    public void stop() {
        if (teeStream != null) {
            System.out.flush();
            System.setOut(oldOut);
        }
    }

    // Get captured console output as String
    public String getCapturedOutput() {
        return baos.toString();
    }
}
