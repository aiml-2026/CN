import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileServer {
    private static final int PORT = 1234;
    private static final String OUTPUT_DIR = "received_files/";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static void main(String[] args) {
        // Create output directory if it doesn't exist
        createOutputDirectory();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started successfully");
            System.out.println("Listening on port: " + PORT);
            System.out.println("Waiting for clients...\n");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     InputStream in = clientSocket.getInputStream();
                     FileOutputStream out = new FileOutputStream(OUTPUT_DIR + generateFileName())) {
                    in.transferTo(out);
                    String clientAddress = clientSocket.getInetAddress().getHostAddress();
                    System.out.println("File received successfully from " + clientAddress);
                    System.out.println("Waiting for next client...\n");
                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            System.err.println("Could not listen on port: " + PORT);
            System.exit(1);
        }
    }

    private static void createOutputDirectory() {
        File directory = new File(OUTPUT_DIR);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Created output directory: " + OUTPUT_DIR);
            } else {
                System.err.println("Failed to create output directory!");
                System.exit(1);
            }
        }
    }

    private static String generateFileName() {
        String timestamp = LocalDateTime.now().format(formatter);
        return "received_file_" + timestamp + ".txt";
    }
}