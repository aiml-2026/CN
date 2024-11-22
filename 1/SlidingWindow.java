import java.util.Scanner;
class SlidingWindowProtocol {
    public static boolean sendFrame(int frame) {
        System.out.println("Frame " + frame + " sent.");
        return true;
    }
    public static boolean receiveAcknowledgment(int frame) {
        System.out.println("Acknowledgment for Frame " + frame + " received.");
        return true;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the total number of frames to send: ");
        int totalFrames = sc.nextInt();
        System.out.print("Enter the window size: ");
        int windowSize = sc.nextInt();
        System.out.println("\nSliding Window Protocol Simulation (Go-Back-N)\n");
        int sentFrame = 0;
        int acknowledgedFrame = 0;
        while (acknowledgedFrame < totalFrames) {
            for (int i = 0; i < windowSize && sentFrame < totalFrames; i++) {
                sendFrame(sentFrame);
                sentFrame++;
            }
            for (int i = acknowledgedFrame; i < sentFrame; i++) {
                boolean ackReceived = receiveAcknowledgment(i);
                if (ackReceived) {
                    System.out.println("Frame " + i + " acknowledged.\n");
                    acknowledgedFrame++;
                } else {
                    System.out.println("Acknowledgment lost for Frame " + i + ". Resending frames starting from Frame " + acknowledgedFrame + "...\n");
                    sentFrame = acknowledgedFrame;
                    break;
                }
            }
        }
        System.out.println("All frames sent and acknowledged successfully.");
        sc.close();
    }
}