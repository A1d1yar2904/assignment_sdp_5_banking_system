package banking.facade;

import banking.factory.*;
import banking.service.*;
import banking.decorator.*;

import java.util.Locale;
import java.util.Scanner;

public class BankApp {
    private String fullName;
    private String password;
    private double kaspiBalance = 200000;
    private double halykBalance = 100000;

    public void run() {
        Scanner sc = new Scanner(System.in, "UTF-8");

        int choice;
        do {
            System.out.println("\nChoose an option:");
            System.out.println("1. Login");
            System.out.println("2. Go to store");
            System.out.println("3. Withdraw money");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    login(sc);
                    break;
                case 2:
                    goToStore(sc);
                    break;
                case 3:
                    withdrawMoney(sc);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice! Please choose again.");
                    break;
            }
        } while (choice != 4);
    }

    private void login(Scanner sc) {
        System.out.print("\nEnter your name: ");
        fullName = sc.next();
        System.out.print("Enter your password: ");
        password = sc.next();

        BankFactory factory = new KaspiFactory();
        AuthService auth = factory.createAuthService(password);
        if (auth.check(fullName, password)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid credentials. Try again.");
        }
    }

    private void goToStore(Scanner sc) {
        if (fullName == null || password == null) {
            System.out.println("Please login first.");
            return;
        }

        System.out.print("\nEnter your name: ");
        String enteredName = sc.next();
        System.out.print("Enter your password: ");
        String enteredPassword = sc.next();

        if (!enteredName.equals(fullName) || !enteredPassword.equals(password)) {
            System.out.println("Invalid credentials. Access denied.");
            return;
        }

        System.out.println("\nChoose a bank account:");
        System.out.println("1. Kaspi");
        System.out.println("2. Halyk");
        System.out.print("Enter your choice: ");
        int bankChoice = sc.nextInt();

        double balance = (bankChoice == 1) ? kaspiBalance : halykBalance;
        String selectedBank = (bankChoice == 1) ? "Kaspi" : "Halyk";

        System.out.println("\nYour current balance in " + selectedBank + ": " + formatMoney(balance) + " ₸");

        System.out.print("\nEnter the amount you want to spend: ");
        double amount = sc.nextDouble();

        if (amount > balance) {
            System.out.println("Insufficient funds. Your balance: " + formatMoney(balance) + " ₸");
            return;
        }

        System.out.println("\nChoose a discount amount:");
        System.out.println("1. 5%");
        System.out.println("2. 10%");
        System.out.println("3. 15%");
        System.out.print("Enter your choice: ");
        int discountChoice = sc.nextInt();
        double discount = 0;
        switch (discountChoice) {
            case 1:
                discount = 0.05;
                break;
            case 2:
                discount = 0.10;
                break;
            case 3:
                discount = 0.15;
                break;
            default:
                System.out.println("Invalid choice! No discount applied.");
                break;
        }

        System.out.println("Your total is: " + amount + " ₸. Applying discount...");
        amount -= amount * discount;
        System.out.println("[Discount] - " + (discount * 100) + "% applied → new amount: " + amount + " ₸");

        if (bankChoice == 1) {
            kaspiBalance -= amount;
        } else {
            halykBalance -= amount;
        }

        System.out.println("Purchase completed successfully!");
        System.out.println("Remaining balance: " + formatMoney((bankChoice == 1) ? kaspiBalance : halykBalance) + " ₸");
    }

    private void withdrawMoney(Scanner sc) {
        if (fullName == null || password == null) {
            System.out.println("Please login first.");
            return;
        }

        System.out.print("\nEnter your name: ");
        String enteredName = sc.next();
        System.out.print("Enter your password: ");
        String enteredPassword = sc.next();

        if (!enteredName.equals(fullName) || !enteredPassword.equals(password)) {
            System.out.println("Invalid credentials. Access denied.");
            return;
        }

        System.out.print("\nEnter your bank account (Kaspi or Halyk): ");
        String bankName = sc.next().trim();

        BankFactory factory = selectFactory(bankName);
        AccountService account = factory.createAccountService();
        PaymentService payment = factory.createPaymentService();

        System.out.print("Enter withdrawal amount: ");
        double amount = sc.nextDouble();

        if (amount > account.getBalance()) {
            System.out.println("Error: insufficient funds. Your balance: " + formatMoney(account.getBalance()) + " ₸");
            return;
        }

        System.out.println("\nChoose a cashback amount:");
        System.out.println("1. 5%");
        System.out.println("2. 10%");
        System.out.println("3. 15%");
        System.out.print("Enter your choice: ");
        int cashbackChoice = sc.nextInt();
        double cashback = 0;
        switch (cashbackChoice) {
            case 1:
                cashback = 0.05;
                break;
            case 2:
                cashback = 0.10;
                break;
            case 3:
                cashback = 0.15;
                break;
            default:
                System.out.println("Invalid choice! No cashback applied.");
                break;
        }

        payment = new FraudDetectionDecorator(payment);
        payment = new CashbackDecorator(payment, account, cashback);

        boolean ok = payment.pay(amount, "User");

        if (ok) {
            boolean withdrawn = account.withdraw(amount);
            double cashbackAmount = amount * cashback;
            account.deposit(cashbackAmount);

            System.out.println("✅ Transaction successful.");
            System.out.println("You have withdrawn: " + formatMoney(amount) + " ₸");
            System.out.println("Cashback received: " + formatMoney(cashbackAmount) + " ₸");
            System.out.println("Remaining balance: " + formatMoney(account.getBalance()) + " ₸");
        } else {
            System.out.println("Transaction failed.");
        }
    }
    private BankFactory selectFactory(String input) {
        if (input.equalsIgnoreCase("Kaspi")) return new KaspiFactory();
        return new HalykFactory();
    }

    private String formatMoney(double m) {
        return (m == (long) m)
                ? String.format("%,d", (long) m)
                : String.format(Locale.ROOT, "%,.2f", m);
    }
}
