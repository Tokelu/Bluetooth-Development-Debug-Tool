package da.au_grp21.bluetoothdevelopmentdebugtool.Database;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

//Data class for representing the log files in the database
public class LogData implements Serializable {
    private String filename, terminalLog, timestamp;
    private TimeZone tz = TimeZone.getDefault();
    public static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public LogData () {}

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTerminalLog() {
        return terminalLog;
    }

    public void setTerminalLog(String terminalLog) {
        this.terminalLog = terminalLog;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    //Custom method for making the timestamp the current time
    //These timestamps have the following format: "dd/mm/yyyy HH:mm:ss"
    public void setTimestampNow(){
        sdf.setTimeZone(tz);
        timestamp = sdf.format(new Timestamp(System.currentTimeMillis()));
    }
}
