import java.io.*;
import java.net.*;

public class FileClient {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) { System.out.println("Usage: java FileClient <file_path>"); return; }
        try (Socket socket = new Socket("localhost", 1234);
             FileInputStream in = new FileInputStream(args[0]);
             OutputStream out = socket.getOutputStream()) {
            in.transferTo(out);
            System.out.println("File sent");
        }
    }
}