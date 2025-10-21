package banking.factory;

import banking.service.*;

public class HalykFactory implements BankFactory {
    @Override public AuthService createAuthService(String expectedPassword) {
        return new HalykAuthService(expectedPassword);
    }
    @Override public AccountService createAccountService() {
        return new HalykAccountService(150000);
    }
    @Override public PaymentService createPaymentService() {
        return new HalykPaymentService();
    }
}