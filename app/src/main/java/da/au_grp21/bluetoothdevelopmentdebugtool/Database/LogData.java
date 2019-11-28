package da.au_grp21.bluetoothdevelopmentdebugtool.Database;


import java.io.Serializable;
import java.sql.Timestamp;

//Data class for representing the log files in the database
public class LogData implements Serializable {
    private String filename, terminalLog, timestamp;

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
    public void setTimestampNow(){
        timestamp = new Timestamp(System.currentTimeMillis()).toString();
    }
}
