package com.example.lfa_trabalho;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class Seta {
    private StackPane primC;
    private StackPane segC;
    private Text texto;
    private Line linha;



    public Seta() {

    }

    public Seta(StackPane primC, StackPane segC, Text texto, Line linha) {
        this.primC = primC;
        this.segC = segC;
        this.texto = texto;
        this.linha = linha;
    }

    public StackPane getPrimC() {
        return primC;
    }

    public void setPrimC(StackPane primC) {
        this.primC = primC;
    }

    public StackPane getSegC() {
        return segC;
    }

    public void setSegC(StackPane segC) {
        this.segC = segC;
    }

    public Text getTexto() {
        return texto;
    }

    public void setTexto(Text texto) {
        this.texto = texto;
    }

    public Line getLinha() {
        return linha;
    }

    public void setLinha(Line linha) {
        this.linha = linha;
    }

}
