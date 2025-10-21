package banking.service;

public class KaspiAuthService implements AuthService {
    private final String expected;

    public KaspiAuthService(String expectedPassword) {
        this.expected = expectedPassword;
    }

    @Override
    public boolean check(String fullName, String password) {
        return expected.equals(password);
    }
}