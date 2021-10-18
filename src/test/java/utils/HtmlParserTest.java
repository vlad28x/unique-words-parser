package utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


class HtmlParserTest {

    private static HtmlParser htmlParser;
    private static UniqueWordsContainer uwContainer;

    @BeforeAll
    static void init() throws MalformedURLException {
        htmlParser = new HtmlParser("https://vlad28x.github.io/2/2.html");
        // Массив всех слов с web-страницы https://vlad28x.github.io/2/2.html. Все слова спарсены вручную
        String[] arrayStrings = {"Справочник", "по", "языку", "C", "Введение", "Процесс", "трансляции", "Целые", "числа",
                "Числа", "с", "плавающей", "запятой", "Указатели", "и", "ссылки", "Google", "Целочисленный", "тип", "данных",
                "это", "тип", "переменные", "которого", "могут", "содержать", "только", "целые", "числа", "без", "дробной",
                "части", "например", "В", "языке", "C", "есть", "основных", "целочисленных", "типов", "доступных", "для",
                "использования", "Категория", "Тип", "Минимальный", "размер", "Символьный", "тип", "данных", "char", "байт",
                "Целочисленный", "тип", "данных", "short", "байта", "int", "байта", "но", "чаще", "всего", "байта", "long",
                "байта", "long", "long", "байт", "Определение", "происходит", "следующим", "образом", "char", "c", "short",
                "int", "si", "допустимо", "short", "s", "предпочтительнее", "int", "i", "long", "int", "li", "допустимо",
                "long", "l", "предпочтительнее", "long", "long", "int", "lli", "допустимо", "long", "long", "ll", "предпочтительнее",
                "Скибин", "Владислав", "по", "всем", "вопросам", "пишите", "по", "адресу", "vladskibin", "bk", "ru"};
        uwContainer = new UniqueWordsContainer();
        uwContainer.addAll(Arrays.asList(arrayStrings));// Map уникальных слов и их количество на web-странице
    }

    @Test
    void HtmlParserInvalidURL() {
        try {
            new HtmlParser("invalidURL");
            fail("Expected exception not thrown");
        } catch (MalformedURLException e) {
            //pass
        }
    }

    @Test
    void HtmlParserValidURL() {
        try {
            new HtmlParser("https://vlad28x.github.io/2/2.html");
        } catch (MalformedURLException e) {
            fail("Exception thrown");
        }
    }

    @Test
    void testGetURL() {
        assertEquals("HTTPS://VLAD28X.GITHUB.IO/2/2.HTML", htmlParser.getURL());
    }

    @Test
    void getUniqueWordsByRegex() {
        assertEquals(uwContainer, htmlParser.getUniqueWordsByRegex());
    }

    @Test
    void getUniqueWordsByJsoup() {
        assertEquals(uwContainer, htmlParser.getUniqueWordsByJsoup());
    }
}