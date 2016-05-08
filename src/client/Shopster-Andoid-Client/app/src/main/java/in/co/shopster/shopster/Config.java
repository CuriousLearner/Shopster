package in.co.shopster.shopster;

/**
 * Created by vikram on 3/4/16.
 */
public class Config {

    private static String DEBUG_LOG_TAG = "SHOPSTER-DEBUG";

    private static boolean debugModeEnabled = false;

    private static String SHOPSTER_API_HOST = "http://shopster-shopsterr.rhcloud.com";

    private static String SHARED_PREF_KEY = "SHOPSTER-SP";

    private static String SHOPSTER_TOKEN_SP_KEY = "SHOPSTER-API-TOKEN";

    private static String SHOPSTER_USER_ID = "USER_ID";

    private static String SHOPSTER_USER_Hash = "USER_Hash";

    private static String SHOPSTER_ORDER_ID = "ORDER_ID";

    private static String SHOPSTER_ORDER_STATUS = "ORDER_STATUS";

    private static String SHOPSTER_ORDER_PRICE = "ORDER_PRICE";

    private static String SHOPSTER_DELIVERY_PREF = "DELIVERY_PREF";


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

    public static String getShopsterApiHost() { return SHOPSTER_API_HOST; }

    public static String getShopsterUserId(){ return Config.SHOPSTER_USER_ID; }

    public static String getSHOPSTER_USER_Hash(){ return Config.SHOPSTER_USER_Hash; }

    public static String getShopsterOrderIdKey() { return Config.SHOPSTER_ORDER_ID; }

    public static String getShopsterOrderStatusKey() { return Config.SHOPSTER_ORDER_STATUS; }

    public static String getShopsterOrderPriceKey() { return Config.SHOPSTER_ORDER_PRICE; }

    public static String getShopsterDeliveryPrefKey() { return Config.SHOPSTER_DELIVERY_PREF; }


    public static void setShopsterApiHost(String shopsterApiHost) { Config.SHOPSTER_API_HOST = shopsterApiHost; }



}
