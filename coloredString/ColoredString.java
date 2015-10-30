package coloredString;

public abstract class ColoredString {

    private final static String ANSI_RESET = "\u001B[0m";
    private final static String ANSI_BLACK = "\u001B[30m";
    private final static String ANSI_RED = "\u001B[31m";
    private final static String ANSI_GREEN = "\u001B[32m";
    private final static String ANSI_YELLOW = "\u001B[33m";
    private final static String ANSI_BLUE = "\u001B[34m";
    private final static String ANSI_PURPLE = "\u001B[35m";
    private final static String ANSI_CYAN = "\u001B[36m";
    private final static String ANSI_WHITE = "\u001B[37m";

    public static void printlnError(String msg) {
        System.err.println(ANSI_RED + "ERROR: " + msg + ANSI_RESET);
    }

    public static void printlnWarning(String msg) {
        System.err.println(ANSI_YELLOW + "WARNING: " + msg + ANSI_RESET);
    }

    public static void printlnSuccess(String msg) {
        System.out.println(ANSI_GREEN + "SUCCESS: " + msg + ANSI_RESET);
    }

    public static void printlnInfo(String msg) {
        System.err.println(ANSI_CYAN + "INFO: " + msg + ANSI_RESET);
    }

    public static void printlnBlack(String msg) {
        System.out.println(ANSI_BLACK + msg + ANSI_RESET);
    }

    public static void printlnRed(String msg) {
        System.out.println(ANSI_RED + msg + ANSI_RESET);
    }

    public static void printlnGreen(String msg) {
        System.out.println(ANSI_GREEN + msg + ANSI_RESET);
    }

    public static void printlnYellow(String msg) {
        System.out.println(ANSI_YELLOW + msg + ANSI_RESET);
    }

    public static void printlnBlue(String msg) {
        System.out.println(ANSI_BLUE + msg + ANSI_RESET);
    }

    public static void printlnPurple(String msg) {
        System.out.println(ANSI_PURPLE + msg + ANSI_RESET);
    }

    public static void printlnCyan(String msg) {
        System.out.println(ANSI_CYAN + msg + ANSI_RESET);
    }

    public static void printlnWhite(String msg) {
        System.out.println(ANSI_WHITE + msg + ANSI_RESET);
    }

}
