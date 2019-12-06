package da.au_grp21.bluetoothdevelopmentdebugtool.Database;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.io.Serializable;
import java.util.List;

public class DatabaseService extends IntentService {

    public static final String DATABASE_SERVICE = "DatabaseService";

    //Static strings used for the intent system
    public static final String SAVE = "SAVE";
    public static final String LOG_NAME = "LOG_NAME";
    public static final String LOG_DATA = "LOG_DATA";
    public static final String LOAD = "LOAD";
    public static final String FIND_BY_NAME = "FIND_BY_NAME";
    public static final String RETURN_LOG = "RETURN_LOG";
    public static final String SINGLE_BROADCAST = "SINGLE_BROADCAST";
    public static final String LIST_BROADCAST = "LIST_BROADCAST";
    public static final String COLLECTION_PATH = "logs";
    public static final String FIND_BY_DATE = "FIND_BY_DATE";
    public static final String RETURN_LOG_LIST = "RETURN_LOG_LIST";
    public static final String DATE = "DATE";

    //Instance of the database
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
                case SAVE: //Case for saving a log. Takes a filename and the terminal data from the intent.
                    SaveLog(intent.getStringExtra(LOG_NAME), intent.getStringExtra(LOG_DATA));
                    break;
                case LOAD: //Case for loading a log. Uses filename for searching.
                    LoadLog(intent.getStringExtra(LOG_NAME));
                    break;
                case FIND_BY_NAME: //Case for finding a log in the database by filename.
                    FindLogByFilename(intent.getStringExtra(LOG_NAME));
                    break;
                case FIND_BY_DATE: //Case for finding a list of logs created on the supplied date.
                    FindLogsByDate(intent.getStringExtra(DATE));
                    break;
                default:
                    Log.d(DATABASE_SERVICE, "onHandleIntent: The given action was not valid.");
            }

        }
    }

    /**
     * Calls the async task for saving a log.
     *
     * @param name: Name of the file.
     * @param terminadata: The data from the terminal which you wish to save.
     */
    public void SaveLog (String name, String terminadata){
        new logSaver().execute(name, terminadata);
    }

    /**
     * Async task for saving the log.
     */
    private class logSaver extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            LogData newLog = new LogData();
            newLog.setFilename(strings[0]);
            newLog.setTerminalLog(strings[1]);
            newLog.setTimestampNow();
            db.collection(COLLECTION_PATH).document(strings[0]).set(newLog);
            return null;
        }
    }

    /**
     * Calls the async task for loading a log file.
     *
     * @param name: Name of the file you wish to load.
     */
    public void LoadLog(String name){
        new logLoader().execute(name);
    }

    /**
     * Async task for loading a file. Returns the file through a local broadcast intent.
     */
    private class logLoader extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... strings) {
            DocumentReference docRef = db.collection(COLLECTION_PATH).document(strings[0]);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        Intent message = new Intent();
                        message.putExtra(RETURN_LOG, documentSnapshot.toObject(LogData.class));
                        message.setAction(SINGLE_BROADCAST);
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
    
    /**
     * Calls the async task, that tries to find the log by the given file name.
     *
     * @param filename: Name of the file you wish to retrieve.
     */
    public void FindLogByFilename(String filename){
        new logNameFinder().execute(filename);
    }
    
    /**
     * Async task for loading a file. Returns the file through a local broadcast intent.
     */
    private class logNameFinder extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            CollectionReference collref = db.collection(COLLECTION_PATH);
            Query query = collref.whereEqualTo("name", strings[0]);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        QuerySnapshot document = task.getResult();
                        List<LogData> queryResult = document.toObjects(LogData.class);
                        LogData returnLog = queryResult.get(1);
                        Intent message = new Intent();
                        message.putExtra(RETURN_LOG, returnLog);
                        message.setAction(SINGLE_BROADCAST);
                        LocalBroadcastManager.getInstance(DatabaseService.this).sendBroadcast(message);
                        //TODO: Find appropriate place to subscribe a broadcast listner to this
                    }
                    else {
                        Toast.makeText(DatabaseService.this, "There is no file with such a name", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return null;
        }
    }

    /**
     * Starts the async task which retrieves a list of logs from the given date.
     * @param date: Date you would like your logs from.
     */
    public void FindLogsByDate(String date){
        new logsByDateFinder().execute(date);
    }

    /**
     * Async task for getting a list of logs based on the supplied date.
     */
    private class logsByDateFinder extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            CollectionReference collref = db.collection(COLLECTION_PATH);
            Query query = collref.whereGreaterThanOrEqualTo("timestamp", strings[0]+" 00:00:00").whereLessThanOrEqualTo("timestamp", strings[0]+" 23:59:59");
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot document = task.getResult();
                        List<LogData> queryResult = document.toObjects(LogData.class);
                        Intent message = new Intent();
                        message.putExtra(RETURN_LOG_LIST, (Serializable) queryResult);
                        message.setAction(LIST_BROADCAST);
                        LocalBroadcastManager.getInstance(DatabaseService.this).sendBroadcast(message);
                        //TODO: Find appropriate place to subscribe a broadcast listner to this
                    }
                }
            });
            return null;
        }
    }
}
