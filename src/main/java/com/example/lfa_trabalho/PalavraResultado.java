package com.example.lfa_trabalho;

public class PalavraResultado {
    public String palavra;
    public String resultado;

    public PalavraResultado(String palavra) {
        this.palavra = palavra;
        this.resultado = " "; // Inicialmente vazio
    }

    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
