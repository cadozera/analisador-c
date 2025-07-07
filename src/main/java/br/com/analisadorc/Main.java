package br.com.analisadorc;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        AnalisadorLexico analisador = new AnalisadorLexico();
        analisador.analisar("teste.c"); // Caminho relativo
        List<Token> tokens = analisador.analisar("teste.c");

        AnalisadorSemantico semantico = new AnalisadorSemantico();
        semantico.analisar(tokens);
    }
}
