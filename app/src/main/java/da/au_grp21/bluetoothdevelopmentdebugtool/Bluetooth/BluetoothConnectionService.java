package da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;
import android.widget.ListView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import da.au_grp21.bluetoothdevelopmentdebugtool.Device.Device;
import da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel.MyViewModel;

public class BluetoothConnectionService extends IntentService{

    //change for branching

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private ArrayList deviceList;
//    private LiveData mutableLiveData;
    private MutableLiveData<Device> devices;
    private String deviceAddress = "cc:42:32:9D:49:f6";

    public static final UUID TX_CHAR_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");    //  Nordic UART RX Characteristic: https://developer.nordicsemi.com/nRF_Connect_SDK/doc/latest/nrf/include/bluetooth/services/nus.html
    public static final UUID RX_CHAR_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");    //  Nordic UART RX Characteristic
    public static final UUID RX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e"); //  Nordic UART RX Service

    public final static String ACTION_DATA_AVAILABLE = "da.au_grp21.bluetoothdevelopmentdebugtool.ACTION_DATA_AVAILABLE";
    public static String EXTRA_DATA = "da.au_grp21.bluetoothdevelopmentdebugtool.EXTRA_DATA";




    public BluetoothConnectionService(){super("BluetoothConnectionService");}

    @Override
    public void onCreate(){
        super.onCreate();
        deviceList = new ArrayList();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startLeScan(btScanCallback);
    }

    @Override
    public void onHandleIntent(@Nullable Intent intent){

    }

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

    public void Connect(){
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        bluetoothGatt = device.connectGatt(this, true, gattCallback);

    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback()
    {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
        {
            Intent intentAction = new Intent();

                //  If Device is connected
            if (newState == BluetoothProfile.STATE_CONNECTED){

                // TODO: DO STUFF WHEN CONNECTED

            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status){
            // Still connected to the Service
            if (status == BluetoothGatt.GATT_SUCCESS){
                // Send Characteristic via broardcast update
                BroadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status){
            super.onServicesDiscovered(gatt, status);

            List<BluetoothGattService> services = gatt.getServices();
            ArrayList<BluetoothGattCharacteristic> characteristics;
            List<BluetoothGattCharacteristic> gattCharacteristics;
            String uuid;

            for (BluetoothGattService gattService : services){
                gattCharacteristics = gattService.getCharacteristics();
                characteristics = new ArrayList<>();

                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics ){
                    characteristics.add(gattCharacteristic);
                    uuid = gattCharacteristic.getUuid().toString();
                        //  BLE data Notify

                    if (uuid.equals("0000fff4-0000-1000-8000-00805f9b34fb")){
                        final int characteristicsProps = gattCharacteristic.getProperties();
                        if ((characteristicsProps | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0){
                            BluetoothGattCharacteristic NotifyCharacteristic = gattCharacteristic;
                            setCharacteristicNotification(gattCharacteristic, true);
                        }
                    }
                }
            }
        }


//        public void setCharacteristicNotification(final BluetoothGattCharacteristic characteristic,
//                                                  boolean enabled) {
//            if (bluetoothAdapter == null || bluetoothGatt == null) {
//                return;
//            }
//            bluetoothGatt.setCharacteristicNotification(characteristic, enabled);
//
//            Log.e("uuid service", characteristic.getUuid() + "");
//            String uuid = "0000fff2-0000-1000-8000-00805f9b34fb";
//
//            if (uuid.equals(characteristic.getUuid().toString())) {
//                Log.e("uuid service2", characteristic.getUuid() + "");
//                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
//                        UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
//                if (descriptor != null) {
//                    descriptor.setValue(enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : new byte[]{0x00, 0x00});
//                    bluetoothGatt.writeDescriptor(descriptor);
//                }
//            }
//        }


        public void setCharacteristicNotification(final BluetoothGattCharacteristic characteristic, Boolean isEnabled){
            if (bluetoothAdapter == null || bluetoothGatt == null){
                return;
            }
            bluetoothGatt.setCharacteristicNotification(characteristic, isEnabled);

            Log.e("UUID Service 1", characteristic.getUuid().toString() + "");
                //  BLE Read Time
            String uuid = "0000fff2-0000-1000-8000-00805f9b34fb";

            if (uuid.equals(characteristic.getUuid().toString())){
                Log.e("UUID Service 2:", characteristic.getUuid().toString()+ "");
//                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
//
//
//                if (descriptor != null) {
//                    descriptor.setValue(isEnabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : new byte[]{0x00, 0x00});
//                    bluetoothGatt.writeDescriptor(descriptor);
//                }
            }
        }
    };

    // Get the serial data out of the characteristic.
    private void BroadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic){
        // Looking for the correct characteristic
        final Intent intent = new Intent(action);
        if (TX_CHAR_UUID.equals(characteristic.getUuid())){
            //  Extract serial data from stream - and transmit via local broadcast manager
            intent.putExtra(EXTRA_DATA, characteristic.getValue());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }


    }

    public class LocalBinder extends Binder {
        BluetoothConnectionService getService() {return BluetoothConnectionService.this;}
        }




}
