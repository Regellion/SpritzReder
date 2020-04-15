package Core;

import javax.swing.*;
import java.util.ArrayList;

// Поток для обнавления JLabel'а
public class Printer extends Thread {
    private ArrayList<String> array;
    private JLabel textArray;
    private long speed;

    public Printer(ArrayList<String> s, JLabel textArray, long speed){
        this.array = s;
        this.textArray = textArray;
        this.speed = speed;
    }
    @Override
    public void run() {
        array.forEach(element-> {
                //TODO доделать цвет буквы красным, в зависимости от метода
            textArray.setText("" + element + "");
            textArray.repaint();
            try {
                //TODO доделать так, чтобы норм выводилось все
                //TODO сделать больше времени на маленьких словах и меньше на больших
                Thread.sleep(speed);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            textArray.setText("");
        });
    }
}

