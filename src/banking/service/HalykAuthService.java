package banking.service;

public class HalykAuthService implements AuthService {
    private final String expected;
    public HalykAuthService(String expectedPassword) { this.expected = expectedPassword; }
    @Override public boolean check(String fullName, String password) { return expected.equals(password); }
}