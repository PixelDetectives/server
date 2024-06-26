package site.pixeldetective.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnector {
    private static Connection instance;

    private static final String RDS_ENDPOINT = "kosta.cm9v59fmxrit.ap-northeast-2.rds.amazonaws.com";
    private static final String DATABASE_NAME = "PixelDetectives";
    private static final String USER = "byun";
    private static final String PASSWORD = "1234";
    private static final String JDBC_URL = "jdbc:mysql://" + RDS_ENDPOINT + ":3306/" + DATABASE_NAME;

    public static Connection getConnection() {
    if (instance == null) {
        setInstance();
        }
    return instance;
    }
    private static void setInstance() {
    try {
        instance = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
    }
    }
}