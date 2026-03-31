package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DBConnection {

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try {
            Properties prop = new Properties();
            InputStream is = DBConnection.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties");

            prop.load(is);

            URL = prop.getProperty("db.url");
            USER = prop.getProperty("db.user");
            PASSWORD = prop.getProperty("db.password");

        } catch (Exception e) {
            throw new RuntimeException("Không load được config DB", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException("Kết nối DB thất bại", e);
        }
    }
}