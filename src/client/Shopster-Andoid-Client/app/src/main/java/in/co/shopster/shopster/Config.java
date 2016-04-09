package in.co.shopster.shopster;

/**
 * Created by vikram on 3/4/16.
 */
public class Config {

    private static String DEBUG_LOG_TAG = "SHOPSTER-DEBUG";

    private static boolean debugModeEnabled = false;

    private static String SHARED_PREF_KEY = "SHOPSTER-SP";

    private static String SHOPSTER_TOKEN_SP_KEY = "SHOPSTER-API-TOKEN";

    public static String getDebugLogTag() { return Config.DEBUG_LOG_TAG; }

    public static void enableDebugLogs() {
        Config.debugModeEnabled = true;
    }

    public static void disableDebugLogs() {
        Config.debugModeEnabled = false;
    }

    public static boolean isDebugModeEnabled() {
        return Config.debugModeEnabled;
    }

    public static String getSharedPrefKey() { return Config.SHARED_PREF_KEY; }

    public static String getShopsterTokenKey() { return Config.SHOPSTER_TOKEN_SP_KEY; }





}
