package da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService;
import da.au_grp21.bluetoothdevelopmentdebugtool.Device.Device;

import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.LOG_DATA;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.LOG_NAME;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.SAVE;

import da.au_grp21.bluetoothdevelopmentdebugtool.R;

public class MyViewModel extends ViewModel {

    private MutableLiveData<Device> devices;
    //TODO:
    private ArrayList<Device> items;
    //TODO:
    private MutableLiveData<List<Device>> numItems;
    private Device currentDevice;

    private String file = null;
    //private boolean connect = false;
    //   private boolean disconneted = true;

    private boolean isSearchingForDevices = false;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList deviceList;
    private ArrayAdapter arrayAdapter;


    // TODO: Is our devices a string, or an obj?
    public LiveData<Device> getDevices() {
        if (devices == null) {
            devices = new MutableLiveData<Device>();
        }
        return devices;
    }

    // TODO: Is our devices a string, or an obj?
    public LiveData<Device> getAllDevices() {
        if (numItems == null) {
            numItems = new MutableLiveData<List<Device>>();
            if (items == null) {
                items = new ArrayList<>();
            }
            numItems.setValue(items);
        }
        return devices;
    }

    // TODO: this function is meant to o an asynchronous operation to fetch devices.
    public void loadDevicesConneced() {

        deviceList = new ArrayList();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceList);
        //recyclerView =   //findViewById(R.id.fragConnectRecyclerView);
        //recyclerView.setAdapter(arrayAdapter);


        if (!isSearchingForDevices) {
            bluetoothAdapter.startLeScan(btScanCallback);
            isSearchingForDevices = !isSearchingForDevices;
        }

    }

    //TODO: chose save location:
//    public void locationToSave(String locationToSave) { //This might be redundant
//        String fileToSave = getFileToSave();
//        saveToDatabase(fileToSave, locationToSave);
//    }

    //TODO: terminalData to member data
    public void saveTerminalDataInformation(String terminalData) {
        // file = terminalData;
        currentDevice.setData(terminalData);

    }

    //TODO: get the file
    public String getTerminalDataInformation() {
        return file;
    }

    // TODO: save the data to the database
    public void saveToDatabase(Context context, String fileName, String terminalData) {
        Intent log = new Intent(context, DatabaseService.class)
                .setAction(SAVE)
                .putExtra(LOG_NAME, fileName)
                .putExtra(LOG_DATA, terminalData);
        context.startService(log);
    }

    //TODO: get from database
    public void getFromDatabase() {

    }
   /* // TODO: disconnect the device
    public boolean getdisconnect() {

        return disconneted;
    }

    // TODO: disconnect the device
    public void setdisconnect(boolean disconnet) {
        disconneted = disconnet;

    }*/



    // TODO: -- no need -- check it a device is conneted before going from the main frag to termial frag
    public void terminal() {

    }

    public void help() {
    }

    // TODO: disconnect the device
    public void setDeviseDisconnect() {
        currentDevice.setConnected(false);
    }

    // TODO: connect the device
    public String getDeviceThatIsConnect() {
        return currentDevice.getName();
    }

    // TODO: connect the device
    public void setDeviceConnect() {
        currentDevice.setConnected(true);

    }

    public boolean getconnection() {
        return currentDevice.getConnected();
    }

    // TODO: used in main activy, must load the new view I goes
    public void loadNewData() {
        numItems = new MutableLiveData<List<Device>>();
        items = new ArrayList<Device>();
        numItems.setValue(items);

    }

    // inspiration: https://bit.ly/2OOVepH
    private BluetoothAdapter.LeScanCallback btScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            if (bluetoothDevice != null) {
                if (!deviceList.contains(bluetoothDevice.getAddress())) {
                    deviceList.add(bluetoothDevice.getAddress());
                } else {
                    deviceList.remove(bluetoothDevice.getAddress());
                    deviceList.add(bluetoothDevice.getAddress());

                }
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };
}

