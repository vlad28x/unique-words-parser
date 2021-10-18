package utils;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// Класс для парсинга уникальных слов с web-страниц
public class HtmlParser {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final URL URL;

    public HtmlParser(URL URL) {
        this.URL = URL;
    }

    public HtmlParser(String url) throws MalformedURLException {
        URL = new URL(url);
    }

    public String getURL() {
        //Всегда в конце URL не будет слеша и вернется строка в верхнем регистре, для удобной работы с БД
        return (URL.getProtocol() + "://" + URL.getHost() + URL.getPath()).replaceAll("/+$", "").toUpperCase(Locale.ROOT);
    }

    // Метод парсит HTML при помощи регулярных выражений.
    // Результат иногда незначительно отличается от метода getUniqueWordsByJsoup.
    // Это связано с различными способами парсинга.
    public UniqueWordsContainer getUniqueWordsByRegex() {
        UniqueWordsContainer uwContainer = new UniqueWordsContainer();
        String text = getHtmlText();
        if (text == null) return null;
        Matcher matcher = Pattern.compile("<body.*?</body>", Pattern.DOTALL).matcher(text);
        if (matcher.find()) text = matcher.group();// Получаем строку вида <body>...</body>
        matcher = Pattern.compile("<script.*?</script>|<noscript.*?</noscript>|<.+?>", Pattern.DOTALL).matcher(text);
        text = matcher.replaceAll(" ");// Удаляем все теги из строки
        text = removeDigitsAndDash(text);
        matcher = Pattern.compile("^ ").matcher(text);
        text = matcher.replaceAll("");// Удаляем в начале строки пробел, если он имеется
        List<String> stringArray = split(text);
        uwContainer.addAll(stringArray);
        return uwContainer;
    }

    // Метод парсит HTML при помощи библиотеки Jsoup.
    // Результат иногда незначительно отличается от метода getUniqueWordsByRegex.
    // Это связано с различными способами парсинга.
    public UniqueWordsContainer getUniqueWordsByJsoup() {
        UniqueWordsContainer uwContainer = new UniqueWordsContainer();
        String text = getHtmlText();
        if (text == null) return null;
        text = Jsoup.parse(text).select("body").text();// Парсинг содержимого тега body
        text = removeDigitsAndDash(text);
        List<String> stringArray = split(text);
        uwContainer.addAll(stringArray);
        return uwContainer;
    }

    // Метод возвращает HTML текст, полученный с web-страницы
    private String getHtmlText() {
        String text = null;
        try (Scanner scanner = new Scanner(URL.openStream())) {
            scanner.useDelimiter("\\Z");
            text = scanner.next();
        } catch (IOException e) {
            LOGGER.warning(e.toString());
        }
        return text;
    }

    // Метод заменяет все цифры (в словах, содержащих цифры, они не удаляются) и
    // выражения вида "---" " — ", которые могут возникнуть после удаления цифр, а также специальные символы на " ".
    // В словах, содержащих дефис, дефис не будет заменятся.
    // К примеру слово "по-другому" будет считаться одним целым словом, а не двумя разными "по" и "другому".
    //
    private String removeDigitsAndDash(String text) {
        Matcher matcher = Pattern.compile("\\s+|\\b\\d+\\b", Pattern.DOTALL).matcher(text);
        text = matcher.replaceAll(" ");
        matcher = Pattern.compile(" -+|-+ | —+|—+ | –+|–+ | −+|−+ |©|°").matcher(text);
        return matcher.replaceAll(" ");
    }

    // Метод возвращает динамический массив из строки при помощи списка разделителей
    private List<String> split(String text) {
        return new ArrayList<>(Arrays.asList(text.split("[\\Q  .,!?\"';:][()«»{}/\\|+=*<>%$#@^_&\\E]+")));
    }
}
