package controllers;

import db.SQLiteConnection;
import impls.DBUniqueWord;
import impls.DBWebsite;
import models.Website;
import utils.HtmlParser;
import utils.UniqueWordsContainer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.*;


public class MainController {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final DBWebsite dbWebsite = new DBWebsite();
    private static final DBUniqueWord dbUniqueWord = new DBUniqueWord();
    private static final Scanner scanner = new Scanner(System.in);
    private static String url, choice = null;
    private static List<Website> websiteArrayList = null;
    private static UniqueWordsContainer uwContainer = null;
    private static HtmlParser htmlParser = null;

    public static void main(String[] args) {
        initLogger();
        SQLiteConnection.createTables();// Создание таблиц в БД, если они отсутствуют
        LOGGER.info("Программа запущена");
        while (true) {
            System.out.println("Введите корректный адрес web-страницы для получения статистики по количеству уникальных слов.\n" +
                    "Для выхода введите - q:");
            url = scanner.next();
            LOGGER.info("Введен URL-адрес: " + url);
            if (url.equals("q")) break;
            try {
                htmlParser = new HtmlParser(url);
                websiteArrayList = dbWebsite.find(htmlParser.getURL());
                if (websiteArrayList.size() > 0) {
                    System.out.println("В БД уже имеются уникальные слова с этого сайта.\n" +
                            "1 - получить уникальные слова при момощи методов (default);\n" +
                            "2 - получить уникальные слова с БД.");
                    choice = scanner.next();
                    if (choice.equals("2")) {
                        uwContainer = dbUniqueWord.getUniqueWordsFromWebsite(websiteArrayList.get(0));
                        LOGGER.info("Получен список уникальных слов с сайта " + url + " из БД");
                        System.out.println("Список уникальных слов:");
                        uwContainer.print();
                    } else methodsChoice();
                } else methodsChoice();
            } catch (MalformedURLException e) {
                LOGGER.warning(e.toString());
                System.out.println("Задан не верный URL-адрес.");
            }
        }
        LOGGER.info("Программа завершена");
    }

    // Иницмализация логгера
    private static void initLogger() {
        try {
            Handler fileHandler = new FileHandler("log.txt", true);// Файловый обработчик
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return "[" + new Date(record.getMillis()) + "] " + record.getLevel().toString() + ": " + record.getMessage() + "\n";
                }
            });// Добавляем свое форматирование
            LOGGER.addHandler(fileHandler);
            LOGGER.setUseParentHandlers(false);// Записи не отправляются в родительский обработчик
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void methodsChoice() {
        System.out.println("1 - парсинг страницы при помощи Jsoup (default);\n" +
                "2 - парсинг страницы при помощи regex.");
        choice = scanner.next();
        if (choice.equals("2")) {
            uwContainer = htmlParser.getUniqueWordsByRegex();
        } else {
            uwContainer = htmlParser.getUniqueWordsByJsoup();
        }
        if (uwContainer != null) {
            LOGGER.info("Получен список уникальных слов с сайта " + url + " " +
                    "при помощи " + (choice.equals("2") ? "регулярных выражений" : "Jsoup"));
            if (websiteArrayList.size() > 0) dbWebsite.delete(websiteArrayList.get(0));
            Website newWebsite = new Website(htmlParser.getURL());
            dbWebsite.add(newWebsite);
            dbUniqueWord.addAll(uwContainer, newWebsite);
            System.out.println("Список уникальных слов:");
            uwContainer.print();
        } else System.out.println("Невозможно получить HTML-страницу сайта " + url);
    }
}