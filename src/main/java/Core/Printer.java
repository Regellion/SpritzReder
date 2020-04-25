package Core;

import org.apache.logging.log4j.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

// TODO сделать логгер остановки потоки и логгер сохранения
// Поток для обновления JLabel'а
public class Printer extends Thread {
    private static Logger logger = LogManager.getLogger(Printer.class);

    private static final double minuteSize = 60000.0;
    private AtomicInteger integer = new AtomicInteger(0);
    private ArrayList<String> array;
    private JLabel textArray;
    private double speed;
    private volatile boolean isActive = true;
    //TODO доработать расчет
    private double smallWordsCoefficient = 1;//1.1;
    private double longWordsCoefficient = 1;//0.9;

    // TODO сделать расчет замедления как написал в телеге
    public Printer(ArrayList<String> array, JLabel textArray, long speed){
        array.removeIf(s -> s.matches(" К О Н Е Ц !"));
        array.add("К О Н Е Ц !");
        this.array = array;
        this.textArray = textArray;
        this.speed = minuteSize / speed;
    }


    @Override
    public void run() {
        array.forEach(element -> {
            if (isActiving()) {
                Loader.getMainWindow().getShowPanel().getReturnButton().addActionListener(e -> {
                    // Если нажали возврат в главное меню, то прерываем вывод
                    isActive = false;
                    textArray.setText(null);
                });
                // TODO доделать цвет буквы красным, в зависимости от метода
                // Выводим слово
                textArray.setText("" + element + "");
                // Перерисовываем панель
                textArray.repaint();
                integer.getAndIncrement();

                try {
                    // Стопорим изображение
                    //TODO сделать больше времени на маленьких словах и меньше на больших
                    if (element.trim().length() > 4) {
                        Thread.sleep((long) (speed * longWordsCoefficient));
                    } else {
                        Thread.sleep((long) (speed * smallWordsCoefficient));
                    }
                } catch (InterruptedException ex) {
                    // Логгирую ошибку потока
                    logger.error(ex);
                }
            }
        });
    }

    private boolean isActiving() {
        return isActive;
    }

    public void stopPrinter() {
        isActive = false;
    }

    public AtomicInteger getInteger() {
        return integer;
    }

    public void setInteger(AtomicInteger integer) {
        this.integer = integer;
    }
}

