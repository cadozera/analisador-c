package br.com.analisadorc;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class AnalisadorLexico {

    public List<Token> analisar(String caminhoArquivo) {
        List<Token> tokens = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            int numeroLinha = 1;

            while ((linha = br.readLine()) != null) {
                System.out.println("Analisando linha " + numeroLinha + ": " + linha);

                // 🎯 Trata diretivas do pré-processador
                if (linha.trim().startsWith("#")) {
                    String[] partesPreprocessador = linha.trim().split("\\s+");
                    String diretiva = partesPreprocessador[0];

                    switch (diretiva) {
                        case "#include":
                            if (partesPreprocessador.length >= 2) {
                                tokens.add(new Token(TokenType.INCLUDE, "#include", numeroLinha));
                                tokens.add(new Token(TokenType.LIBRARY, partesPreprocessador[1], numeroLinha));
                            }
                            break;
                        case "#define":
                            tokens.add(new Token(TokenType.DEFINE, "#define", numeroLinha));
                            if (partesPreprocessador.length >= 2)
                                tokens.add(new Token(TokenType.IDENTIFIER, partesPreprocessador[1], numeroLinha));
                            if (partesPreprocessador.length >= 3)
                                tokens.add(new Token(TokenType.NUMBER, partesPreprocessador[2], numeroLinha));
                            break;
                        case "#ifdef":
                            tokens.add(new Token(TokenType.IFDEF, "#ifdef", numeroLinha));
                            if (partesPreprocessador.length >= 2)
                                tokens.add(new Token(TokenType.IDENTIFIER, partesPreprocessador[1], numeroLinha));
                            break;
                        case "#ifndef":
                            tokens.add(new Token(TokenType.IFNDEF, "#ifndef", numeroLinha));
                            if (partesPreprocessador.length >= 2)
                                tokens.add(new Token(TokenType.IDENTIFIER, partesPreprocessador[1], numeroLinha));
                            break;
                        case "#endif":
                            tokens.add(new Token(TokenType.ENDIF, "#endif", numeroLinha));
                            break;
                        default:
                            System.err.println("Diretiva desconhecida na linha " + numeroLinha + ": " + diretiva);
                    }

                    numeroLinha++;
                    continue;
                }

                // 🧠 Detectar e isolar strings entre aspas
                List<String> strings = new ArrayList<>();
                Matcher m = Pattern.compile("\"(.*?)\"").matcher(linha);
                int i = 0;
                while (m.find()) {
                    strings.add(m.group());
                    linha = linha.replace(m.group(), "___STRING" + i + "___");
                    i++;
                }

                // ✂️ Separação de tokens
                String[] partes = linha.split(
                        "\\s+|(?<=[=+\\-*/%&|^!<>~;,()\\[\\]{}\\.])|(?=[=+\\-*/%&|^!<>~;,()\\[\\]{}\\.])"
                );

                // Substitui strings temporárias
                for (int j = 0; j < partes.length; j++) {
                    if (partes[j].matches("___STRING\\d+___")) {
                        int index = Integer.parseInt(partes[j].replaceAll("\\D+", ""));
                        partes[j] = strings.get(index);
                    }
                }

                // Geração dos tokens
                for (String parte : partes) {
                    if (parte.isBlank()) continue;

                    Token token = reconhecerToken(parte, numeroLinha);
                    tokens.add(token);
                    System.out.println(token);
                }

                numeroLinha++;
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        return tokens;
    }

    // Você já deve ter seu método reconhecerToken() implementado
    private Token reconhecerToken(String lexema, int linha) {
        // Palavras-chave da linguagem C
        Set<String> palavrasChave = Set.of(
                "int", "float", "char", "double", "void", "if", "else", "while", "for", "return", "struct"
        );

        // Operadores comuns
        Set<String> operadores = Set.of(
                "+", "-", "*", "/", "=", "==", "!=", "<", ">", "<=", ">=", "&&", "||", "&", "|", "!", "%", ".", "->"
        );

        // Símbolos
        Set<String> simbolos = Set.of(
                ";", ",", "(", ")", "{", "}", "[", "]"
        );

        // Verifica palavra-chave
        if (palavrasChave.contains(lexema)) {
            return new Token(TokenType.KEYWORD, lexema, linha);
        }

        // Verifica operador
        if (operadores.contains(lexema)) {
            return new Token(TokenType.OPERATOR, lexema, linha);
        }

        // Verifica símbolo
        if (simbolos.contains(lexema)) {
            return new Token(TokenType.SYMBOL, lexema, linha);
        }

        // Verifica string
        if (lexema.matches("\".*\"")) {
            return new Token(TokenType.STRING, lexema, linha);
        }

        // Verifica número inteiro ou decimal
        if (lexema.matches("\\d+")) {
            return new Token(TokenType.NUMBER, lexema, linha);
        }
        if (lexema.matches("\\d+\\.\\d+")) {
            return new Token(TokenType.NUMBER, lexema, linha);
        }

        // Verifica identificador (nomes de variáveis, funções, etc.)
        if (lexema.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            return new Token(TokenType.IDENTIFIER, lexema, linha);
        }

        // Caso nenhum padrão reconhecido
        return new Token(TokenType.UNKNOWN, lexema, linha);
    }
}



