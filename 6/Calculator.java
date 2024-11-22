import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculator extends Remote {
    int add(int a, int b) throws RemoteException;
    int subtract(int a, int b) throws RemoteException;
    int multiply(int a, int b) throws RemoteException;
    double divide(int a, int b) throws RemoteException;
    int modulus(int a, int b) throws RemoteException;
    double exponent(int a, int b) throws RemoteException;
    double squareRoot(int a) throws RemoteException;
    double logarithm(int a) throws RemoteException;
}