package utils;

import java.util.*;

// Класс для удобного добавления уникальных слов в Map с автоматическим подсчетом их количества
public class UniqueWordsContainer {
    private final Map<String, Integer> map;

    public UniqueWordsContainer() {
        map = new TreeMap<>();
    }

    public void add(String word) {
        word = word.toUpperCase(Locale.ROOT);
        map.put(word, map.getOrDefault(word, 0) + 1);
    }

    public void put(String word, Integer amount) {
        map.put(word, amount);
    }

    public void addAll(Collection<String> collection) {
        for (String s : collection) this.add(s);
    }

    public Set<Map.Entry<String, Integer>> getSetOfEntry() {
        return map.entrySet();
    }

    public void print() {
        for (Map.Entry<String, Integer> entry : getSetOfEntry())
            System.out.println(entry.getKey() + " - " + entry.getValue());
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniqueWordsContainer that = (UniqueWordsContainer) o;
        return map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
