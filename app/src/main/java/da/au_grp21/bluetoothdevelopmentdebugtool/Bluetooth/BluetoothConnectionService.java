package da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import java.util.ArrayList;

import javax.annotation.Nullable;

import da.au_grp21.bluetoothdevelopmentdebugtool.Device.Device;

public class BluetoothConnectionService extends IntentService{


    private BluetoothAdapter bluetoothAdapter;
    private ArrayList deviceList;


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

//                    devices.postValue(device);
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

}
