package com.example.lfa_trabalho;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;

public class MultiplosTeste {
    @FXML
    private TableView<PalavraResultado> tableView;
    @FXML
    private TableColumn<PalavraResultado, String> coluna1;
    @FXML
    private TableColumn<PalavraResultado, String> coluna2;

    private ObservableList<PalavraResultado> listaPalavras = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        coluna1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPalavra()));
        coluna2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getResultado()));

        coluna1.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));

        // Permitir edição na coluna
        coluna1.setOnEditCommit(event -> {
            PalavraResultado palavraSelecionada = event.getRowValue();
            palavraSelecionada.setPalavra(event.getNewValue());  // Atualiza o objeto na lista
            tableView.refresh();  // Atualiza a visualização
        });

        tableView.setItems(listaPalavras);
    }

    @FXML
    public void adicionarPalavra(ActionEvent actionEvent) {
        listaPalavras.add(new PalavraResultado(""));
    }

    public void multiplosTestes(ActionEvent actionEvent) {
        for (PalavraResultado palavra : listaPalavras) {
            TextField txtP = new TextField(palavra.getPalavra());
            TextField txtR = new TextField(palavra.getResultado());
            HelloController.avaliarPalavra(txtP, txtR);
            palavra.setPalavra(txtP.getText());
            palavra.setResultado(txtR.getText());
        }
        tableView.refresh();
    }
}
