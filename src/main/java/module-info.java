module com.example.filemanager {

    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;


    opens com.example.filemanager to javafx.fxml;
    exports com.example.filemanager;
}