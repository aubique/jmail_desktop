package dev.aubique.bquiz.dal;

import java.sql.Connection;

public interface ConnectionConfigurable {
    Connection getConnection();
}
