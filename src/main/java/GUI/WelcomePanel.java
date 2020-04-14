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
    private JButton RSVPButton;
    private JButton spritzButton;


    private static final String TAB_HTML = "&nbsp;&nbsp;&nbsp;&nbsp;";
    WelcomePanel(){
        spritzImg.setIcon(new ImageIcon("src\\main\\resources\\images\\spritzExample.jpg"));
        RSVPImg.setIcon(new ImageIcon("src\\main\\resources\\images\\RSVPExample.jpg"));
        welcomeText.setText(" Добро пожаловать в программу NAME_NAME");
        mainText.setText("<html><div WIDTH=%d><left>" + TAB_HTML + "Данная программа предназначена для тренеровки скорочтения." +
                " Программа поддерживает следующие форматы: doc, docx, epub, fb2, html, odt, pdf, rtf, txt.<br>" +
                TAB_HTML + " Для начала работы выберите схему, по которой будет строится принцип скорочтения:<br>" +
                " 1) RSVP - метод выравнивания слов по центру.<br>" +
                " 2) Spritz - метод выравнивания слов по Optimal Recognition Position(рекомендуемый)</left></div></html>");

    }


    JPanel getPanel() {
        return mainPanel;
    }

    public JButton getRSVPButton() {
        return RSVPButton;
    }

    public JButton getSpritzButton() {
        return spritzButton;
    }
}

