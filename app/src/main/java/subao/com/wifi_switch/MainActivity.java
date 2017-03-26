package subao.com.wifi_switch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

      private Button    openwifiscan ;

      private Button    closewifiscan ;

     private  Button    openwifisleeping;

     private  Button   closewifisleeping;

    private  Button     openwifi ;

    private   Button   closewifi ;

    private EditText   editText  ;

     private WifiScanSwitch.SearchWifiListener   searchWifiListener ;


     private   WifiScanSwitch  wifiScanSwitch ;

    private    WifiSleepingSwitch  wifiSleepingSwitch ;

    public    static  final   String  TAG = "switch" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



         initWideget()  ;

          initVariable() ;



    }


       private    void   initWideget  () {

           openwifiscan  = (Button) findViewById(R.id.openscan) ;

           closewifiscan =   (Button) findViewById(R.id.closescan)  ;

           openwifisleeping = (Button)  findViewById(R.id.opensleeping)  ;

           closewifisleeping   =  (Button) findViewById(R.id.closesleeping)   ;

           editText       =  (EditText) findViewById(R.id.editText);

           openwifi      =   (Button)  findViewById(R.id.openwifi)  ;

           closewifi    =   (Button)  findViewById(R.id.closewifi)  ;




            openwifiscan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    wifiScanSwitch.search();
                }
            });


           closewifiscan.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   wifiScanSwitch.closeSearch();

                   String  closewifiscan  =  "closewifiscan" ;
                   editText.setText(closewifiscan);

               }
           });


            openwifisleeping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     wifiSleepingSwitch.restoreWifiDormancy(getApplicationContext());
                       String  openwifisleeping  =  "openwifisleeping" ;
                     editText.setText(openwifisleeping);
                }
            });


            closewifisleeping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    wifiSleepingSwitch.nerverSleeping(getApplicationContext());

                    String  closewifisleeping  =  "closewifisleeping" ;
                    editText.setText(closewifisleeping);
                }
            });


               openwifi.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       wifiScanSwitch.openWifi();

                       String  openwifi  =  "openwifi" ;
                       editText.setText(openwifi);

                   }
               });


               closewifi.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                        wifiScanSwitch.closeWifi();
                       String  closewifi  =  "closewifi" ;
                       editText.setText(closewifi);

                   }
               });


       }





      private   void   initVariable ()  {

          searchWifiListener  =   new WifiScanSwitch.SearchWifiListener() {
              @Override
              public void onSearchWifiFailed(WifiScanSwitch.ErrorType errorType) {

                Log.v(TAG,errorType.toString());

                 editText.setText(errorType.toString());

              }

              @Override
              public void onSearchWifiSuccess(List<String> results) {

                        int  i  =0  ;
                        StringBuilder   builder   =   new StringBuilder() ;

                       for  (String s: results)    {

                           String  res  =   i+":"+s  ;
                         builder.append(res);
                         builder.append('\n');

                           i++;

                       }

                       editText.setText(builder.toString());


              }

          }  ;

        wifiScanSwitch  =   new WifiScanSwitch(getApplicationContext(), searchWifiListener) ;

        wifiSleepingSwitch  = new WifiSleepingSwitch()  ;

      }




















}
