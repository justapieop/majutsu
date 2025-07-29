package net.justapie.majutsu.db.repository.document;

import ch.qos.logback.classic.Logger;
import net.justapie.majutsu.db.DbClient;
import net.justapie.majutsu.db.repository.user.UserRepository;
import net.justapie.majutsu.db.schema.user.User;
import net.justapie.majutsu.db.schema.user.UserRole;
import net.justapie.majutsu.utils.CryptoUtils;
import net.justapie.majutsu.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DocumentRepository {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(UserRepository.class);
    private static final Connection CONNECTION = DbClient.getInstance().getConnection();

    public boolean deleteDocument(int id){
        String sql = "DELETE FROM documents WHERE id = ?";
        try (PreparedStatement stmt = CONNECTION.prepareStatement(sql)){
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            LOGGER.error("Error deleteing document:" + e.getMessage());
            return false;
        }
    }

}
