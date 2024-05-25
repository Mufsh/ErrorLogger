import java.util.Scanner;

public class ErrorLogMonitorRunner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the input file: ");
        String inFile = scanner.nextLine();
        System.out.print("Enter the name of the output file: ");
        String outFile = scanner.nextLine();
        ErrorLogMonitor monitor = new ErrorLogMonitor();
        monitor.processLogs(monitor, inFile, outFile);
        scanner.close();
    }
}
