import java.net.InetAddress;
import java.net.UnknownHostException;
public class Subnetting {
    public static void main(String[] args) {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String ipAddress = localHost.getHostAddress();
            System.out.println("Device IP Address: " + ipAddress);
            String subnetMask = "255.255.255.0";
            String binaryIP = ipToBinary(ipAddress);
            String binaryMask = ipToBinary(subnetMask);
            String networkAddress = calculateNetworkAddress(binaryIP, binaryMask);
            System.out.println("Network Address: " + binaryToIp(networkAddress));
            String broadcastAddress = calculateBroadcastAddress(networkAddress, binaryMask);
            System.out.println("Broadcast Address: " + binaryToIp(broadcastAddress));
            int numberOfHosts = (int) Math.pow(2, (32 - getCIDR(subnetMask))) - 2;
            System.out.println("Number of Hosts: " + numberOfHosts);
        } catch (UnknownHostException e) {
            System.out.println("Unable to get the IP address of the device.");
            e.printStackTrace();
        }
    }
    public static String ipToBinary(String ip) {
        String[] octets = ip.split("\\.");
        StringBuilder binaryIp = new StringBuilder();
        for (String octet : octets) {
            String binaryOctet = String.format("%8s", Integer.toBinaryString(Integer.parseInt(octet))).replace(' ', '0');
            binaryIp.append(binaryOctet);
        }
        return binaryIp.toString();
    }
    public static String binaryToIp(String binaryIp) {
        StringBuilder ip = new StringBuilder();
        for (int i = 0; i < binaryIp.length(); i += 8) {
            int octet = Integer.parseInt(binaryIp.substring(i, i + 8), 2);
            ip.append(octet).append(".");
        }
        return ip.substring(0, ip.length() - 1);
    }
    public static String calculateNetworkAddress(String ip, String mask) {
        StringBuilder networkAddress = new StringBuilder();
        for (int i = 0; i < ip.length(); i++) {
            networkAddress.append(ip.charAt(i) == '1' && mask.charAt(i) == '1' ? '1' : '0');
        }
        return networkAddress.toString();
    }
    public static String calculateBroadcastAddress(String networkAddress, String mask) {
        StringBuilder broadcastAddress = new StringBuilder();
        for (int i = 0; i < networkAddress.length(); i++) {
            broadcastAddress.append(mask.charAt(i) == '0' ? '1' : networkAddress.charAt(i));
        }
        return broadcastAddress.toString();
    }
    public static int getCIDR(String subnetMask) {
        return ipToBinary(subnetMask).replace("0", "").length();
    }
}