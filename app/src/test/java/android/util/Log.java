package android.util;


// Easy way to mock Log.d which must not be called directly in unit tests.
// See https://stackoverflow.com/questions/36787449/how-to-mock-method-e-in-log
@SuppressWarnings({"unused", "SameReturnValue"})
public class Log {
    @SuppressWarnings("SameReturnValue")
    public static int d(String tag, String msg) {
        System.out.println("DEBUG: " + tag + ": " + msg);
        return 0;
    }

    public static int i(String tag, String msg) {
        System.out.println("INFO: " + tag + ": " + msg);
        return 0;
    }

    public static int w(String tag, String msg) {
        System.out.println("WARN: " + tag + ": " + msg);
        return 0;
    }

    public static int e(String tag, String msg) {
        System.out.println("ERROR: " + tag + ": " + msg);
        return 0;
    }
}