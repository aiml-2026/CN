import java.util.HashMap;
public class ARP {
    HashMap<String, String> arpTable = new HashMap<>();
    void sendARPRequest(String ip) {
        System.out.println("Sending ARP Request for IP: " + ip);
        String mac = "00:1A:2B:3C:4D:5E";
        arpTable.put(ip, mac); 
        System.out.println("Received MAC: " + mac + " for IP: " + ip);
    }
    public static void main(String[] args) {
        new ARP().sendARPRequest("192.168.1.1");
    }
}