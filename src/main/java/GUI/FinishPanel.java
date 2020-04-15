package GUI;

import javax.swing.*;
import java.util.ArrayList;

public class FinishPanel extends FormPanel{
    private JPanel mainFinishPanel;
    private JPanel infoPanel;
    private JLabel infoText;
    private JPanel infoTextPane;
    private JLabel mainText;
    private JPanel buttonPanel;
    private JButton startMenuButton;
    private JPanel visualPanel;
    private JTextField readingSpeed;
    private JLabel textArray;

    // Использую HTML тэги для автопереноса строки
    private static final String START_STRING_HTML = "<html><div WIDTH=%d><center>";
    private static final String FINISH_STRING_HTML = "</center></div></html>";
    private long speed;

    FinishPanel(){
        infoText.setText(START_STRING_HTML + "Осталось одно действие и мы начнем!" + FINISH_STRING_HTML);
        mainText.setText("<html><div WIDTH=%d><left>Пожалуйста, введите в поле ниже желаемую скорость" +
                " чтения слов в минуту(по умолчанию скорость чтения 100 слов в минуту)</left></div></html>");


        ArrayList<String> s = new ArrayList<>();
        s.add("a");
        s.add("af");
        s.add("aff");
        s.add("aff");
        s.add("afff");
        s.add("affff");
        s.add("asa");
        s.add("bb");
        s.add("c");
        readingSpeed.addActionListener(e -> {
            //TODO сделать проверку на длинну строки, если больше 3000 то вызывать окно ошибки
            if (readingSpeed.getText().length() < 6) {
                speed = Long.parseLong(readingSpeed.getText());
                readingSpeed.setText("");
                // TODO думаю тут без многопоточки не обойтись
                s.forEach(el-> {
                    textArray.setText("");
                    textArray.setText(el);
                    textArray.repaint();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                });
            }else {
                //TODO
                System.out.println("its bigger");
            }
        });

    }

    public JPanel getPanel() {
        return mainFinishPanel;
    }

    JButton getStartMenuButton() {
        return startMenuButton;
    }
}
