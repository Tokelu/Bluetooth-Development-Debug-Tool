package da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.ListView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import da.au_grp21.bluetoothdevelopmentdebugtool.Device.Device;
import da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel.MyViewModel;

public class BluetoothConnectionService extends Service{ //IntentService {

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //change for branching
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private final static String TAG = BluetoothConnectionService.class.getSimpleName();
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private BluetoothManager bluetoothManager;

    private ArrayList deviceList;
//    private LiveData mutableLiveData;
    private MutableLiveData<Device> devices;
//    private String bluetoothDeviceAddress = "cc:42:32:9D:49:f6";
    private String bluetoothDeviceAddress;

    public static final UUID TX_CHAR_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");    //  Nordic UART TX Characteristic: https://developer.nordicsemi.com/nRF_Connect_SDK/doc/latest/nrf/include/bluetooth/services/nus.html
    public static final UUID RX_CHAR_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");    //  Nordic UART RX Characteristic
    public static final UUID RX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e"); //  Nordic UART RX Service
    public static final UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");            //  https://www.oreilly.com/library/view/getting-started-with/9781491900550/ch04.html

//    public final static String ACTION_DATA_AVAILABLE = "da.au_grp21.bluetoothdevelopmentdebugtool.ACTION_DATA_AVAILABLE";
//    public static String EXTRA_DATA = "da.au_grp21.bluetoothdevelopmentdebugtool.EXTRA_DATA";

    public final String ACTION_GATT_CONNECTED = "com.nordicsemi.nrfUART.ACTION_GATT_CONNECTED"; // help and inspiration taken from https://github.com/NordicPlayground/Android-nRF-UART
    public final static String ACTION_GATT_DISCONNECTED = "com.nordicsemi.nrfUART.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.nordicsemi.nrfUART.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.nordicsemi.nrfUART.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.nordicsemi.nrfUART.EXTRA_DATA";
    public final static String DEVICE_DOES_NOT_SUPPORT_UART = "com.nordicsemi.nrfUART.DEVICE_DOES_NOT_SUPPORT_UART";

    private int connectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING   = 1;
    private static final int STATE_CONNECTED    = 2;


//    public BluetoothConnectionService(){super("BluetoothConnectionService");}

    @Override
    public void onCreate(){
        super.onCreate();
        //  kindly borrowed from https://developer.android.com/training/permissions/requesting#java
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            //TODO: some kind of message to the user that we do not have bluetooth permissions.

            //TODO: some method to ask user for permissions
            // getPermissions() {}
        }
        deviceList = new ArrayList();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startLeScan(btScanCallback);
    }

//    @Override
//    public void onHandleIntent(@Nullable Intent intent){
//
//    }

    // inspiration: https://bit.ly/2OOVepH
    private BluetoothAdapter.LeScanCallback btScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            Device device = new Device();



            if (bluetoothDevice != null) {

                // When scanning, check if device list contains a device with a specific mac address and add it if it does not.
                if (deviceList.contains((bluetoothDevice.getAddress())) == deviceList.contains(device.getMac())) {
                    device.setMac(bluetoothDevice.getAddress());
                    device.setName(bluetoothDevice.getName());
                    device.setConnected(false);

                    devices.postValue(device);
//                    deviceList.add(device);
                }
                // if device is no longer in scanning range, remove device from list.
                else {
                    deviceList.remove(device.getMac());
                    deviceList.add(device.getMac());
                }
//                arrayAdapter.notifyDataSetChanged();
            }
        }
    };

//        //  This is the connection method
//    public void Connect(){
//        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
//        bluetoothGatt = device.connectGatt(this, true, gattCallback);


        //  This is a callback method for gatt event that we're looking for (like services discovered, and connection change)
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                String intentAction;

                //  If Device is connected
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    intentAction = ACTION_GATT_CONNECTED;
                    connectionState = STATE_CONNECTED;  //  setting our state to connected
                    broadcastUpdate(intentAction);  //  broardcasting that we're connected
                    //  Logging connection status and discovered services
                    Log.i(TAG, "Connected to gatt server");
                    Log.i(TAG, "Trying to start discovery of available services: " + bluetoothGatt.discoverServices());
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    intentAction = ACTION_GATT_DISCONNECTED;
                    connectionState = STATE_DISCONNECTED;   //  setting our state to disconnected
                    //  Logging connection status and broadcasting
                    Log.i(TAG, "Disconnected from gatt server");
                }
            }

            @Override
            //  For discovered services
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.i(TAG, "bluetoothGatt = " + bluetoothGatt);
                    broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                } else {
                    Log.i(TAG, "onServicesDiscovered received: " + status);
                }
            }

            @Override
            //  For updates on charateristics. (i.e. the data we need from the device.)
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic); //  broadcasting the data
                }
            }

            @Override
            //  For broadcasting that new data is ready.
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        };




        //  Broadcaster for intent actions like connected / disconnected etc.
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

        //  Broardcaster for data from BLE device
        //  https://www.bluetooth.com/wp-content/uploads/Sitecore-Media-Library/Gatt/Xml/Characteristics/org.bluetooth.characteristic.string.xml
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        if (TX_CHAR_UUID.equals(characteristic.getUuid())) {

                // logging received data to check if its the right data we get.
            Log.d(TAG, String.format("Received TX: %d",characteristic.getValue() ));
            intent.putExtra(EXTRA_DATA, characteristic.getValue());
        }
        else {}
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

        //  Returns List of services provided by the gatt server (the device) we're connected to.
    public List<BluetoothGattService> getGattServices(){
        if (bluetoothGatt == null) {return null;}   //  If we have no Bluetooth gatt server, there is no service either.
        return bluetoothGatt.getServices();
    }




    private final IBinder binder = new LocalBinder();

    public IBinder onBind(Intent intent){
        return binder;
    }

    public boolean onUnbind(Intent intent){
        // when we're done with a device we need to release resources.  close() is invoked when the UI is disconnected from the Service and tends to this
        //TODO: close();
//        close();
        return super.onUnbind(intent);
    }

        //  the binder for the bluetooth service
    public class LocalBinder extends Binder{
        BluetoothConnectionService getService() {return BluetoothConnectionService.this;}
    }

//    public class LocalBinder extends Binder {
//        BluetoothConnectionService getService() {return BluetoothConnectionService.this;}
//    }

        //  Method for Initialization of the Bluetooth adapter, returns false if either the service or the adapter fails, true if success.
    public boolean initialize(){
        if (bluetoothManager == null){
            bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null){
                Log.e(TAG, "Initialization of the Bluetooth Manager failed.");
                return false;
            }
        }

        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothManager == null){
            Log.e(TAG, "Could not get Bluetooth Adapter");
            return false;
        }
        return true;
    }

        //  This is the method for connecting to a device - deviceAddress is a string containing a MAC address
    public boolean connect(final String deviceAddress){
        if (bluetoothAdapter == null){  //  Checking if we have an initialized adapter to connect through
            Log.i(TAG, "BluetoothAdapter is not initialized.");
            return false;
        }
        if (deviceAddress == null){ //  checking if an (MAC) address was given
            Log.i(TAG, "Unspecified MAC address.");
            return false;
        }

            //  Reconnecting to device we have had connection last
        if (bluetoothDeviceAddress != null && deviceAddress.equals(bluetoothDeviceAddress) && bluetoothGatt != null){
            Log.i(TAG, "Trying to reconnect using an existing bluetoothBatt.");
            if (bluetoothGatt.connect()){    // if connection is successfull
                connectionState = STATE_CONNECTING;
                return true;
            }
            else {
                return false;
            }
        }

            //  Checking if a given device can be found.
        final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        if (device == null){
            Log.i(TAG, "The specified Device was not found.");
            return false;
        }
            //  Connecting to the specified device.
            //  NOTE:  NOT sure if we want the autoConnect parameter to be true or false
        bluetoothGatt = device.connectGatt(this,false, gattCallback);
        Log.i(TAG, "Setting up connection");
        bluetoothDeviceAddress = deviceAddress;
        connectionState = STATE_CONNECTED;
        return true;
    }

        //  Method to disconnect from connected device.
    public void disconnect(){
        if (bluetoothGatt == null || bluetoothAdapter == null){
            Log.i(TAG, "No Bluetooth adapter, nothing to disconnect");
            return;
        }
        bluetoothGatt.disconnect();
    }

        //  Resource cleanup when device connection is no longer needed.
    public void close(){
        if (bluetoothGatt == null) {return;}    //  if we have no Bluetooth gatt there's nothing to clean
            Log.i(TAG, "Closing Bluetooth gatt.");
            bluetoothDeviceAddress = null;
            bluetoothGatt.close();
            bluetoothGatt = null;
    }
        //  Reading the characteristic
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (bluetoothAdapter == null || bluetoothGatt == null) {
                Log.w(TAG, "Nothing to read: BluetoothAdapter is not initialized");
                return;
        }
        bluetoothGatt.readCharacteristic(characteristic);
    }

    public void enableTXNotification() {

        BluetoothGattService RxService = bluetoothGatt.getService(RX_SERVICE_UUID);
        if (RxService == null) {
            Log.e(TAG, "Did not find Tx Service.");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        BluetoothGattCharacteristic TxChar = RxService.getCharacteristic(TX_CHAR_UUID);
        if (TxChar == null) {
            Log.e(TAG, "Did not find Tx Characteristic.");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        bluetoothGatt.setCharacteristicNotification(TxChar, true);
            //  I'm not entirely sure as to why we need the CCCD UUID, but several tutorials and helpsites prescribe its necessity: https://www.oreilly.com/library/view/getting-started-with/9781491900550/ch04.html
        BluetoothGattDescriptor descriptor = TxChar.getDescriptor(CCCD);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        bluetoothGatt.writeDescriptor(descriptor);
    }

    public void writeRXCharacteristic(byte[] value) {


        BluetoothGattService RxService = bluetoothGatt.getService(RX_SERVICE_UUID);
        Log.e(TAG, "BluetoothGatt null:" + bluetoothGatt);
        if (RxService == null) {
            Log.e(TAG, "Did not find Rx Service.");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        BluetoothGattCharacteristic RxChar = RxService.getCharacteristic(RX_CHAR_UUID);
        if (RxChar == null) {
            Log.e(TAG, "Did not find Rx Characteristic.");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        RxChar.setValue(value);
        boolean status = bluetoothGatt.writeCharacteristic(RxChar);

        Log.d(TAG, "write TXchar - status = " + status);
    }







}





























