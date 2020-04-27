package GUI;

import Core.ArrayCreator;
import Core.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
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

    private static Logger logger = LogManager.getLogger(InputPanel.class);
    private static final Marker INPUT_URL = MarkerManager.getMarker("INPUTS");
    private static final Marker INPUT_FILE = MarkerManager.getMarker("INPUTS");
    private static final Marker INPUT_TEXT = MarkerManager.getMarker("INPUTS");
    private static final Marker SELECT_RSVP = MarkerManager.getMarker("INPUTS");
    private static final Marker SELECT_SPRITZ = MarkerManager.getMarker("INPUTS");
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
            String message = urlText.getText();
            // Логгер ввода URL
            logger.info(INPUT_URL, "Input: " + message);
            // TODO сделать 1 метод на 3 кнопки
            try {
                array = ArrayCreator.autoParser(urlOrFile);
                if (array == null){
                    stringText.setText("Что то пошло не так, попробуйте ввести адресс заного.");
                }else {
                    stringText.setText("Вы ввели: " + urlOrFile + " Для продолжения нажмите кнопку RSVP или Spritz.\n");
                }
                logger.info(INPUT_URL, "Input completed.");
                urlText.setText(null);
            } catch (IOException | TikaException ex) {
                ex.printStackTrace();
                // Логгер неудачного ввода URL
                logger.warn(INPUT_URL, "Input is failed.");

            }
        });
        // Удаляем информационный текст из поля по клику мышки на поле
        urlText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(urlText.getText().equals("Это поле для ввода адреса сайта.")) {
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
                File file = fileOpen.getSelectedFile();
                urlOrFile = file.getAbsolutePath().trim();
                // Логгер выбора файла
                logger.info(INPUT_FILE, "Input file: " + file.getAbsolutePath());
                try {
                    stringText.setText("Вы успешно загрузили файл: " + file.getName() + "\nДля продолжения нажмите кнопку RSVP или Spritz.\n");
                    array = ArrayCreator.autoParser(urlOrFile);
                    if(array == null){
                        // Логгер неудачного выбора файла
                        logger.warn(INPUT_FILE, "Input file is failed.");
                    } else {
                        // Логгер успешного выбора файла
                        logger.info(INPUT_FILE, "Input file completed.");
                    }
                    urlText.setText(null);
                } catch (IOException | TikaException ex) {
                    // Логгер неудачного выбора файла
                    logger.warn(INPUT_FILE, "Input file is failed.");
                }
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
                    // Логгер введения текста
                    logger.info(INPUT_TEXT, "Input text.");
                    // Вызываем метод парсинга строки
                    array = ArrayCreator.parserString(str);
                    if (array == null){
                        stringText.setText("Что то пошло не так, попробуйте ввести текст заного.");
                        // Логгер неверного ввода текста
                        logger.warn(INPUT_TEXT, "Input text is failed.");
                    }else {
                        stringText.setText("Вы ввели свой текст. Для продолжения нажмите кнопку RSVP или Spritz.");
                        // Логгер успешного ввода текста
                        logger.info(INPUT_TEXT, "Input text completed.");
                    }
                    urlText.setText(null);
                }
            }
        });

        // Удаляем информационный текст из поля, по клику мышки
        stringText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                //TODO вынести все сообщения в константы
                if (stringText.getText().equals("Это поле для ввода многострочного текста.")
                        || stringText.getText().endsWith("Для продолжения нажмите кнопку RSVP или Spritz.\n")
                        || stringText.getText().equals("Вы успешно загрузили файл. Для продолжения нажмите кнопку RSVP или Spritz.")
                        || stringText.getText().startsWith("Что то пошло не так")) {
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
            // Логгер выбора метода парсинга
            logger.info(SELECT_RSVP, "Select RSVP method.");
            if(array != null) {
                array = ArrayCreator.createRSVP(array);
                // здесь передавать списки дальше
                Loader.getMainWindow().getShowPanel().setArray(array);
                // Логгер успеха парсинга
                logger.info(SELECT_RSVP, "Parsing completed.");
            } else {
                // Логгер неудачи парсинга
                logger.warn(SELECT_RSVP, "Parsing is failed.");
            }
        });

        spritzButton.addActionListener(e -> {
            // Логгер выбора метода парсинга
            logger.info(SELECT_SPRITZ, "Select spritz method.");
            if(array != null) {
                array = ArrayCreator.createSpritz(array);
                // здесь передавать списки дальше
                Loader.getMainWindow().getShowPanel().setArray(array);
                // Логгер удачного парсинга
                logger.info(SELECT_SPRITZ, "Parsing completed.");
            } else {
                // Логгер неудачного парсинга
                logger.warn(SELECT_SPRITZ, "Parsing is failed.");
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


    public void setStringText(String stringText) {
        this.stringText.setText(stringText);
    }

    // Устанавливаем инфо текст
    private void setInfoText(){
        infoText.setText(START_INFO_TEXT_HTML + "Осталось всего несколько шагов!" + FINISH_INFO_TEXT_HTML);
    }

    // Устанавливаем текст ввода
    private void setInputText(){
        // TODO см текст
        inputText.setText(START_INPUT_TEXT_HTML + TAB_HTML + "Если вы хотите прочитать текст из файла или сайта, " +
                "то введите в поле ниже адресс сайта или выберите файл нажав соответствующую кнопку" +
                ", после того как вы введете информацию, нажмите кнопку Spritz или RSVP, для чтения " +
                "соответствующим образом." +
                "<br>НАПИСАТЬ ПРО ФОРМАТЫ СДЕЛАТЬ ПОДРОБНУЮ ИНСТРУКЦИЮ КАК ЧТО ВВОДИТЬ И КУДА: doc, docx, epub, fb2, html, odt, pdf, rtf, txt.<br>" + FINISH_INPUT_TEXT_HTML);
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
