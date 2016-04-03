package in.co.shopster.shopster;

/**
 * Created by vikram on 3/4/16.
 */
public class Config {

    private static String DEBUG_LOG_TAG = "SHOPSTER-DEBUG";

    private static boolean debugModeEnabled = false;

    public static String getDebugLogTag() {
        return Config.DEBUG_LOG_TAG;
    }

    public static void enableDebugLogs() {
        Config.debugModeEnabled = true;
    }

    public static void disableDebugLogs() {
        Config.debugModeEnabled = false;
    }

    public static boolean isDebugModeEnabled() {
        return Config.debugModeEnabled;
    }


}
