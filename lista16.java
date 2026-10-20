
/*
Exercício 1: Aplicações

1 - Um jogo em que o jogador salva checkpoints do progresso
e pode voltar ao ponto salvo após perder.

R: Faz sentido usar Memento. O jogo precisa salvar e restaurar
seu estado anterior, sem expor seus atributos internos ao
sistema responsável pelos checkpoints.

2 - Um editor de texto que precisa oferecer desfazer/refazer,
restaurando o estado anterior do documento a cada passo.

R: Faz sentido usar Memento. O padrão permite guardar versões
anteriores do documento e recuperá-las ao desfazer ou refazer
alterações.

3 - Um formulário em várias etapas em que o usuário pode voltar
a um passo anterior e recuperar os dados daquele momento.

R: Faz sentido usar Memento. Cada etapa pode ter seu estado
salvo, permitindo restaurar os dados preenchidos anteriormente.

4 - Uma classe Configuracao lida uma única vez por um consumidor,
sem histórico ou necessidade de restaurar estados.

R: Não faz sentido usar Memento. Como não existe necessidade
de guardar estados anteriores, o padrão adicionaria uma
complexidade desnecessária.

5 - Uma classe Ponto, com apenas x e y, que nunca precisa
restaurar um estado passado.

R: Não faz sentido usar Memento. A classe apenas armazena
coordenadas e não precisa manter um histórico de alterações.
*/

/* ----------------------------------------------------------------- */

/*
Exercício 2: Rastreando o anti-pattern em um projeto real

1 - O Jogo expõe getters e setters para todos os atributos.
Por que isso quebra o encapsulamento? Quais são os riscos?

R: Isso permite que outras classes alterem diretamente qualquer
parte do estado do jogo, sem passar por suas regras. Assim,
podem surgir estados inconsistentes, como vidas negativas ou
informações que não correspondem ao progresso do jogador.

2 - Se o Jogo ganhar um novo atributo, como inimigos, quantos
pontos precisam ser alterados? O que isso indica?

R: No GerenciadorSave, pelo menos dois pontos precisam mudar:
o salvamento e a restauração. Também seria necessário atualizar
o Jogo e possivelmente o Main. Isso demonstra um acoplamento
forte, pois o gerenciador depende dos atributos internos do jogo.

3 - Quais são as consequências de guardar o histórico
em uma lista de String[]?

R: Os dados perdem seus tipos originais e passam a depender
da ordem dos índices. Isso exige conversões, dificulta a
manutenção e pode causar erros de restauração, inclusive
valores incorretos sem apresentar uma exceção.

4 - Refatoração com Memento:

Diagrama antes:

+---------------------------+
|           Jogo            |
+---------------------------+
| - fase: String            |
| - vidas: int              |
| - pontuacao: int          |
| - x: double               |
| - y: double               |
+---------------------------+
| + jogar(...)              |
| + getters/setters         |
| + toString()              |
+---------------------------+
             ^
             | acessa e altera atributos
             |
+---------------------------+
|      GerenciadorSave      |
+---------------------------+
| - checkpoints:            |
|   List<String[]>          |
+---------------------------+
| + salvar(Jogo)            |
| + restaurar(Jogo)         |
+---------------------------+

Main utiliza Jogo e GerenciadorSave.


Diagrama depois:

+---------------------------+
|           Jogo            |
|       (Originator)        |
+---------------------------+
| - fase: String            |
| - vidas: int              |
| - pontuacao: int          |
| - x: double               |
| - y: double               |
+---------------------------+
| + jogar(...)              |
| + salvar(): Checkpoint    |
| + restaurar(Checkpoint)   |
| + toString()              |
+---------------------------+
             |
             | cria e restaura
             v
+---------------------------+
|     <<interface>>         |
|        Checkpoint         |
|  (Interface estreita)     |
+---------------------------+
             ^
             | implementa
             |
+---------------------------+
|     Jogo.Estado           |
|   (Memento imutável)      |
+---------------------------+
| - fase: String            |
| - vidas: int              |
| - pontuacao: int          |
| - x: double               |
| - y: double               |
+---------------------------+

+---------------------------+
|      GerenciadorSave      |
|        (Caretaker)        |
+---------------------------+
| - checkpoints:            |
|   List<Checkpoint>        |
+---------------------------+
| + salvar(Checkpoint)      |
| + restaurar(): Checkpoint |
+---------------------------+
             |
             | armazena sem inspecionar
             v
         Checkpoint

Main coordena Jogo e GerenciadorSave.

R: Justificativas da refatoração:

Checkpoint (Memento):
A interface Checkpoint não expõe nenhum atributo. O estado
salvo fica na implementação privada Jogo.Estado, que utiliza
record para manter os dados imutáveis.

Jogo (Originator):
O Jogo passa a ser responsável por salvar e restaurar seu
próprio estado. Assim, seus atributos continuam privados
e não precisam mais de getters e setters públicos.

GerenciadorSave (Caretaker):
O gerenciador apenas armazena e devolve checkpoints.
Ele não conhece os atributos do jogo e não realiza
conversões de tipos.

Interfaces estreita e ampla:
O caretaker utiliza a interface estreita Checkpoint,
sem acesso aos dados internos. Já o originator conhece
a implementação completa do estado e consegue restaurar
todos os seus atributos.

Main:
O Main continua simulando o mesmo jogo, mas agora coordena
o salvamento e a restauração sem manipular diretamente
os atributos internos.


5 - Depois da refatoração, como adicionar um novo atributo
deixa de afetar o caretaker? Como o histórico fica seguro?

R: Um novo atributo precisa ser adicionado apenas ao Jogo
e à sua representação de estado, sem alterar o GerenciadorSave.
O histórico passa a utilizar List<Checkpoint>, mantendo
os tipos originais e evitando erros de índices e conversões.
*/
