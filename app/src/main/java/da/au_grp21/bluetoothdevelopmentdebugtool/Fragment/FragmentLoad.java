package da.au_grp21.bluetoothdevelopmentdebugtool.Fragment;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import da.au_grp21.bluetoothdevelopmentdebugtool.Database.LogData;
import da.au_grp21.bluetoothdevelopmentdebugtool.Database.LogListAdapter;
import da.au_grp21.bluetoothdevelopmentdebugtool.Device.Device;
import da.au_grp21.bluetoothdevelopmentdebugtool.R;
import da.au_grp21.bluetoothdevelopmentdebugtool.ViewModel.MyViewModel;

import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.LIST_BROADCAST;
import static da.au_grp21.bluetoothdevelopmentdebugtool.Database.DatabaseService.SINGLE_BROADCAST;

/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link FragmentLoad#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLoad extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button loadBtnBack, loadBtnSearch;
    EditText loadSeachtxt;
    private MyViewModel vm;

    //Fields for the recycler view
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private LogListAdapter myAdapter;


    public FragmentLoad() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentLoad.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentLoad newInstance(String param1, String param2) {
        FragmentLoad fragment = new FragmentLoad();
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

        //Setting up intent filter and using this to subscribe to the local broadcast manager
        IntentFilter filter = new IntentFilter();
        filter.addAction(SINGLE_BROADCAST);
        filter.addAction(LIST_BROADCAST);
        LocalBroadcastManager.getInstance(FragmentLoad.this.getActivity()).registerReceiver(vm.onDatabaseResponse, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(FragmentLoad.this.getActivity()).unregisterReceiver(vm.onDatabaseResponse);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_load_data, container, false);

        initializeUI(v);
        loadBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vm.seachForOldData(FragmentLoad.this.getActivity(), loadSeachtxt.getText().toString());
            }
        });
        loadBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.fragmentMain);
            }
        });

        return v;
    }

    private void initializeUI(final View view) {
        loadBtnBack = view.findViewById(R.id.fragLoadBtnBack);
        loadBtnSearch = view.findViewById(R.id.fragLoadBtnSearch);
        loadSeachtxt = view.findViewById(R.id.fragLoadSeachtxt);
        recyclerView = this.getActivity().findViewById(R.id.fragLoadRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getActivity());

        myAdapter = new LogListAdapter(); //TODO: Steen will find out how to do this mutable live data in recycler view shit
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter.setClickListener(new LogListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                vm.setChosenLog(myAdapter.GetItem(position));
                Navigation.findNavController(view).navigate(R.id.fragmentTerminalScr);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        vm.getLogs().observe(this, new Observer<List<LogData>>() {
            @Override
            public void onChanged(List<LogData> logData) {
                myAdapter.SetLogList(logData);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
