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

// Задание 4. Калькулятор для вещественных чисел (+, -, *, /).
// Действия выполняются последовательно, без приоритета: 2 + 3 * 4 = 20.
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
        if (key.equals("+") || key.equals("-") || key.equals("*") || key.equals("/")) {
            applyOperation(key);
        } else if (key.equals("=")) {
            applyOperation(null);
        } else if (key.equals("C")) {
            clear();
        } else if (key.equals("⌫")) {
            backspace();
        } else if (key.equals("±")) {
            negate();
        } else if (key.equals(".")) {
            addPoint();
        } else {
            addDigit(key);
        }
    }

    private void addDigit(String digit) {
        if (startNewNumber || display.getText().equals("0")) {
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
        if (text.equals("0")) {
            return;
        }
        if (text.startsWith("-")) {
            display.setText(text.substring(1));
        } else {
            display.setText("-" + text);
        }
    }

    private void backspace() {
        if (startNewNumber) {
            return;
        }
        String text = display.getText();
        text = text.substring(0, text.length() - 1);
        if (text.isEmpty() || text.equals("-")) {
            text = "0";
        }
        display.setText(text);
    }

    private void clear() {
        accumulator = 0;
        pendingOperation = null;
        startNewNumber = true;
        display.setText("0");
        history.setText("");
    }

    // считает предыдущую операцию (действия идут по очереди, без приоритета)
    // и запоминает новую. Для "=" новая операция - null
    private void applyOperation(String nextOperation) {
        if (startNewNumber && display.getText().equals("Ошибка")) {
            return;
        }
        double current = Double.parseDouble(display.getText());

        if (pendingOperation == null) {
            accumulator = current;
        } else if (!startNewNumber || nextOperation == null) {
            if (pendingOperation.equals("/") && current == 0) {
                showDivisionByZero();
                return;
            }
            accumulator = calculate(accumulator, current, pendingOperation);
        }

        String historyText = format(accumulator);
        if (nextOperation == null) {
            history.setText("");
        } else {
            history.setText(historyText + " " + nextOperation);
        }
        display.setText(historyText);
        pendingOperation = nextOperation;
        startNewNumber = true;
    }

    private double calculate(double a, double b, String operation) {
        if (operation.equals("+")) {
            return a + b;
        } else if (operation.equals("-")) {
            return a - b;
        } else if (operation.equals("*")) {
            return a * b;
        } else {
            return a / b;
        }
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

    // убирает ".0" у целых чисел: 5.0 -> 5
    private String format(double value) {
        if (value == (long) value) {
            return "" + (long) value;
        }
        return "" + value;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
