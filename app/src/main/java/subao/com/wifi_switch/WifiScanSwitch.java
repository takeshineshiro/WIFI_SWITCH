package subao.com.wifi_switch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wong on 2017/3/22.
 */

public class WifiScanSwitch {

    private static final int WIFI_SEARCH_TIMEOUT = 3; //扫描WIFI的超时时间

    private Context mContext;
    private WifiManager mWifiManager;
    private WiFiScanReceiver mWifiReceiver;
    private Lock mLock;
    private Condition mCondition;
    private SearchWifiListener mSearchWifiListener;
    private boolean mIsWifiScanCompleted = false;

    private  boolean unregistered  = false;






    public static enum ErrorType {
        SEARCH_WIFI_TIMEOUT, //扫描WIFI超时（一直搜不到结果）
        NO_WIFI_FOUND,       //扫描WIFI结束，没有找到任何WIFI信号
    }

    //扫描结果通过该接口返回给Caller
    public interface SearchWifiListener {
            void onSearchWifiFailed(ErrorType errorType);
            void onSearchWifiSuccess(List<String> results);
    }

      public WifiScanSwitch(Context context, SearchWifiListener listener ) {

        mContext = context;
        mSearchWifiListener = listener;

        mLock = new ReentrantLock();
        mCondition = mLock.newCondition();
        mWifiManager=(WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);

        mWifiReceiver = new WiFiScanReceiver();
    }







    public void search() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                //如果WIFI没有打开，则打开WIFI
                if( !mWifiManager.isWifiEnabled() ) {
                    mWifiManager.setWifiEnabled(true);
                }

                //注册接收WIFI扫描结果的监听类对象

                IntentFilter intentFilter  =   new IntentFilter() ;

                intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
              //  intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

                mContext.registerReceiver(mWifiReceiver, intentFilter);

                //开始扫描
                mWifiManager.startScan();

                mLock.lock();

                //阻塞等待扫描结果
                try {
                    mIsWifiScanCompleted = false;
                    mCondition.await(WIFI_SEARCH_TIMEOUT, TimeUnit.SECONDS);
                    if( !mIsWifiScanCompleted ) {
                        mSearchWifiListener.onSearchWifiFailed(ErrorType.SEARCH_WIFI_TIMEOUT);
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    mLock.unlock();


                }

            }
        }).start();
    }

    //系统WIFI扫描结果消息的接收者
    protected class WiFiScanReceiver extends BroadcastReceiver {

        public void onReceive(Context c, Intent intent) {

            //提取扫描结果
            List<String> ssidResults = new ArrayList<String>();
            List<ScanResult> scanResults = mWifiManager.getScanResults();
            for(ScanResult result : scanResults ) {
                ssidResults.add(result.SSID);
            }

            //检测扫描结果
            if( ssidResults.isEmpty() ) {
                mSearchWifiListener.onSearchWifiFailed(ErrorType.NO_WIFI_FOUND);
            }
            else {
                mSearchWifiListener.onSearchWifiSuccess(ssidResults);
            }

            mLock.lock();
            mIsWifiScanCompleted = true;
            mCondition.signalAll();
            mLock.unlock();
        }
    }


      public    void  closeSearch ()  {

            if (!unregistered) {
                //删除注册的监听类对象
                mContext.unregisterReceiver(mWifiReceiver);
                 unregistered= true;
            }

      }


       public   void  openWifi  ()  {

           if( !mWifiManager.isWifiEnabled() ) {
               mWifiManager.setWifiEnabled(true);
           }
       }


       public  void  closeWifi()  {

           if( mWifiManager.isWifiEnabled() ) {
               mWifiManager.setWifiEnabled(false);
           }

       }





}