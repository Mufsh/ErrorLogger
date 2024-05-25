
# ErrorLogger
# Error Log Monitoring Application

## Overview
The Error Log Monitoring application, implemented in Java, tracks error log entries with timestamps, log types, and severity levels. It offers functionalities to compute statistics for log entries based on timestamps, log types, or combinations of both.

## Features

- **Log Entry Submission:** Users can submit error log entries with timestamps, log types, and severity levels.

- **Statistics Computation:**
  - Compute statistics for log entries based on log type, timestamp, or both.
  - Statistics include min, max, and mean severity levels.

- **Input/Output Handling:**
  - Reads log entries from an input file.
  - Writes computed statistics to an output file.

- **Error Handling:**
  - Validates log type lengths to not exceed 100 characters.
  - Properly handles file-related errors during input/output operations.

- **Modular Structure:**
  - Organized into separate classes and packages for clarity and maintainability.

# Technologies Used
  Java

# Installation
## Clone the repository
```
git clone https://github.com/mufsh/errorlogger.git
cd errorlogger/src
```

## Environment Setup:
 Must have jdk on your device
# Running the Application
  Comiple the program
 ```
javac ErrorLogMonitorRunner.java
```
Execute the program
```
java ErrorLogMonitorRunner
```



# Assumptions
 * The input file is correctly formatted with a single space between each word/text












