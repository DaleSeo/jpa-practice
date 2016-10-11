package seo.dale.practice.jpa.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreator {

    public static void createTable() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TABLE member (id INTEGER NOT NULL, name VARCHAR(255), age INTEGER, PRIMARY KEY (id))");
    }

}
