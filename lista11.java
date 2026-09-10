/*
Exercício 1: Aplicações

1 - Um serviço de envio de e-mails que, em alguns fluxos, precisa registrar log, em outros comprimir os anexos e em outros fazer os dois, sem criar uma classe para cada combinação.
R: Faz sentido usar Decorator. Log e compressão são responsabilidades opcionais e combináveis, que podem envolver o mesmo serviço de e-mail em diferentes ordens. Os decorators evitam classes como EmailComLog, EmailComCompressao e EmailComLogECompressao.

2 - Um editor de imagens que permite aplicar filtros em cadeia (brilho, contraste, sépia) em qualquer ordem e quantidade sobre uma foto.
R: Faz sentido usar Decorator. Cada filtro pode receber uma imagem e devolver outra imagem com uma transformação adicional, preservando a mesma interface. Dessa forma, os filtros podem ser combinados dinamicamente sem criar uma classe para cada sequência possível.

3 - Um repositório de produtos usado pelo sistema todo; para um único caso de uso de alta leitura, é preciso adicionar cache em memória, sem mudar a interface que os demais consumidores já usam.
R: Faz sentido usar Decorator. Um RepositorioComCache pode implementar a mesma interface do repositório original e acrescentar a responsabilidade de cache somente onde ela for necessária. Os demais consumidores continuam usando o contrato existente sem saber se há cache.

4 - Um catálogo de livros em que cada Livro tem apenas título, autor e preço com estrutura simples e estável que nunca ganha variações de comportamento.
R: Não faz sentido usar Decorator. Livro é um objeto simples, sem responsabilidades opcionais que precisem ser adicionadas em tempo de execução. Criar decorators nesse cenário só aumentaria a quantidade de classes sem benefício prático.

5 - Uma concessionária em que todo carro vendido sai exatamente igual, sempre com o mesmo pacote opcional embutido na classe Carro.
R: Não faz sentido usar Decorator. Como não há variações de acessórios nem necessidade de combinações dinâmicas, a classe Carro pode conter diretamente o comportamento fixo. O padrão só seria útil se os opcionais pudessem ser escolhidos e combinados por venda.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 2: Analogia

R: Um presente embrulhado representa bem o Decorator. O presente é o objeto base; podem ser acrescentados papel de presente, fita, cartão e laço. Mesmo depois dos acréscimos, ele continua sendo um presente, mas ganha aparência ou informações adicionais.

Não seria viável manter uma versão pronta para cada combinação, como presente com papel e fita, presente com cartão e laço, ou presente com todos os itens. Os acessórios são colocados um sobre o outro quando necessário, e cada um acrescenta algo sem modificar o presente original.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 3: Anti-pattern

1 - Por que modelar os complementos como flags booleanas dentro de Cafe é um problema de design? O que acontece com essa classe a cada novo complemento, como café com caramelo?
R: Cafe acumula a responsabilidade da bebida e de todos os complementos possíveis. Para cada novo complemento, é necessário adicionar um novo campo, mais condições em custo() e mais condições em getDescricao(). A classe cresce continuamente e fica fechada para extensão sem modificação.

2 - Que bugs e confusões esse código tende a gerar?
R: É fácil atualizar o preço e esquecer a descrição, ou adicionar o complemento em getDescricao() e esquecer custo(). Algumas combinações podem exigir regras próprias, como desconto ou incompatibilidade entre complementos, e essas regras deixam os ifs cada vez mais difíceis de manter. As flags também não representam bem comportamentos que vão além de apenas somar um valor.

3 - Solução com Decorator:

interface Bebida {
    double custo();
    String getDescricao();
}

class Cafe implements Bebida {
    @Override
    public double custo() {
        return 5.0;
    }

    @Override
    public String getDescricao() {
        return "Café";
    }
}

abstract class BebidaDecorator implements Bebida {
    protected final Bebida bebida;

    protected BebidaDecorator(Bebida bebida) {
        this.bebida = bebida;
    }
}

class Leite extends BebidaDecorator {
    public Leite(Bebida bebida) {
        super(bebida);
    }

    @Override
    public double custo() {
        return bebida.custo() + 1.5;
    }

    @Override
    public String getDescricao() {
        return bebida.getDescricao() + " com leite";
    }
}

class Chantilly extends BebidaDecorator {
    public Chantilly(Bebida bebida) {
        super(bebida);
    }

    @Override
    public double custo() {
        return bebida.custo() + 2.0;
    }

    @Override
    public String getDescricao() {
        return bebida.getDescricao() + " com chantilly";
    }
}

class Canela extends BebidaDecorator {
    public Canela(Bebida bebida) {
        super(bebida);
    }

    @Override
    public double custo() {
        return bebida.custo() + 0.5;
    }

    @Override
    public String getDescricao() {
        return bebida.getDescricao() + " com canela";
    }
}

// Bebida pedido = new Canela(new Chantilly(new Leite(new Cafe())));

R: Bebida é a interface comum; Cafe é o componente concreto; BebidaDecorator mantém uma referência para outra Bebida e delega suas operações. Leite, Chantilly e Canela são decorators concretos que acrescentam preço e descrição. As combinações são montadas ao envolver uma bebida com os decorators desejados, sem alterar a classe Cafe para cada novo complemento.
*/
