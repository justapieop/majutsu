package net.justapie.majutsu.utils;


import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class CryptoUtils {
    private static final CryptoUtils INSTANCE = new CryptoUtils();
    private static final Argon2 ARGON2 = Argon2Factory.create();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public String generatePassword(int length) {
        StringBuilder generated = new StringBuilder();

        for (int i = 0; i < length; i++) {
            generated.append(CHARACTERS.charAt(Utils.getInstance().rng(0, CHARACTERS.length() - 1)));
        }

        return generated.toString();
    }

    public String hashPassword(String password) {
        return ARGON2.hash(3, 65536, 1, password.toCharArray());
    }

    public boolean comparePassword(String password, String hash) {
        return ARGON2.verify(hash, password.toCharArray());
    }

    public static CryptoUtils getInstance() {
        return INSTANCE;
    }
}
