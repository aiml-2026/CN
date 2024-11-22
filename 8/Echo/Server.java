import java.io.*;
import java.net.*;
public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            System.out.println("Hello Jayanthan,\nServer has started in PORT :"+"6666");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    
                    String message;
                    while ((message = in.readLine()) != null) {
                        out.println(message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}