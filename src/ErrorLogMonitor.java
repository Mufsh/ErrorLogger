import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

class ErrorLogMonitor {
    private List<LogEntry> logs;
    private Map<String, List<LogEntry>> logsByType;
    private TreeMap<BigInteger, List<LogEntry>> logsByTimestamp;

    public ErrorLogMonitor() {
        this.logs = new ArrayList<>();
        this.logsByType = new HashMap<>();
        this.logsByTimestamp = new TreeMap<>();
    }

    public void addLogEntry(BigInteger timestamp, String logType, double severity) {
        if (logType.length() > 100) {
            throw new IllegalArgumentException("Log type length cannot exceed 100 characters.");
        }
        LogEntry logEntry = new LogEntry(timestamp, logType, severity);
        logs.add(logEntry);

        // Updating logs by type
        logsByType.computeIfAbsent(logType, k -> new ArrayList<>()).add(logEntry);

        // Updating logs by timestamp
        logsByTimestamp.computeIfAbsent(timestamp, k -> new ArrayList<>()).add(logEntry);
    }

    public String computeSeverityByType(String logType) {
        List<LogEntry> filteredLogs = logsByType.getOrDefault(logType, Collections.emptyList());

        return computeStatistics(filteredLogs);
    }

    public String computeSeverityBeforeTimestamp(BigInteger timestamp) {
        List<LogEntry> filteredLogs = logsByTimestamp.headMap(timestamp, false).values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return computeStatistics(filteredLogs);
    }

    public String computeSeverityAfterTimestamp(BigInteger timestamp) {
        List<LogEntry> filteredLogs = logsByTimestamp.tailMap(timestamp, false).values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return computeStatistics(filteredLogs);
    }

    public String computeSeverityByTypeBeforeTimestamp(String logType, BigInteger timestamp) {
        List<LogEntry> filteredLogs = logsByType.getOrDefault(logType, Collections.emptyList()).stream()
                .filter(log -> log.getTimestamp().compareTo(timestamp) < 0)
                .collect(Collectors.toList());

        return computeStatistics(filteredLogs);
    }

    public String computeSeverityByTypeAfterTimestamp(String logType, BigInteger timestamp) {
        List<LogEntry> filteredLogs = logsByType.getOrDefault(logType, Collections.emptyList()).stream()
                .filter(log -> log.getTimestamp().compareTo(timestamp) > 0)
                .collect(Collectors.toList());

        return computeStatistics(filteredLogs);
    }

    private String computeStatistics(List<LogEntry> logs) {
        if (logs.isEmpty()) {
            return "Min Severity: 0.0, Max Severity: 0.0, Mean Severity: 0.0";
        }
        double min = logs.stream().mapToDouble(LogEntry::getSeverity).min().orElse(Double.NaN);
        double max = logs.stream().mapToDouble(LogEntry::getSeverity).max().orElse(Double.NaN);
        double mean = logs.stream().mapToDouble(LogEntry::getSeverity).average().orElse(Double.NaN);

        String minSeverity = formatSeverity(min);
        String maxSeverity = formatSeverity(max);
        String meanSeverity = formatSeverity(mean);

        return String.format("Min Severity: %s, Max Severity: %s, Mean Severity: %s", minSeverity, maxSeverity,
                meanSeverity);
    }

    private String formatSeverity(double value) {
        if (Double.isNaN(value)) {
            return "NaN";
        }
        if (value == 0.0) {
            return "0";
        }
        return String.format("%.6f", value).replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    public void processLogs(ErrorLogMonitor monitor, String inFile, String outFile) {

        try (BufferedReader reader = new BufferedReader(new FileReader(inFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 2) {
                    int command = Integer.parseInt(parts[0]);

                    switch (command) {
                        case 1:
                            BigInteger timestamp = new BigInteger(parts[1]);
                            String logType = parts[2];
                            double severity = Double.parseDouble(parts[3]);
                            monitor.addLogEntry(timestamp, logType, severity);
                            writer.write("No output");
                            writer.newLine();
                            break;
                        case 2:
                            String logTypeQuery = parts[1];
                            String severityStats = monitor.computeSeverityByType(logTypeQuery);
                            writer.write(severityStats);
                            writer.newLine();
                            break;
                        case 3:
                            String queryType = parts[1];
                            BigInteger queryTimestamp = new BigInteger(parts[2]);
                            String timestampStats;
                            if (queryType.equals("BEFORE")) {
                                timestampStats = monitor.computeSeverityBeforeTimestamp(queryTimestamp);
                            } else {
                                timestampStats = monitor.computeSeverityAfterTimestamp(queryTimestamp);
                            }
                            writer.write(timestampStats);
                            writer.newLine();
                            break;
                        case 4:
                            String logTypeQuery2 = parts[2];
                            BigInteger queryTimestamp2 = new BigInteger(parts[3]);
                            String typeTimestampStats;
                            if (logTypeQuery2.length() <= 100) {
                                if (parts[0].contains("BEFORE")) {
                                    typeTimestampStats = monitor.computeSeverityByTypeBeforeTimestamp(logTypeQuery2,
                                            queryTimestamp2);
                                } else {
                                    typeTimestampStats = monitor.computeSeverityByTypeAfterTimestamp(logTypeQuery2,
                                            queryTimestamp2);
                                }
                                writer.write(typeTimestampStats);
                                writer.newLine();
                            }
                            break;
                        default:
                            break;
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("All the output is written to the file: " + outFile);
    }
}
