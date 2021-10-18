package DAO;

import models.UniqueWord;
import models.Website;
import utils.UniqueWordsContainer;

public interface UniqueWordDAO extends GeneralDAO<UniqueWord> {
    boolean addAll(UniqueWordsContainer uniqueWordsContainer, Website website);

    UniqueWordsContainer getUniqueWordsFromWebsite(Website object);
}
