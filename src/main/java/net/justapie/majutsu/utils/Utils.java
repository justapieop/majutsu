package net.justapie.majutsu.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import me.fthomys.SnowflakeLib.SnowflakeFactory;
import me.fthomys.SnowflakeLib.SnowflakeGenerator;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class Utils {
    private static final Utils INSTANCE = new Utils();
    private static final Dotenv DOTENV = new DotenvBuilder().ignoreIfMissing().load();
    private static final Logger ROOT_LOGGER = (Logger) LoggerFactory.getLogger("Root");
    private static final SnowflakeGenerator SNOWFLAKE_GENERATOR = new SnowflakeFactory()
            .withEpoch(new Date().getTime())
            .enableNtpCheck(true)
            .withCustomNtpServer("time.google.com")
            .build();

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

    public long generateSnowflakeId() {
        return SNOWFLAKE_GENERATOR.generateId();
    }

    public static Utils getInstance() {
        return INSTANCE;
    }
}
