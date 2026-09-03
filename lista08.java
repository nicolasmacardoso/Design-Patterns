/*
Exercício 1: Aplicações

1 - Um módulo de previsão do tempo que precisa consultar dois fornecedores externos (SDKs de terceiros) com APIs diferentes e não modificáveis, enquanto o restante da aplicação deve usar uma única interface de consulta.
R: Faz sentido usar Adapter. Os SDKs possuem interfaces incompatíveis e pertencem a terceiros, portanto não devem ser modificados. Um adaptador para cada fornecedor pode convertê-los para uma interface comum usada pela aplicação, reduzindo o acoplamento.

2 - Uma aplicação que hoje usa uma biblioteca legada de envio de e-mail e pretende trocá-la por outra, sem alterar os serviços que já fazem os envios.
R: Faz sentido usar Adapter. Os serviços podem depender de uma interface própria, como ServicoEmail, enquanto adaptadores encapsulam as chamadas da biblioteca legada e da nova biblioteca. A troca fica concentrada no adaptador, sem alterar todos os pontos que enviam e-mail.

3 - Um time que decide preparar o terreno e já cria adaptadores para todas as classes do sistema, mesmo sem existir hoje nenhuma interface incompatível real.
R: Não faz sentido usar Adapter. Sem uma incompatibilidade concreta, os adaptadores só aumentam a quantidade de classes e dificultam a leitura do projeto. Isso é overengineering: o padrão deve resolver uma necessidade de integração real.

4 - Um Checkout que precisa integrar um SDK de pagamento de terceiros para processar os pedidos.
R: Faz sentido usar Adapter. O Checkout pode depender de uma interface de pagamento definida pelo próprio sistema, enquanto um adaptador converte essa interface para a API do SDK externo. Assim, detalhes do fornecedor não se espalham pelo domínio e uma futura troca de gateway fica mais simples.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 2: Analogia

R: Um intérprete em uma reunião entre uma empresa brasileira e uma empresa japonesa representa bem o Adapter. As duas equipes falam idiomas diferentes, mas o intérprete entende o que uma parte diz e transmite a mesma informação no formato compreensível pela outra, sem mudar nenhum dos idiomas.

Se a empresa brasileira passar a negociar com uma equipe inglesa, ela não precisa aprender japonês nem alterar sua comunicação interna: basta usar um intérprete adequado para o novo idioma. Os dois lados continuam seguindo seus próprios padrões, e o intermediário resolve a incompatibilidade entre eles.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 3: Anti-pattern

1 - Por que essa forma de chamar os fornecedores diretamente é um problema de design?
R: PainelClima está acoplado às APIs concretas dos fornecedores, conhece suas assinaturas e ainda precisa aplicar regras específicas, como a conversão de Fahrenheit para Celsius. Como outros módulos repetem esse código, a mesma regra fica espalhada e qualquer alteração precisa ser feita em vários lugares.

2 - Que tipo de bugs ou dificuldades aparecem quando um SDK de terceiros muda ou quando um novo fornecedor precisa ser adicionado?
R: Se um fornecedor mudar a unidade, o nome do método ou o tipo retornado, todos os módulos que o chamam diretamente podem apresentar temperatura errada ou parar de compilar. Para adicionar outro fornecedor, seria necessário repetir if/else e conversões em cada módulo, aumentando o risco de divergências e regressões.

3 - Solução com Adapter:

interface ConsultaClima {
    double consultarTemperaturaCelsius(String cidade);
}

class AccuWeatherAdapter implements ConsultaClima {
    private final AccuWeatherApi api;

    public AccuWeatherAdapter(AccuWeatherApi api) {
        this.api = api;
    }

    @Override
    public double consultarTemperaturaCelsius(String cidade) {
        int fahrenheit = api.getTemperature(cidade);
        return (fahrenheit - 32) * 5.0 / 9.0;
    }
}

class OpenWeatherAdapter implements ConsultaClima {
    private final OpenWeatherApi api;

    public OpenWeatherAdapter(OpenWeatherApi api) {
        this.api = api;
    }

    @Override
    public double consultarTemperaturaCelsius(String cidade) {
        return api.getTempCelsius(cidade);
    }
}

class PainelClima {
    private final ConsultaClima consultaClima;

    public PainelClima(ConsultaClima consultaClima) {
        this.consultaClima = consultaClima;
    }

    public void exibirTemperatura(String cidade) {
        double celsius = consultaClima.consultarTemperaturaCelsius(cidade);
        System.out.println("Temperatura: " + celsius + "°C");
    }
}

R: ConsultaClima é a interface que o cliente entende. Cada adaptador encapsula um SDK externo e converte sua assinatura e unidade para o contrato comum. PainelClima recebe uma ConsultaClima pronta e deixa de depender de AccuWeatherApi, OpenWeatherApi ou da escolha feita por if/else.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 4: Exemplo real

1 - Quais são as duas interfaces incompatíveis que o InputStreamReader liga? O que a classe estende e o que ela recebe no construtor?
R: InputStreamReader liga uma fonte de bytes, InputStream, ao contrato de leitura de caracteres, Reader. A classe estende Reader e seus construtores recebem um InputStream junto com um charset, nome de charset ou CharsetDecoder; também existe a opção de usar o charset padrão.

2 - Observe os métodos read(), ready() e close(): para onde eles delegam? Por que o cliente que lê texto enxerga apenas um Reader e nunca precisa conhecer o InputStream por baixo?
R: Esses métodos delegam para o campo StreamDecoder sd: read() chama sd.read(), ready() chama sd.ready() e close() chama sd.close(). Como InputStreamReader é um Reader, o cliente trabalha apenas com a abstração de caracteres e usa seus métodos normalmente; o InputStream encapsulado é um detalhe interno do adaptador.

3 - Onde acontece a conversão de bytes para caracteres? Que variação do padrão Adapter essa classe representa e por que ela funciona com qualquer InputStream?
R: A conversão acontece no StreamDecoder, criado por StreamDecoder.forInputStreamReader(...) com o InputStream e o charset escolhido. É um Adapter de objeto, pois InputStreamReader estende o tipo esperado pelo cliente, Reader, e encapsula outro objeto, InputStream, em vez de herdar dele. Como arquivos, sockets e memória podem fornecer implementações de InputStream, qualquer uma delas pode ser usada pelo mesmo adaptador.
*/
