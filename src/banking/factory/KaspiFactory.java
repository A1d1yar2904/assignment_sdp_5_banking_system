package banking.factory;

import banking.service.*;

public class KaspiFactory implements BankFactory {
    @Override public AuthService createAuthService(String expectedPassword) {
        return new KaspiAuthService(expectedPassword);
    }
    @Override public AccountService createAccountService() {
        return new KaspiAccountService(200000);
    }
    @Override public PaymentService createPaymentService() {
        return new KaspiPaymentService();
    }
}