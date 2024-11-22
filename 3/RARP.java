import java.util.HashMap;
public class RARP {
    HashMap<String, String> rarpTable = new HashMap<>();
    void sendRARPRequest(String mac) {
        System.out.println("Sending RARP Request for MAC: " + mac);
        String ip = "192.168.1.100"; // Dummy IP address
        rarpTable.put(mac, ip); 
        System.out.println("Received IP: " + ip + " for MAC: " + mac);
    }
    public static void main(String[] args) {
        new RARP().sendRARPRequest("00:1A:2B:3C:4D:5E");
    }
}