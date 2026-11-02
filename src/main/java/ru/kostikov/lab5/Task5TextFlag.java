package ru.kostikov.lab5;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Задание 5. «Текстовый флаг»: выбор цвета каждой из трёх полос через RadioButton
 * и вывод сообщения с выбранными цветами. Размер окна изменить нельзя.
 */
public class Task5TextFlag extends Application {
    private static final String[] COLORS = {"Красный", "Белый", "Синий", "Зелёный", "Жёлтый"};

    @Override
    public void start(Stage stage) {
        ToggleGroup top = new ToggleGroup();
        ToggleGroup middle = new ToggleGroup();
        ToggleGroup bottom = new ToggleGroup();

        HBox stripes = new HBox(25,
                stripeColumn("Верхняя полоса", top, "top"),
                stripeColumn("Средняя полоса", middle, "middle"),
                stripeColumn("Нижняя полоса", bottom, "bottom"));

        Label result = new Label("Выберите цвета и нажмите «Нарисовать»");
        result.setId("result");
        result.setStyle("-fx-font-size: 15px;");

        Button draw = new Button("Нарисовать");
        draw.setId("draw");
        draw.setOnAction(e -> result.setText("Флаг: "
                + selected(top) + ", " + selected(middle) + ", " + selected(bottom)));

        VBox root = new VBox(15, stripes, draw, result);
        root.setPadding(new Insets(20));

        stage.setTitle("Текстовый флаг");
        stage.setScene(new Scene(root, 480, 260));
        stage.setResizable(false);
        stage.show();
    }

    private VBox stripeColumn(String title, ToggleGroup group, String idPrefix) {
        Label label = new Label(title);
        label.setStyle("-fx-font-weight: bold;");
        VBox column = new VBox(6, label);
        for (int i = 0; i < COLORS.length; i++) {
            RadioButton radio = new RadioButton(COLORS[i]);
            radio.setId(idPrefix + i);
            radio.setToggleGroup(group);
            column.getChildren().add(radio);
        }
        // Первый цвет выбран по умолчанию, чтобы полоса никогда не была пустой
        group.selectToggle(group.getToggles().get(0));
        return column;
    }

    private static String selected(ToggleGroup group) {
        return ((RadioButton) group.getSelectedToggle()).getText();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
