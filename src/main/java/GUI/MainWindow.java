package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class MainWindow extends JFrame {
    // Создаем панели
    private WelcomePanel welcomePanel = new WelcomePanel();
    private InputPanel inputPanel = new InputPanel();

    private static final int width = 800;
    private static final int height = 600;

    public MainWindow(){
        // Заголовок программы
        //TODO придумать норм название
        super("Speed Reader");
        // добавляем кнопки
        JButton buttonRSVP = welcomePanel.getRSVPButton();
        JButton buttonSpritz = welcomePanel.getSpritzButton();
        JButton buttonReturn = inputPanel.getReturnButton();


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
        buttonReturn.addActionListener(this::buttonClick);
    }

    // Мульти слушатель кнопок
    private void buttonClick(ActionEvent e) {
        // При нажатии на кнопку главного меню чистим ГУИ
        this.getContentPane().remove(welcomePanel.getPanel());

        // Если нажата кнопка метода RSVP
        if(e.getSource().equals(welcomePanel.getRSVPButton())) {
            inputPanel.printWelcomeText("\"RSVP\"");
            windowSwitcher(inputPanel);


            // Если нажата кнопка метода спритз
        } else if(e.getSource().equals(welcomePanel.getSpritzButton())){
            inputPanel.printWelcomeText("\"Spritz\"");
            windowSwitcher(inputPanel);




            // Если во втором окне нажата кнопка возврата
        } else if(e.getSource().equals(inputPanel.getReturnButton())){
            // Не используется метод windowSwitcher потому что неправильно кладет размеры на форму
            this.setContentPane(welcomePanel.getPanel());
        }
        this.repaint();
    }


    // Метод переключения окон
    private void windowSwitcher(FormPanel formName){
        // Добавляем форму
        this.setContentPane(formName.getPanel());
        // Выстраиваем по местам
        pack();
        // указываем размер
        //TODO сделать не во все окно!! мб можно удалить пак и эту строку
        //this.setExtendedState(MAXIMIZED_BOTH);
        this.setSize(new Dimension(800, 600));
    }

}
