package da.au_grp21.bluetoothdevelopmentdebugtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import da.au_grp21.bluetoothdevelopmentdebugtool.Bluetooth.BluetoothConnectionService;
import da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel.MyViewModel;

import static android.content.Intent.CATEGORY_DEFAULT;


public class MainActivity extends AppCompatActivity
{
    private BluetoothAdapter bluetoothAdapter;

    MyViewModel vm;
    private int Bluetooth_Enable_Request = 1994;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckBluetoothPermissions();
        Intent intent = new Intent(this, BluetoothConnectionService.class);
        startService(intent);


        vm = ViewModelProviders.of(this).get(MyViewModel.class);
        if (savedInstanceState == null) {
            vm.loadNewData();
        }
    }

    private void CheckBluetoothPermissions()
    {
        BluetoothAdapter bluetoothAdapterPermCheck = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapterPermCheck ==null){
            Toast.makeText(this, R.string.notSupported_Bluetooth, Toast.LENGTH_SHORT).show();
            finish();
        }
        else if (!bluetoothAdapterPermCheck.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(intent, Bluetooth_Enable_Request);
        }
    }
}
