package DAO;

import models.Website;

import java.util.List;

public interface WebsiteDAO extends GeneralDAO<Website> {
    List<Website> findAll();

    List<Website> find(String text);
}
