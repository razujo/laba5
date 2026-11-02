package ru.kostikov.lab5;

import java.util.ArrayList;
import java.util.List;

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

/**
 * Задание 3. Заказ в ресторане: выбор блюд, количество порций и чек.
 */
public class Task3Restaurant extends Application {

    /** Блюдо меню. */
    private record Dish(String name, int price) { }

    /** Строка меню на форме: блюдо, флажок выбора и счётчик порций. */
    private record MenuRow(Dish dish, CheckBox checkBox, Spinner<Integer> count) { }

    private final List<Dish> menu = List.of(
            new Dish("Борщ", 250),
            new Dish("Цезарь с курицей", 390),
            new Dish("Паста карбонара", 450),
            new Dish("Стейк из говядины", 890),
            new Dish("Чизкейк", 280),
            new Dish("Чай", 120)
    );

    private final List<MenuRow> rows = new ArrayList<>();
    private final TextArea receipt = new TextArea();

    @Override
    public void start(Stage stage) {
        GridPane menuPane = new GridPane();
        menuPane.setHgap(15);
        menuPane.setVgap(8);
        menuPane.add(new Label("Блюдо"), 0, 0);
        menuPane.add(new Label("Цена"), 1, 0);
        menuPane.add(new Label("Порций"), 2, 0);

        for (int i = 0; i < menu.size(); i++) {
            Dish dish = menu.get(i);
            CheckBox checkBox = new CheckBox(dish.name());
            checkBox.setId("dish" + i);
            Spinner<Integer> count = new Spinner<>(0, 20, 0);
            count.setId("count" + i);
            count.setPrefWidth(80);
            count.setDisable(true);

            // По умолчанию у выбранного блюда 1 порция, у снятого — 0
            checkBox.setOnAction(e -> {
                count.setDisable(!checkBox.isSelected());
                count.getValueFactory().setValue(checkBox.isSelected() ? 1 : 0);
                updateReceipt();
            });
            count.valueProperty().addListener((obs, oldValue, newValue) -> updateReceipt());

            rows.add(new MenuRow(dish, checkBox, count));
            menuPane.add(checkBox, 0, i + 1);
            menuPane.add(new Label(dish.price() + " ₽"), 1, i + 1);
            menuPane.add(count, 2, i + 1);
        }

        Button order = new Button("Сформировать чек");
        order.setId("order");
        order.setOnAction(e -> updateReceipt());
        menuPane.add(order, 0, menu.size() + 1);

        receipt.setId("receipt");
        receipt.setEditable(false);
        receipt.setFont(Font.font("Consolas", 13));
        receipt.setPrefWidth(360);
        updateReceipt();

        Label title = new Label("Меню ресторана");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

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

    private void updateReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-18s %5s %9s%n", "Блюдо", "Кол.", "Сумма"));
        sb.append("-".repeat(34)).append('\n');
        int total = 0;
        for (MenuRow row : rows) {
            int count = row.count().getValue();
            if (row.checkBox().isSelected() && count > 0) {
                int sum = row.dish().price() * count;
                total += sum;
                sb.append(String.format("%-18s %5d %7d ₽%n", row.dish().name(), count, sum));
            }
        }
        sb.append("-".repeat(34)).append('\n');
        sb.append(String.format("%-18s %15d ₽", "ИТОГО:", total));
        receipt.setText(sb.toString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
