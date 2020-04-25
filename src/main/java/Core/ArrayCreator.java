package Core;

import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ArrayCreator {

    // Средняя длина русского слова(7) + 7(на всякий случай), оптимальная длинна слова до 14 символов(чтобы быстро читать)
    private static final int MAX_WORD_LENGTH = 13;
    // Регулярки для клин кода
    private static final String SPACE_REGEX = "\\s";
    private static final String HYPHEN_WORD_REGEX = ".+-.+";
    // определение формата парсинга файлов
    private static final String HTTP_FORMAT_REGEX = "http";
    private static final String TXT = "text/plain";
    private static final String DOC = "application/msword";
    private static final String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    private static final String RTF = "application/rtf";
    private static final String PDF = "application/pdf";
    private static final String ODT = "application/vnd.oasis.opendocument.text";
    private static final String FB2 = "application/x-fictionbook+xml";
    private static final String EPUB = "application/epub+zip";
    private static final String HTML_FILE = "text/html";


    // Метод создания листа строк по методу Spritz
    public static ArrayList<String> createSpritz(ArrayList<String> array){
        if(array == null){
            return null;
        }
        ArrayList<String> splitArray = spliterator(array);
        // проходим по всему массиву
        for (int i = 0; i < splitArray.size(); i++){
            StringBuilder spaces = new StringBuilder();
            // Определяем сколько пробелов нужно в зависимости от длинны слова
            int length = splitArray.get(i).length();
            // Если слово длинной в 1 символ то + 3 пробела
            if(length == 1){
                spaces.append(" ".repeat(3));
                splitArray.set(i, spaces + splitArray.get(i));
                // Если слово длинной от 2 до 6 символов то + 2 пробела
            } else if(length == 2 || length == 3 || length == 4 || length == 5 ){
                spaces.append(" ".repeat(2));
                splitArray.set(i, spaces + splitArray.get(i));
                // Если от 7 до 9 то + пробел
            } else if(length == 6 || length == 7 || length == 8 || length == 9){
                spaces.append(" ".repeat(1));
                splitArray.set(i, spaces + splitArray.get(i));
            }  // В остальных случаях ничего не добавляем
        }
        return splitArray;
    }

    // Метод создания листа строк по методу RSVP
    public static ArrayList<String> createRSVP(ArrayList<String> array){
        if(array == null){
            return null;
        }

        ArrayList<String> splitArray = spliterator(array);
        // получаем новый размер строк
        int maxWordLength = 0;
        if(splitArray.size() != 0) {
            maxWordLength = splitArray.stream().max(Comparator.comparing(String::length)).get().length();
        } else {
            //TODO логгировать как ошибку
            // Мб удалить все иф эльс
            System.out.println("Что то пошло не так с длинной нового arrayList'а! Смотри в ArrayCreator, метод createRSVP");
        }

        StringBuilder spaces = new StringBuilder();
        spaces.append(" ".repeat(maxWordLength / 2));

        ArrayList<String> RSVPtext = new ArrayList<>();
        for (String s : splitArray) {
            String result = spaces + s;
            for (int i = 0; i < s.length() / 2; i++) {
                result = result.substring(1);
            }
            RSVPtext.add(result);
        }
        return RSVPtext;
    }


    // Метод корректного деления строк на подстроки
    private static ArrayList<String> spliterator(ArrayList<String> array){
        // Если на вход передают старый массив, то удаляем у него пробелы
        for (int i = 0; i < array.size(); i++){
            array.set(i,array.get(i).trim());
        }
        // Обходим длинну всех элиментов массива
        for (int i = 0; i < array.size(); i++){
            // Если слово длиннее то
            if(array.get(i).length() > MAX_WORD_LENGTH){
                // Проверка на дефисы
                if(array.get(i).matches(HYPHEN_WORD_REGEX)){
                    String tempString = array.get(i);
                    // дефисные слова делим по дефисам
                    array.set(i, tempString.substring(0, tempString.indexOf('-') + 1));
                    array.add(i + 1, tempString.substring(tempString.indexOf('-') + 1));
                }
                // TODO Попробывать убрать переносы в символьных строках
                // Если слово без дифисов то делим его пополам
                String tempString = array.get(i);
                // Получаем новые слова
                array.set(i, tempString.substring(0, tempString.length() / 2) + "-");
                array.add(i + 1, tempString.substring(tempString.length() / 2));
            }
        }
        // Запускаем рекурсию, если все еще остались слова длиннее нормы
        if(array.stream().max(Comparator.comparing(String::length)).get().length() > MAX_WORD_LENGTH){
            spliterator(array);
        }
        return array;
    }

    // Метод автопарсинга
    public static ArrayList<String> autoParser(String filePath) throws IOException, TikaException {
        ArrayList<String> arr = null;

        Tika tika = new Tika();
        // Определяем тип файла Тики
        String media = tika.detect(filePath);
        // Если текст не является ссылкой
        if(!filePath.startsWith(HTTP_FORMAT_REGEX)) {
            // Если путь не указывает на docx документ то парсим как все файлы
            if (media.equals(PDF) || media.equals(RTF)
                    || media.equals(TXT) || media.equals(ODT)
                    || media.equals(FB2) || media.equals(EPUB)
                    || media.equals(HTML_FILE)) {
                arr = parserAllFiles(filePath);
                // Если путь указывает на docx или doc документы, то парсим отдельными методами
            } else if (media.equals(DOCX)) {
                arr = parserFileDOCX(filePath);
            } else if (media.equals(DOC)){
                arr = parserFileDOC(filePath);
            }
            else {
                // TODO логгируем
                // Ошибка пути или формата файла
                errorFormatFileOrFilePath(filePath);
            }
            // Если текст является ссылкой то парсим как ссылку
        } else {
            arr = parserURL(filePath);
        }
        return arr;
    }

    // Метод парсинга сайтов
    private static ArrayList<String> parserURL(String url) {
        StringBuilder builder = new StringBuilder();
        try {
            Document document = Jsoup.connect(url).maxBodySize(0).userAgent("Opera").get();
            // Ищем блоки с контентом и получаем текст блока
            builder.append(document.select("div[class*='content']").text());
            // Если сайт кривой и не содержит блока с контентом
            if(builder.length() == 0){
                builder.append(document.select("body").text());
                // Смотрим, есть ли на сайте подходящий текст
                if(builder.length() == 0){
                    // Ошибка извлечения текста из файла
                    errorInputURLMessage(url);
                    // TODO логгируем и пишем про неудачу экстракта
                    builder = null;
                }
            }
        } catch (Exception e) {
            // Ошибка подключения к сайту
            errorInputURLMessage(url);
            //TODO логгируем и пишем про неудачу коннекта
            builder = null;
        }
        return getArrayList(builder);
    }

    // Метод обработки .doc файла
    private static ArrayList<String> parserFileDOC(String filePath) throws IOException {
        File file = new File(filePath);
        StringBuilder builder = new StringBuilder();
        if(file.isFile()){
            WordExtractor wordExtractor = new WordExtractor(new FileInputStream(filePath));
            builder.append(wordExtractor.getText());
        }else {
            // TODO логгировать
            // Ошибка формата или пути файла
            errorFormatFileOrFilePath(file.getAbsolutePath());
            builder = null;
        }
        return getArrayList(builder);
    }


    // метод обработки .docx файла
    private static ArrayList<String> parserFileDOCX(String filePath) throws IOException {
        File file = new File(filePath);
        StringBuilder builder = new StringBuilder();
        if(file.isFile()){
            XWPFDocument document = new XWPFDocument(new FileInputStream(filePath));
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            builder.append(extractor.getText());
        }
        else {
            // TODO логгировать
            // Ошибка формата файла
            errorFormatFileOrFilePath(file.getAbsolutePath());
            builder = null;
        }
        return getArrayList(builder);
    }


    // Метод обработки всех остальных файлов
    private static ArrayList<String> parserAllFiles(String filePath) throws IOException, TikaException {
        File file = new File(filePath);
        StringBuilder builder = new StringBuilder();
        return getArrayList(getStringBuilder(file, builder));
    }



    // Метод получения строки из файла
    private static StringBuilder getStringBuilder(File file, StringBuilder builder) throws IOException, TikaException {
        if(file.isFile()){
            if(file.length()==0){
                // TODO логгировать
                // Ошибка пустого файла
                errorFileIsEmptyMessage(file);
                return null;
            }
            Tika tika = new Tika();
            // Размер текста делаем неограниченным
            tika.setMaxStringLength(-1);
            builder.append(tika.parseToString(file));
        }
        else {
            //TODO мб удалить все иф эльсы проверок на файл
            // Логгировать
            System.out.println("Путь: " + file.getAbsolutePath() + " не ведет к файлу!");
            return null;

        }
        return builder;
    }



    // Метод получения ArrayList из StringBuilder
    private static ArrayList<String> getArrayList(StringBuilder builder){
        if(builder == null){
            return null;
        }
        // деление слов по пробелам
        String[] words = builder.toString().split(SPACE_REGEX);
        ArrayList<String> finalWords = new ArrayList<>();
        String[] temp;
        for (String s : words) {
            // если слово с переносом длинное то делим по переносу
            if (s.matches(HYPHEN_WORD_REGEX) && s.length() > MAX_WORD_LENGTH) {
                // Ввожу специальный маркер на второй дефис, чтобы удалялся именно он
                temp = s.replaceAll("-", "-#").split("#");
            } else {
                // если короткое, то добавляем как есть
                temp = s.split(SPACE_REGEX);
            }
            finalWords.addAll(Arrays.asList(temp));
        }
        finalWords.removeIf(word-> word.equals(""));
        return finalWords;
    }


    // Метод обработки строки
    public static ArrayList<String> parserString(String text){
        ArrayList<String> arr = getArrayList(new StringBuilder(text));
        if(text.length() == 0){
            // TODO логгировать
            // Окно ошибки пустого текста от пользователя
            errorInputTextIsEmpty();
            arr = null;
        }
        return arr;
    }

    // Окно ошибки подключения к сайту или получения текста страницы
    private static void errorInputURLMessage(String url){
        String message = "К сожалению, программа не нашла текст по адрессу: \n" + url + "\n" +
                "Проверьте адрес сайта или Ваше подключение к интернету";
        JOptionPane.showConfirmDialog(Loader.getMainWindow(), message, "Parsing error", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    // Окно ошибки формата
    private static void errorFormatFileOrFilePath(String path){
        String format = path.replaceAll(".+\\.", "");
        String message = "К сожалению, программа не нашла текст по адрессу: \n" + path + "\n" +
                "Проверьте адрес или формат выбраного файла!\n" +
                "Формат выбранного файла: " + format;
        JOptionPane.showConfirmDialog(Loader.getMainWindow(), message, "File input error", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    // Окно ошибки "файл пуст"
    private static void errorFileIsEmptyMessage(File file){
        String message = "Файл: \n" + file.getAbsolutePath() + " пуст!";
        JOptionPane.showConfirmDialog(Loader.getMainWindow(), message, "File is empty", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    // Окно ошибки "введенный текст пуст"
    private static void errorInputTextIsEmpty(){
        String message = "Введенный текст не сожержит слов!";
        JOptionPane.showConfirmDialog(Loader.getMainWindow(), message, "String is empty", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
    }
}
