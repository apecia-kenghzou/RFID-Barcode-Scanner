package com.uhf.uhfdemo;
import static com.uhf.uhfdemo.MyApp.ifRMModule;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import android.content.Context;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.os.Handler;


import com.google.gson.GsonBuilder;
import com.uhf.base.UHFManager;
import com.uhf.base.UHFModuleType;
import com.uhf.event.BackResult;
import com.uhf.event.BaseFragment;
import com.uhf.event.GetRFIDThread;
import com.uhf.event.OnKeyListener;
import com.uhf.util.MLog;
import com.google.gson.Gson;
import com.example.iscandemo.iScanInterface;
import android.os.IScanListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Objects;

public class LeftLeftFragment extends BaseFragment implements View.OnClickListener, BackResult, AdapterView.OnItemSelectedListener,OnKeyListener,IScanListener{
    private String[] tagNumber;
    private String readNumber;
    private String takeTime;

    private String epc_to_store;
    private TextView tag,TextProcess1,TextProcess2,TextProcess3,TextProcess4;

    private CheckBox checkBox1a,checkBox2,checkBox3,checkBox4;
    private Integer mode =0;
    // mode 0 == barcode
    // mode 1 == api
    // mode 2 == rfid
    private Button resetBtn;
    private iScanInterface miScanInterface;
    UHFManager uhfmanager = UHFManager.getUHFImplSigleInstance(UHFModuleType.SLR_MODULE);

    //Handle Reset button Function.
    public void handleReset (View view){
        System.out.println(("Reset!"));

    }

    private Handler handler = new Handler();
    public void startOrStopRFID() {
        boolean flag = !GetRFIDThread.getInstance().isIfPostMsg();

        int Temp = 0;
        boolean ifOpenFan = false;
        Runnable task;
        if (flag) {
            int labelNum = 0;
            if (UHFModuleType.SLR_MODULE == UHFManager.getType() && MyApp.if5100Module){
                MyApp.getMyApp().getUhfMangerImpl().slrInventoryModeSet(0);
            }
            Boolean i = MyApp.getMyApp().getUhfMangerImpl().startInventoryTag();

            long pauseTime = 3;
            long tempTime = pauseTime;
            long startTime = System.currentTimeMillis() - tempTime;
            long a = System.currentTimeMillis();

           // reducingPowerDissipation(true);
           // setVolumeTimer();
            //acquireWakeLock();
        } else {
            long b = System.currentTimeMillis();

            Boolean i = MyApp.getMyApp().getUhfMangerImpl().stopInventory();
            mode=0;
            checkBox3.setChecked(true);
           // reducingPowerDissipation(false);
           // cancelVolumeTimer();
            //releaseWakeLock();
        }
        GetRFIDThread.getInstance().setIfPostMsg(flag);
       // read_RFID.setText(flag ? R.string.stop_rfid : R.string.read_rfid);


    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tagNumber = new String[]{"Tags" + ":"};
        readNumber = "Tags/S" + ":";
        takeTime = "Time(ms)" + ":";
        LeftLeftFragment context = this;
        //miScanInterface = new iScanInterface(context.getContext());
        GetRFIDThread.getInstance().setBackResult(this);
        return inflater.inflate(R.layout.fragment_leftleft, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //TextProcess1.setText("Loading...")
        tag = view.findViewById(R.id.textTest);
        TextProcess1 = view.findViewById(R.id.TextProcess1);
        TextProcess2 = view.findViewById(R.id.TextProcess2);
        TextProcess3 = view.findViewById(R.id.TextProcess3);
        TextProcess4 = view.findViewById(R.id.TextProcess4);


        //CheckBox1.setChecked(true)
        checkBox1a = view.findViewById(R.id.checkBox1);
        checkBox2 = view.findViewById(R.id.checkBox2);
        checkBox3 = view.findViewById(R.id.checkBox3);
        checkBox4 = view.findViewById(R.id.checkBox4);

        resetBtn = view.findViewById(R.id.resetBtn);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 0;
                updateUI(4,true);
            }
        });

        //registerForContextMenu(view);
        Context context = requireContext();
        miScanInterface = new iScanInterface(context);
        miScanInterface.registerScan(this);
        miScanInterface.setOCREnable(true,0);
        miScanInterface.setAimLightMode(0);
        MyApp.getMyApp().getUhfMangerImpl().powerSet(6);
        miScanInterface.setOutputMode(1);
//        miScanInterface.setBarcodeEnable(0,true);
//
//        miScanInterface.setBarcodeEnable(1,false);
//        miScanInterface.setBarcodeEnable(2,false);
//        miScanInterface.setBarcodeEnable(3,false);
//        miScanInterface.setBarcodeEnable(4,false);
//        miScanInterface.setBarcodeEnable(6,false);
//        miScanInterface.setBarcodeEnable(48,false);
//        miScanInterface.setBarcodeEnable(9,false);
//        miScanInterface.setBarcodeEnable(10,false);
//        miScanInterface.setBarcodeEnable(11,false);
//        miScanInterface.setBarcodeEnable(12,false);
//        miScanInterface.setBarcodeEnable(13,false);
//        miScanInterface.setBarcodeEnable(19,false);
//        miScanInterface.setBarcodeEnable(20,false);
//        miScanInterface.setBarcodeEnable(17,true);
//        miScanInterface.setBarcodeEnable(15,true);
        boolean ifsucesss = uhfmanager.powerOn(); //Power on the module
        if (!ifsucesss) {
            MLog.e("powerOn failed\n");
        }
        SystemClock. sleep(3000);
        uhfmanager.readTagModeSet(0,0,0,0); //Set the returned data type
        uhfmanager.slrInventoryModeSet(3); //Set inventory mod
        uhfmanager.startInventoryTag(); //Start inventory
        boolean ifInventory = true;
        boolean finalIfInventory = ifInventory;
        new Thread(new Runnable() { //Start the thread to receive tag data
            @Override
            public void run() {
                while(finalIfInventory)
                    tagNumber = uhfmanager. readTagFromBuffer();
            }
        }).start();
        uhfmanager.stopInventory();//stop inventory
        ifInventory = false;
        MLog.e(Arrays.toString(tagNumber));

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        miScanInterface.unregisterScan(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void postResult(String[] tagData) {
        MLog.e("idata","Reading tag Data");
        String tid = tagData[0];//获取TID
        String epc = tagData[1]; //拿到EPC
        MLog.e("idata","tid = " + tid + " epc = " + epc);
        tag.setText(epc);
        updateUI(2,true);
      //  MLog.e(Arrays.toString(tagData));
    }

    @Override
    public void postInventoryRate(long rate) {

    }

    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {

       // startOrStopRFID();
        //MLog.e("powerOn = " + MyApp.getMyApp().getUhfMangerImpl().powerOn());
       // MyApp.getMyApp().getiScanInterface().scan_start();
//        if(mode==0) {
//            MyApp.getMyApp().getUhfMangerImpl().startInventoryTag();
//            mode=1;
//        }else{
//
//            MLog.e("stop scanning");
//            MyApp.getMyApp().getUhfMangerImpl().stopInventory();
//        }
        //MLog.e(Arrays.toString(tagNumber));
        if(mode ==0){
            //MLog.e("barcode scanning");
            miScanInterface.scan_start();

        }else if(mode ==1){
            MLog.e("idata","calling API");
            mode =2;
            String pfinfo ="{\"processed_pnr_id\":\"56RESO-2024-01-10\",\"products_air_segment_operating_flight_designator_carrier_code\":\"MH3023\"}";
            Gson gson = new Gson();
            PassengerFlightInfo jsonObject = gson.fromJson(pfinfo, PassengerFlightInfo.class);
            //PassengerFlightInfo jsonObject = new PassengerFlightInfo("56RESO-2024-01-10","MH3023");
            MLog.e(String.valueOf(jsonObject));
            // Access JSON object properties
            String pnr_date = jsonObject.getPnr();
            String flight = jsonObject.getFlightNo();
            String[] pnr_parts = pnr_date.split("-");
            MLog.e("idata",pnr_date);
            // Get the first part
            String pnr = pnr_parts[0];
            String showtextProcess2 = "pnr :"+pnr + "\nflight No:" + flight;
            TextProcess2.setText(showtextProcess2);
            epc_to_store = stringToHex(pnr+flight);
            MLog.e("idata",epc_to_store);
            // PassengerFlightInfo  a = new PassengerFlightInfo("asd,","asd");
            updateUI(1,true);
        } else if(mode ==2){
            MLog.e("idata","rfid scanning");
            boolean status = MyApp.getMyApp().getUhfMangerImpl().writeDataToEpc("000000", 2, 6, epc_to_store);
            String showText =(status ? getString(R.string.write_success) : getString(R.string.write_failed));
            TextProcess3.setText(showText);
            MLog.e("idata",showText);
            if(status){
                MLog.e("idata","reading RFID");
                //String result = MyApp.getMyApp().getUhfMangerImpl().readTag("000000", 0, 0, 0, "0", 1, 2, 6);
                String result = MyApp.getMyApp().getUhfMangerImpl().readTag("000000", 0, 0, 0, "0", 2, 0, 6);
                MLog.e("idata",result);
                updateUI(2, true);
                if (result!=null) {
                    String showTP4 = "Upload Successful :\n RFID ID :"+result;
                    TextProcess4.setText(showTP4);
                    mode = 3;
                    updateUI(3,true);
                }else{
                    String showTP4 = "Upload Unsuccessful ";
                    TextProcess4.setText(showTP4);
                }
                //mode=3;
            }

            //startOrStopRFID();

            //MLog.e(Arrays.toString(tagNumber));
           // mode =0;
        }else{
            mode=0;
            updateUI(4,true);
        }

    }

    @Override
    public void onKeyUp(int keyCode, KeyEvent event) {

        //miScanInterface.scan_stop();
    }
    @Override
    public void onScanResults(String s, int i, long l, long l1, String s1) {
        Log.d("idata", "data = " + s);
        Log.d("idata", "type = " + i);
        Log.d("idata", "scantime = " + l);
        Log.d("idata", "keytime = " + l1);
        Log.d("idata", "changing my checkbox to true = " + l1);
        Log.d("idata", "changing my checkbox to true = " + checkBox1a.isChecked());

        updateUI(0,true);
        TextProcess1.setText(s);
        mode =1;


        Log.d("idata", "changing my checkbox to true = " + checkBox1a.isChecked());
    }
    private void updateUI(Integer i,Boolean b) {
        //should have id + info to easily change the ui
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (i) {
                    case 0:
                        checkBox1a.setChecked(true);
                        break;
                    case 1:
                        checkBox2.setChecked(true);
                        break;
                    case 2:
                        checkBox3.setChecked(true);
                        break;
                    case 3:
                        checkBox4.setChecked(true);
                        break;
                    default:
                        checkBox1a.setChecked(false);
                        checkBox2.setChecked(false);
                        checkBox3.setChecked(false);
                        checkBox4.setChecked(false);
                        TextProcess1.setText("");
                        TextProcess2.setText("");
                        TextProcess3.setText("");
                        TextProcess4.setText("");
                        break;
                }
            }
        });
    }
    public static final String URL = "http://www.example.com/abc";
    public void callAPI(){
        OkHttpClient client = new OkHttpClient();

        // Replace the URL with your API endpoint
        String url = "https://api.example.com/data";

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String jsonData = responseBody.string();
                System.out.println("JSON Response:\n" + jsonData);
            } else {
                System.out.println("Response body is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String stringToHex(String input) {
        // Get byte array of the string
        byte[] bytes = input.getBytes();

        // Initialize StringBuilder to store hexadecimal representation
        StringBuilder hexString = new StringBuilder();

        // Iterate over each byte and convert to hexadecimal
        for (byte b : bytes) {
            // Convert byte to unsigned integer
            int unsignedByte = b & 0xff;
            // Convert integer to hexadecimal string
            String hex = Integer.toHexString(unsignedByte);
            // Append leading zero if necessary
            if (hex.length() == 1) {
                hexString.append('0');
            }
            // Append hexadecimal string to result
            hexString.append(hex);
        }

        // Return the hexadecimal representation
        return hexString.toString();
    }


}
