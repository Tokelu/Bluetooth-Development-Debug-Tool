package da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import da.au_grp21.bluetoothdevelopmentdebugtool.R;

public class MyViewModel extends ViewModel {

    private MutableLiveData<String> devices;
    //TODO: private ArrayList<Device> items;
    //TODO: private MutableLiveData<List<Device>> numItems;
    private MutableLiveData<List<String>> numItems;
    private ArrayList<String> items;
    private String file = null;
    private boolean connect = false;
    private boolean disconneted = true;

    private boolean isSearchingForDevices = false;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList deviceList;
    private ArrayAdapter arrayAdapter;


    // TODO: Is our devices a string, or an obj?
    public LiveData<String> getDevices() {
        // public LiveData<Device> getDevices() {
        if (devices == null) {
            devices = new MutableLiveData<String>();
            // devices = new MutableLiveData<Device>();

        }
        return devices;
    }

    // TODO: Is our devices a string, or an obj?
    public LiveData<String> getAllDevices() {
        if (numItems == null) {
            numItems = new MutableLiveData<List<String>>();
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

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceList);
        recyclerView =   //findViewById(R.id.fragConnectRecyclerView);
        recyclerView.setAdapter(arrayAdapter);





        if (!isSearchingForDevices){
            bluetoothAdapter.startLeScan(btScanCallback);
            isSearchingForDevices = !isSearchingForDevices;
        }

    }

    //TODO: chose save location:
    public void locationToSave(String locationToSave) {
        String fileToSave = getFileToSave();
        saveToDatabase(fileToSave, locationToSave);
    }

    //TODO: file to save
    public void saveFile(String fil) {
        file = fil;

    }

    //TODO: get the file
    public String getFileToSave() {
        return file;
    }

    // TODO: save the data to the database
    public void saveToDatabase(String fileToSave, String saveFileHere) {


    }

    // TODO: disconnect the device
    public boolean getdisconnect() {

        return disconneted;
    }

    // TODO: disconnect the device
    public void setdisconnect(boolean disconnet) {
        disconneted = disconnet;

    }

    // TODO: connect the device
    public boolean getconnect() {
        return connect;
    }

    // TODO: connect the device
    public void setconnect(boolean conneted) {
        connect = conneted;
    }

    // TODO: check it a device is conneted before going from the main frag to termial frag
    public void terminal() {

    }
   /* // TODO: disconnect the device
    public String getDisconnectDevice() {

        return disconnetedDevise;
    }

    // TODO: disconnect the device
    public void setDisconnectDevise(String disconnet) {
        disconnetedDevise = disconnet;

    }

    // TODO: connect the device
    public String  getConnectDevice() {
        return connetDevice;
    }

    // TODO: connect the device
    public void setConnectDevice(String conneted) {
        connetDevice = conneted;
    }
    */

    // TODO: used in main activy, must load the new view I goes
    public void loadNewData() {
        numItems = new MutableLiveData<List<String>>();
        items = new ArrayList<String>();
        numItems.setValue(items);

    }
        // inspiration: https://bit.ly/2OOVepH
    private BluetoothAdapter.LeScanCallback btScanCallback = new BluetoothAdapter.LeScanCallback(){
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes){
            if (bluetoothDevice != null){
                if (!deviceList.contains(bluetoothDevice.getAddress())){
                    deviceList.add(bluetoothDevice.getAddress());
                }
                else {
                    deviceList.remove(bluetoothDevice.getAddress());
                    deviceList.add(bluetoothDevice.getAddress());

                }
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };
}

