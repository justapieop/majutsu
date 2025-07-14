package me.justapie.majutsu.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import org.slf4j.LoggerFactory;

public class Utils {
    private static final Utils INSTANCE = new Utils();
    private static final Dotenv DOTENV = new DotenvBuilder().ignoreIfMissing().load();
    private static final Logger ROOT_LOGGER = (Logger) LoggerFactory.getLogger("Root");

    public String getEnv(String key) {
        return DOTENV.get(key);
    }

    public String getEnv(String key, String s) {
        return DOTENV.get(key, s);
    }

    public boolean isDebugMode() {
        return this.getEnv("DEBUG_MODE", "false").equalsIgnoreCase("true");
    }

    public Logger getRootLogger() {
        if (this.isDebugMode()) {
            ROOT_LOGGER.setLevel(Level.DEBUG);
        }
        return ROOT_LOGGER;
    }

    public static Utils getInstance() {
        return INSTANCE;
    }
}
