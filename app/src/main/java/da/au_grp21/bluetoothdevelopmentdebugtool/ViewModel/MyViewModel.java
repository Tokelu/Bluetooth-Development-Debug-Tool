package da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService;

import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.LOG_DATA;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.LOG_NAME;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.SAVE;

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
//    public void locationToSave(String locationToSave) { //This might be redundant
//        String fileToSave = getFileToSave();
//        saveToDatabase(fileToSave, locationToSave);
//    }

    //TODO: file to save
    public void saveFile(String fil) {
        file = fil;

    }

    //TODO: get the file
    public String getFileToSave() {
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

    // TODO: -- no need -- check it a device is conneted before going from the main frag to termial frag
    public void terminal() {

    }

    public void help() {
    }

  /* // TODO: disconnect the device
    public String getDisconnectDevice() {

        return disconnetedDevise;
    }

    // TODO: disconnect the device
    public void setDisconnectDevise(String disconnet) {
        setdisconnect(true);
        setconnect(false);
        disconnetedDevise = disconnet;

    }

    // TODO: connect the device
    public String  getConnectDevice() {
        return connetDevice;
    }

    // TODO: connect the device
    public void setConnectDevice(String conneted) {
        setconnect(true);
        setdisconnect(false);
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

