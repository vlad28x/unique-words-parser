package impls;

import DAO.UniqueWordDAO;
import db.SQLiteConnection;
import models.UniqueWord;
import models.Website;
import utils.UniqueWordsContainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

public class DBUniqueWord implements UniqueWordDAO {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public boolean add(UniqueWord object) {
        try (Connection connection = SQLiteConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO unique_word(text, amount, website_id) VALUES (?, ?, ?)")) {
            statement.setString(1, object.getText());
            statement.setInt(2, object.getAmount());
            statement.setInt(3, object.getWebsiteId());
            if (statement.executeUpdate() > 0) {
                object.setId(statement.getGeneratedKeys().getInt(1));
                return true;
            }
        } catch (SQLException e) {
            LOGGER.warning(e.toString());
        }
        return false;
    }

    @Override
    public boolean addAll(UniqueWordsContainer uniqueWordsContainer, Website website) {
        for (Map.Entry<String, Integer> entry : uniqueWordsContainer.getSetOfEntry()) {
            if (!add(new UniqueWord(entry.getKey(), entry.getValue(), website.getId()))) return false;
        }
        return true;
    }

    @Override
    public boolean update(UniqueWord object) {
        try (Connection connection = SQLiteConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE unique_word SET text=?, amount=?, website_id=? WHERE id=?")) {
            statement.setString(1, object.getText());
            statement.setInt(2, object.getAmount());
            statement.setInt(3, object.getWebsiteId());
            statement.setInt(4, object.getId());
            if (statement.executeUpdate() > 0) return true;
        } catch (SQLException e) {
            LOGGER.warning(e.toString());
        }
        return false;
    }

    @Override
    public boolean delete(UniqueWord object) {
        try (Connection connection = SQLiteConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM unique_word WHERE id=?")) {
            statement.setInt(1, object.getId());
            if (statement.executeUpdate() > 0) return true;
        } catch (SQLException e) {
            LOGGER.warning(e.toString());
        }
        return false;
    }

    @Override
    public UniqueWordsContainer getUniqueWordsFromWebsite(Website object) {
        UniqueWordsContainer uwContainer = new UniqueWordsContainer();
        try (Connection connection = SQLiteConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT text, amount FROM unique_word WHERE website_id=?")) {
            statement.setInt(1, object.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                uwContainer.put(resultSet.getString("text"), resultSet.getInt("amount"));
            }
        } catch (SQLException e) {
            LOGGER.warning(e.toString());
        }
        return uwContainer;
    }
}
