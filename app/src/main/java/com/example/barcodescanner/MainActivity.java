package com.example.barcodescanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.felhr.usbserial.UsbSerialDevice;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    BluetoothSocket btSocket = null;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Button scanBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Bluetooth stuff
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice hc05 = btAdapter.getRemoteDevice("00:18:91:D8:3B:DC"); // Specific MAC address to my Bt device

        //Making the connection

        int counter = 0;
        do {
            try {
                btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                System.out.println(btSocket);
                btSocket.connect();
                System.out.println(btSocket.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
        } while (!btSocket.isConnected() && (counter < 3));

        //Sending a test signal
        try {
            OutputStream outputStream = btSocket.getOutputStream();
            outputStream.write('A');
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Closing the connection
//        try {
//            btSocket.close();
//            System.out.println(btSocket.isConnected());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //~~ Making the status bar green
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.green));
        }


        //~~~ making the app theme green
        
        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0F9D58"));
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        //Making a serial connection to the arduino.
//        UsbDevice device = null;
//        UsbDeviceConnection usbConnection = null;
//        UsbSerialDevice serial = UsbSerialDevice.createUsbSerialDevice(device, usbConnection);

        scanBtn = findViewById(R.id.scanBt);
        scanBtn.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {


        ScanCode();
    }

    private void ScanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Scanning Result");
                builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ScanCode();
                    }
                }).setNegativeButton("Open Bin", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //!! (Missing stuff) check What kind of bin To Open
                        //int binNum = findRecycleBinViaBarcode(result.getContents());

                        //!! (Missing stuff) Send command to arduino to open the can
                        openBin('A');
                        System.out.println("Sending letter%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                Toast.makeText(this, "No result", Toast.LENGTH_LONG).show();

            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void openBin(char a) {
        //Bluetooth stuff
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice hc05 = btAdapter.getRemoteDevice("00:18:91:D8:3B:DC"); // Specific MAC address to my Bt device

        //Making the connection
//        BluetoothSocket btSocket = null;
//        int counter = 0;
//        do {
//            try {
//                btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
//                System.out.println(btSocket);
//                btSocket.connect();
//                System.out.println(btSocket.isConnected());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            counter++;
//        } while (!btSocket.isConnected() && (counter < 3));

        try {
            OutputStream outputStream = btSocket.getOutputStream();
            outputStream.write('A');
        } catch (IOException e) {
            e.printStackTrace();
        }


//        try {
//            btSocket.close();
//            System.out.println(btSocket.isConnected());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }
}