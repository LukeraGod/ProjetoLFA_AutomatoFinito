package com.example.lfa_trabalho;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class Gramatica {

    public TextArea textAreaPalavrasGeradas;
    @FXML
    private ComboBox<String> comboBoxTipoGramatica; // Corrigido para String
    @FXML
    private TableView<Producao> tabelaProducoes;
    @FXML
    private TableColumn<Producao, String> colunaLHS;
    @FXML
    private TableColumn<Producao, String> colunaRHS;

    @FXML
    private TextField campoVariaveis;
    @FXML
    private TextField campoTerminais;
    @FXML
    private TextField campoSimboloInicial;

    private ObservableList<Producao> producoes;

    @FXML
    public void initialize() {
        producoes = FXCollections.observableArrayList();

        colunaLHS.setCellValueFactory(new PropertyValueFactory<>("lhs"));
        colunaRHS.setCellValueFactory(new PropertyValueFactory<>("rhs"));

        tabelaProducoes.setItems(producoes);

        // Configurando as opções do ComboBox
        comboBoxTipoGramatica.setItems(FXCollections.observableArrayList("GLD", "GLE", "GLUD", "GLUE"));
        comboBoxTipoGramatica.getSelectionModel().selectFirst(); // Seleciona a primeira opção por padrão
    }

    @FXML
    private void adicionarProducao() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Adicionar Produção");
        dialog.setHeaderText("Nova Produção");
        dialog.setContentText("Insira a produção no formato Es -> Prod (Utilize 'ε' para produção vazia). Use '|' para múltiplas produções:");

        dialog.showAndWait().ifPresent(producao -> {
            String[] partes = producao.split("->");
            if (partes.length == 2) {
                String lhs = partes[0].trim();
                String[] rhsProducoes = partes[1].trim().split("\\|");
                for (String rhs : rhsProducoes) {
                    String rhsTrimmed = rhs.trim();
                    if (rhsTrimmed.equals("ε")) {
                        rhsTrimmed = "";
                    }
                    producoes.add(new Producao(lhs, rhsTrimmed));
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Formato inválido. Use Es -> Prod.", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }

    @FXML
    private void gerarPalavras() {
        String simboloInicial = campoSimboloInicial.getText().trim();
        StringBuilder resultado = new StringBuilder("Palavras Geradas:\n");
        int profundidadeMaxima = 20;

        String tipoGramatica = comboBoxTipoGramatica.getValue();

        switch (tipoGramatica) {
            case "GLD":
                gerarPalavrasGLD(simboloInicial, "", resultado, profundidadeMaxima);
                break;
            case "GLE":
                gerarPalavrasGLE(simboloInicial, "", resultado, profundidadeMaxima);
                break;
            case "GLUD":
                gerarPalavrasGLUD(simboloInicial, "", resultado, profundidadeMaxima);
                break;
            case "GLUE":
                gerarPalavrasGLUE(simboloInicial, "", resultado, profundidadeMaxima);
                break;
        }

        if (resultado.length() == 0) {
            resultado.append("Nenhuma palavra pôde ser gerada com a gramática fornecida.");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, resultado.toString(), ButtonType.OK);
        alert.setHeaderText("Palavras Geradas");
        alert.showAndWait();
    }

    private void gerarPalavrasGLD(String simbolo, String palavraAtual, StringBuilder resultado, int profundidadeRestante) {
        if (profundidadeRestante <= 0) {
            return;
        }

        if (simbolo.isEmpty()) {
            resultado.append(palavraAtual).append("\n");
            return;
        }

        String primeiroSimbolo = simbolo.substring(0, 1);
        String restoSimbolo = simbolo.substring(1);

        boolean encontrouProducao = false;
        for (Producao producao : producoes) {
            if (producao.getLhs().equals(primeiroSimbolo)) {
                encontrouProducao = true;
                gerarPalavrasGLD(producao.getRhs() + restoSimbolo, palavraAtual, resultado, profundidadeRestante - 1);
            }
        }

        if (!encontrouProducao) {
            if (!primeiroSimbolo.equals(primeiroSimbolo.toUpperCase())) {
                gerarPalavrasGLD(restoSimbolo, palavraAtual + primeiroSimbolo, resultado, profundidadeRestante - 1);
            }
        }
    }

    private void gerarPalavrasGLE(String simbolo, String palavraAtual, StringBuilder resultado, int profundidadeRestante) {
        if (profundidadeRestante <= 0) {
            return;
        }

        if (simbolo.isEmpty()) {
            resultado.append(palavraAtual).append("\n");
            return;
        }

        String primeiroSimbolo = simbolo.substring(0, 1);
        String restoSimbolo = simbolo.substring(1);

        for (Producao producao : producoes) {
            if (producao.getLhs().equals(primeiroSimbolo)) {
                gerarPalavrasGLE(producao.getRhs() + restoSimbolo, palavraAtual, resultado, profundidadeRestante - 1);
            }
        }

        // Se o primeiro símbolo não for não-terminal, continue com o resto
        if (!primeiroSimbolo.equals(primeiroSimbolo.toUpperCase())) {
            gerarPalavrasGLE(restoSimbolo, palavraAtual + primeiroSimbolo, resultado, profundidadeRestante - 1);
        }
    }

    private void gerarPalavrasGLUD(String simbolo, String palavraAtual, StringBuilder resultado, int profundidadeRestante) {
        if (profundidadeRestante <= 0) {
            return;
        }

        if (simbolo.isEmpty()) {
            resultado.append(palavraAtual).append("\n");
            return;
        }

        String primeiroSimbolo = simbolo.substring(0, 1);
        String restoSimbolo = simbolo.substring(1);

        boolean encontrouProducao = false;
        for (Producao producao : producoes) {
            if (producao.getLhs().equals(primeiroSimbolo)) {
                encontrouProducao = true;
                gerarPalavrasGLUD(producao.getRhs() + restoSimbolo, palavraAtual, resultado, profundidadeRestante - 1);
            }
        }

        // Se não encontrou produção, continua com a palavra atual
        if (!encontrouProducao) {
            gerarPalavrasGLUD(restoSimbolo, palavraAtual + primeiroSimbolo, resultado, profundidadeRestante - 1);
        }
    }

    private void gerarPalavrasGLUE(String simbolo, String palavraAtual, StringBuilder resultado, int profundidadeRestante) {
        if (profundidadeRestante <= 0) {
            return;
        }

        if (simbolo.isEmpty()) {
            resultado.append(palavraAtual).append("\n");
            return;
        }

        String primeiroSimbolo = simbolo.substring(0, 1);
        String restoSimbolo = simbolo.substring(1);

        boolean encontrouProducao = false;
        for (Producao producao : producoes) {
            if (producao.getLhs().equals(primeiroSimbolo)) {
                encontrouProducao = true;
                gerarPalavrasGLUE(producao.getRhs() + restoSimbolo, palavraAtual, resultado, profundidadeRestante - 1);
            }
        }

        if (!encontrouProducao) {
            gerarPalavrasGLUE(restoSimbolo, palavraAtual + primeiroSimbolo, resultado, profundidadeRestante - 1);
        }
    }

    public static class Producao {
        private String lhs;
        private String rhs;

        public Producao(String lhs, String rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public String getLhs() {
            return lhs;
        }

        public void setLhs(String lhs) {
            this.lhs = lhs;
        }

        public String getRhs() {
            return rhs;
        }

        public void setRhs(String rhs) {
            this.rhs = rhs;
        }
    }
}
