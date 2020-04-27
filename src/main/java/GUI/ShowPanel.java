package GUI;

import Core.Printer;
import Core.SaveFile;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;


public class ShowPanel extends FormPanel{

    private JPanel mainShowPanel;
    private JButton returnButton;
    private JLabel infoText;
    private JLabel mainText;
    private JTextField speedText;
    private JLabel showElement;
    private JButton stopButton;
    private JButton saveButton;
    private JLabel rating;

    private static Logger logger = LogManager.getLogger(ShowPanel.class);
    private static final Marker ERROR_INPUT_SPEED = MarkerManager.getMarker("INPUTS");
    private static final Marker CORRECT_INPUT_SPEED = MarkerManager.getMarker("INPUTS");
    private static final Marker SAVE_BUTTON_PRESSED = MarkerManager.getMarker("INPUTS");
    private static final Marker SAVING_IS_DONE = MarkerManager.getMarker("INPUTS");
    private static final Marker SAVING_FAILED = MarkerManager.getMarker("INPUTS");

    // Использую HTML тэги для автопереноса строки
    private static final String START_STRING_HTML = "<html><div WIDTH=%d><center>";
    private static final String FINISH_STRING_HTML = "</center></div></html>";
    private static final String TAB_HTML = "&nbsp;&nbsp;&nbsp;&nbsp;";
    private ArrayList<String> array;
    private ArrayList<String> newArray = null;
    private long speed = 100;
    private Printer printer;
    private int iterator = 0;

    ShowPanel(){
        //TODO Если буду менять размеры кнопки стоп\старт
        // то надо изменить так же сдвиг окна вывода в лево!!!!!!
        // TODO обьеденить все по методам и сгруппировать
        // TODO сделать методы кастомизации всего интерфейса
        // TODO сделать кнопку сохранить
        infoText.setText(START_STRING_HTML + "Осталось одно действие и мы начнем!" + FINISH_STRING_HTML);

        setMainText();

        // Устанавливаем дефолтную скорость чтения
        // TODO подумать над необходимостью выводоа скорости
        // speedText.setText(String.valueOf(speed));

        // Настройка выводящего элемента
        showElementSettings();
        // TODO убрать html тэги и сделать методом
        // Метод добавления рейтинга
        setRating();

        // Делаем кнопку стоп не доступной в начале
        stopButton.setEnabled(false);
        // Если нажата кнопка стоп то останавливаем вывод, а затем продолжаем
        stopButton.addActionListener(e -> {
            // Считаем, стоп это или старт
            iterator++;
        stopButtonIsPressed();
        });

        // Делаем кнопку сохранений невидимой в начале
        saveButton.setVisible(false);
        // TODO вынести в отдельный метод


        // Действия при нажатии кнопки сохранения
        saveButton.addActionListener(e -> {
            // Логгер нажатия на кнопку
            logger.info(SAVE_BUTTON_PRESSED, "Save button is pressed.");
            // Задаем имя сохранения
            String saveName = null;

            // Сохраняем вывод массива
            // TODO сделать иконку
            JFileChooser fileChooser = new JFileChooser("save/");
            fileChooser.setDialogTitle("Сохранение файла");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            // Настройка фильтра файлов
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Save files",  "fr");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                // Получаем имя файла
                saveName = fileChooser.getSelectedFile().getName();
            }

            // Если файл выбран
            if (saveName != null) {
                // Удаляем надписи о конце
                // TODO мб убрать пробелы
                array.removeIf(s -> s.endsWith("К О Н Е Ц !"));
                SaveFile saveFile = new SaveFile(saveName,
                        new ArrayList<>(array.subList(printer.getInteger().get(), array.size())));
                try {
                    serializationSave(saveFile);
                    // Логгер успешного сохранения
                    logger.info(SAVING_IS_DONE, "Saving successfully. Save name: " + saveFile.getNameSave());
                } catch (IOException ex) {
                    // Логгер провала
                    logger.warn(SAVING_FAILED, "Saving failed. " + ex);
                }
                // Иначе логируем отмену
            } else {
                // Логгер отмены
                logger.info(SAVING_FAILED, "Saving is canceled.");
            }
        });

        // Если пользователь ввел скорость чтения, то выводим слова на экран
        //TODO переделать текст для букв
        // выводить нормы чтения
        speedText.addActionListener(e -> speedInsert());

        // Если нажата кнопка возврата в главное меню, останавливаем поток
        returnButton.addActionListener(e -> returnButtonPressed());
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
                try {
                    workerDone();
                }catch (Exception ex){
                    logger.error(ex);
                }
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
            // Настройка доступа к кнопке сохранений
            if(printer.getInteger().get() != 1){
                saveButton.setEnabled(true);
            } else {
                saveButton.setEnabled(false);
            }


            saveButton.setVisible(true);
            // Останавливаем поток вывода слов
            printer.stopPrinter();
            // Если последнее слово в листе то блокируем кнопку стоп
            if(printer.getInteger().get() == getArray().size()
                    // проверка на длинну нового списка
                    || ((printer.getInteger().get() - 1) == getArray().size() )){
                // Блокировка кнопки
                stopButton.setEnabled(false);
                // Сокрытие кнопки
                saveButton.setVisible(false);
            }
            // иначе запускаем вывод с прошлого места
        } else {
            saveButton.setVisible(false);
            AtomicInteger newInteger = printer.getInteger();
            newArray = new ArrayList<>(array.subList(printer.getInteger().get(), array.size()));

            printer = new Printer(newArray, showElement, speed);
            printer.setInteger(newInteger);
            printer.start();
        }
    }

    // Метод действий после введения скорости
    private void speedInsert(){
        // Делаем кнопку стоп доступной при вводе скорости
        stopButton.setEnabled(true);
        // Делаем проверку на длинну слова и на то, чтобы это были цифры
        if ((speedText.getText().length() < 6
                && speedText.getText().matches("\\d+"))
                // Если введли пустую строку, задаем значение по умолчанию
                || speedText.getText().equals("")) {
            // Устанавливаем скорость
            setSpeed();
            // Лог корректного ввода скорости
            logger.info(CORRECT_INPUT_SPEED, "Input speed: " + speed);
            // Создаем новый поток принтер
            printer = new Printer(getArray(), showElement, speed);
            // Делаем поток демоном
            printer.setDaemon(true);
            printer.start();
            // После ввода, делаем поле ввода недоступным
            speedText.setEnabled(false);
        }else {
            errorInputSpeedMessage();
        }
    }

    // Устанавливаем скорость либо по умолчанию либо введеную
    private void setSpeed(){
        if(speedText.getText().equals("0") || speedText.getText().equals("")){
            speed = 100;
            // Отображаем дефолтный текст
            speedText.setText("100");
        } else {
            speed = Integer.parseInt(speedText.getText());
        }
    }

    // Метод действий нажатой кнопки возврата в главное меню
    private void returnButtonPressed(){
        saveButton.setVisible(false);
        // Стопорим принтер
        try {
            printer.stopPrinter();
        }catch (Exception ex){
            logger.error("The text display method was not started. The return button is pressed." + ex);
        }
        // Очщаем поле скорости
        speedText.setText(null);
        // обновляем итератор
        iterator = 0;
        // Делаем недоступной кнопку стоп\пуск
        stopButton.setEnabled(false);
    }

    // Метод настройки mainText
    private void setMainText(){
        //TODO HTML!!!!!!!!!!!!
        mainText.setText("<html><div WIDTH=%d><left>" + TAB_HTML +
                "Пожалуйста, введите в поле ниже желаемую скорость" +
                " чтения слов в минуту(по умолчанию скорость чтения 100 слов в минуту)</left></div></html>");
    }
    // Метод добавления рейтинга
    private void setRating(){
        rating.setText(START_STRING_HTML + "<h1>Рейтинг скорости чтения</h1></center></div><br>" +
                "<div WIDTH=%d><left>Школьный минимум 5 класса: 100 сл/мин.<br>" +
                "Средняя скорость чтения: 200 сл/мин.<br>" +
                "Быстрая скорость чтения: 450 сл/мин.<br>" +
                "Очень быстрая скорость чтения: 850 сл/мин.<br>" +
                "Рекорд России: 9 800 сл/мин.<br>" +
                "Мировой рекорд: 163 333 сл/мин.</left></div></html>");
    }

    // Метод вывода окна ошибки
    private void errorInputSpeedMessage(){
        String message = "Вы ввели не корректную, либо слишком высокую скорость чтения.\n" +
                "Скорость чтения не должна превышать 99999.";
        JOptionPane.showConfirmDialog(showElement, message, "Input error",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
        // Лог не корректной скорости
        logger.warn(ERROR_INPUT_SPEED, "Ошибка ввода скорости. " + message +
                " - введено: " + getSpeedText().getText());
    }


    // Метод сохранения прочитаного (сериализации)
    private void serializationSave(SaveFile saveFile) throws IOException {
        FileWriter writer = new FileWriter("save/"+ saveFile.getNameSave() + ".fr");
        new GsonBuilder()
                .disableHtmlEscaping()
                .create().toJson(saveFile, writer);
        writer.close();
    }

    public JPanel getPanel() {
        return mainShowPanel;
    }

    public JButton getReturnButton() {
        return returnButton;
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

    private ArrayList<String> getNewArray() {
        return newArray;
    }
}
