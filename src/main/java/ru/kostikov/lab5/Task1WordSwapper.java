package ru.kostikov.lab5;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Задание 1. Перекидыватель слов: строка перелетает из одного поля в другое,
 * стрелка на кнопке показывает направление следующего переброса.
 */
public class Task1WordSwapper extends Application {
    private static final String RIGHT = "→";
    private static final String LEFT = "←";

    @Override
    public void start(Stage stage) {
        TextField left = new TextField();
        left.setId("left");
        left.setPromptText("Введите строку");

        TextField right = new TextField();
        right.setId("right");

        Button swap = new Button(RIGHT);
        swap.setId("swap");
        swap.setStyle("-fx-font-size: 18px;");
        swap.setPrefWidth(60);

        swap.setOnAction(e -> {
            if (RIGHT.equals(swap.getText())) {
                right.setText(left.getText());
                left.clear();
                swap.setText(LEFT);
            } else {
                left.setText(right.getText());
                right.clear();
                swap.setText(RIGHT);
            }
        });

        HBox root = new HBox(10, left, swap, right);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        stage.setTitle("Перекидыватель слов");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
