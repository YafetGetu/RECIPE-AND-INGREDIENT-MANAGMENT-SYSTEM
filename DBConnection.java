import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

public class DBConnection {
    private static final String CONFIG_PATH = "config/db.properties";

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    private static Connection connection = null;

    static {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            props.load(fis);

            URL = props.getProperty("url");
            USER = props.getProperty("user");
            PASSWORD = props.getProperty("password");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to load DB config:\n" + e.getMessage(), "Config Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            throw new RuntimeException("Failed to load DB config");
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to the database:\n" + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            throw new RuntimeException("Database connection error!");
        }
        return connection;
    }
}
