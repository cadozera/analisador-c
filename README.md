# 🧠 Analisador Léxico e Semântico para C em Java

Este projeto é um analisador léxico e semântico de código C, desenvolvido em Java. Ele lê um arquivo `.c`, transforma em tokens e verifica se variáveis foram declaradas corretamente e usadas com o tipo apropriado.

---

## 🚀 Funcionalidades

✔️ Reconhecimento de:
- Palavras-chave (ex: `int`, `char`, `float`)
- Identificadores
- Operadores e símbolos (`=`, `+`, `[]`, `{}`, `;` etc.)
- Literais numéricos, strings e caracteres
- Diretivas como `#include`  
- Arrays e acesso por índice

✔️ Validações semânticas:
- Uso de variáveis não declaradas
- Declaração repetida de variáveis
- Tipos incorretos em atribuições

---

## 📂 Estrutura

analisador-c/

├── src/main/java/br/com/analisadorc/

│ ├── AnalisadorLexico.java

│ ├── AnalisadorSemantico.java

│ ├── Token.java

│ ├── TokenType.java

│ ├── Variavel.java

│ └── Main.java

├── exemplos/

│ └── exemplo1.c

├── build.gradle (opcional)

└── README.md

▶️ 3. Compile e rode o analisador
Se estiver usando linha de comando:

javac src/main/java/br/com/analisadorc/*.java
java -cp src/main/java br.com.analisadorc.Main exemplos/exemplo1.c

💬 Exemplo de saída
txt
Copy
Edit
Analisando linha 1: int x;
Token{type=KEYWORD, lexeme='int', line=1}
Token{type=IDENTIFIER, lexeme='x', line=1}
Variável 'x' do tipo 'int' declarada.

Analisando linha 5: y = 5;
Token{type=IDENTIFIER, lexeme='y', line=5}
Erro semântico na linha 5: variável 'y' não declarada.

🧭 Próximos passos

- Suporte a funções, escopo local e global

- Validação de tipos em expressões aritméticas

- Geração de relatório em JSON

- Testes automatizados (JUnit)

