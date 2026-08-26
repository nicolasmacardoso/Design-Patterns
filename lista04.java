/* 
Exercício 1: Aplicações
Para cada cenário abaixo, indique se o padrão Builder é apropriado ou não e justifique em 2–3 frases.

1 - Uma classe ConfiguracaoServidor que representa as configurações de conexão de um serviço, com campos como host, porta, timeout, quantidadeDeTentativas, usarSSL e proxy, onde a maioria dos campos é opcional e varia entre ambiente de desenvolvimento e produção.
R: faz sentido usar builder. a classe possui vários campos opcionais e pode ter configurações diferentes em cada ambiente

2 - Um componente responsável por montar requisições HTTP para chamadas a APIs externas, onde o código encadeia URL, método, cabeçalhos e corpo de forma legível (Request.Builder do OkHttp).
R: faz sentido usar. uma requisição possui várias partes opcionais e o encadeamento deixa o código mais claro e fácil de configurar

3 - Uma classe simples Ponto com apenas dois campos obrigatórios (x, y), utilizada em um sistema de CAD e instanciada dezenas de vezes por segundo.
R: não faz sentido usar. um construtor simples já é suficiente.

4 - Um RelatorioFinanceiro com muitas opções de configuração: título, período, filtros, ordenação, formato de saída (PDF, CSV), marca d’água e rodapé, sendo que diferentes módulos geram combinações diferentes sem alterar o código de montagem.
R: Faz sentido usar, o relatório pode ser montado de diferentes maneiras

5 - Uma classe Produto com três campos obrigatórios (nome, preco, quantidadeEstoque), criada em um único ponto do sistema através do construtor tradicional.
Para cada item, responda:
R: não faz sentido, possui somente 3 campos obrigatórios e é criada em um único local.

- “Faz sentido usar Builder” ou “Não faz sentido usar Builder”.
- Explique rapidamente o porquê (quantidade de campos, opcionais, legibilidade, risco de overengineering, etc.).

------------------------------------------

Exercício 2: Analogia
Crie uma analogia própria para explicar o padrão Builder para alguém que não é da área de TI.

1 - Descreva uma situação do mundo real em que:
    - um objeto seja montado passo a passo (etapas bem definidas),
    - existam partes obrigatórias e partes opcionais,
    - e as mesmas etapas possam gerar variações diferentes do resultado final.

2 - Explique por que essa analogia representa bem:
    - a separação entre o que é montado (Produto) e como é montado (Builder),
    - a ideia de montagem em passos claros,
    - e a possibilidade de variar combinações sem criar um novo processo do zero.

3 - Indique também uma limitação da sua analogia (algo que não encaixa perfeitamente com o padrão).

R: uma analogia para o builder seria a montagem de uma pizza, a massa e o molho são obrigatórios, enquanto queijo, borda recheada e outros ingredientes são opcionais.

ela representa o produto final, enquanto o funcionário que adiciona cada ingrediente representa o Builder.

a limitação, é que no mundo real alguns elementos não podem ser removidos facilmente depois de colocados, no builder normalmente é possível alterar as configurações antes de criar o objeto final.

------------------------------------------

Exercício 3: Anti-pattern
Considere o código Java abaixo, usado em uma aplicação de e-commerce:

*/
public class Pedido {

    private final String cliente;
    private final String endereco;
    private final List<String> itens;
    private final double desconto;
    private final String cupom;
    private final double frete;
    private final String observacoes;

    public Pedido(String cliente) { ... }

    public Pedido(String cliente, String endereco) { ... }

    public Pedido(String cliente, String endereco, List<String> itens) { ... }

    public Pedido(String cliente, String endereco, List<String> itens, double desconto) { ... }

    public Pedido(String cliente, String endereco, List<String> itens, double desconto, String cupom) { ... }

    public Pedido(String cliente, String endereco, List<String> itens, double desconto, String cupom, double frete, String observacoes) { ... }

    // ...outros métodos de regras de negócio...
}
/*  

Responda:

1 - Por que esse uso de construtores sobrecarregados (telescópicos) é um problema de design?
R: deixa a classe extensa e difícil de manter

2 - Que tipo de bugs ou confusões podem acontecer quando um desenvolvedor cria um Pedido chamando esses construtores?
R: pode trocar paraêmtros de tipos iguais, como cliente, endereco e cupom, sem o compilador perceber, ou pode nao entender o significado de cada argumento

3 - O que acontece com esse código a cada novo campo opcional adicionado à Pedido? Quantos construtores seriam necessários para N campos opcionais?
R: cada novo campo aumenta a quantidade de construtores e torna o código mais difícil de modificar. para aceitar todas as combinações possíveis, poderiam ser necessários até 2^N construtores.

4 - Sugira outra abordagem de design usando o padrão Builder e explique, em linhas gerais, como a criação do objeto passaria a funcionar (construtor privado, Builder interno, métodos fluentes e build()).
R: a classe Pedido teria um construtor privado que recebe um Builder. o Builder armazenaria os valores temporariamente, ofereceria métodos fluentes como setEndereco() e setCupom() e criaria o pedido pelo método build().

------------------------------------------

Exercício 4: Exemplo real
Acesse o seguinte arquivo em um projeto open source:

Projeto: OkHttp (biblioteca HTTP para Java/Kotlin)
Arquivo: Request.kt
O OkHttp é uma das bibliotecas mais utilizadas para requisições HTTP em Java/Kotlin. A classe Request representa uma requisição HTTP imutável a partir do momento em que é criada.

Responda:

1 - Procure explicar, em linhas gerais, por que a classe Request é imutável e qual o papel do seu Builder nessa garantia.
R: a request recebe os valores do builder durante a crianção e não oferece métodos para modificá-los depois. a documentação ressalva que ela é imutável quando o body também é nulo ou imutável.

2 - Observe os métodos de configuração da classe Builder (por exemplo, url(...), header(...), method(...), get(), post(body)). O que esses métodos têm em comum no tipo de retorno e por que isso permite o encadeamento fluente visto em aula?
R: métodos como url(), header(), get() e post() retornam o próprio builder. isso permite chamar vários métodos em sequência antes de usar build()

3 - Analise o método build() e as verificações feitas nesse exemplo (por exemplo, checkNotNull(builder.url)). Relacione esse comportamento com a validação de campos obrigatórios do build() visto em aula.
R: ao criar a request, o código verifica se a URL foi informada usando checkNotNull, isso impede a criação de uma requisição sem um campo obrigatório

4 - O Builder desse exemplo é uma classe interna. Compare com o que foi apresentado na aula sobre Builder interno static e comente se a relação entre o Builder e o produto final é a mesma que você aprendeu.
R: o builder é declarado dentro de Request, mantendo uma relação direta com o produto criado, em Kotlin, uma classe interna sem a palavra inner funciona de maneira semelhanet a uma classe interna estática do Java.
*/


