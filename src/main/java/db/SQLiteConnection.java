package db;

import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class SQLiteConnection {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlite:unique_words.db";// Относительный путь
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);// Включение внешних ключей
            return DriverManager.getConnection(url, config.toProperties());
        } catch (SQLException e) {
            LOGGER.warning(e.toString());
        }
        return null;
    }

    // Создание таблиц в БД, если они отсутствуют
    public static void createTables() {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS website(\n" +
                    "    id INTEGER PRIMARY KEY,\n" +
                    "    url TEXT NOT NULL UNIQUE\n" +
                    ");\n" +
                    "CREATE TABLE IF NOT EXISTS unique_word(\n" +
                    "    id INTEGER PRIMARY KEY,\n" +
                    "    text TEXT NOT NULL,\n" +
                    "    amount INTEGER NOT NULL,\n" +
                    "    website_id INTEGER NOT NULL,\n" +
                    "    FOREIGN KEY(website_id) REFERENCES website(id) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                    ");\n");
        } catch (SQLException e) {
            LOGGER.warning(e.toString());
        }
    }
}
