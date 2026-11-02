package ru.kostikov.lab5;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Задание 2. Несколько виджетов с чекбоксами рядом: снятый флажок прячет виджет,
 * установленный — показывает.
 */
public class Task2ToggleWidgets extends Application {

    @Override
    public void start(Stage stage) {
        GridPane root = new GridPane();
        root.setHgap(15);
        root.setVgap(12);
        root.setPadding(new Insets(20));

        Button button = new Button("Кнопка");
        TextField textField = new TextField("Текстовое поле");
        Slider slider = new Slider(0, 100, 40);
        ColorPicker colorPicker = new ColorPicker();

        addRow(root, 0, "Кнопка", button);
        addRow(root, 1, "Текстовое поле", textField);
        addRow(root, 2, "Ползунок", slider);
        addRow(root, 3, "Выбор цвета", colorPicker);

        stage.setTitle("Показ и скрытие виджетов");
        stage.setScene(new Scene(root, 380, 200));
        stage.show();
    }

    private void addRow(GridPane grid, int row, String name, Node widget) {
        CheckBox checkBox = new CheckBox(name);
        checkBox.setId("cb" + row);
        checkBox.setSelected(true);
        // Обработчик: видимость виджета зависит от состояния чекбокса
        checkBox.setOnAction(e -> widget.setVisible(checkBox.isSelected()));
        grid.add(checkBox, 0, row);
        grid.add(widget, 1, row);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
