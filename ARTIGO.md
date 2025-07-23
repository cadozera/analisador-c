# 🧠 Analisador Léxico e Semântico para C em Java

Desenvolvimento de um Analisador Léxico e Semântico

Introdução
O desenvolvimento de compiladores é uma área essencial da ciência da computação. Um compilador transforma o código-fonte
escrito em uma linguagem de alto nível em uma forma executável por uma máquina. Dentro desse processo, o analisador
léxico e o analisador semântico desempenham papéis fundamentais. Este artigo descreve, de forma prática, como projetar e
implementar essas duas etapas.

1. O que é um Analisador Léxico?

- O analisador léxico, também chamado de scanner ou tokenizer, é o primeiro estágio de um compilador. Sua principal
  função é ler o código-fonte bruto e convertê-lo em uma sequência de tokens. Cada token é uma unidade básica, como
  palavras-chave, identificadores, operadores, números ou símbolos.

- Principais Tarefas:

    1. Eliminar espaços em branco e comentários.
    2. Reconhecer padrões (palavras-chave, identificadores, literais, operadores).
    3. Categorizar cada token.
    4. Gerar erros léxicos quando encontra sequências inválidas.

2. O que é um Analisador Semântico?

- Enquanto o analisador léxico trabalha na superfície do código, o analisador semântico atua em um nível mais profundo.
  Ele é responsável por verificar se a combinação de tokens faz sentido dentro das regras da linguagem. Por exemplo,
  garante que tipos de variáveis sejam coerentes, que identificadores estejam declarados e que expressões sigam as
  restrições semânticas.

- Principais Tarefas:
    - Verificar tipos (type checking).
    - Verificar escopo e declaração de variáveis.
    - Detectar usos incorretos (ex.: atribuir valor inteiro a uma variável booleana).
    - Construir uma tabela de símbolos.

3. Etapas de Desenvolvimento
    - Definição da Gramática
        - Antes de escrever o código, é necessário definir:
            1. O alfabeto léxico: quais caracteres são válidos.
            2. Os padrões regulares: descrevem a formação dos tokens.
            3. As regras semânticas: o que é permitido na combinação dos tokens.
    - Implementação do Analisador Léxico
        - Pode-se usar ferramentas como Lex, Flex ou escrever o scanner manualmente em linguagens como Python, C ou
          Java.
    - Implementação do Analisador Semântico
        - Normalmente, o analisador semântico é integrado à análise sintática. Ele percorre a árvore sintática gerada e
          faz verificações de contexto.
        - Exemplo Simples:
        - Verificar se variáveis foram declaradas antes de serem usadas.
          Checar tipos em expressões aritméticas.

4. Desafios e Boas Práticas
    - Testar exaustivamente entradas válidas e inválidas.
    - Manter uma tabela de símbolos bem estruturada.
    - Fornecer mensagens de erro claras.
    - Usar automação de geração de código (ex.: Lex/Yacc, ANTLR) para projetos maiores.


5. Conclusão

- O desenvolvimento de um analisador léxico e semântico é uma ótima forma de compreender como compiladores funcionam
  internamente. Mesmo projetos simples ajudam a fixar conceitos como gramáticas formais, autômatos e verificação de
  tipos.

Dominar essas etapas abre portas para criar interpretadores, validadores de entrada e até linguagens próprias.

- Referências:
    - Aho, A.V., Lam, M.S., Sethi, R., Ullman, J.D. Compiladores: Princípios, Técnicas e Ferramentas.
- Ferramentas:
    - Lex/Flex, Yacc/Bison, ANTLR.
