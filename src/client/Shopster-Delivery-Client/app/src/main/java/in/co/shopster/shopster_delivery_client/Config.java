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

    private static String SHOPSTER_USER_ID_SP_KEY = "SHOPSTER-UID";

    private static String SHOPSTER_USER_HASH_SP_KEY = "SHOPSTER-UHASH";

    private static String SHOPSTER_DELIVERY_OBJ_QUEUED_ID_SP_KEY = "SHOPSTER-DELIVERY-OBJ-QID";

    private static String SHOPSTER_DELIVERY_OBJ_ORDER_ID_SP_KEY = "SHOPSTER-DELIVERY-OBJ-ORDER-ID";

    private static String SHOPSTER_DELIVERY_OBJ_DELIVERED_BY_SP_KEY = "SHOPSTER-DELIVERY-OBJ-DELIVERED-BY";

    private static String SHOPSTER_DELIVERY_OBJ_IS_DELIVERED_SP_KEY = "SHOPSTER-DELIVERY-OBJ-IS-DELIVERED";

    private static String SHOPSTER_DELIVERY_OBJ_DELIVERY_TYPE_SP_KEY = "SHOPSTER-DEIVERY-OBJ-DELIVERY-TYPE";



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

    public static String getShopsterUserIdKey() { return SHOPSTER_USER_ID_SP_KEY; }


    public static String getShopsterUserHashKey() { return SHOPSTER_USER_HASH_SP_KEY; }

    public static String getShopsterDeliveryObjQueueIdKey() { return SHOPSTER_DELIVERY_OBJ_QUEUED_ID_SP_KEY; }


    public static String getShopsterDeliveryObjOrderIdKey() { return SHOPSTER_DELIVERY_OBJ_ORDER_ID_SP_KEY; }

    public static String getShopsterDeliveryObjDeliveredByKey() { return SHOPSTER_DELIVERY_OBJ_DELIVERED_BY_SP_KEY; }


    public static String getShopsterDeliveryObjIsDeliveredKey() { return SHOPSTER_DELIVERY_OBJ_IS_DELIVERED_SP_KEY; }

    public static String getShopsterDeliveryObjDeliveryTypeKey() { return SHOPSTER_DELIVERY_OBJ_DELIVERY_TYPE_SP_KEY; }



    public static void setShopsterApiHost(String shopsterApiHost) { Config.SHOPSTER_API_HOST = shopsterApiHost; }



}

