package br.com.analisadorc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class AnalisadorLexico {
    public void analisar(String caminhoArquivo) {
        List<Token> tokens = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            int numeroLinha = 1;

            while ((linha = br.readLine()) != null) {
                System.out.println("Analisando linha " + numeroLinha + ": " + linha);

                if (linha.trim().startsWith("#include")) {
                    String[] partesInclude = linha.trim().split("\\s+");
                    if (partesInclude.length >= 2) {
                        tokens.add(new Token(TokenType.PREPROCESSOR_KEYWORD, partesInclude[0], numeroLinha));
                        tokens.add(new Token(TokenType.LIBRARY, partesInclude[1], numeroLinha));
                        System.out.println(tokens.get(tokens.size() - 2));
                        System.out.println(tokens.get(tokens.size() - 1));
                    } else {
                        System.err.println("Erro: diretiva #include malformada na linha " + numeroLinha);
                    }

                    numeroLinha++;
                    continue;
                }

                List<String> strings = new ArrayList<>();
                Matcher m = Pattern.compile("\"(.*?)\"").matcher(linha);
                int i = 0;
                while (m.find()) {
                    strings.add(m.group());
                    linha = linha.replace(m.group(), "___STRING" + i + "___");
                    i++;
                }

                List<String> floats = new ArrayList<>();
                Matcher re_float = Pattern.compile("([0-9]+\\.[0-9]*|\\.[0-9]+)([eE][+-]?[0-9]+)?[fF]?").matcher(linha);
                int p = 0;
                while (re_float.find()) {
                    String floatLiteral = re_float.group();
                    linha = linha.replace(floatLiteral, "___FLOAT" + i + "___");
                    floats.add(floatLiteral);
                    p++;
                }
                String[] partes = linha.split("(?<=\\|\\||&&|==|!=|<=|>=|\\+\\+|--|\\+=|-=|\\*=|/=|%=|&=|\\^=|>>|<<|->)|(?=\\|\\||&&|==|!=|<=|>=|\\+\\+|--|\\+=|-=|\\*=|/=|%=|&=|\\^=|>>|<<|->)|(?<=[=+\\-*/%&^!<>~;,()\\[\\].{}])|(?=[=+\\-*/%&^!<>~;,()\\[\\].{}:])|\\s+");

                for (int q = 0; q < partes.length; q++) {
                    if (partes[q].matches("___FLOAT\\d+___")) {
                        int index = Integer.parseInt(partes[q].replaceAll("\\D+", ""));
                        partes[q] = floats.get(index);
                    }
                }

                for (int j = 0; j < partes.length; j++) {
                    if (partes[j].matches("___STRING\\d+___")) {
                        int index = Integer.parseInt(partes[j].replaceAll("\\D+", ""));
                        partes[j] = strings.get(index);
                    }
                }

                // 🔎 Reconhece tokens
                for (String parte : partes) {
                    String limpo = parte.trim();
                    if (limpo.isBlank()) continue;

                    Token token = reconhecerToken(limpo, numeroLinha);
                    tokens.add(token);
                    System.out.println(token);
                }

                numeroLinha++;
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    private Token reconhecerToken(String lexema, int linha) {
        // Palavras reservadas
        switch (lexema) {
            case "int":
            case "float":
            case "double":
            case "char":
            case "boolean":
            case "void":
                return new Token(TokenType.VOID, lexema, linha);
            case "if":
            case "else":
            case "while":
            case "for":
            case "do":
            case "break":
            case "continue":
            case "return":
                return new Token(TokenType.RETURN, lexema, linha);
            case "true":
            case "false":
                return new Token(TokenType._BOOL, lexema, linha);
            case "#include":
            case "#define":
                return new Token(TokenType.PREPROCESSOR_KEYWORD, lexema, linha);
        }

        // Operadores
        switch (lexema) {
            case "+":
            case "-":
            case "*":
            case "/":
            case "%":
            case ":":
            case "^":
            case "?":
                return new Token(TokenType.OPERATOR, lexema, linha);
            case "=":
                return new Token(TokenType.ASSIGN, lexema, linha);
            case "==":
            case "!=":
            case "<":
            case "<=":
            case ">":
            case ">=":
                return new Token(TokenType.COMPARISON, lexema, linha);
            case "&&":
            case "||":
            case "!":
                return new Token(TokenType.LOGICAL, lexema, linha);
            case "(":
                return new Token(TokenType.LPAREN, lexema, linha);
            case ")":
                return new Token(TokenType.RPAREN, lexema, linha);
            case "{":
                return new Token(TokenType.LBRACE, lexema, linha);
            case "}":
                return new Token(TokenType.RBRACE, lexema, linha);
            case "[":
                return new Token(TokenType.LBRACKET, lexema, linha);
            case "]":
                return new Token(TokenType.RBRACKET, lexema, linha);
            case ";":
                return new Token(TokenType.SEMICOLON, lexema, linha);
            case ",":
                return new Token(TokenType.COMMA, lexema, linha);
            case "&":
                return new Token(TokenType.AMPERSAND, lexema, linha);
            case "|":
                return new Token(TokenType.BITWISE, lexema, linha);
            case "~":
                return new Token(TokenType.BITWISENOT, lexema, linha);
        }

        // Literais e identificadores
        if (lexema.matches("\\d+")) {
            // Números inteiros
            return new Token(TokenType.INTEGER, lexema, linha);
        } else if (lexema.matches("\\d*\\.\\d+")) {
            // Números decimais
            return new Token(TokenType.FLOAT, lexema, linha);
        } else if (lexema.matches("<[a-zA-Z0-9_.]+>")) {
            return new Token(TokenType.LIBRARY, lexema, linha);
        } else if (lexema.matches("'.'")) {
            // Caracteres
            return new Token(TokenType.CHAR, lexema, linha);
        } else if (lexema.matches("\".*\"")) {
            // Strings
            return new Token(TokenType.STRING, lexema, linha);
        } else if (lexema.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            // Identificadores
            return new Token(TokenType.IDENTIFIER, lexema, linha);
        } else if (lexema.matches("<\\s*[a-zA-Z_][a-zA-Z0-9_]*\\.h\\s*>")) {
            // Identificadores
            return new Token(TokenType.HEADER_FILE, lexema, linha);
        } else if (lexema.equals(".")) {
            return new Token(TokenType.OPERATOR, lexema, linha);
        } else if (lexema.matches("0[xX][0-9a-fA-F]+")) {
            return new Token(TokenType.CONSTANT_HEX, lexema, linha);
        } else if (lexema.matches("0[bB][01]+")) {
            return new Token(TokenType.CONSTANT_BIN, lexema, linha);
        } else if (lexema.matches("([0-9]+\\.[0-9]*|\\.[0-9]+)([eE][+-]?[0-9]+)?[fF]?")) {
            return new Token(TokenType.CONSTANT_FLOAT, lexema, linha);
        }

        // Token não reconhecido
        return new Token(TokenType.ERROR, lexema, linha);
    }
}