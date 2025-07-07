package br.com.analisadorc;
import java.util.*;

public class AnalisadorSemantico {
    private Map<String, Variavel> tabelaSimbolos = new HashMap<>();

    public void analisar(List<Token> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            // Detectar declaração: ex. int x;
            if (token.getType() == TokenType.INT || token.getType().name().equals("FLOAT")) {
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

            // Detectar uso da variável (excluindo quando for uma declaração)
            if (token.getType() == TokenType.IDENTIFIER) {
                boolean isDeclaracao = (i > 0 &&
                        (tokens.get(i - 1).getType() == TokenType.INT ||
                                tokens.get(i - 1).getLexeme().equals("float")));

                if (!isDeclaracao && !tabelaSimbolos.containsKey(token.getLexeme())) {
                    System.err.println("Erro semântico na linha " + token.getLine() +
                            ": variável '" + token.getLexeme() + "' não declarada.");
                }
            }
        }
    }
}
