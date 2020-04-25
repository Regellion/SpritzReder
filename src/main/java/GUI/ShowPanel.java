package GUI;

import Core.Printer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;


public class ShowPanel extends FormPanel{

    private JPanel mainShowPanel;
    private JButton startMenuButton;
    private JLabel infoText;
    private JLabel mainText;
    private JTextField speedText;
    private JLabel showElement;
    private JButton stopButton;

    // Использую HTML тэги для автопереноса строки
    private static final String START_STRING_HTML = "<html><div WIDTH=%d><center>";
    private static final String FINISH_STRING_HTML = "</center></div></html>";
    private static final String TAB_HTML = "&nbsp;&nbsp;&nbsp;&nbsp;";
    private ArrayList<String> array;
    private long speed;
    private Printer printer;
    private int iterator = 0;

    ShowPanel(){
        //TODO Если буду менять размеры кнопки стоп\старт
        // то надо изменить так же сдвиг окна вывода в лево!!!!!!
        // TODO обьеденить все по методам и сгруппировать
        // TODO сделать методы кастомизации всего интерфейса
        infoText.setText(START_STRING_HTML + "Осталось одно действие и мы начнем!" + FINISH_STRING_HTML);
        setMainText();
        // Настройка выводящего элемента
        showElementSettings();

        // Если нажата кнопка стоп то останавливаем вывод, а затем продолжаем
        stopButton.addActionListener(e -> {
            // Считаем, стоп это или старт
            iterator++;
        stopButtonIsPressed();
        });

        // Если пользователь ввел скорость чтения, то выводим слова на экран
        speedText.addActionListener(e -> speedInsert());

        // Если нажата кнопка возврата в главное меню, останавливаем поток
        startMenuButton.addActionListener(e -> startMenuButtonPressed());
    }

    // Метод настройки showElement
    private void showElementSettings(){
        // Рамка на выводящее поле
        showElement.setBorder(BorderFactory.createLoweredBevelBorder());
        //Очищаем элемент от всякого хлама
        showElement.setText(null);
    }

    // Метод действия при нажатой кнопке стоп
    private void stopButtonIsPressed(){
        // Делаем кнопку снятия с паузы недоступной на 0,01 секунды, для фикса бага со спамом кнопки
        stopButton.setEnabled(false);
        timer();
        // Создаем воркера для корректной рабботы кнопки стоп
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() {
                return null;
            }

            @Override
            protected void done(){
                workerDone();
            }
        };
        worker.execute();
    }

    // Подметод таймера
    private void timer(){
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        // делаем кнопку стоп доступной через 0.01 секунды после нажатия
                        stopButton.setEnabled(true);
                    }
                }, 10);
    }

    // Подметод работы воркера
    private void workerDone(){
        // Если кнопка нажата 1, 3, 5... раз то останавливаем вывод
        if(iterator % 2 != 0) {
            // Останавливаем поток вывода слов
            printer.stopPrinter();
            // Если последнее слово в листе то блокируем кнопку стоп
            if(printer.getInteger().get() == getArray().size()
                    // проверка на длинну нового списка
                    || ((printer.getInteger().get() - 1) == getArray().size() )){
                // Блокировка кнопки
                stopButton.setEnabled(false);
            }
            // иначе запускаем вывод с прошлого места
        } else {
            AtomicInteger newInteger = printer.getInteger();
            ArrayList<String> newArray = new ArrayList<>(array.subList(printer.getInteger().get(), array.size()));

            printer = new Printer(newArray, showElement, speed);
            printer.setInteger(newInteger);
            printer.start();
        }
    }


    // Метод действий после введения скорости
    private void speedInsert(){
        // Делаем проверку на длинну слова и на то, чтобы это были цифры
        if ((speedText.getText().length() < 6
                && speedText.getText().matches("\\d+"))
                // Если введли пустую строку, задаем значение по умолчанию
                || speedText.getText().equals("")) {
            // Устанавливаем скорость
            setSpeed();

            // Создаем новый поток принтер
            printer = new Printer(getArray(), showElement, speed);
            // Делаем поток демоном
            printer.setDaemon(true);
            printer.start();
            // После ввода, делаем поле ввода недоступным
            speedText.setEnabled(false);
        }else {
            // TODO логги
            errorInputSpeedMessage();
        }
    }


    // Устанавливаем скорость либо по умолчанию либо введеную
    private void setSpeed(){
        if(speedText.getText().equals("0") || speedText.getText().equals("")){
            speed = 100;
        } else {
            speed = Integer.parseInt(speedText.getText());
        }
    }


    // Метод действий нажатой кнопки возврата в главное меню
    private void startMenuButtonPressed(){
        // Стопорим принтер
        printer.stopPrinter();
        // Очщаем поле скорости
        speedText.setText(null);
        // обновляем итератор
        iterator = 0;
        // Делаем доступной кнопку стоп\пуск
        stopButton.setEnabled(true);
    }

    // Метод настройки mainText
    private void setMainText(){
        mainText.setText("<html><div WIDTH=%d><left>" + TAB_HTML + "Пожалуйста, введите в поле ниже желаемую скорость" +
                " чтения слов в минуту(по умолчанию скорость чтения 100 слов в минуту)</left></div></html>");
    }
    // Метод вывода окна ошибки
    private void errorInputSpeedMessage(){
        String message = "Вы ввели слишком высокую скорость чтения.\n" +
                "Скорость чтения не должна превышать 99999.";
        JOptionPane.showConfirmDialog(showElement, message, "Input error", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    public JPanel getPanel() {
        return mainShowPanel;
    }


    public JButton getStartMenuButton() {
        return startMenuButton;
    }


    private ArrayList<String> getArray() {
        return array;
    }


    void setArray(ArrayList<String> array) {
        this.array = array;
    }

    JTextField getSpeedText() {
        return speedText;
    }
}
