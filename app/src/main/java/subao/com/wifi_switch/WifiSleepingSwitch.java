package subao.com.wifi_switch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by wong on 2017/3/22.
 */

public class WifiSleepingSwitch   {

    public   static   final String TAG =   "wifisleeping" ;

     private   static   WifiSleepingSwitch     wifiSleepingSwitch ;

      public  static   final String WIFI_SLEEP_POLICY =  "wifi_sleep_policy";




    public     WifiSleepingSwitch   ()  {


    }



    public void nerverSleeping(Context context) {

           int  value  ;
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
              value = Settings.System.getInt(context.getContentResolver(), android.provider.Settings.Global.WIFI_SLEEP_POLICY, android.provider.Settings.Global.WIFI_SLEEP_POLICY_DEFAULT);
          }
          else  {
               value = Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.WIFI_SLEEP_POLICY, android.provider.Settings.System.WIFI_SLEEP_POLICY_DEFAULT);
          }

        Log.d(TAG, "setWifiDormancy() returned: " + value);
        final SharedPreferences prefs = context.getSharedPreferences("wifi_sleep_policy", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(WIFI_SLEEP_POLICY, value);
        editor.apply();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (android.provider.Settings.Global.WIFI_SLEEP_POLICY_NEVER != value) {
                Settings.System.putInt(context.getContentResolver(), android.provider.Settings.Global.WIFI_SLEEP_POLICY, android.provider.Settings.Global.WIFI_SLEEP_POLICY_NEVER);
            }
        }

        else {
            if (android.provider.Settings.System.WIFI_SLEEP_POLICY_NEVER != value) {
                Settings.System.putInt(context.getContentResolver(), android.provider.Settings.System.WIFI_SLEEP_POLICY, android.provider.Settings.System.WIFI_SLEEP_POLICY_NEVER);
            }

        }



    }


    public void restoreWifiDormancy(Context context){
        final SharedPreferences prefs = context.getSharedPreferences("wifi_sleep_policy", Context.MODE_PRIVATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            int defaultPolicy = prefs.getInt(WIFI_SLEEP_POLICY, android.provider.Settings.Global.WIFI_SLEEP_POLICY_DEFAULT);
            Settings.System.putInt(context.getContentResolver(), android.provider.Settings.Global.WIFI_SLEEP_POLICY, defaultPolicy);
        }
        else  {
            int defaultPolicy = prefs.getInt(WIFI_SLEEP_POLICY, Settings.System.WIFI_SLEEP_POLICY_DEFAULT);
            Settings.System.putInt(context.getContentResolver(), Settings.System.WIFI_SLEEP_POLICY, defaultPolicy);

        }

    }























}
