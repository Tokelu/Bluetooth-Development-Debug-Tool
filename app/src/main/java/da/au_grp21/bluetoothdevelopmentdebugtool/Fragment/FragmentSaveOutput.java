package da.au_grp21.bluetoothdevelopmentdebugtool.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import da.au_grp21.bluetoothdevelopmentdebugtool.Device.Device;
import da.au_grp21.bluetoothdevelopmentdebugtool.Help.myToast;
import da.au_grp21.bluetoothdevelopmentdebugtool.R;
import da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel.MyViewModel;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * Use the {@link FragmentSaveOutput#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSaveOutput extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView saveOutputTxt;
    TextView saveOutputTxtExtension;
    EditText saveOutputEditTxt;
    Button saveOutputBtnBack;
    Button saveOutputBtnSave;
    private myToast testToast;
    private MyViewModel vm;

    public FragmentSaveOutput() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSaveOutput.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSaveOutput newInstance(String param1, String param2) {
        FragmentSaveOutput fragment = new FragmentSaveOutput();
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
        View v = inflater.inflate(R.layout.fragment_save_output, container, false);
        saveOutputEditTxt = v.findViewById(R.id.fragSaveOutputEditText);
        saveOutputBtnBack = v.findViewById(R.id.fragSaveOutputButtonBack);
        saveOutputBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.fragmentTerminalScr);
            }
        });
        saveOutputBtnSave = v.findViewById(R.id.fragSaveOutputButtonSave);
        saveOutputBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Where to go? how do we do this?
                testToast.showToast(FragmentSaveOutput.this.getActivity(), R.string.dataSaveOnDatabase);
                vm.saveToDatabase(FragmentSaveOutput.this.getActivity(), saveOutputEditTxt.getText().toString(), vm.getDeviceThatIsConnect().getData());
                vm.SetDeviceSaved();
                Navigation.findNavController(v).navigate(R.id.fragmentTerminalScr);

            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        vm.getDevice().observe(this, new Observer<Device>() {
            @Override
            public void onChanged(Device device) {
                //TODO: This function should get what?
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
