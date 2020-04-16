package GUI;

import Core.Printer;

import javax.swing.*;
import java.util.ArrayList;

public class ShowPanel extends FormPanel{

    private JPanel mainShowPanel;
    private JPanel infoPanel;
    private JPanel buttonPanel;
    private JPanel mainPanel;
    private JButton startMenuButton;
    private JLabel infoText;
    private JLabel mainText;
    private JPanel insertAndShowPanel;
    private JPanel insertPanel;
    private JTextField speedText;
    private JPanel showPanel;
    private JLabel showElement;

    // Использую HTML тэги для автопереноса строки
    private static final String START_STRING_HTML = "<html><div WIDTH=%d><center>";
    private static final String FINISH_STRING_HTML = "</center></div></html>";
    private static final String TAB_HTML = "&nbsp;&nbsp;&nbsp;&nbsp;";
    private ArrayList<String> array;
    private long speed;
    private Printer printer;
    ShowPanel(){
        infoText.setText(START_STRING_HTML + "Осталось одно действие и мы начнем!" + FINISH_STRING_HTML);
        mainText.setText("<html><div WIDTH=%d><left>" + TAB_HTML + "Пожалуйста, введите в поле ниже желаемую скорость" +
                " чтения слов в минуту(по умолчанию скорость чтения 100 слов в минуту)</left></div></html>");
        // Рамка на выводящее поле
        showElement.setBorder(BorderFactory.createLoweredBevelBorder());

        //Очищаем элемент от всякого хлама
        showElement.setText("");

        // Если пользователь ввел скорость чтения, то выводим слова на экран
        speedText.addActionListener(e -> {
            //TODO сделать проверку на длинну строки, если больше 3000 то вызывать окно ошибки
            if (speedText.getText().length() < 6) {
                speed = Long.parseLong(speedText.getText());
                speedText.setText("");
                // TODO потоки перекрываются, надо сделать синхронизед блок или что нить подобное
                printer = new Printer(getArray(), showElement, speed);
                // Делаем поток демоном
                printer.setDaemon(true);
                printer.start();
            }else {
                //TODO сделать окно
                System.out.println("its bigger");
            }
        });

        // Если нажата кнопка возврата в главное меню, останавливаем поток
        startMenuButton.addActionListener(e -> {
            printer.stopRunning();
        });
    }

    public JPanel getPanel() {
        return mainShowPanel;
    }

    JButton getStartMenuButton() {
        return startMenuButton;
    }

    private ArrayList<String> getArray() {
        return array;
    }

    void setArray(ArrayList<String> array) {
        this.array = array;
    }

}
