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

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ExpressaoRegular {
    @FXML
    private TableView<PalavraResultado> tableView;
    @FXML
    private TableColumn<PalavraResultado, String> coluna1;
    @FXML
    private TableColumn<PalavraResultado, String> coluna2;
    @FXML
    private TextField textFieldExpressao;

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

    @FXML
    public void testarPalavras(ActionEvent actionEvent) {
        String expressao = textFieldExpressao.getText().trim();
        if (expressao.isEmpty()) {
            System.out.println("Por favor, insira uma expressão regular.");
            return;
        }

        // Substituir ε pela string vazia no regex (|) para garantir compatibilidade
        String regex = "^(" + expressao.replace("ε", "") + ")$";

        try {
            Pattern pattern = Pattern.compile(regex);
            for (PalavraResultado palavra : listaPalavras) {
                // Checar se a palavra é vazia
                if (palavra.getPalavra().isEmpty()) {
                    // Apenas validar a palavra nula se a expressão explicitamente a aceitar
                    boolean aceitaNulo = pattern.matcher("").matches();
                    palavra.setResultado(aceitaNulo ? "Válido" : "Inválido");
                } else {
                    // Verificar a palavra normalmente
                    boolean match = pattern.matcher(palavra.getPalavra()).matches();
                    palavra.setResultado(match ? "Válido" : "Inválido");
                }
            }
        } catch (PatternSyntaxException e) {
            System.out.println("Expressão regular inválida: " + e.getMessage());
            for (PalavraResultado palavra : listaPalavras) {
                palavra.setResultado("Erro na expressão");
            }
        }

        tableView.refresh();
    }






}
