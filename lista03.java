/* 
Exercício 1: Aplicações
Para cada cenário abaixo, indique se o padrão Singleton é apropriado ou não e justifique em 2–3 frases.

1 - Um serviço de configurações da aplicação que carrega propriedades de um arquivo (application.properties) e é lido por vários módulos de um sistema web.
2 - Um componente responsável por gerenciar conexões HTTP para chamadas a APIs externas, onde diferentes partes do sistema podem precisar de configurações de timeout e autenticação distintas.
3 - Um logger central que registra eventos da aplicação em arquivo/console, usado por dezenas de classes diferentes.
4 - Uma classe que representa o usuário autenticado atual em um sistema web com múltiplos usuários acessando simultaneamente.
5 - Um cache em memória compartilhado entre vários serviços do back-end, que armazena dados frequentemente lidos do banco.
Para cada item, responda:

“Faz sentido usar Singleton” ou “Não faz sentido usar Singleton”.
Explique rapidamente o porquê (unicidade, escopo, concorrência, testes, etc.).
1 - Faz sentido, pois as configurações normalmente são carregadas uma única vez e são usadas na aplicação inteira.
2 - Não faz sentido, pois nas diferentes partes onde é usado podem precisar da sua própia configuração.
3 - Faz sentido, pois pode ser usado de forma igual por várias classes, unindo a gravação dos eventos de log.
4 - não faz sentido, pois precisamos pegar o usuário logado e usando um singleton, todos possuiriam a mesma instância
5 - faz sentido, pois pode manter uma única coleção de dados pra evitar consultas repetidas ao banco.
*/
/* ----------------------------------------------------------------- */
/* 
Exercício 2: Analogia
Crie uma analogia própria para explicar o padrão Singleton para alguém que não é da área de TI.

1 - Descreva uma situação do mundo real em que:
    - exista algo que deve ser único,
    - e que seja acessado por várias pessoas/locais diferentes.
2 - Explique por que essa analogia representa bem:
    - a ideia de única instância,
    - e de ponto global de acesso.
3 - Indique também uma limitação da sua analogia (algo que não encaixa perfeitamente com o padrão). 
R: Painel de senhas de um banco. Tem um painel central que apresenta a senha atual, ele representa uma instância única porque todos consultam o mesmo painel e o acesso global porque está visível em uma área comum. A limitação é que o painel apenas exibe as informações e não oferece todas as funcionalidades que um objeto Singleton poderia possuir.
*/
/* ----------------------------------------------------------------- */
/* Exercício 3: Anti-pattern
Considere o código Java abaixo, usado em uma aplicação desktop de vendas:
*/
public class CarrinhoDeComprasSingleton {

    private static CarrinhoDeComprasSingleton instancia;

    private List<Item> itens = new ArrayList<>();

    private CarrinhoDeComprasSingleton() { }

    public static CarrinhoDeComprasSingleton getInstancia() {
        if (instancia == null) {
            instancia = new CarrinhoDeComprasSingleton();
        }
        return instancia;
    }

    public void adicionarItem(Item item) {
        itens.add(item);
    }

    public List<Item> getItens() {
        return itens;
    }

    // ...outros métodos de regras de negócio...
}
/*
Em uma versão futura, essa aplicação passa a ser multiusuário (cada cliente loga com sua conta, possivelmente em paralelo).

Responda:

1 - Por que esse uso de Singleton é um problema arquitetural nesse cenário?
R: porque o singleton mantém apenas uma instância do carrinho de compras, em um sistema com vários usuários, cada cliente precisa ter seu próprio.

2 - Que tipo de bugs ou comportamentos estranhos podem acontecer quando vários usuários utilizarem o sistema ao mesmo tempo?
R: 
- visualizar produtos adicionado por outro no carrinho;
- remover itens do carrinho de outro usuário;
- valor total incluir produtos de vários usuários;

3 - Sugira outra abordagem de design para o carrinho (sem usar Singleton) e explique, em linhas gerais, como as instâncias deveriam ser gerenciadas. 
R: criar uma classe comum sem singleton.
o sistema poderia criar uma instância para cada usuário e poderiam ser armazenadas em uma sessão, associadas ao usuário.
*/
/* ----------------------------------------------------------------- */
/* 
Exercício 4: Exemplo real
Acesse o seguinte arquivo em um projeto open source:

Projeto: Apache Spark
Arquivo: JavaRecoverableNetworkWordCount
O Apache Spark é um framework de processamento distribuído de dados em larga escala. No arquivo indicado, há uma classe que implementa um contador de palavras que pode ser recuperado após falhas.

Responda:

1 - Procure explicar, em linhas gerais, quais funcionalidades estão implementadas nesse exemplo.
R: O programa recebe textos pela rede, separa e conta as palavras. Ele ignora algumas palavras, registra quantas foram descartadas, salva os resultados em arquivo e usa checkpoint para se recuperar de falhas.

2 - Quais as classes presentes nesse arquivo que podem ser consideradas Singletons, como vimos em aula? Justifique sua resposta.
2: As classes são singletons. Elas mantêm uma única instância estática e fornecem acesso a ela pelo método getInstance().

3 - As soluções apresentadas no código são thread-safe? Explique o porquê.
R: Sim. O código utiliza volatile e synchronized

4 - Porque há duas verificações de if (instance == null) no método getInstance()? Podemos considerar essa estratégia desperdício de recursos? Justifique sua resposta.
R: a primeira evita sincronizações quando a instância já existe. A segunda garante que outra thread não tenha criado a instância enquanto aguardava, não é desperdício, é uma forma de combinar segurança e desempenho.
*/


