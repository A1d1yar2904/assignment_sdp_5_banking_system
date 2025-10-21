package banking.facade;

import banking.factory.*;
import banking.service.*;
import banking.decorator.*;

import java.util.Locale;
import java.util.Scanner;

public class BankApp {
    public void run() {
        Scanner sc = new Scanner(System.in, "UTF-8");

        System.out.print("Full name: ");
        String fullName = sc.nextLine().trim();

        System.out.print("Create password: ");
        String password = sc.nextLine();

        System.out.print("Select your bank account (Kaspi or Halyk): ");
        String bankName = sc.nextLine().trim();

        BankFactory factory = selectFactory(bankName);
        AuthService auth = factory.createAuthService(password);
        AccountService account = factory.createAccountService();
        PaymentService payment = factory.createPaymentService();

        payment = new FraudDetectionDecorator(payment);

        System.out.print("Apply cashback? (yes/no): ");
        String cbAns = sc.nextLine().trim().toLowerCase(Locale.ROOT);
        if (cbAns.startsWith("y")) {
            System.out.print("Do you want cashback (5, 10, or 15%)? Enter 0 to skip: ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
                if (choice == 5 || choice == 10 || choice == 15) {
                    double p = choice / 100.0;
                    payment = new CashbackDecorator(payment, account, p);
                } else if (choice != 0) {
                    System.out.println("Invalid choice. Cashback not applied.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Cashback not applied.");
            }
        }

        System.out.println("Your current balance: " + formatMoney(account.getBalance()) + " ₸");
        System.out.print("Enter withdrawal amount: ");
        double amount;
        try {
            amount = Double.parseDouble(sc.nextLine().trim());
            if (amount <= 0) {
                System.out.println("Error: the amount must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: invalid amount format.");
            return;
        }

        if (amount > account.getBalance()) {
            System.out.println("Error: insufficient funds. Your balance: " + formatMoney(account.getBalance()) + " ₸");
            return;
        }

        System.out.println("Verification required. Please re-enter your password: ");
        String verify = sc.nextLine();

        if (!auth.check(fullName, verify)) {
            System.out.println("Verification failed: incorrect password. Transaction canceled.");
            return;
        }

        boolean ok = payment.pay(amount, fullName);
        if (!ok) {
            System.out.println("Transaction declined by the payment service.");
            return;
        }

        boolean withdrawn = account.withdraw(amount);
        if (!withdrawn) {
            System.out.println("Error: transaction failed during balance update.");
            return;
        }

        System.out.println("✅ Transaction successful.");
        System.out.println("You have withdrawn: " + formatMoney(amount) + " ₸");
        System.out.println("Remaining balance: " + formatMoney(account.getBalance()) + " ₸");
    }

    private BankFactory selectFactory(String input) {
        String s = input.toLowerCase(Locale.ROOT);
        if (s.contains("kaspi") || s.contains("кас")) return new KaspiFactory();
        return new HalykFactory();
    }

    private String formatMoney(double m) {
        return (m == (long) m)
                ? String.format("%,d", (long) m)
                : String.format(Locale.ROOT, "%,.2f", m);
    }
}