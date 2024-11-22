import java.rmi.Naming;
import java.util.Scanner;
public class Client {
    public static void main(String[] args) {
        try {
            Calculator calculator = (Calculator) Naming.lookup("rmi://localhost:6666/CalculatorService");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose an operation: ");
            System.out.println("1. Addition");
            System.out.println("2. Subtraction");
            System.out.println("3. Multiplication");
            System.out.println("4. Division");
            System.out.println("5. Modulus");
            System.out.println("6. Exponent");
            System.out.println("7. Square Root");
            System.out.println("8. Logarithm");
            System.out.print("Enter Your Choice: ");
            int choice = scanner.nextInt();

            System.out.print("Enter the first number: ");
            int num1 = scanner.nextInt();
            System.out.print("Enter the second number: ");
            int num2 = scanner.nextInt();
            
            
            switch (choice) {
                case 1:
                    System.out.println("Result of addition: " + calculator.add(num1, num2));
                    System.out.println("Operation Success...");
                    break;
                case 2:
                    System.out.println("Result of subtraction: " + calculator.subtract(num1, num2));
                    System.out.println("Operation Success...");
                    break;
                case 3:
                    System.out.println("Result of multiplication: " + calculator.multiply(num1, num2));
                    System.out.println("Operation Success...");
                    break;
                case 4:
                    try {
                        System.out.println("Result of division: " + calculator.divide(num1, num2));
                        System.out.println("Operation Success...");
                    } catch (ArithmeticException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("Result of modulus: " + calculator.modulus(num1, num2));
                    System.out.println("Operation Success...");
                    break;
                case 6:
                    System.out.println("Result of exponent: " + calculator.exponent(num1, num2));
                    System.out.println("Operation Success...");
                    break;
                case 7:
                    System.out.println("Result of square root: " + calculator.squareRoot(num1));
                    System.out.println("Operation Success...");
                    break;
                case 8:
                    System.out.println("Result of logarithm: " + calculator.logarithm(num1));
                    System.out.println("Operation Success...");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid operation.");
                    System.out.println("Operation Failed...");
            }
            scanner.close();
        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        }
    }
}