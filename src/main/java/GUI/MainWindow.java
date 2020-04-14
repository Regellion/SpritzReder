package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class MainWindow extends JFrame {

    private WelcomePanel welcomePanel = new WelcomePanel();
    private static final int width = 800;
    private static final int height = 600;

    public MainWindow(){
        // Заголовок программы
        //TODO придумать норм название
        super("Name_NAME");
        // добавляем кнопки
        JButton buttonRSVP = welcomePanel.getRSVPButton();
        JButton buttonSpritz = welcomePanel.getSpritzButton();


        // Операция по закрытию
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Устанавливаем размер окна
        this.setMinimumSize(new Dimension(width, height));
        // TODO
        // Устанавливаем иконку окна
        //this.setIconImage(new ImageIcon());
        // Добавляем стартовую панель
        this.getContentPane().add(welcomePanel.getPanel());
        // Устанавливаем окно по умолчанию по центру
        this.setLocationRelativeTo(null);
        // Делаем фрейм видимым
        this.setVisible(true);

        buttonRSVP.addActionListener(this::buttonClick);
        buttonSpritz.addActionListener(this::buttonClick);
    }

    // Мульти слушатель кнопок
    private void buttonClick(ActionEvent e) {
        // При нажатии на кнопку главного меню чистим ГУИ
        this.getContentPane().remove(welcomePanel.getPanel());

        // Если нажата кнопка метода RSVP
        if(e.getSource().equals(welcomePanel.getRSVPButton())) {
            windowSwitcher(welcomePanel);
            // Задаем значение булиан переменной метода

            // Если нажата кнопка метода спритз
        } else if(e.getSource().equals(welcomePanel.getSpritzButton())){
            windowSwitcher(welcomePanel);



            // Если во втором окне нажата кнопка возврата
        } /*else if(e.getSource().equals(secondForm.getReturnButton())){
            // Не используется метод windowSwitcher потому что неправильно кладет размеры на форму
            this.setContentPane(mainForm.getPanel());
        }*/
        this.repaint();
    }

    // Метод переключения окон
    private void windowSwitcher(FormPanel formName){
        // Добавляем форму
        this.setContentPane(formName.getPanel());
        // Выстраиваем по местам
        pack();
        // указываем размер
        //TODO сделать не во все окно!!
        this.setExtendedState(MAXIMIZED_BOTH);
    }
}
