package da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {

    private MutableLiveData<String> devices = new MutableLiveData<>();

    public LiveData<String> getDevices() {
        if (devices == null) {
            devices = new MutableLiveData<String>();

        }
        return devices;
    }

    public void loadDevicesConneced() {
        //Do an asynchronous operation to fetch devices.
    }

    public void saveToDatabase() {

    }

    public void disconnect() {

    }

    public void terminal() {

    }
}

