package da.au_grp21.bluetoothdevelopmentdebugtool.Device;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import da.au_grp21.bluetoothdevelopmentdebugtool.R;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.MyViewHolder> {
    private ArrayList<Device> deviceArrayList;
    private OnItemClickListener clickListener; //Click listner
    private OnItemLongClickListner longClickListner; //Long click listner

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public interface OnItemLongClickListner{
        void OnItemLongClick(int position);
    }

    public DeviceListAdapter(ArrayList<Device> devices) {deviceArrayList = devices;}

    public void setClickListener(OnItemClickListener listener){
        clickListener = listener;
    }

    public void setLongClickListner(OnItemLongClickListner listner){
        longClickListner = listner;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView MAC, name, paired;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listner, final OnItemLongClickListner longlistner) {
            super(itemView);

            //Grabbing the UI
            MAC = itemView.findViewById(R.id.deviceListElementTextViewMacAddr);
            name = itemView.findViewById(R.id.deviceListElementTextViewDeviceName);
            paired = itemView.findViewById(R.id.deviceListElementTextViewBonded);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listner != null){
                        listner.OnItemClick(getAdapterPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(longlistner != null){
                        longlistner.OnItemLongClick(getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    public DeviceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_list_element, parent, false);
        MyViewHolder vh = new MyViewHolder(v, clickListener, longClickListner);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Device current = GetItem(position);
        holder.paired.setText(R.string.deviceListElementTextViewIsBonded + " " + current.getPaired());
        holder.name.setText(R.string.deviceListElementTextViewDeviceName + " " + current.getName());
        holder.MAC.setText(R.string.deviceListElementTextViewMac_address + " " + current.getMac());
    }

    @Override
    public int getItemCount() {return deviceArrayList.size();}

    public Device GetItem(int position) {return  deviceArrayList.get(position);}
}
