package da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.content.BroadcastReceiver;
import android.content.BroadcastReceiver;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth.BluetoothConnectionService;
import da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService;
import da.au_grp21.bluetoothdevelopmentdebugtool.Database.LogData;
import da.au_grp21.bluetoothdevelopmentdebugtool.Device.Device;

import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.DATE;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.FIND_BY_DATE;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.FIND_BY_NAME;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.LIST_BROADCAST;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.LOAD;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.LOG_DATA;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.LOG_NAME;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.RETURN_LOG;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.RETURN_LOG_LIST;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.SAVE;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.SINGLE_BROADCAST;

import da.au_grp21.bluetoothdevelopmentdebugtool.Device.DeviceListAdapter;
import da.au_grp21.bluetoothdevelopmentdebugtool.Fragment.FragmentTerminalScr;

import da.au_grp21.bluetoothdevelopmentdebugtool.R;

public class MyViewModel extends ViewModel {

    private final static String TAG = MyViewModel.class.getSimpleName();

    private MutableLiveData<Device> devices;
    private ArrayList<Device> items;
    private MutableLiveData<List<Device>> numItems;
    private Device currentDevice;
    BluetoothConnectionService bluetoothConnectionService;

    //Fields for the list of logs used in FragmentLoad
    private ArrayList<LogData> logList;
    private MutableLiveData<List<LogData>> logs;
    private LogData chosenLog;

    public LogData getChosenLog() {
        return chosenLog;
    }

    public void setChosenLog(LogData chosenLog) {
        this.chosenLog = chosenLog;
    }

    private String file = null;
    private boolean connect = false;
    //   private boolean disconneted = true;

    private boolean isSearchingForDevices = false;

    public LiveData<List<LogData>> getLogs() {
        if (logs == null) {
            logs = new MutableLiveData<>();
        }
        return logs;
    }


    public LiveData<Device> getDevice() {
        if (devices == null) {
            devices = new MutableLiveData<Device>();
        }
        return devices;
    }

    // TODO:
    public LiveData<List<Device>> getAllDevices() {
        if (numItems == null) {
            numItems = new MutableLiveData<List<Device>>();
            if (items == null) {
                items = new ArrayList<>();
            }
            numItems.setValue(items);//setValue?
        }
        return numItems;
    }


    // TODO: this function is meant to o an asynchronous operation to fetch devices.
    // Please make it return the list of devices
    public void loadDevicesConneced() {

        BluetoothConnectionService bluetoothConnectionService = new BluetoothConnectionService();

        if (!isSearchingForDevices) {
            if (!bluetoothConnectionService.initialize()) {
                Log.i(TAG, "Initializing Bluetooth adapter");
                bluetoothConnectionService.initialize();
            }

            //NOTE: Not done yet


            isSearchingForDevices = !isSearchingForDevices;
        }


    }

    //TODO: chose save location: This might(WILL) be redundant
//    public void locationToSave(String locationToSave) { //
//        String fileToSave = getFileToSave();
//        saveToDatabase(fileToSave, locationToSave);
//    }

    //TODO: terminalData to member data
    public void saveTerminalDataInformation(String terminalData) {
        // file = terminalData;
        currentDevice.setData(terminalData);

    }

    // Save the data to the database
    public void saveToDatabase(Context context, String fileName, String terminalData) {
        Intent log = new Intent(context, DatabaseService.class)
                .setAction(SAVE)
                .putExtra(LOG_NAME, fileName)
                .putExtra(LOG_DATA, terminalData);
        context.startService(log);
    }

    //This function sets the device attribut connected to false
    public void setDeviceDisconnect() {
        currentDevice.setConnected(false);
    }

    //This function gets the device
    public Device getDeviceThatIsConnect() {
        return currentDevice;
    }

    //This function sets the device attribut connected to true
    public void setDeviceConnect() {
        currentDevice.setConnected(true);
    }

    //Sets the input item as the current connected device
    public void ConnectToDevice(Device device) {
        currentDevice = device;
        setDeviceConnect();
        bluetoothConnectionService.connect(device.getMac());
    }

    //This function disconnects the bluetooth device
    public void disconnectDevice() {
        bluetoothConnectionService.disconnect();
        setDeviceDisconnect();
    }

    //This function gets the device attribut connected
    public boolean getconnection() {
        return currentDevice.getConnected();
    }

    // TODO: used in main activy, must load the new view I goes
    public void loadNewData() {
        numItems = new MutableLiveData<List<Device>>();
        items = new ArrayList<Device>();
        numItems.setValue(items);
    }

    // //This function finds the old logs in the database
    public void seachForOldData(Context context, String searchString) {
        Date mightBe = LogData.sdf.parse(searchString, new ParsePosition(0));
        if (mightBe != null) {
            Intent retriever = new Intent(context, DatabaseService.class)
                    .setAction(FIND_BY_DATE)
                    .putExtra(DATE, searchString);
            context.startService(retriever);
        } else {
            Intent retriever = new Intent(context, DatabaseService.class)
                    .setAction(FIND_BY_NAME)
                    .putExtra(LOG_NAME, searchString);
            context.startService(retriever);
        }
    }

    public Boolean chechIfDataIsSaved() {
        return currentDevice.getSave();
    }

    public void SetDeviceSaved() {
        currentDevice.setSave(true);
    }

    //This function close the bluetooth
    public void closeBluetooth() {
        bluetoothConnectionService.close();
    }

    //TODO:
    // This function will return the terminal output from the bluetooth device
    public String broadcastCharcteristic() {
        String hej = "hej";
        return hej;
    }



    public BroadcastReceiver onDatabaseResponse = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case SINGLE_BROADCAST:
                    logList.add((LogData) intent.getSerializableExtra(RETURN_LOG));
                    logs.setValue(logList);
                    break;
                case LIST_BROADCAST:
                    logList.addAll((List<LogData>) intent.getSerializableExtra(RETURN_LOG_LIST));
                    logs.setValue(logList);
                    break;
            }
        }
    };

}

