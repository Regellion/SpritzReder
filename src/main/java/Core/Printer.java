package Core;

import javax.swing.*;
import java.util.ArrayList;


// Поток для обновления JLabel'а
public class Printer extends Thread {
    private static final double minuteSize = 60000.0;

    private ArrayList<String> array;
    private JLabel textArray;
    private double speed;
    private volatile boolean isActive = true;
    //TODO переименовать и доработать расчет
    private double smallWordsCoof = 1.1;
    private double longWordsCoof = 0.9;

    // TODO сделать расчет замедления как написал в телеге
    public Printer(ArrayList<String> s, JLabel textArray, long speed){
        this.array = s;
        this.textArray = textArray;
        this.speed = minuteSize / speed;

    }
    //TODO если скорость 1 слово в минуту, и мы возвращаемся туда-сюда, то решить что то с очисткой массива
    @Override
    public void run() {
            array.forEach(element -> {
                if(isActiving()) {
                    // TODO доделать цвет буквы красным, в зависимости от метода
                    // Выводим слово
                    textArray.setText("" + element + "");
                    // Перерисовываем панель
                    textArray.repaint();
                    try {
                        // Стопорим изображение
                        //TODO сделать больше времени на маленьких словах и меньше на больших
                        if(element.trim().length() > 4){
                            Thread.sleep((long) (speed * longWordsCoof));
                        } else {
                            Thread.sleep((long) (speed * smallWordsCoof));
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                        // TODO логгировать
                    }
                }

                textArray.setText(null);
            });
            // В конце текста выводим сообщение
            textArray.setText("КОНЕЦ");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            textArray.setText(null);
    }

    private boolean isActiving() {
        return isActive;
    }

    public void stopPrinter() {
        isActive = false;
    }

}

