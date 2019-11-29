package da.au_grp21.bluetoothdevelopmentdebugtool.Database;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseService extends IntentService {

    public static final String DATABASE_SERVICE = "DatabaseService";
    public static final String SAVE = "SAVE";
    public static final String LOG_NAME = "LOG_NAME";
    public static final String LOG_DATA = "LOG_DATA";
    public static final String LOAD = "LOAD";
    public static final String FIND = "FIND";
    public static final String RETURN_LOG = "RETURN_LOG";
    public static final String BROADCAST = "BROADCAST";
    private FirebaseFirestore db;

    public DatabaseService() {
        super("DatabaseService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(DATABASE_SERVICE, "Service created");
        if (db == null){
            db = FirebaseFirestore.getInstance();
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action){
                case SAVE:
                    SaveLog(intent.getStringExtra(LOG_NAME), intent.getStringExtra(LOG_DATA));
                    break;
                case LOAD:
                    LoadLog(intent.getStringExtra(LOG_NAME));
                    break;
                case FIND:
                    //TODO: Add some kind of search logic
                default:
                    Log.d(DATABASE_SERVICE, "onHandleIntent: The given action was not valid.");
            }

        }
    }

    public void SaveLog (String name, String terminadata){
        new logSaver().execute(name, terminadata);
    }

    private class logSaver extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            LogData newLog = new LogData();
            newLog.setFilename(strings[0]);
            newLog.setTerminalLog(strings[1]);
            newLog.setTimestampNow();
            db.collection("logs").document(strings[0]).set(newLog);
            return null;
        }
    }

    public void LoadLog(String name){
        new logLoader().execute(name);
    }

    private class logLoader extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... strings) {
            DocumentReference docRef = db.collection("logs").document(strings[0]);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        Intent message = new Intent();
                        message.putExtra(RETURN_LOG, documentSnapshot.toObject(LogData.class));
                        message.setAction(BROADCAST);
                        LocalBroadcastManager.getInstance(DatabaseService.this).sendBroadcast(message);
                        //TODO: Find appropriate place to subscribe a broadcast listner to this
                    }
                    else{
                        Log.d(DATABASE_SERVICE, "logLoader: The searched for document does not exist.");
                    }
                }
            });
            return null;
        }
    }
}
