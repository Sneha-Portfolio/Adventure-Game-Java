module com.example.advgame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.advgame to javafx.fxml;
    exports com.example.advgame;
}