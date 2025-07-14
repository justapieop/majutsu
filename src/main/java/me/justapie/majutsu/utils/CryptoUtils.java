package me.justapie.majutsu.utils;


import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class CryptoUtils {
    private static final CryptoUtils INSTANCE = new CryptoUtils();
    private static final Argon2 ARGON2 = Argon2Factory.create();

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
