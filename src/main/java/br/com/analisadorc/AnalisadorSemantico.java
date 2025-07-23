package br.com.analisadorc;
import java.util.*;

public class AnalisadorSemantico {
    private Map<String, Variavel> tabelaSimbolos = new HashMap<>();

    public void analisar(List<Token> tokens) {
        // 1ª PASSAGEM: declarações
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (token.getLexeme().equals("int") || token.getLexeme().equals("float") || token.getLexeme().equals("char")) {
                if (i + 1 < tokens.size()) {
                    Token prox = tokens.get(i + 1);
                    if (prox.getType() == TokenType.IDENTIFIER) {
                        String nomeVar = prox.getLexeme();
                        String tipoVar = token.getLexeme();

                        if (tabelaSimbolos.containsKey(nomeVar)) {
                            System.err.println("Erro semântico na linha " + prox.getLine() +
                                    ": variável '" + nomeVar + "' já declarada.");
                        } else {
                            tabelaSimbolos.put(nomeVar, new Variavel(nomeVar, tipoVar));
                            System.out.println("Variável '" + nomeVar + "' do tipo '" + tipoVar + "' declarada.");
                        }
                    }
                }
            }
        }
        // 2ª PASSAGEM: usos e atribuições
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (token.getType() == TokenType.IDENTIFIER) {
                String nomeVar = token.getLexeme();

                // Ignora se for declaração
                boolean isDeclaracao = (i > 0 &&
                        (tokens.get(i - 1).getLexeme().equals("int") ||
                                tokens.get(i - 1).getLexeme().equals("float") ||
                                tokens.get(i - 1).getLexeme().equals("char")));

                if (isDeclaracao) continue;

                // Verifica se foi declarada
                if (!tabelaSimbolos.containsKey(nomeVar)) {
                    System.err.println("Erro semântico na linha " + token.getLine() +
                            ": variável '" + nomeVar + "' não declarada.");
                }

                // Verifica tipo na atribuição
                if (i + 2 < tokens.size() && tokens.get(i + 1).getLexeme().equals("=")) {
                    Token valor = tokens.get(i + 2);
                    Variavel var = tabelaSimbolos.get(nomeVar);

                    if (var != null) {
                        String tipoValor = switch (valor.getType()) {
                            case INT -> "int";
                            case FLOAT -> "float";
                            case CHAR -> "char";
                            case IDENTIFIER -> {
                                Variavel origem = tabelaSimbolos.get(valor.getLexeme());
                                if (origem != null) yield origem.getTipo();
                                else {
                                    System.err.println("Erro semântico na linha " + valor.getLine() +
                                            ": variável '" + valor.getLexeme() + "' não declarada.");
                                    yield "indefinido";
                                }
                            }
                            default -> "indefinido";
                        };

                        if (!tipoValor.equals("indefinido") && !tipoValor.equals(var.getTipo())) {
                            System.err.println("Erro semântico na linha " + valor.getLine() +
                                    ": tipo incompatível. Esperado '" + var.getTipo() +
                                    "', mas recebeu '" + tipoValor + "'.");
                        }
                    }
                }
            }
        }

    }
}
