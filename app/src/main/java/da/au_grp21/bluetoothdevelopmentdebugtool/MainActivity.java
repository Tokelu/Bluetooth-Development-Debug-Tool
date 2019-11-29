package da.au_grp21.bluetoothdevelopmentdebugtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel.MyViewModel;

public class MainActivity extends AppCompatActivity
{
    MyViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vm = ViewModelProviders.of(this).get(MyViewModel.class);
        if (savedInstanceState == null) {
            vm.loadNewData();
        }
    }
}
