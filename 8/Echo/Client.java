import java.io.*;
import java.net.*;
public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 3000);
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
             
            String input;
            while ((input = consoleInput.readLine()) != null) {
                out.println(input);  // Send input to server
                System.out.println("Echo Response: " + in.readLine());  // Print echoed response
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}