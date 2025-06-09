package br.com.analisadorc;

public class Main {
    public static void main(String[] args) {
        AnalisadorLexico analisador = new AnalisadorLexico();
        analisador.analisar("teste.c"); // Caminho relativo
    }
}
