package da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService;
import da.au_grp21.bluetoothdevelopmentdebugtool.Device.Device;

import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.LOG_DATA;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.LOG_NAME;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.SAVE;

public class MyViewModel extends ViewModel {

    private MutableLiveData<Device> devices;
    //TODO:
    private ArrayList<Device> items;
    //TODO:
    private MutableLiveData<List<Device>> numItems;
    private Device currentDevice;

    private String file = null;
    private boolean connet = false;
    //   private boolean disconneted = true;

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
    // Please make it return the list of devices
    public void loadDevicesConneced() {

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
    public void setDeviceDisconnect() {
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

    //Sets the input item as the current connected device
    public void ConnectToDevice(Device device){
        currentDevice = device;
        setDeviceConnect();
    }


    // TODO: used in main activy, must load the new view I goes
    public void loadNewData() {
        numItems = new MutableLiveData<List<Device>>();
        items = new ArrayList<Device>();
        numItems.setValue(items);
    }
}

