/*
Exercício 1: Aplicações

1 - Um sistema de notificações que precisa variar em duas dimensões: o tipo de conteúdo (informativo, alerta, promoção) e o canal de envio (SMS, e-mail, push). Sem o padrão, cada combinação viraria uma classe nova.
R: Faz sentido usar Bridge. Tipo de conteúdo e canal são dimensões independentes, e combiná-los por herança produziria classes como AlertaSms, AlertaEmail e PromocaoPush. Com Bridge, cada conteúdo referencia um canal e as duas hierarquias podem crescer sem explosão de subclasses.

2 - Uma aplicação multiplataforma que precisa rodar a mesma lógica sobre APIs de sistema operacional diferentes (Windows, Linux, macOS), sem multiplicar as classes da interface.
R: Faz sentido usar Bridge. A lógica da aplicação pode ser uma hierarquia de abstração, enquanto as APIs de cada sistema operacional são implementações separadas. Isso permite manter a mesma interface da aplicação e trocar ou adicionar plataformas sem duplicar a lógica.

3 - Um módulo de persistência em que a mesma lógica de repositório precisa funcionar com MySQL ou PostgreSQL, podendo até trocar de banco em tempo de execução.
R: Faz sentido usar Bridge. O repositório representa uma dimensão e o acesso ao banco representa outra, que pode ser fornecida por uma interface comum. Dessa forma, a lógica do repositório não fica presa ao banco concreto e pode operar com MySQL ou PostgreSQL.

4 - Um framework de testes que precisa executar testes em diferentes ambientes com diferentes configurações de hardware.
R: Faz sentido usar Bridge se os tipos de teste e os ambientes de execução variarem de forma independente. O teste pode delegar sua execução a um ambiente, permitindo combinar testes unitários, integração ou carga com máquinas, navegadores ou configurações diferentes sem criar uma classe para cada combinação.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 2: Analogia

R: Uma editora que produz livros em diferentes formatos representa bem o Bridge. A editora possui diferentes tipos de conteúdo, como romance, livro técnico e livro infantil, e diferentes formatos de publicação, como impresso, e-book e audiolivro.

Criar uma equipe específica para cada combinação, como RomanceImpresso, RomanceEbook, TecnicoImpresso e TecnicoAudiolivro, ficaria cada vez mais difícil de manter. Em vez disso, o conteúdo do livro e o formato de publicação são escolhidos separadamente no momento da produção. Assim, novos gêneros ou formatos podem ser incluídos sem reconstruir todas as combinações existentes.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 3: Anti-pattern

1 - Por que essa hierarquia, uma classe por combinação de forma e cor, é um problema de design?
R: A forma e a cor são duas dimensões ortogonais, mas estão misturadas na mesma hierarquia. Isso duplica código de desenho e obriga a criar uma classe para cada combinação, tornando a manutenção mais cara conforme o editor recebe novas formas e cores.

2 - O que acontece ao adicionar uma nova forma, como Triangulo, ou uma nova cor, como Verde? Quantas classes existiriam para N formas e M cores?
R: Ao adicionar Triangulo, seria necessário criar TrianguloVermelho, TrianguloAzul e uma classe para cada cor existente. Ao adicionar Verde, seria necessário criar CirculoVerde, QuadradoVerde e uma classe para cada forma existente. Para N formas e M cores, seriam necessárias N x M classes concretas.

3 - Solução com Bridge:

interface Cor {
    String getNome();
}

class Vermelho implements Cor {
    @Override
    public String getNome() {
        return "vermelho";
    }
}

class Azul implements Cor {
    @Override
    public String getNome() {
        return "azul";
    }
}

abstract class Forma {
    protected final Cor cor;

    protected Forma(Cor cor) {
        this.cor = cor;
    }

    public abstract void desenhar();
}

class Circulo extends Forma {
    public Circulo(Cor cor) {
        super(cor);
    }

    @Override
    public void desenhar() {
        System.out.println("Desenhando círculo " + cor.getNome());
    }
}

class Quadrado extends Forma {
    public Quadrado(Cor cor) {
        super(cor);
    }

    @Override
    public void desenhar() {
        System.out.println("Desenhando quadrado " + cor.getNome());
    }
}

R: Forma e suas subclasses, como Circulo e Quadrado, compõem a primeira hierarquia. Cor e suas implementações, como Vermelho e Azul, compõem a segunda. Forma mantém uma referência para Cor e delega a ela a informação da cor, permitindo criar new Circulo(new Azul()) ou new Quadrado(new Vermelho()) sem classes específicas para cada combinação.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 4: Exemplo real

1 - Quais são as duas hierarquias do padrão nesse exemplo? Onde está a abstração e onde está a implementação?
R: A abstração é a hierarquia de controles, formada por Control e suas subclasses, como Button. A implementação é a hierarquia de aparências definida pela interface Skin<C extends Skinnable> e por implementações como ButtonSkin. O controle mantém e usa uma Skin para separar comportamento e estado da apresentação visual.

2 - Em Button.java, localize o método createDefaultSkin(). O que ele retorna? Por que o Button delega a própria aparência a um ButtonSkin em vez de embutir todo o desenho no próprio controle?
R: createDefaultSkin() retorna new ButtonSkin(this), declarado pelo método com o tipo Skin<?>. Ao delegar a aparência, Button mantém apenas o comportamento e os dados do controle, enquanto ButtonSkin concentra a renderização e os detalhes visuais. Essa separação permite evoluir ou trocar a aparência sem alterar a lógica do botão, que é a ideia central do Bridge.
*/
