#Unique words parser

##Краткое описание

Unique words parser позволяет парсить произвольную HTML-страницу и выдает статистику по
количеству уникальных слов. Используется интерфейс командной строки.

##Используемые технологии

При разработке были использованы следующие технологии:
1. Java 1.8;
2. СУБД SQLite;
3. Библиотеки:
   * Jsoup 1.14.3 - для парсинга HTML-страниц;
   * sqlite-jdbc 3.36.0.3 - для доступа и создания файлов базы данных SQLite на Java.;
   * Junit 5.0 - для тестирования.

##Используемые паттерны

Для абстрагирования и инкапсулирования доступа к БД был использован паттерн DAO (Data Access Object).

##Особенности

1. Логирование посредством встроенного в Java логгера;
2. Сохранение статистики в БД.

##Использование

###Класс UniqueWordsContainer

Этот класс является оберткой для набора пар "слово-частота". Содержит удобный метод для добавления слов, 
который автоматически считает частоту встречаемости слов, при этом все слова переводятся в верхний регистр.

    word = word.toUpperCase(Locale.ROOT);
    ap.put(word, map.getOrDefault(word, 0) + 1);

###Класс HtmlParser

В следующих методах приняты соглашения: 
1. Набор цифр, который не является частью слова - не считается словом
   ("Битрикс24" - это отдельное слово, "88-44-33" - не является словом);
2. Слова, содержащие дефис (тире) не будут разделяться, т.е. слово "IT-проект" будет считаться одним словом. 
   Но если перед или после дефиса (тире) будут пробелы, то это соглашение не работает, так как будет считаться, что
   дефис (тире) выступают в роли знака препинания;
3. Разбитие текста на отдельные слова происходит при помощи списка разделителей;
4. Некоторые специфические символы будут считаться словами, например: "•".

####getUniqueWordsByRegex
Метод парсит HTML-страницу при помощи написанных вручную регулярных выражений и UniqueWordsContainer.
Метод рассматривает HTML-мнемоники и HTML-коды вида "&#***;" как отдельные слова в их исходном представлении.

####getUniqueWordsByJsoup
Метод парсит HTML при помощи библиотеки Jsoup и возвращает UniqueWordsContainer. 
Метод рассматривает HTML-мнемоники и HTML-коды вида "&#***;" как отдельные символы, которые соответствуют этим кодам.

Следующий кусок HTML-страницы спарсится по-разному:

    <p>IT&#8209;продукт</p>

getUniqueWordsByRegex - {"IT", "продукт"};
<br>
getUniqueWordsByJsoup - {IT‑продукт}.