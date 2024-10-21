package com.example.lfa_trabalho;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class UnicoTeste extends Application {

    public TextField palavraDigitada;
    public TextField resultado;

    public void testeUnico(MouseEvent mouseEvent) {
        HelloController.avaliarPalavra(palavraDigitada,resultado);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root  = FXMLLoader.load(getClass().getResource("unicoTeste.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
