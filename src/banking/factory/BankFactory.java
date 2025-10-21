package banking.factory;

import banking.service.*;

public interface BankFactory {
    AuthService createAuthService(String expectedPassword);
    AccountService createAccountService();
    PaymentService createPaymentService();
}