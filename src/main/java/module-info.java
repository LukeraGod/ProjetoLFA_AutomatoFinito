module com.example.lfa_trabalho {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lfa_trabalho to javafx.fxml;
    exports com.example.lfa_trabalho;
}