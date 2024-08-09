module lab01 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    opens Bomberman to javafx.fxml;
    exports Bomberman;
}