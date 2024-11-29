module org.example.weathersystemfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.weathersystemfx to javafx.fxml;
    exports org.example.weathersystemfx;
}