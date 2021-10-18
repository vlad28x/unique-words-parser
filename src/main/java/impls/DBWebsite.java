package impls;

import DAO.WebsiteDAO;
import db.SQLiteConnection;
import models.Website;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DBWebsite implements WebsiteDAO {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public boolean add(Website object) {
        try (Connection connection = SQLiteConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO website(url) values (?)")) {
            statement.setString(1, object.getUrl());
            if (statement.executeUpdate() > 0) {
                object.setId(statement.getGeneratedKeys().getInt(1));
                return true;
            }
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Website object) {
        try (Connection connection = SQLiteConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE website SET url=? WHERE id=?")) {
            statement.setString(1, object.getUrl());
            statement.setInt(2, object.getId());
            if (statement.executeUpdate() > 0) return true;
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Website object) {
        try (Connection connection = SQLiteConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM website WHERE url=?")) {
            statement.setString(1, object.getUrl());
            if (statement.executeUpdate() > 0) return true;
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
        }
        return false;
    }

    @Override
    public List<Website> findAll() {
        ArrayList<Website> websiteList = new ArrayList<>();
        try (Connection connection = SQLiteConnection.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM website");
            while (resultSet.next()) {
                websiteList.add(new Website(resultSet.getInt("id"), resultSet.getString("url")));
            }
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
        }
        return websiteList;
    }

    @Override
    public List<Website> find(String text) {
        ArrayList<Website> websiteList = new ArrayList<>();
        try (Connection connection = SQLiteConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM website WHERE url=?")) {
            statement.setString(1, text);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                websiteList.add(new Website(resultSet.getInt("id"), resultSet.getString("url")));
            }
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
        }
        return websiteList;
    }
}
