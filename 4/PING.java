import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;
public class PING {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter IP or hostname to ping: ");
        String host = scanner.nextLine();
        try {
            InetAddress inet = InetAddress.getByName(host);
            for (int i = 1; i <= 100; i++) {
                long start = System.currentTimeMillis();
                if (inet.isReachable(1000)) {
                    System.out.println("Reply from " + host + ": time=" + (System.currentTimeMillis() - start) + "ms");
                } else {
                    System.out.println("Request timed out.");
                }
                Thread.sleep(100);
            }
        } catch (IOException e) {
            System.out.println("Host unreachable: " + e.getMessage());
        }
        scanner.close();
    }
}