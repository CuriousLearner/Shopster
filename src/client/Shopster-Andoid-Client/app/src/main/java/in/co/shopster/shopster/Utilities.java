package in.co.shopster.shopster;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by vikram on 19/3/16.
 */
public class Utilities {

    public static void showToast(String message, Context ctx, boolean isLong) {
        Toast.makeText(
                ctx,
                message,
                (isLong) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT
        ).show();
    }

    public static void writeDebugLog(String msg) {
        if(Config.isDebugModeEnabled()) {
            Log.d(Config.getDebugLogTag(), msg);
        }
    }

    public static void setSharedPreference(Context ctx, String prefKey, String prefVal) {
        SharedPreferences.Editor she = ctx.getSharedPreferences(Config.getSharedPrefKey(), 0).edit();
        she.putString(prefKey, prefVal);
        she.apply();
    }

    public static String getSharedPreference(Context ctx, String prefKey) {
        SharedPreferences sp = ctx.getSharedPreferences(Config.getSharedPrefKey(), 0);
        return sp.getString(prefKey, "");
    }


    public static void clearAllSharedPreferences(Context ctx) {
        SharedPreferences.Editor spe = ctx.getSharedPreferences(Config.getSharedPrefKey(), 0).edit();
        spe.clear();
        spe.apply();
    }


}
