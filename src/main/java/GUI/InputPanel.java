package GUI;

import Core.ArrayCreator;
import Core.Loader;
import org.apache.tika.exception.TikaException;
import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    private JLabel infoText;
    private JLabel inputText;
    private JTextField urlText;
    private JButton fileSelectButton;
    private JButton returnButton;
    private JLabel infoTextStringPanel;
    private JTextArea stringText;
    private JButton RSVPButton;
    private JButton spritzButton;

    // Использую HTML тэги для автопереноса строки
    private static final String TAB_HTML = "&nbsp;&nbsp;&nbsp;&nbsp;";
    private static final String START_INFO_TEXT_HTML = "<html><div WIDTH=%d><center>";
    private static final String FINISH_INFO_TEXT_HTML = "</center></div></html>";
    private static final String START_INPUT_TEXT_HTML = "<html><div WIDTH=%d>";
    private static final String FINISH_INPUT_TEXT_HTML = "</div></html>";
    private static final String START_INFO_TEXT_STRING_HTML = "<html><div WIDTH=%d>";
    private static final String FINISH_INFO_TEXT_STRING_HTML = "</div></html>";

    InputPanel(){
        // Ввод инфо текста
        setInfoText();
        // TODO Вывести все сообщения в константы
        // TODO вывести html тэги в константы
        // Выводим краткую инструкцию
        setInputText();
        // Выводим инструкцию для ввода строк
        setInfoTextStringPanel();

        urlText.setText("Это поле для ввода адреса сайта.");
        // Если в однострочное поле ввели текст и нажали enter
        urlText.addActionListener(e -> {
            urlOrFile = urlText.getText().trim();
            //TODO в конце удалить sout
            //TODO логгер ввода
            System.out.println(urlOrFile);
            // TODO сделать 1 метод на 3 кнопки
            try {
                array = ArrayCreator.autoParser(urlOrFile);
                //TODO логгер успеха
                //TODO мб сделать диалоговое окно с выбором методов
                urlText.setText("Для продолжения нажмите кнопку RSVP или Spritz");
                stringText.setText(null);
            } catch (IOException | TikaException ex) {
                ex.printStackTrace();
                //TODO диалоговое окно, говорящее ввести заного
                //TODO логгер неудачи
                urlText.setText("Что то пошло не так, попробуйте ввести адресс заного");
            }
        });
        // Удаляеи информационный текст из поля по клику мышки на поле
        urlText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(urlText.getText().equals("Это поле для ввода адреса сайта.")
                        || urlText.getText().equals("Для продолжения нажмите кнопку RSVP или Spritz")) {
                    urlText.setText(null);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(urlText.getText().equals("")){
                    urlText.setText("Это поле для ввода адреса сайта.");
                }
            }
        });

        // Если выбран файл
        fileSelectButton.setText("Выбрать файл");
        returnButton.setText("Назад");

        // Действие на кнопку выбора файла
        fileSelectButton.addActionListener(e -> {
            JFileChooser fileOpen = new JFileChooser();
            int ret = fileOpen.showDialog(null, "Выбрать файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                //TODO логгер ввода
                File file = fileOpen.getSelectedFile();
                urlOrFile = file.getAbsolutePath().trim();
                //TODO в конце удалить sout
                System.out.println(urlOrFile);
                try {
                    //TODO логгер успеха
                    array = ArrayCreator.autoParser(urlOrFile);
                    urlText.setText(null);
                    stringText.setText("Вы успешно загрузили файл. Для продолжения нажмите кнопку RSVP или Spritz.");
                } catch (IOException | TikaException ex) {
                    ex.printStackTrace();
                    //TODO логгер ошибки
                    //TODO диалоговое окно ошибки
                    stringText.setText("Что то пошло не так, попробуйте загрузить файл заного");
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
        stringText.setText("Это поле для ввода многострочного текста.");
        // если пользователь ввел в мноострочное поле текст и жмет shift + enter, делаем ввод с новой строки
        stringText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER && e.isShiftDown()){
                    stringText.append("\n");
                }
            }
        });


        // Если пользователь нажал enter без shift то присваиваем текст переменной
        stringText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER && !e.isShiftDown()){
                    str = stringText.getText().trim();
                    //TODO логгер ввода
                    //TODO в конце удалить sout
                    System.out.println(str);
                    // Вызываем метод парсинга строки
                    //TODO сделать проверку на успех\неудачу ввода
                    //TODO логгер успеха
                    //TODO логгер неудачи
                    //TODO диалоговое окно неудачи
                    array = ArrayCreator.parserString(str);
                    stringText.setText("Для продолжения нажмите кнопку RSVP или Spritz.");
                    urlText.setText(null);
                    //TODO написать об успехе или неудачи выбора файла
                }
            }
        });

        // Удаляем информационный текст из поля, по клику мышки
        stringText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                //TODO вынести все сообщения в константы
                if (stringText.getText().equals("Это поле для ввода многострочного текста.")
                        || stringText.getText().equals("Для продолжения нажмите кнопку RSVP или Spritz.\n")
                        || stringText.getText().equals("Вы успешно загрузили файл. Для продолжения нажмите кнопку RSVP или Spritz.")) {
                    stringText.setText(null);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(stringText.getText().equals("")){
                    stringText.setText("Это поле для ввода многострочного текста.");
                }
            }
        });

        RSVPButton.addActionListener(e -> {
            if(array != null) {
                array = ArrayCreator.createRSVP(array);
                // здесь передавать списки дальше
                Loader.getMainWindow().getShowPanel().setArray(array);
                //TODO логгер метода парсинга
            } else {
                //TODO логги
            }
        });

        spritzButton.addActionListener(e -> {
            if(array != null) {
                array = ArrayCreator.createSpritz(array);
                // здесь передавать списки дальше
                Loader.getMainWindow().getShowPanel().setArray(array);
                //TODO логгер метода парсинга
            } else {
                //TODO логги
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


    // Устанавливаем инфо текст
    private void setInfoText(){
        infoText.setText(START_INFO_TEXT_HTML + "Осталось всего несколько шагов!" + FINISH_INFO_TEXT_HTML);
    }
    // Устанавливаем текст ввода
    private void setInputText(){
        inputText.setText(START_INPUT_TEXT_HTML + TAB_HTML + "Если вы хотите прочитать текст из файла или сайта, " +
                "то введите в поле ниже адресс сайта или выберите файл нажав соответствующую кнопку" +
                ", после того как вы введете информацию, нажмите кнопку Spritz или RSVP, для чтения " +
                "соответствующим образом:" + FINISH_INPUT_TEXT_HTML);
    }
    // Устанавливаем текст многострочной панели
    private void setInfoTextStringPanel(){
        infoTextStringPanel.setText(START_INFO_TEXT_STRING_HTML + TAB_HTML + "Если вы желаете ввести свой текст, или часть " +
                "скопированного текста, то введите его в поле ниже:" + FINISH_INFO_TEXT_STRING_HTML);
    }

    ArrayList<String> getArray() {
        return array;
    }
}
