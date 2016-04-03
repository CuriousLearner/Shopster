package in.co.shopster.shopster;

import android.content.Context;
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



}
