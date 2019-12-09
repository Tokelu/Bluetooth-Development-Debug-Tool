package da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.IBinder;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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

import static da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth.BluetoothConnectionService.ACTION_DATA_AVAILABLE;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth.BluetoothConnectionService.ACTION_GATT_CONNECTED;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth.BluetoothConnectionService.ACTION_GATT_DISCONNECTED;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth.BluetoothConnectionService.ACTION_GATT_SERVICES_DISCOVERED;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth.BluetoothConnectionService.DEVICE_DOES_NOT_SUPPORT_UART;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth.BluetoothConnectionService.EXTRA_DATA;
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

import da.au_grp21.bluetoothdevelopmentdebugtool.MainActivity;
import da.au_grp21.bluetoothdevelopmentdebugtool.R;

public class MyViewModel extends ViewModel {

    private final static String TAG = MyViewModel.class.getSimpleName();

    private MutableLiveData<Device> devices;
    private ArrayList<Device> items;
    private MutableLiveData<List<Device>> numItems;
    private Device currentDevice;

    //Fields for the list of logs used in FragmentLoad
    private ArrayList<LogData> logList = new ArrayList<>();
    private MutableLiveData<List<LogData>> logs;
    private LogData chosenLog = null;

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

    public LiveData<List<Device>> getAllDevices() {
        numItems = MainActivity.service.getDevices();
        if (numItems == null) {
            numItems = new MutableLiveData<>();
        }
        return numItems;
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
    }

    public void disconnectDevice() {

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


    public void fetchData( /* Karakteristik fra BLE */ ){

    }

    public void setConnectedStatus(/* Karakteristik fra BLE */) {


    }

    public BroadcastReceiver onBluetoothChange = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {


                case ACTION_GATT_SERVICES_DISCOVERED:   //  informerer om at der er fundet BLE enheder

                case ACTION_GATT_CONNECTED:     //  Denne her informerer blot om at vi er connected.

                case ACTION_GATT_DISCONNECTED:  //  Denne her informerer blot om at vi er connected.

                case ACTION_DATA_AVAILABLE:     //  Informerer om at data er klar fra BLE
//                    fetchData(intent.getAction());

                case EXTRA_DATA:                //  Indeholder data
//                    readDataFromBle();

                case DEVICE_DOES_NOT_SUPPORT_UART:  //  I tilfælde af at der forbindes til en enhed der har annonceret at den har uart men alligevel ikke har det.

            }

        }
    };

    public BroadcastReceiver onDatabaseResponse = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case SINGLE_BROADCAST:
                    LogData extractData = (LogData) intent.getSerializableExtra(RETURN_LOG);
                    logList.add(extractData);
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

