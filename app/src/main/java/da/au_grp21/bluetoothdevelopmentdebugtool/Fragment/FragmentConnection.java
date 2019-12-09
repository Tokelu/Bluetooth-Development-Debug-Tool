package da.au_grp21.bluetoothdevelopmentdebugtool.Fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import da.au_grp21.bluetoothdevelopmentdebugtool.Device.Device;
import da.au_grp21.bluetoothdevelopmentdebugtool.Device.DeviceListAdapter;
import da.au_grp21.bluetoothdevelopmentdebugtool.MainActivity;
import da.au_grp21.bluetoothdevelopmentdebugtool.R;
import da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel.MyViewModel;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link FragmentConnection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentConnection extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button conBtnScan, conBtnBack;

    //Fields for the recycler view
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DeviceListAdapter myAdapter;

    private MyViewModel vm;

    public FragmentConnection() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_connection, container, false);
        conBtnScan = v.findViewById(R.id.fragConnectButtonScan);
        conBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.service.startLeScanWrapper();

            }
        });
        conBtnBack = v.findViewById(R.id.fragConnectButtonBack);
        conBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.fragmentMain);
            }
        });

        recyclerView = v.findViewById(R.id.fragConnectRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getActivity());

        myAdapter = new DeviceListAdapter();
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter.setClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                vm.ConnectToDevice(myAdapter.GetItem(position));
                Navigation.findNavController(v).navigate(R.id.fragmentTerminalScr);
            }
        });


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        vm.getAllDevices().observe(this, new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                myAdapter.SetDeviceList(devices);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

