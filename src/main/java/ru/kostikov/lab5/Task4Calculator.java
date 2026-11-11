package ru.kostikov.lab5;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Задание 4. Калькулятор для вещественных чисел (+, -, *, /).
 * Действия выполняются последовательно, без приоритета: 2 + 3 * 4 = 20.
 */
public class Task4Calculator extends Application {
    private final Label display = new Label("0");
    private final Label history = new Label("");

    private double accumulator = 0;
    private String pendingOperation = null;
    private boolean startNewNumber = true;

    @Override
    public void start(Stage stage) {
        display.setId("display");
        display.setMaxWidth(Double.MAX_VALUE);
        display.setAlignment(Pos.CENTER_RIGHT);
        display.setStyle("-fx-font-size: 26px; -fx-padding: 5; -fx-background-color: #f4f4f4; "
                + "-fx-border-color: #c8c8c8;");
        history.setMaxWidth(Double.MAX_VALUE);
        history.setAlignment(Pos.CENTER_RIGHT);
        history.setStyle("-fx-text-fill: gray;");

        String[][] keys = {
                {"7", "8", "9", "/"},
                {"4", "5", "6", "*"},
                {"1", "2", "3", "-"},
                {"±", "0", ".", "+"},
                {"C", "⌫", "="}
        };

        GridPane grid = new GridPane();
        grid.setHgap(6);
        grid.setVgap(6);
        for (int r = 0; r < keys.length; r++) {
            for (int c = 0; c < keys[r].length; c++) {
                String key = keys[r][c];
                Button button = new Button(key);
                button.setId("key" + key);
                button.setPrefSize(60, 45);
                button.setStyle("-fx-font-size: 16px;");
                button.setOnAction(e -> onKey(key));
                if ("=".equals(key)) {
                    button.setPrefWidth(126);
                    grid.add(button, c, r, 2, 1);
                } else {
                    grid.add(button, c, r);
                }
            }
        }

        VBox root = new VBox(8, history, display, grid);
        root.setPadding(new Insets(12));

        stage.setTitle("Калькулятор");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    private void onKey(String key) {
        switch (key) {
            case "+", "-", "*", "/" -> applyOperation(key);
            case "=" -> applyOperation(null);
            case "C" -> clear();
            case "⌫" -> backspace();
            case "±" -> negate();
            case "." -> addPoint();
            default -> addDigit(key);
        }
    }

    private void addDigit(String digit) {
        if (startNewNumber || "0".equals(display.getText())) {
            display.setText(digit);
            startNewNumber = false;
        } else {
            display.setText(display.getText() + digit);
        }
    }

    private void addPoint() {
        if (startNewNumber) {
            display.setText("0.");
            startNewNumber = false;
        } else if (!display.getText().contains(".")) {
            display.setText(display.getText() + ".");
        }
    }

    private void negate() {
        String text = display.getText();
        if (!"0".equals(text)) {
            display.setText(text.startsWith("-") ? text.substring(1) : "-" + text);
        }
    }

    private void backspace() {
        if (startNewNumber) {
            return;
        }
        String text = display.getText();
        text = text.substring(0, text.length() - 1);
        display.setText(text.isEmpty() || "-".equals(text) ? "0" : text);
    }

    private void clear() {
        accumulator = 0;
        pendingOperation = null;
        startNewNumber = true;
        display.setText("0");
        history.setText("");
    }

    /**
     * Выполняет отложенную операцию над накопленным результатом и текущим числом,
     * затем запоминает новую операцию (null для «=»).
     */
    private void applyOperation(String nextOperation) {
        if (startNewNumber && "Ошибка".equals(display.getText())) {
            return;
        }
        double current = Double.parseDouble(display.getText());

        if (pendingOperation == null) {
            accumulator = current;
        } else if (!startNewNumber || nextOperation == null) {
            if ("/".equals(pendingOperation) && current == 0) {
                showDivisionByZero();
                return;
            }
            accumulator = calculate(accumulator, current, pendingOperation);
        }

        String historyText = format(accumulator);
        history.setText(nextOperation == null ? "" : historyText + " " + nextOperation);
        display.setText(historyText);
        pendingOperation = nextOperation;
        startNewNumber = true;
    }

    private static double calculate(double a, double b, String operation) {
        return switch (operation) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            default -> throw new IllegalArgumentException("Неизвестная операция " + operation);
        };
    }

    private void showDivisionByZero() {
        clear();
        display.setText("Ошибка");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Деление на ноль");
        alert.setContentText("На ноль делить нельзя. Калькулятор сброшен.");
        alert.show();
    }

    /** Убирает «.0» у целых результатов: 5.0 -> 5. */
    private static String format(double value) {
        if (value == Math.rint(value) && !Double.isInfinite(value) && Math.abs(value) < 1e15) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
