package da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends ViewModel {

    private MutableLiveData<String> devices;
    //TODO: private ArrayList<Device> items;
    //TODO: private MutableLiveData<List<Device>> numItems;
    private MutableLiveData<List<String>> numItems;
    private ArrayList<String> items;
    private String file = null;
    private boolean connet = false;
    private boolean disconneted = true;

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
        return connet;
    }

    // TODO: connect the device
    public void setconnect(boolean conneted) {
        connet = conneted;
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
}

