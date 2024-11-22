import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server. Type messages:");

            String message;
            while ((message = userInput.readLine()) != null) {
                out.println(message); // Send message to server
                System.out.println(in.readLine()); // Print server's response
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
