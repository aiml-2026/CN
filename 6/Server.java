import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Server extends UnicastRemoteObject implements Calculator {
    public Server() throws RemoteException { super(); }
    public int add(int a, int b) { return a + b; }
    public int subtract(int a, int b) { return a - b; }
    public int multiply(int a, int b) { return a * b; }
    public double divide(int a, int b) {
        if (b == 0) throw new ArithmeticException("Cannot divide by zero.");
        return (double) a / b;
    }
    public int modulus(int a, int b) { return a % b; }
    public double exponent(int a, int b) { return Math.pow(a, b); }
    public double squareRoot(int a) { return Math.sqrt(a); }
    public double logarithm(int a) { return Math.log(a); }
    public static void main(String[] args) {
        try {
            Server calculatorServer = new Server();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the port number: ");
            int port = scanner.nextInt();
            LocateRegistry.createRegistry(port);
            Naming.rebind("rmi://localhost:" + port + "/CalculatorService", calculatorServer);
            System.out.println("Server is ready for operation...");
        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        }
    }
}