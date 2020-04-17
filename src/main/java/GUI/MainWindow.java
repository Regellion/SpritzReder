package GUI;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class MainWindow extends JFrame {
    // Создаем панели
    private ShowPanel showPanel = new ShowPanel();
    private WelcomePanel welcomePanel = new WelcomePanel();
    private InputPanel inputPanel = new InputPanel();

    private static final int width = 800;
    private static final int height = 600;

    public MainWindow(){
        // Заголовок программы
        super("Speed Reader");
        // добавляем кнопки
        JButton buttonNext = welcomePanel.getNextButton();
        JButton buttonReturn = inputPanel.getReturnButton();
        JButton buttonRSVP = inputPanel.getRSVPButton();
        JButton buttonSpritz = inputPanel.getSpritzButton();
        JButton buttonStartMenu = showPanel.getStartMenuButton();

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


        buttonNext.addActionListener(this::buttonClick);
        buttonReturn.addActionListener(this::buttonClick);
        buttonRSVP.addActionListener(this::buttonClick);
        buttonSpritz.addActionListener(this::buttonClick);
        buttonStartMenu.addActionListener(this::buttonClick);
    }

    // Мульти слушатель кнопок
    private void buttonClick(ActionEvent e) {
        // При нажатии на кнопку главного меню чистим ГУИ
        this.getContentPane().remove(welcomePanel.getPanel());

            // Если нажата кнопка продолжить
        if(e.getSource().equals(welcomePanel.getNextButton())){
            windowSwitcher(inputPanel);
            // Если во втором окне нажата кнопка возврата
        } else if(e.getSource().equals(inputPanel.getReturnButton())){
            // Не используется метод windowSwitcher потому что неправильно кладет размеры на форму
            //this.setContentPane(welcomePanel.getPanel());
            windowSwitcher(welcomePanel);
        } else if(e.getSource().equals(inputPanel.getRSVPButton())){
            // TODO тут брать аррэй из инпут панели и проверять на ноль, и если норм то уже открывать, если нет, выбивать окно!
            windowSwitcher(showPanel);
        } else if(e.getSource().equals(inputPanel.getSpritzButton())){
            // TODO тут брать аррэй из инпут панели и проверять на ноль, и если норм то уже открывать, если нет, выбивать окно!
            windowSwitcher(showPanel);
        } else if(e.getSource().equals(showPanel.getStartMenuButton())){
            windowSwitcher(welcomePanel);
            // делаем поле ввода скорости видимым
            showPanel.getSpeedText().setEnabled(true);
        }
        //TODO
        // мб можно удалить
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

    ShowPanel getShowPanel() {
        return showPanel;
    }
}
