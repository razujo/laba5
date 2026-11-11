package ru.kostikov.lab5;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Главное окно лабораторной работы №5: меню для запуска каждого задания в отдельном окне.
public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Label title = new Label("Лабораторная работа №5 — JavaFX");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox root = new VBox(10, title,
                menuButton("1. Перекидыватель слов", new Task1WordSwapper()),
                menuButton("2. Показ и скрытие виджетов", new Task2ToggleWidgets()),
                menuButton("3. Заказ в ресторане", new Task3Restaurant()),
                menuButton("4. Калькулятор", new Task4Calculator()),
                menuButton("5. Текстовый флаг", new Task5TextFlag()));
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        stage.setTitle("Лабораторная работа №5");
        stage.setScene(new Scene(root, 320, 300));
        stage.show();
    }

    private Button menuButton(String text, Application task) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(e -> {
            try {
                task.start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
