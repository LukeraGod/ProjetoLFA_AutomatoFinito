package com.example.lfa_trabalho;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HelloController {
    public AnchorPane anchorPane;
    @FXML
    private Label welcomeText;

    static List<StackPane> listaEstados = new ArrayList<>();
    static List<StackPane> listaFinais = new ArrayList<>();
    static StackPane inicial = null;
    static List<Seta> listaSetas = new ArrayList<>();

    private StackPane firstCircle = null;
    private StackPane secondCircle = null;
    int acao = -1;




    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void criarEstado(MouseEvent mouseEvent) {
        if (acao == 1) {

            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            int numeroDoCirculo = 0;

            Circle circle = new Circle(x, y, 20); // O último parâmetro é o raio do círculo (ajustável)
            circle.setFill(Color.LIGHTGRAY);



            if(listaEstados.isEmpty())
                numeroDoCirculo = 1; // Número do círculo será o próximo da lista
            else{
                int prox = 1;
                int i = 0;

                while( i < listaEstados.size() && ((Label)(listaEstados.get(i).getChildren().get(1))).getText().compareTo(""+prox) == 0 ){
                    i++;
                    prox++;
                }


                numeroDoCirculo = prox;
            }
            // Criando o Label com o número no meio do círculo
            Label label = new Label(String.valueOf(numeroDoCirculo));
            label.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Definir fonte e tamanho
            label.setTextAlignment(TextAlignment.CENTER);

            // Criando um StackPane para sobrepor o Circle e o Label
            StackPane stackPane = new StackPane();
            stackPane.setLayoutX(x - 20); // Ajusta a posição horizontal para o centro do círculo
            stackPane.setLayoutY(y - 20); // Ajusta a posição vertical para o centro do círculo
            stackPane.getChildren().addAll(circle, label); // Adiciona o círculo e o texto

            // Adicionando o StackPane ao AnchorPane
            anchorPane.getChildren().add(stackPane);

            // Adicionando o StackPane à lista de estados
            listaEstados.add(stackPane);
            if(listaEstados.size()>1)
                organizaLista();

            // Configura o clique em cada círculo para acionar a ação de conectar
            stackPane.setOnMouseClicked(event -> selecionarCirculo(stackPane,event));
        }

    }

    private void organizaLista() {
        int i = listaEstados.size()-1;
        while(i>0){
            StackPane stac1 = listaEstados.get(i);
            StackPane stac2 = listaEstados.get(i-1);
            if( Integer.parseInt(((Label)stac1.getChildren().get(1)).getText()) < Integer.parseInt(((Label)stac2.getChildren().get(1)).getText())){
                listaEstados.set(i,stac2);
                listaEstados.set(i-1,stac1);
                i--;
            }
            else
                i=0;
        }
    }

    public void selecionarCirculo(StackPane stackPane, MouseEvent event) {
        if (acao == 2) {
            if (firstCircle == null) {
                firstCircle = stackPane;
            } else if (secondCircle == null) {
                secondCircle = stackPane;

                desenharLinha(firstCircle, secondCircle);

                firstCircle = null;
                secondCircle = null;
            }
        } else if (acao == 3) {
            if (event.getButton() == MouseButton.SECONDARY) {
                ContextMenu contextMenu = new ContextMenu();
                MenuItem option1 = new MenuItem("Inicial");
                MenuItem option2 = new MenuItem("Final");

                option1.setOnAction(e -> {
                    if (inicial != null) {

                        anchorPane.getChildren().removeIf(node -> node instanceof Polygon && ((Polygon) node).getUserData() == inicial);
                        ((Circle) inicial.getChildren().get(0)).setFill(Color.LIGHTGRAY);
                    }


                    inicial = stackPane;
                    Circle circle = (Circle) stackPane.getChildren().get(0);
                    circle.setFill(Color.rgb(1, 96, 234));

                    double startX = stackPane.getLayoutX() - 19;
                    double startY = stackPane.getLayoutY() + 20;

                    Polygon triangle = new Polygon();
                    triangle.getPoints().addAll(
                            startX, startY - 10,
                            startX, startY + 10,
                            startX + 20, startY
                    );
                    triangle.setFill(Color.BLACK);

                    triangle.setUserData(stackPane);

                    anchorPane.getChildren().add(triangle);
                });

                option2.setOnAction(e -> {
                    if (!listaFinais.contains(stackPane)) {
                        listaFinais.add(stackPane);
                        ((Circle) stackPane.getChildren().get(0)).setStyle("-fx-stroke: black; -fx-stroke-width: 5;");
                    } else {
                        ((Circle) stackPane.getChildren().get(0)).setStyle("-fx-stroke: none;");
                        listaFinais.remove(stackPane);
                    }
                });

                contextMenu.getItems().addAll(option1, option2);
                contextMenu.show(stackPane, event.getScreenX(), event.getScreenY());
            }
        }
    }

    private void desenharLinha(StackPane firstCircle, StackPane secondCircle) {
        // Verificar se já existe uma seta entre esses dois estados
        Optional<Seta> setaExistente = listaSetas.stream()
                .filter(seta -> seta.getPrimC() == firstCircle && seta.getSegC() == secondCircle)
                .findFirst();

        // Diálogo para inserir o novo texto na linha
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Texto na Linha");
        dialog.setHeaderText("Insira o texto para colocar na linha:");
        dialog.setContentText("Texto:");

        Optional<String> resultado = dialog.showAndWait();
        if (!resultado.isPresent() || resultado.get().isEmpty()) {
            return;  // Se o usuário não digitar nada, não faz nada
        }

        String novoTexto = resultado.get().trim();

        if (setaExistente.isPresent()) {
            // Se já existir uma seta, adicionar o novo texto separado por uma vírgula
            Seta seta = setaExistente.get();
            Text textoAtual = seta.getTexto();
            String textoAntigo = textoAtual.getText();

            // Adicionar o novo texto, separado por vírgula
            textoAtual.setText(textoAntigo + "," + novoTexto);
        } else {
            // Se não existir uma seta, criar uma nova linha e seta

            double startX = firstCircle.getLayoutX() + 20;
            double startY = firstCircle.getLayoutY() + 20;
            double endX = secondCircle.getLayoutX() + 20;
            double endY = secondCircle.getLayoutY() + 20;

            // Ajustar as coordenadas para evitar que a linha sobreponha o círculo
            double deltaX = endX - startX;
            double deltaY = endY - startY;
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            double offsetX = (deltaX / distance) * 20;
            double offsetY = (deltaY / distance) * 20;

            startX += offsetX;
            startY += offsetY;
            endX -= offsetX;
            endY -= offsetY;

            Line line = new Line(startX, startY, endX, endY);
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(2);
            anchorPane.getChildren().add(line);

            // Criar a seta no final da linha
            adicionarSeta(endX, endY, startX, startY);

            // Criar o texto e adicioná-lo ao AnchorPane
            Text texto = new Text(novoTexto);
            texto.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            texto.setX((startX + endX) / 2);
            texto.setY((startY + endY) / 2 - 10);
            anchorPane.getChildren().add(texto);

            // Adicionar a nova seta à lista de setas
            listaSetas.add(new Seta(firstCircle, secondCircle, texto, line));
        }
    }

    private void adicionarSeta(double endX, double endY, double startX, double startY) {
        double angle = Math.atan2(endY - startY, endX - startX);
        double arrowLength = 15;

        double arrowX1 = endX - arrowLength * Math.cos(angle - Math.PI / 6);
        double arrowY1 = endY - arrowLength * Math.sin(angle - Math.PI / 6);
        double arrowX2 = endX - arrowLength * Math.cos(angle + Math.PI / 6);
        double arrowY2 = endY - arrowLength * Math.sin(angle + Math.PI / 6);

        Polygon arrowHead = new Polygon();
        arrowHead.getPoints().addAll(
                endX, endY,
                arrowX1, arrowY1,
                arrowX2, arrowY2
        );
        arrowHead.setFill(Color.BLACK);
        anchorPane.getChildren().add(arrowHead);
    }


    public void mudarParaEstado(MouseEvent mouseEvent) {
        acao = 1;
        firstCircle = secondCircle = null;
    }

    public void mudarParaSeta(MouseEvent mouseEvent) {
        acao = 2;
        firstCircle = secondCircle = null;
    }

    public void mudarParaSelec(ActionEvent actionEvent) {
        acao = 3;
        firstCircle = secondCircle = null;
    }



    public void testeUnico(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root  = FXMLLoader.load(getClass().getResource("unicoTeste.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void testeMultiplo(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root  = FXMLLoader.load(getClass().getResource("multiplosTestes.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    //                                          testes



    public static void avaliarPalavra(TextField palavraDigitada, TextField resultado) {
        if(inicial == null ){
            resultado.setText("Reprovado");
        }
        else
        if(palavraDigitada.getText().isEmpty()){
            if(listaFinais.contains(inicial))
                resultado.setText("Aprovado");
        }
        else{
            StackPane stackPane = inicial;
            String palavra = palavraDigitada.getText();
            List<StackPane> caminhos = new ArrayList<>();
            int i = 0;
            encontrarCaminhos(stackPane,palavra.charAt(i++),caminhos);
            while(i<palavra.length() && !caminhos.isEmpty()){
                int tam = caminhos.size();
                for(int j = 0; j<tam && !caminhos.isEmpty(); j++){
                    stackPane = caminhos.remove(0);
                    encontrarCaminhos(stackPane,palavra.charAt(i),caminhos);
                }
                i++;

            }
            if(caminhos.isEmpty())
                    resultado.setText("Reprovado");
            else{
                if(verificarFim(listaFinais,caminhos))
                    resultado.setText("Aprovado");
                else
                    resultado.setText("Reprovado");
            }
        }

    }

    private static boolean verificarFim(List<?> listaFinais, List<?> caminhos) {
        List<?> listaVerif = new ArrayList<>(listaFinais);
        listaVerif.retainAll(caminhos);
        return !listaVerif.isEmpty();
    }

    private static List<StackPane> encontrarCaminhos(StackPane stackPane,char letra,List<StackPane> caminhos) {
        for(Seta seta : listaSetas){
            if(seta.getPrimC() == stackPane && letras(seta.getTexto()).contains(letra)){
                caminhos.add(seta.getSegC());
            }
        }

        return caminhos;
    }

    private static List<Character> letras(Text texto) {
        List<Character> letras = new ArrayList<>();

        // Divide o texto com base na vírgula
        String[] partes = texto.getText().split(",");

        for (String parte : partes) {
            if (!parte.trim().isEmpty()) {
                letras.add(parte.trim().charAt(0));
            }
        }

        return letras;
    }

    public void excluirElemento(ActionEvent actionEvent) {

    }



}
