package da.au_grp21.bluetoothdevelopmentdebugtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth.BluetoothConnectionService;
import da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel.MyViewModel;

import static android.content.Intent.CATEGORY_DEFAULT;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth.BluetoothConnectionService.ACTION_DATA_AVAILABLE;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth.BluetoothConnectionService.ACTION_GATT_CONNECTED;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth.BluetoothConnectionService.ACTION_GATT_DISCONNECTED;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth.BluetoothConnectionService.ACTION_GATT_SERVICES_DISCOVERED;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth.BluetoothConnectionService.DEVICE_DOES_NOT_SUPPORT_UART;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth.BluetoothConnectionService.EXTRA_DATA;


public class MainActivity extends AppCompatActivity
{
    public static BluetoothConnectionService service;
    private BluetoothAdapter bluetoothAdapter;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 42;

    MyViewModel vm;
    private int Bluetooth_Enable_Request = 1994;
    private Boolean bound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        //  Checking if we have permission for loacation_fine:
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CheckBluetoothPermissions();
        Intent intent = new Intent(this, BluetoothConnectionService.class);
        startService(intent);
        if (!bound) {
            bindToBackgroundService();
        }


        vm = ViewModelProviders.of(this).get(MyViewModel.class);
        if (savedInstanceState == null) {
            vm.loadNewData();
        }

    }

    @Override
    protected void onDestroy() {
        unbindService(bluetoothService);
        super.onDestroy();
    }


    private ServiceConnection bluetoothService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BluetoothConnectionService.LocalBinder binder = (BluetoothConnectionService.LocalBinder) iBinder;
            service = binder.getService();


        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            service.close();


        }
    };

    void bindToBackgroundService() {
        bindService(new Intent(this, BluetoothConnectionService.class), bluetoothService, Context.BIND_AUTO_CREATE);
        bound = true;
    }


    private void CheckBluetoothPermissions()
    {
        BluetoothAdapter bluetoothAdapterPermCheck = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapterPermCheck ==null){
            Toast.makeText(this, R.string.notSupported_Bluetooth, Toast.LENGTH_SHORT).show();
            finish();
        }
        else if (!bluetoothAdapterPermCheck.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(intent, Bluetooth_Enable_Request);
        }
    }

    public void fetchData(Intent intent){

//        intent.getAction()

//        service.readCharacteristic(intent);

    }


    public void setConnectedStatus(Intent intent) {
        if (intent.getAction() == ACTION_GATT_CONNECTED ){

            //TODO: set connected status to Connected
        }
        else if (intent.getAction() == ACTION_GATT_DISCONNECTED){
            //TODO: Set connected status to Not Connected.
        }



    }

    public void readDataFromBle(Intent intent){

        String bleDeviceData = intent.getExtras().toString();

        // for at teste kan vi sætte teksten manuelt:
//        bleDeviceData = "Log data would appear here";

    }



    public BroadcastReceiver onBluetoothChange = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {


                case ACTION_GATT_SERVICES_DISCOVERED:   //  informerer om at der er fundet BLE enheder

                case ACTION_GATT_CONNECTED:     //  Denne her informerer blot om at vi er connected.

                case ACTION_GATT_DISCONNECTED:  //  Denne her informerer blot om at vi er connected.
                    setConnectedStatus(intent);

                case ACTION_DATA_AVAILABLE:     //  Informerer om at data er klar fra BLE
                    fetchData(intent);

                case EXTRA_DATA:                //  Indeholder data
                    readDataFromBle(intent);

                case DEVICE_DOES_NOT_SUPPORT_UART:  //  I tilfælde af at der forbindes til en enhed der har annonceret at den har uart men alligevel ikke har det.

            }

        }
    };


}
