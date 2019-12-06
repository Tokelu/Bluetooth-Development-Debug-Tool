package da.au_grp21.bluetoothdevelopmentdebugtool.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import da.au_grp21.bluetoothdevelopmentdebugtool.Device.Device;
import da.au_grp21.bluetoothdevelopmentdebugtool.R;
import da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel.MyViewModel;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link FragmentMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMain extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // LinearLayout FragMainLayoutHead, mainFragLayoutConnectionStatus, linearLayout2;
    TextView mainFragTextViewTitle, fragMainTextViewConnected, fragMainTextViewDevice, fragMainTextViewDeviceName;
    CheckBox connectionIndicator;
    //  View divider, divider2;


    //  private OnFragmentInteractionListener mListener;
    private MyViewModel vm;
    Button mainBtnTerminal, mainBtnDisconnet, mainBtnConDev, mainBtnHelp, mainBtnExit, mainBtnLoad;


    public FragmentMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMain.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMain newInstance(String param1, String param2) {
        FragmentMain fragment = new FragmentMain();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        fragMainTextViewConnected = v.findViewById(R.id.fragMainTextViewConnected);
        fragMainTextViewDevice = v.findViewById(R.id.fragMainTextViewDevice);
        fragMainTextViewDeviceName = v.findViewById(R.id.fragMainTextViewDeviceName);

        fragMainTextViewDevice.setText(getString(R.string.deviceListElementTextViewMac_address));

        mainBtnTerminal = v.findViewById(R.id.fragMainButtonTerminal);
        mainBtnTerminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vm.getconnection() == true) {
                    Navigation.findNavController(v).navigate(R.id.fragmentTerminalScr);
                } else
                    Navigation.findNavController(v).navigate(R.id.fragmentConnection);
            }
        });
        mainBtnDisconnet = v.findViewById(R.id.fragMainButtonDisconnect);
        mainBtnDisconnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Disconnect device, not move to a frag
                vm.setDeviceDisconnect();

            }
        });
        mainBtnConDev = v.findViewById(R.id.fragMainButtonConnectDevice);
        mainBtnConDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.fragmentConnection);
            }
        });
        mainBtnHelp = v.findViewById(R.id.fragMainButtonHelp);
        mainBtnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: how do we make the help thingy?
                vm.help();

            }
        });
        mainBtnExit = v.findViewById(R.id.fragMainButtonExit);
        mainBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

        mainBtnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.fragmentLoad);
            }
        });
        connectionIndicator = v.findViewById(R.id.fragMainConnectionIndicator);
        //TODO: How do we check this? By a

        connectionIndicator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //connectionIndicator.setText(isChecked ? getString(R.string.deviesIsConneted) : getString(R.string.deviesIsNotConneted));
            }
        });
        return v;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        vm.getDevices().observe(this, new Observer<Device>() {
            @Override
            public void onChanged(Device device) {
                fragMainTextViewConnected.setText(device.getConnected() ? getString(R.string.deviesIsConneted) : getString(R.string.deviesIsNotConneted));
                fragMainTextViewDevice.setText(getString(R.string.deviceListElementTextViewMac_address) + device.getMac());
                fragMainTextViewDeviceName.setText(device.getName());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
