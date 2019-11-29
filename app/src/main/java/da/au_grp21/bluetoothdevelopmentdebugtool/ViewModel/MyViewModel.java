package da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends ViewModel {

    private MutableLiveData<String> devices = new MutableLiveData<>();

    // TODO: Is our devices a string, or an obj?
    public LiveData<String> getDevices() {
        if (devices == null) {
            devices = new MutableLiveData<String>();

        }
        return devices;
    }

    // TODO: this function is meant to o an asynchronous operation to fetch devices.
    public void loadDevicesConneced() {

    }

    // TODO: save the data to the database
    public void saveToDatabase() {

    }

    // TODO: disconnect the device
    public void disconnect() {

    }

    // TODO: connect the device
    public void connect() {

    }

    // TODO: check it a device is conneted before going from the main frag to termial frag
    public void terminal() {

    }

    // TODO: used in main activy, must load the new view I goes
    public void loadNewData() {

    }
}

