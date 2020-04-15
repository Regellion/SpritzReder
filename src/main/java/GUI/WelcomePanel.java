package GUI;

import javax.swing.*;

public class WelcomePanel extends FormPanel{

    private JPanel mainPanel;
    private JPanel welcomeTextPanel;
    private JLabel welcomeText;
    private JPanel mainTextPanel;
    private JLabel mainText;
    private JLabel RSVPImg;
    private JLabel spritzImg;
    private JPanel buttonPanel;
    private JButton nextButton;


    private static final String TAB_HTML = "&nbsp;&nbsp;&nbsp;&nbsp;";
    // Использую HTML тэги для автопереноса строки
    private static final String START_STRING_HTML = "<html><div WIDTH=%d><left>";
    private static final String FINISH_STRING_HTML = "</left></div></html>";
    WelcomePanel(){
        spritzImg.setIcon(new ImageIcon("src\\main\\resources\\images\\spritzExample.jpg"));
        RSVPImg.setIcon(new ImageIcon("src\\main\\resources\\images\\RSVPExample.jpg"));
        welcomeText.setText(" Добро пожаловать в программу Speed Reader");
        mainText.setText(START_STRING_HTML + TAB_HTML + "Данная программа предназначена для тренеровки скорочтения." +
                " Программа поддерживает следующие форматы: doc, docx, epub, fb2, html, odt, pdf, rtf, txt.<br>" +
                TAB_HTML + " Для начала работы выберите схему, по которой будет строится принцип скорочтения:<br>" +
                " 1) RSVP - метод выравнивания слов по центру.<br>" +
                " 2) Spritz - метод выравнивания слов по Optimal Recognition Position(рекомендуемый)" + FINISH_STRING_HTML);

    }


    JPanel getPanel() {
        return mainPanel;
    }


    JButton getNextButton() {
        return nextButton;
    }
}

