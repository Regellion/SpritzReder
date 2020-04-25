package GUI;

import javax.swing.*;

public class WelcomePanel extends FormPanel{

    private JPanel mainPanel;
    private JLabel welcomeText;
    private JLabel mainText;
    private JLabel RSVPImg;
    private JLabel spritzImg;
    private JButton nextButton;

    // HTML табуляция
    private static final String TAB_HTML = "&nbsp;&nbsp;&nbsp;&nbsp;";
    // Использую HTML тэги для автопереноса строки
    private static final String START_STRING_HTML = "<html><div WIDTH=%d><left>";
    private static final String FINISH_STRING_HTML = "</left></div></html>";
    WelcomePanel(){
        // Устанавливаю картинки
        setImages();
        welcomeText.setText(" Добро пожаловать в программу Speed Reader");
        // Устанавливаю главный текст
        setMainText();
    }

    private void setImages(){
        // Картинка, отвечающая за метод спритз
        spritzImg.setIcon(new ImageIcon("images/spritzExample.jpg"));
        // Картинка, отвечающая за метод RSVP
        RSVPImg.setIcon(new ImageIcon("images/RSVPExample.jpg"));
    }

    private void setMainText(){
        mainText.setText(START_STRING_HTML + TAB_HTML + "Данная программа предназначена для тренеровки скорочтения." +
                " Программа поддерживает следующие форматы: doc, docx, epub, fb2, html, odt, pdf, rtf, txt.<br>" +
                TAB_HTML + "Данная программа может работать по двум основным принципам скорочтения:<br>" +
                " 1) RSVP - метод выравнивания слов по центру.<br>" +
                " 2) Spritz - метод выравнивания слов по Optimal Recognition Position(рекомендуемый)<br>" +
                TAB_HTML + "Далее Вам будет дана возможность вбырать один из этих методов." + FINISH_STRING_HTML);
    }

    JPanel getPanel() {
        return mainPanel;
    }

    JButton getNextButton() {
        return nextButton;
    }
}

