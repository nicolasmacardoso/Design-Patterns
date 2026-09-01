/*
Exercício 1: Aplicações

1 - Uma aplicação de interface gráfica que precisa trocar o tema/plataforma (Windows, macOS, Linux). Cada plataforma exige uma família coerente de componentes (botão, checkbox, janela), e misturar componentes de plataformas diferentes quebra a interface.
R: Faz sentido usar Abstract Factory. Cada fábrica concreta pode criar uma família completa e coerente de componentes para um sistema operacional. A aplicação depende apenas das interfaces Botao, Checkbox e Janela, podendo trocar de plataforma sem conhecer as classes concretas.

2 - Uma classe simples Ponto com apenas dois campos obrigatórios (x, y), utilizada em um sistema de CAD e instanciada dezenas de vezes por segundo através do construtor tradicional.
R: Não faz sentido usar Abstract Factory. Não existem famílias de objetos relacionadas nem variantes que precisem ser mantidas coerentes. Usar uma fábrica para criar somente Ponto adicionaria complexidade sem resolver um problema real.

3 - Um módulo de acesso a bancos de dados que precisa manter famílias consistentes por fornecedor: para MySQL existem ConexaoMySQL, ComandoMySQL e TransacaoMySQL; para PostgreSQL existem as versões PostgreSQL. Todos os objetos usados juntos devem vir do mesmo fornecedor.
R: Faz sentido usar Abstract Factory. Uma fabrica MySQL pode fornecer conexao, comando e transação MySQL, enquanto a fabrica PostgreSQL fornece somente as versões PostgreSQL. Isso evita misturar objetos incompatíveis e desacopla o módulo das implementações específicas de cada fornecedor.

4 - Uma loja que vende kits de móveis por estilo (moderno, vitoriano, art déco). Cada kit é composto por cadeira + sofá + mesa de centro e o cliente espera que os três combinem entre si.
R: Faz sentido usar Abstract Factory. Cada fábrica de estilo cria uma família de móveis compatíveis, garantindo que cadeira, sofá e mesa de centro mantenham a mesma identidade visual. Para incluir um novo estilo, basta adicionar uma nova fábrica e seus produtos.

5 - Uma classe Produto com três campos obrigatórios (nome, preco, quantidadeEstoque), criada em um único ponto do sistema através do construtor tradicional e sem variações.
R: Não faz sentido usar Abstract Factory. Há apenas um objeto simples, sem variações ou produtos relacionados que precisem ser criados juntos. O construtor tradicional é mais direto e evita overengineering.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 2: Analogia

R: Uma empresa de eventos que monta cenários para festas pode representar o Abstract Factory. Ela possui pacotes completos de decoração: o pacote infantil inclui painel, mesas e lembrancinhas infantis; o pacote casamento inclui painel, mesas e lembrancinhas elegantes; o pacote corporativo inclui versões mais sóbrias desses mesmos itens.

Os itens de cada pacote foram escolhidos para combinar entre si. Se alguém misturar o painel infantil com as mesas corporativas e as lembrancinhas de casamento, o resultado ficará visualmente incoerente. A empresa de eventos funciona como a fábrica: ela entrega uma família completa de itens sem que o cliente precise saber como cada peça foi produzida ou escolhida.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 3: Anti-pattern

1 - Por que essa forma de criar os componentes com new é um problema de design?
R: Aplicacao conhece diretamente todas as classes concretas e concentra a regra de escolha das famílias de componentes. Ao crescer a quantidade de sistemas operacionais ou componentes, os if/else aumentam e fica mais fácil criar combinações incorretas.

2 - Que tipo de bug ou comportamento estranho pode acontecer quando o código mistura componentes de famílias diferentes, como no linux? E o que aconteceria ao adicionar um novo sistema operacional, como mac?
R: No caso linux, o BotaoLinux é criado junto com CheckboxWindows, quebrando a consistência visual e possivelmente usando comportamentos incompatíveis com o mesmo tema. Para adicionar mac, seria necessário alterar Aplicacao, criar novos if/else e lembrar de instanciar corretamente cada componente, o que aumenta o risco de regressão.

3 - Solução com Abstract Factory:

interface Botao {
    void renderizar();
}

interface Checkbox {
    void alternar();
}

class BotaoWindows implements Botao {
    @Override
    public void renderizar() {
        // Renderiza botão no estilo Windows.
    }
}

class CheckboxWindows implements Checkbox {
    @Override
    public void alternar() {
        // Alterna checkbox no estilo Windows.
    }
}

class BotaoLinux implements Botao {
    @Override
    public void renderizar() {
        // Renderiza botão no estilo Linux.
    }
}

class CheckboxLinux implements Checkbox {
    @Override
    public void alternar() {
        // Alterna checkbox no estilo Linux.
    }
}

interface FabricaComponentes {
    Botao criarBotao();
    Checkbox criarCheckbox();
}

class FabricaWindows implements FabricaComponentes {
    @Override
    public Botao criarBotao() {
        return new BotaoWindows();
    }

    @Override
    public Checkbox criarCheckbox() {
        return new CheckboxWindows();
    }
}

class FabricaLinux implements FabricaComponentes {
    @Override
    public Botao criarBotao() {
        return new BotaoLinux();
    }

    @Override
    public Checkbox criarCheckbox() {
        return new CheckboxLinux();
    }
}

class Aplicacao {
    private final Botao botao;
    private final Checkbox checkbox;

    public Aplicacao(FabricaComponentes fabrica) {
        botao = fabrica.criarBotao();
        checkbox = fabrica.criarCheckbox();
    }

    public void exibir() {
        botao.renderizar();
        checkbox.alternar();
    }
}

R: Botao e Checkbox são os produtos abstratos. FabricaComponentes é a fábrica abstrata, que declara os métodos para criar a família de componentes; FabricaWindows e FabricaLinux são fábricas concretas. Aplicacao recebe a fábrica pronta, normalmente escolhida na inicialização, e por isso não precisa conhecer o sistema operacional nem usar new nas classes concretas.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 4: Exemplo real

1 - Que família de produtos a interface KingdomFactory produz? Liste os métodos de criação.
R: KingdomFactory produz a família de produtos de um reino: Castle, King e Army. Seus métodos de criação são createCastle(), createKing() e createArmy().

2 - No ElfKingdomFactory, que objetos concretos são criados por cada método? O que aconteceria se um desses métodos retornasse, por engano, um produto orc, como OrcCastle?
R: ElfKingdomFactory cria ElfCastle em createCastle(), ElfKing em createKing() e ElfArmy em createArmy(). Se createCastle() retornasse OrcCastle, o reino teria componentes de temas diferentes e perderia a coerência que o padrão deve garantir. Mesmo que o compilador aceite por OrcCastle implementar Castle, a regra de negócio e a identidade da família seriam quebradas.

3 - O cliente do exemplo, a classe App, monta o reino recebendo uma fábrica. O que precisaria mudar no código do cliente para trocar de reino elfo para orc? Relacione com o princípio OCP.
R: O cliente precisa apenas receber ou selecionar uma OrcKingdomFactory no lugar de ElfKingdomFactory, sem alterar a lógica que chama createKing(), createCastle() e createArmy(). Para adicionar outro reino, cria-se uma nova fábrica concreta e seus produtos; o código que trabalha com KingdomFactory continua igual. Isso segue OCP, pois o sistema é aberto para extensão e fechado para modificação do cliente já testado.
*/
