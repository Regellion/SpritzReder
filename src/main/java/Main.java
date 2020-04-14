//////////////////////////////////////////////////////////////////////////
// Программа поддерживает чтение: txt, rtf, pdf, doc, docx, odt, epub,  //
// fb2, html_file, html_site, string input                              //
// Добавить в ближайшее время:                                          //
// 1) НЕ поддерживается djvu, mobi                                      //
// 2) корректное прочтение длинных строк с отступами                    //
// 3) Проверку возможности чтения рус. названий файлов.                 //
// 4) Проверку возможности извлечения только текста из файлов(картинки) //
// 5) Гуи и кнопки                                                      //
// 6) логгирование                                                      //
// 7) реализовать продолжение чтения с последнего места                 //
// 8) реализовать обучение скорочтению                                  //
// 9) реализовать задания и соревнования                                //
//////////////////////////////////////////////////////////////////////////

import Core.ArrayCreator;
import GUI.MainWindow;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            new MainWindow();
            //TODO сделать чтобы строка с текстом или с путем приходила от гуи вместе со слушателем
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите текст или адресс файла: ");
            String textOrFile = scanner.nextLine();
            long start = System.currentTimeMillis();
            ArrayList<String> arr = new ArrayCreator(textOrFile).getArrayList();

            //TODO сделать проверку на ноль!!!!
            arr.forEach(s -> {
                System.out.println(s);
                try {
                    // Пример задержки, скорость чтени будет ~ 4 слова в секунду
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            //arr.forEach(System.out::println);
            System.out.println(System.currentTimeMillis() - start);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
