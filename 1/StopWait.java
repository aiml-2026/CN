import java.util.Scanner;

class StopWait{
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
        System.out.print("Enter the frame size: ");
        int frameSize = sc.nextInt();
        System.out.println("\nStop-and-Wait Protocol Simulation\n");
        int currentFrame = 0;
        while (currentFrame < totalFrames) {
            System.out.println("Sending Frame " + currentFrame);
            boolean frameSent = sendFrame(currentFrame);
            if (frameSent) {
                boolean ackReceived = receiveAcknowledgment(currentFrame);
                if (ackReceived) {
                    System.out.println("Frame " + currentFrame + " acknowledged.\n");
                    currentFrame++;
                } else {
                    System.out.println("No acknowledgment for Frame " + currentFrame + ", resending...\n");
                }
            }
        }
        System.out.println("All frames sent successfully.");
        sc.close();
    }
}