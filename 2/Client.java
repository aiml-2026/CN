import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Client {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 6666;
        try (Socket socket = new Socket(hostname, port)) {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            Scanner a = new Scanner(System.in);
            System.out.println("Enter message to send to server:");
            String message = a.nextLine();
            writer.println(message);
            System.out.println("Sent to server: " + message);
            String response = reader.readLine();
            System.out.println("Received from server: " + response);
            a.close();
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}