package da.au_grp21.bluetoothdevelopmentdebugtool.Fragment;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import da.au_grp21.bluetoothdevelopmentdebugtool.Help.myToast;
import da.au_grp21.bluetoothdevelopmentdebugtool.R;
import da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel.MyViewModel;

public class FragmentNotSaved extends Fragment {
    TextView popUptxt;
    Button bntYes, bntCancle;//popUpBtnNo popUpBtnYes
    private MyViewModel vm;
    private myToast testToast;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentNotSaved() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTerminalScr.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTerminalScr newInstance(String param1, String param2) {
        FragmentTerminalScr fragment = new FragmentTerminalScr();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_not_saved_fragment, container, false);
        bntYes = v.findViewById(R.id.popUpBtnYes);
        bntYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testToast.showToast(FragmentNotSaved.this.getContext(), R.string.willNotSave);
                Navigation.findNavController(v).navigate((R.id.fragmentMain));
            }
        });
        bntCancle = v.findViewById(R.id.popUpBtnNo);
        bntCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testToast.showToast(FragmentNotSaved.this.getContext(), R.string.willSave);
                Navigation.findNavController(v).navigate(R.id.fragmentSaveOutput);
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = ViewModelProviders.of(this).get(MyViewModel.class);
        // TODO: Use the ViewModel
    }

}
