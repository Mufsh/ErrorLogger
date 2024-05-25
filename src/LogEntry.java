import java.math.BigInteger;

class LogEntry {
    BigInteger timestamp;
    String logType;
    double severity;

    public LogEntry(BigInteger timestamp, String logType, double severity) {
        this.timestamp = timestamp;
        this.logType = logType;
        this.severity = severity;
    }

    public BigInteger getTimestamp() {
        return timestamp;
    }

    public String getLogType() {
        return logType;
    }

    public double getSeverity() {
        return severity;
    }
}