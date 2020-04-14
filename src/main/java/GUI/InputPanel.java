package GUI;

import javax.swing.*;

import java.io.File;


public class InputPanel extends FormPanel{
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
    // Использую HTML тэги для автопереноса строки
    private static final String START_STRING_HTML = "<html><div WIDTH=%d><center>";
    private static final String FINISH_STRING_HTML = "</center></div></html>";

    InputPanel(){
        this.printWelcomeText("");
        // Выводим краткую инструкцию
        inputText.setText("<html><div WIDTH=%d>Если вы хотите прочитать текст из файла или сайта, " +
                "то введите в поле ниже адресс сайта или выберите файл нажав соответствующую кнопку:</div></html>");
        // Выводим инструкцию для ввода строк
        infoTextStringPanel.setText("<html><div WIDTH=%d>Если вы желаете ввести свой текст, или часть " +
                "скопированного текста, то введите его в поле ниже:</div></html>");
        // Если в однострочное поле ввели текст и нажали enter

        urlText.addActionListener(e -> {
            urlOrFile = urlText.getText().trim();
            //TODO если активировано то надо на 3ю панель переходить
            System.out.println(urlOrFile);
            urlText.setText("");
        });
        // Если выбран файл
        fileSelectButton.setText("Выбрать файл");
        stringButton.setText("Ввести текст");
        returnButton.setText("Назад");
        fileSelectButton.addActionListener(e -> {
            JFileChooser fileopen = new JFileChooser();
            int ret = fileopen.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                urlOrFile = file.getAbsolutePath().trim();
                //TODO если активировано то надо на 3ю панель переходить
                System.out.println(urlOrFile);
            }
        });

        // Если ввод нескольких строк
        // Устанавливаем человеческую табуляцию
        stringText.setTabSize(4);
        // Определяем перенос строк
        stringText.setLineWrap(true);
        stringText.setWrapStyleWord(true);
        // Подключаем слушателя
        stringButton.addActionListener(e -> {
            str = stringText.getText().trim();
            //TODO если активировано то надо на 3ю панель переходить
            System.out.println(str);
            stringText.setText("");
        });
        returnButton.addActionListener(e -> {

        });
    }

    public JPanel getPanel() {
        return mainInputPanel;
    }

    public JButton getReturnButton() {
        return returnButton;
    }

    private JLabel getInfoText() {
        return this.infoText;
    }

    public void printWelcomeText(String method){
        // выводим строку которая показывает выбранный метод
        getInfoText().setText(START_STRING_HTML + "Вы выбрали метод скорочтения "+ method +
                ". Осталось всего несколько шагов и мы начинаем!" + FINISH_STRING_HTML);
    }
}
