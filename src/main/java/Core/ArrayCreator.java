package Core;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ArrayCreator {
    //TODO сделать так чтобы значение задавалось через формы
    private boolean isSpritz = true;
    // Средняя длина русского слова(7) + 7(на всякий случай), оптимальная длинна слова до 14 символов(чтобы быстро читать)
    private static final int MAX_WORD_LENGTH = 13;
    private ArrayList<String> arrayList;
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
    //private static final String MOBI = "application/x-mobipocket-ebook";
    private static final String EPUB = "application/epub+zip";
    private static final String HTML_FILE = "text/html";

    public ArrayCreator(String textOrFile) throws IOException, TikaException {
        if(isSpritz) {
            //TODO сдеать выбор зависящий от слушателя метода выбора
            arrayList = createSpritz(ArrayCreator.autoParser(textOrFile));
        } else {
            arrayList = createRSVP(ArrayCreator.autoParser(textOrFile));
        }
    }

    // Метод создания листа строк по методу Spritz
    private static ArrayList<String> createSpritz(ArrayList<String> array){
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
            }   // В остальных случаях ничего не добавляем
        }
        return splitArray;
    }

    // Метод создания листа строк по методу RSVP
    private static ArrayList<String> createRSVP(ArrayList<String> array){
        if(array == null){
            return null;
        }
        ArrayList<String> splitArray = spliterator(array);
        // получаем новый размер строк
        int maxWordLength = 0;

        if(splitArray.size() != 0) {
            maxWordLength = splitArray.stream().max(Comparator.comparing(String::length)).get().length();
        } else {
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
    private static ArrayList<String> autoParser(String filePath) throws IOException, TikaException {
        ArrayList<String> arr;

        //TODO проверять слушателя на путь к файлу
        Tika tika = new Tika();
        String media = tika.detect(filePath);
        if(!filePath.startsWith(HTTP_FORMAT_REGEX)) {
            if (media.equals(PDF) || media.equals(RTF)
                    || media.equals(TXT) || media.equals(DOC)
                    || media.equals(ODT) || media.equals(FB2)
                    || media.equals(EPUB) || media.equals(HTML_FILE)
                /*TODO моби распарсить*/) {
                arr = parserAllFiles(filePath);
            } else if (media.equals(DOCX)) {
                arr = parserFileDOCX(filePath);
            } else {
                System.out.println("Такой формат временно не поддерживается!");
                arr = null;
            }
        }else {
            arr = parserURL(filePath);
        }

        //TODO проверять слушателя на ввод строки
        if(media.equals(0)){
            arr = parserString(filePath);
        } else {
            System.out.println("Это не строка!");
        }


        return arr;
    }

    // TODO допилить метод парсинга моби
    // Метод парсинга .mobi файлов


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
                    System.out.println("К сожалению, программа не нашла текст по указанной ссылке.");
                    return null;
                }
            }
        } catch (IOException e) {
            System.out.println("Ссылка не ведет к сайту или документу!");
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
            System.out.println("Путь: " + filePath + " не ведет к файлу!");
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
                System.out.println("Ваш файл пуст!");
                return null;
            }
            Tika tika = new Tika();
            tika.setMaxStringLength(-1);
            builder.append(tika.parseToString(file));
        }
        else {
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


    // TODO доделать метод чтобы принимал строку с отступами
    // Метод обработки строки
    private static ArrayList<String> parserString(String text){
        String[] arr = text.split(SPACE_REGEX);
        if(text.length() == 0){
            System.out.println("Ваш текст не сожержит слов!");
        }
        return new ArrayList<>(Arrays.asList(arr));
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    public boolean isSpritz() {
        return isSpritz;
    }

    public void setSpritz(boolean spritz) {
        isSpritz = spritz;
    }
}
