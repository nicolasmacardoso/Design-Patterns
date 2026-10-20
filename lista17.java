
/*
Exercício 1: Aplicações

1 - Uma barra de ferramentas de um editor em que cada botão
dispara uma ação e o usuário precisa de desfazer/refazer.

R: Faz sentido usar Command. Cada botão pode executar um
comando sem conhecer sua implementação. Isso também facilita
o controle das operações de desfazer e refazer.

2 - Um agendador de tarefas que precisa enfileirar jobs,
executá-los depois e às vezes cancelá-los.

R: Faz sentido usar Command. As tarefas podem ser representadas
por objetos e armazenadas em uma fila para execução futura.
Também é possível remover comandos antes de executá-los.

3 - Um sistema de macros em que o usuário grava uma sequência
de ações e depois a repete como um único comando.

R: Faz sentido usar Command. Cada ação pode ser armazenada
como um objeto, permitindo executar várias operações em
sequência sem conhecer seus detalhes internos.

4 - Uma classe Relatorio que sempre gera o mesmo PDF,
sem fila, parametrização ou necessidade de desfazer.

R: Não faz sentido usar Command. Como a operação é única
e fixa, um método simples já resolve o problema. Aplicar
o padrão apenas aumentaria a complexidade.

5 - Uma classe Ponto com x e y, que apenas soma coordenadas
e nunca precisa reverter a operação.

R: Não faz sentido usar Command. A operação é simples e
não precisa de histórico, agendamento ou desfazer.
Utilizar o padrão seria desnecessário.
*/

/* ----------------------------------------------------------------- */

/*
Exercício 2: Rastreando o anti-pattern em um projeto real

1 - A Macro utiliza switch e chama o Editor diretamente.
Por que isso gera acoplamento forte? O que acontece ao
adicionar a ação SUBSTITUIR?

R: A Macro precisa conhecer todas as operações do Editor
e seus parâmetros. Para adicionar SUBSTITUIR, seria
necessário alterar o switch e implementar outro caso.
Isso torna a classe dependente de cada nova operação.

2 - As ações são strings como "DIGITAR:Ola".
Quais problemas de tipo e validação isso traz?

R: As strings não garantem que os parâmetros estejam
corretos. Um valor inválido em APAGAR pode causar erro
de conversão, e parâmetros ausentes podem gerar erro
de índice. Além disso, o separador ":" pode causar
problemas ao interpretar os comandos.

3 - Por que não é possível desfazer a macro original?
O que falta para reverter cada passo?

R: A Macro apenas executa as ações e não guarda informações
para desfazê-las. Seria necessário registrar os comandos
executados e implementar uma operação inversa para cada um,
como recuperar o texto removido pelo comando APAGAR.

4 - Refatoração com Command:

Diagrama antes:

+---------------------------+
|          Editor           |
+---------------------------+
| - texto: StringBuilder    |
+---------------------------+
| + digitar(String): void   |
| + apagar(int): String     |
| + getTexto(): String      |
+---------------------------+
             ^
             | chama diretamente
             |
+---------------------------+
|           Macro           |
+---------------------------+
| - editor: Editor          |
| - acoes: List<String>     |
+---------------------------+
| + Macro(Editor)           |
| + adicionar(String)       |
| + executar()              |
+---------------------------+
| switch para interpretar   |
| DIGITAR e APAGAR          |
+---------------------------+

Main cria Editor e Macro e adiciona ações em String.


Diagrama depois:

+---------------------------+
|     <<interface>>         |
|          Comando          |
+---------------------------+
| + executar(): void        |
| + desfazer(): void        |
+---------------------------+
             ^
             | implementam
      +------+------+
      |             |
+----------------+  +----------------+
| DigitarComando |  | ApagarComando  |
+----------------+  +----------------+
| - editor       |  | - editor       |
| - texto        |  | - quantidade   |
|                |  | - removido     |
+----------------+  +----------------+
| + executar()   |  | + executar()   |
| + desfazer()   |  | + desfazer()   |
+----------------+  +----------------+
      |                    |
      +---------+----------+
                |
                | utilizam
                v
+---------------------------+
|          Editor           |
|         (Receiver)        |
+---------------------------+
| - texto: StringBuilder    |
+---------------------------+
| + digitar(String): void   |
| + apagar(int): String     |
| + getTexto(): String      |
+---------------------------+

+---------------------------+
|           Macro           |
|         (Invoker)         |
+---------------------------+
| - comandos: List<Comando> |
| - historico: List<Comando>|
+---------------------------+
| + adicionar(Comando)      |
| + executar()              |
| + desfazer()              |
+---------------------------+
             |
             | guarda e dispara
             v
          Comando

Main cria os comandos e os adiciona à Macro.

R: Justificativas da refatoração:

Comando (Interface):
Define os métodos executar() e desfazer(), criando um
contrato comum para todas as operações. Assim, a Macro
não precisa conhecer os detalhes de cada comando.

DigitarComando e ApagarComando (Comandos concretos):
Cada classe representa uma operação específica. O comando
de digitar desfaz removendo o texto inserido, enquanto
o de apagar guarda o texto removido para recuperá-lo.

Editor (Receiver):
Continua responsável por realizar as operações no texto.
A validação da quantidade evita apagar mais caracteres
do que existem no editor.

Macro (Invoker):
Armazena objetos Comando em vez de strings. Ela executa
os comandos e mantém um histórico para desfazê-los na
ordem inversa, sem conhecer suas implementações.

Main:
Continua criando o editor e executando a macro, mas agora
utiliza comandos concretos. Também demonstra a restauração
do texto após desfazer todas as operações.


5 - Como adicionar uma nova ação fica mais simples?
Como os comandos permitem enfileirar, agendar e desfazer?

R: Basta criar uma nova classe que implemente Comando,
sem modificar a Macro. Como cada ação é um objeto, ela
pode ser armazenada em listas ou filas para execução futura.
O método desfazer() permite reverter as operações executadas.
*/
