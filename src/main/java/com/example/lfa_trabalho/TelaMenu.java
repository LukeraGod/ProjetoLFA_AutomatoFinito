package com.example.lfa_trabalho;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class TelaMenu {

    public void automato(MouseEvent mouseEvent) throws IOException {
        Stage stage = new Stage();
        Parent root  = FXMLLoader.load(getClass().getResource("hello-view.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    public void gramatica(MouseEvent mouseEvent) throws IOException {
        Stage stage = new Stage();
        Parent root  = FXMLLoader.load(getClass().getResource("gramatica.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void expressao(MouseEvent mouseEvent) throws IOException {
        Stage stage = new Stage();
        Parent root  = FXMLLoader.load(getClass().getResource("expressaoRegular.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
