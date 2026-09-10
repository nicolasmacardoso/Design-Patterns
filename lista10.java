/*
Exercício 1: Aplicações

1 - Um explorador de arquivos que precisa listar e calcular o tamanho de pastas, que podem conter arquivos ou outras pastas (estrutura recursiva).
R: Faz sentido usar Composite. Arquivo e pasta podem ser tratados por uma interface comum, enquanto a pasta contém outros elementos da mesma interface. Assim, calcular o tamanho ou listar uma pasta funciona recursivamente sem o cliente precisar diferenciar arquivo de subpasta.

2 - Um cardápio digital de um restaurante em que seções podem conter itens de cardápio ou outras subseções, e o total de calorias de uma seção deve somar todos os itens abaixo dela.
R: Faz sentido usar Composite. Item e seção representam uma estrutura parte-todo, pois uma seção pode conter itens simples ou outras seções. A mesma operação de calcular calorias pode ser chamada em qualquer nível e a seção soma seus filhos recursivamente.

3 - Um motor de interface gráfica em que um painel pode conter controles simples (botões, rótulos) ou outros painéis aninhados, e o sistema precisa desenhar/ocultar qualquer elemento da mesma forma.
R: Faz sentido usar Composite. Controles simples e painéis podem implementar uma interface comum, como ComponenteVisual, com desenhar() e ocultar(). O painel delega essas operações para seus filhos, inclusive outros painéis, permitindo ao cliente tratar toda a árvore de modo uniforme.

4 - Um cadastro de produtos com uma lista simples e plana (nome, preço, estoque) que nunca terá itens compostos ou hierarquia.
R: Não faz sentido usar Composite. Não existe relação recursiva parte-todo nem necessidade de tratar folhas e grupos da mesma maneira. Uma lista simples de produtos resolve o problema com menos classes e menos complexidade.

5 - Um sistema de uma rede de lojas em que cada loja pertence a uma região, cada região a um estado e cada estado ao país, e é preciso, a partir de qualquer nível, somar o faturamento de tudo abaixo daquele nó.
R: Faz sentido usar Composite. Loja é uma folha, enquanto região, estado e país são compostos que agrupam outros nós da mesma hierarquia. Todos podem expor calcularFaturamento(), e cada nó composto soma o resultado dos filhos de forma recursiva.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 2: Analogia

R: Uma caixa de presentes representa bem o Composite. Uma caixa pode conter presentes individuais, como um livro ou uma caneca, mas também pode conter outras caixas menores. Para descobrir o valor total de uma caixa, basta perguntar o valor dela: se for um presente, ela informa seu próprio valor; se for uma caixa, soma o valor de tudo que está dentro.

Quem organiza a entrega não precisa ter uma regra diferente para presente e caixa. Ele pode colocar qualquer item na embalagem e pedir o valor total para o item principal, pois a própria estrutura resolve as partes internas recursivamente. Isso simplifica o uso da hierarquia e permite aninhar caixas quantas vezes forem necessárias.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 3: Anti-pattern

1 - Por que calcular o total fora dos objetos, usando instanceof, é um problema de design? Onde mora a lógica da árvore e o que acontece se outro trecho do sistema precisar percorrer a mesma estrutura?
R: Pedido conhece todos os tipos concretos e concentra a lógica da árvore em métodos externos. A recursão fica fora de Caixa, embora Caixa seja quem conhece seus itens. Se outro trecho precisar imprimir, aplicar desconto ou calcular peso, ele terá de repetir instanceof e a travessia recursiva, criando duplicação e risco de comportamento diferente.

2 - O que acontece ao adicionar um novo tipo, como ProdutoComDesconto ou um serviço de montagem? Que bugs ou confusões esse código tende a gerar?
R: Todo método que usa instanceof precisa ser alterado para reconhecer o novo tipo. É fácil esquecer um trecho, retornar zero indevidamente ou usar uma regra de cálculo diferente entre métodos. Além disso, List<Object> não impede inserir objetos que não pertencem a um pedido, reduzindo a segurança de tipos.

3 - Solução com Composite:

interface ItemPedido {
    double calcularTotal();
    void imprimir();
}

class Produto implements ItemPedido {
    private final String nome;
    private final double preco;

    public Produto(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    @Override
    public double calcularTotal() {
        return preco;
    }

    @Override
    public void imprimir() {
        System.out.println(nome + ": R$ " + preco);
    }
}

class Caixa implements ItemPedido {
    private final String nome;
    private final List<ItemPedido> itens = new ArrayList<>();

    public Caixa(String nome) {
        this.nome = nome;
    }

    public void adicionar(ItemPedido item) {
        itens.add(item);
    }

    @Override
    public double calcularTotal() {
        double soma = 0;
        for (ItemPedido item : itens) {
            soma += item.calcularTotal();
        }
        return soma;
    }

    @Override
    public void imprimir() {
        System.out.println(nome + ":");
        for (ItemPedido item : itens) {
            item.imprimir();
        }
    }
}

class Pedido {
    public double calcularTotal(ItemPedido item) {
        return item.calcularTotal();
    }
}

R: ItemPedido é o Component, pois define as operações comuns para produtos e caixas. Produto é a folha, que retorna seu próprio preço; Caixa é o Composite, que armazena List<ItemPedido> e executa a recursão ao chamar o mesmo método em cada filho. O cliente usa apenas calcularTotal() ou imprimir(), sem precisar saber se recebeu uma folha ou uma caixa com diversos níveis internos.
*/
