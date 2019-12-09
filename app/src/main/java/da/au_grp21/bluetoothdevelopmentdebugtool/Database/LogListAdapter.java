package da.au_grp21.bluetoothdevelopmentdebugtool.Database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import da.au_grp21.bluetoothdevelopmentdebugtool.R;

public class LogListAdapter extends RecyclerView.Adapter<LogListAdapter.LogViewHolder> {
    private ArrayList<LogData> logArrayList = new ArrayList<>();
    private LogListAdapter.OnItemClickListener clickListener; //Click listner
    private LogListAdapter.OnItemLongClickListner longClickListner; //Long click listner
    private Context context;

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public interface OnItemLongClickListner {
        void OnItemLongClick(int position);
    }

    public void setClickListener(LogListAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    public void setLongClickListner(LogListAdapter.OnItemLongClickListner listner) {
        longClickListner = listner;
    }

    public void SetLogList(List<LogData> logs){
        logArrayList.clear();
        logArrayList.addAll(logs);
        notifyDataSetChanged();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {
        private TextView name, timestamp;

        public LogViewHolder(@NonNull View itemView, final LogListAdapter.OnItemClickListener listner, final LogListAdapter.OnItemLongClickListner longlistner) {
            super(itemView);

            //Grabbing the UI
            name = itemView.findViewById(R.id.logListCardName);
            timestamp = itemView.findViewById(R.id.logListCardTimestamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listner != null) {
                        listner.OnItemClick(getAdapterPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (longlistner != null) {
                        longlistner.OnItemLongClick(getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    public LogListAdapter.LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_list_element, parent, false);
        LogListAdapter.LogViewHolder vh = new LogListAdapter.LogViewHolder(v, clickListener, longClickListner);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        LogData current = GetItem(position);
        holder.timestamp.setText(context.getResources().getString(R.string.logTimestamp) + " " + current.getTimestamp());
        holder.name.setText(context.getResources().getString(R.string.logName) + " " + current.getFilename());
    }

    @Override
    public int getItemCount() {
        return logArrayList.size();
    }

    public LogData GetItem(int position) {
        return logArrayList.get(position);
    }
}
