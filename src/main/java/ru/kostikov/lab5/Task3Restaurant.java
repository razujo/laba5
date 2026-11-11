package ru.kostikov.lab5;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

// Задание 3. Заказ в ресторане
public class Task3Restaurant extends Application {

    // меню: названия блюд и цены (одинаковый индекс = одно блюдо)
    String[] names = {"Борщ", "Цезарь с курицей", "Паста карбонара", "Стейк из говядины", "Чизкейк", "Чай"};
    int[] prices = {250, 390, 450, 890, 280, 120};

    // для каждого блюда свой чекбокс и свой счётчик порций
    CheckBox[] checkBoxes = new CheckBox[names.length];
    Spinner<Integer>[] spinners = new Spinner[names.length];

    TextArea receipt = new TextArea();

    @Override
    public void start(Stage stage) {
        GridPane menuPane = new GridPane();
        menuPane.setHgap(15);
        menuPane.setVgap(8);
        menuPane.add(new Label("Блюдо"), 0, 0);
        menuPane.add(new Label("Цена"), 1, 0);
        menuPane.add(new Label("Порций"), 2, 0);

        for (int i = 0; i < names.length; i++) {
            CheckBox checkBox = new CheckBox(names[i]);
            checkBox.setId("dish" + i);
            Spinner<Integer> spinner = new Spinner<>(0, 20, 0);
            spinner.setId("count" + i);
            spinner.setPrefWidth(80);
            spinner.setDisable(true);

            // если блюдо выбрали - ставим 1 порцию, если сняли - 0
            checkBox.setOnAction(e -> {
                if (checkBox.isSelected()) {
                    spinner.setDisable(false);
                    spinner.getValueFactory().setValue(1);
                } else {
                    spinner.setDisable(true);
                    spinner.getValueFactory().setValue(0);
                }
                updateReceipt();
            });
            // когда меняется количество порций - пересчитываем чек
            spinner.valueProperty().addListener((obs, oldValue, newValue) -> updateReceipt());

            checkBoxes[i] = checkBox;
            spinners[i] = spinner;
            menuPane.add(checkBox, 0, i + 1);
            menuPane.add(new Label(prices[i] + " ₽"), 1, i + 1);
            menuPane.add(spinner, 2, i + 1);
        }

        Button order = new Button("Сформировать чек");
        order.setId("order");
        order.setOnAction(e -> updateReceipt());
        menuPane.add(order, 0, names.length + 1);

        receipt.setId("receipt");
        receipt.setEditable(false);
        receipt.setFont(Font.font("Consolas", 13));
        receipt.setPrefWidth(360);
        updateReceipt();

        Label title = new Label("Меню ресторана");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // BorderPane: сверху заголовок, слева меню, справа чек
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setTop(title);
        root.setLeft(menuPane);
        root.setRight(receipt);
        BorderPane.setMargin(menuPane, new Insets(10, 20, 0, 0));
        BorderPane.setMargin(receipt, new Insets(10, 0, 0, 0));

        stage.setTitle("Заказ в ресторане");
        stage.setScene(new Scene(root, 780, 330));
        stage.show();
    }

    void updateReceipt() {
        String line = "----------------------------------\n";
        String text = String.format("%-18s %5s %9s%n", "Блюдо", "Кол.", "Сумма");
        text = text + line;
        int total = 0;
        for (int i = 0; i < names.length; i++) {
            if (spinners[i] == null) {
                continue;
            }
            int count = spinners[i].getValue();
            if (checkBoxes[i].isSelected() && count > 0) {
                int sum = prices[i] * count;
                total = total + sum;
                text = text + String.format("%-18s %5d %7d ₽%n", names[i], count, sum);
            }
        }
        text = text + line;
        text = text + String.format("%-18s %15d ₽", "ИТОГО:", total);
        receipt.setText(text);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
