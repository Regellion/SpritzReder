package GUI;

import Core.Printer;

import javax.swing.*;
import java.util.ArrayList;
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
        infoText.setText(START_STRING_HTML + "Осталось одно действие и мы начнем!" + FINISH_STRING_HTML);
        mainText.setText("<html><div WIDTH=%d><left>" + TAB_HTML + "Пожалуйста, введите в поле ниже желаемую скорость" +
                " чтения слов в минуту(по умолчанию скорость чтения 100 слов в минуту)</left></div></html>");
        // Рамка на выводящее поле
        showElement.setBorder(BorderFactory.createLoweredBevelBorder());

        //Очищаем элемент от всякого хлама
        showElement.setText(null);




        // Если нажата кнопка стоп то останавливаем вывод, а затем продолжаем
        stopButton.addActionListener(e -> {
            iterator++;
                SwingWorker worker = new SwingWorker() {
                @Override
                protected Object doInBackground() {
                    return null;
                }

                @Override
                protected void done(){
                    // Если кнопка нажата 1, 3, 5... раз то останавливаем вывод
                    if(iterator % 2 != 0) {
                        printer.stopPrinter();
                        // TODO удалить cout
                        System.out.println(printer.getInteger());
                        // иначе запускаем вывод с прошлого места
                    } else {
                        // TODO удалить cout
                        System.out.println(" 2 met " + printer.getInteger());
                        AtomicInteger newInteger = printer.getInteger();
                        ArrayList<String> newArray = new ArrayList<>(array.subList(printer.getInteger().get(), array.size()));
                        printer = new Printer(newArray, showElement, speed);
                        printer.setInteger(newInteger);
                        printer.start();
                        // TODO Решить что то с двойным нажатием стоп кнопки после выключения
                        if(newInteger.get() == getArray().size()){
                            stopButton.setEnabled(false);
                        }
                    }
                }
            };
            worker.execute();
        });

        // Если пользователь ввел скорость чтения, то выводим слова на экран
        speedText.addActionListener(e -> {
                    //TODO сделать проверку на длинну строки, если больше 6 то вызывать окно ошибки
                    if (speedText.getText().length() < 6) {
                        speed = Integer.parseInt(speedText.getText());
                        printer = new Printer(getArray(), showElement, speed);
                        // Делаем поток демоном
                        printer.setDaemon(true);
                        printer.start();
                        // После ввода, делаем поле ввода недоступным
                        speedText.setEnabled(false);

                    }else {
                        //TODO сделать окно
                        System.out.println("its bigger");
                    }
        });



        // Если нажата кнопка возврата в главное меню, останавливаем поток
        startMenuButton.addActionListener(e -> {
            //TODO проверять, работает ли принтер и если да, то стопорить
            printer.stopPrinter();
            // Оищаем поле скорости
            speedText.setText(null);
            iterator = 0;
            stopButton.setEnabled(true);
        });


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

    public JButton getStopButton() {
        return stopButton;
    }
}
