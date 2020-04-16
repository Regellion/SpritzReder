package GUI;

import Core.ArrayCreator;
import Core.Loader;
import org.apache.tika.exception.TikaException;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class InputPanel extends FormPanel{
    private ArrayList<String> array;
    // Файл или url
    private static String urlOrFile;
    // Свой ввод текста
    private static String str;


    private JPanel mainInputPanel;
    private JPanel infoPanel;
    private JLabel infoText;
    private JPanel inputInputPanel;
    private JLabel inputText;
    private JPanel selectionPanel;
    private JTextField urlText;
    private JButton fileSelectButton;
    private JPanel buttonFileSelect;
    private JPanel buttonPanel;
    private JButton returnButton;
    private JPanel stringPanel;
    private JLabel infoTextStringPanel;
    private JTextArea stringText;
    private JButton stringButton;
    private JButton RSVPButton;
    private JButton spritzButton;
    // Использую HTML тэги для автопереноса строки
    private static final String START_STRING_HTML = "<html><div WIDTH=%d><center>";
    private static final String FINISH_STRING_HTML = "</center></div></html>";

    InputPanel(){
        // Ввод инфо текста
        infoText.setText(START_STRING_HTML + "Осталось всего несколько шагов и мы начинаем!" + FINISH_STRING_HTML);

        // Выводим краткую инструкцию
        inputText.setText("<html><div WIDTH=%d>Если вы хотите прочитать текст из файла или сайта, " +
                "то введите в поле ниже адресс сайта или выберите файл нажав соответствующую кнопку" +
                ", после того как вы введете информацию, нажмите кнопку Spritz или RSVP, для чтения " +
                "соответствующим образом:</div></html>");
        // Выводим инструкцию для ввода строк
        infoTextStringPanel.setText("<html><div WIDTH=%d>Если вы желаете ввести свой текст, или часть " +
                "скопированного текста, то введите его в поле ниже:</div></html>");

        // Если в однострочное поле ввели текст и нажали enter
        urlText.addActionListener(e -> {
            urlOrFile = urlText.getText().trim();
            //TODO если активировано то надо на 3ю панель переходить
            System.out.println(urlOrFile);
            // TODO сделать 1 метод на 3 кнопки
            try {
                array = ArrayCreator.autoParser(urlOrFile);
            } catch (IOException | TikaException ex) {
                ex.printStackTrace();
            }
            //TODO написать об успехе или неудачи ввода адреса сайта

            urlText.setText("");
        });
        // Если выбран файл
        fileSelectButton.setText("Выбрать файл");
        stringButton.setText("Ввести текст");
        returnButton.setText("Назад");
        // Действие на кнопку выбора файла
        fileSelectButton.addActionListener(e -> {
            JFileChooser fileopen = new JFileChooser();
            int ret = fileopen.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                urlOrFile = file.getAbsolutePath().trim();
                //TODO если активировано то надо на 3ю панель переходить
                System.out.println(urlOrFile);
                try {
                    array = ArrayCreator.autoParser(urlOrFile);
                } catch (IOException | TikaException ex) {
                    ex.printStackTrace();
                }
                //TODO написать об успехе или неудачи выбора файла
            }
        });

        // Если ввод нескольких строк
        // Устанавливаем человеческую табуляцию
        stringText.setTabSize(4);
        // Определяем перенос строк
        stringText.setLineWrap(true);
        stringText.setWrapStyleWord(true);
        // Подключаем слушателя на многострочное поле ввода текста
        stringButton.addActionListener(e -> {
            str = stringText.getText().trim();
            //TODO если активировано то надо на 3ю панель переходить
            System.out.println(str);

            // Вызываем метод парсинга строки
            array = ArrayCreator.parserString(str);
            stringText.setText("");
            //TODO написать об успехе или неудачи выбора файла

        });

        RSVPButton.addActionListener(e -> {
            if(array != null) {
                array = ArrayCreator.createRSVP(array);
                // здесь передавать списки дальше
                Loader.getMainWindow().getShowPanel().setArray(array);
            } else {
                //TODO потом сделать окно предупреждения
                System.out.println("Вы оставили все поля пустыми!");
            }

        });

        spritzButton.addActionListener(e -> {
            if(array != null) {
                array = ArrayCreator.createSpritz(array);
                // здесь передавать списки дальше
                Loader.getMainWindow().getShowPanel().setArray(array);
            } else {
                //TODO потом сделать окно предупреждения
                System.out.println("Вы оставили все поля пустыми!");
            }
        });


    }

    public JPanel getPanel() {
        return mainInputPanel;
    }

    JButton getReturnButton() {
        return returnButton;
    }

    JButton getRSVPButton() {
        return RSVPButton;
    }

    JButton getSpritzButton() {
        return spritzButton;
    }

    /*public ArrayList<String> getArray() {
        return array;
    }*/


}
