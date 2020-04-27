//////////////////////////////////////////////////////////////////////////
// Программа поддерживает чтение: txt, rtf, pdf, doc, docx, odt, epub,  //
// fb2, html_file, html_site, string input                              //
// Добавить в ближайшее время:                                          //
// 1) НЕ поддерживается djvu, mobi                                      //
// 2) Проверку возможности извлечения только текста из файлов(картинки) //
// 3) логгирование                                                      //
// 4) реализовать продолжение чтения с последнего места                 //
// 5) v 2.0 реализовать обучение скорочтению                            //
// 6) v 2.5 реализовать задания и соревнования                          //
//////////////////////////////////////////////////////////////////////////


import Core.Loader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Main {
    //TODO попробовать составлять лист в отдельном потоке
    // сделать кнопки тнр 14 жирным
    // сделать логгирование по завершению программы
    // попробовать избавиться от лодера
    // ВЫДЕЛИТЬ ВСЕ ДЕЙСТВИЯ С ВИЗУАЛЬНЫМИ ЭЛЕМЕНТАМИ В ОТДЕЛЬНЫЕ БЛОКИ, ПРЕДВАРИТЕЛЬНО СГРУППИРОВАВ ИХ
    // Нормально сгруппировать методы во всех классах
    public static void main(String[] args) {
            new Loader();


        /*File saveFile = new File("save/тест файл.fr");
        // метод загрузки
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(getJsonFile(saveFile.getAbsolutePath()));
        SaveFile saveFileNew = new SaveFile(
                (String) jsonObject.get("nameSave"),
                (ArrayList<String>) jsonObject.get("dataArray"));

        System.out.println(saveFileNew.getNameSave());
        saveFileNew.getDataArray().forEach(e-> System.out.println(e));*/
    }

    private static String getJsonFile(String fileName) throws IOException {
        StringBuilder builder = new StringBuilder();

        List<String> lines = Files.readAllLines(Paths.get(fileName));
        lines.forEach(builder::append);

        return builder.toString();
    }
}
