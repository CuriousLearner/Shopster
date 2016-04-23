package in.co.shopster.shopster_delivery_client;

/**
 * Created by vikram on 23/4/16.
 */
public class Config {

    private static String DEBUG_LOG_TAG = "SHOPSTER-DEL-DEBUG";

    private static boolean debugModeEnabled = false;

    private static String SHOPSTER_API_HOST = "http://shopster-shopsterr.rhcloud.com";

    private static String SHARED_PREF_KEY = "SHOPSTER-DELIVERY-SP";

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

    public static String getShopsterApiHost() { return SHOPSTER_API_HOST; };

    public static void setShopsterApiHost(String shopsterApiHost) { Config.SHOPSTER_API_HOST = shopsterApiHost; }



}

