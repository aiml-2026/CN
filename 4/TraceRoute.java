import java.io.*;
import java.util.Scanner;
public class TraceRoute {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter host to trace route: ");
        String host = scanner.nextLine();
        try {
            ProcessBuilder pb = new ProcessBuilder("tracert", host);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}