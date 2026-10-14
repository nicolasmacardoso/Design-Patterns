/*
Exercício 1: Aplicações

1 - Uma estação meteorológica publica novas medições e vários displays precisam se atualizar automaticamente, sem que a estação conheça cada display.
R: Faz sentido usar Observer. A estação notifica os displays registrados sem depender das classes concretas deles.

2 - Uma interface gráfica em que um botão pode ter vários listeners e o modelo precisa avisar as views quando muda.
R: Faz sentido usar Observer. Vários interessados recebem os eventos, e novos listeners podem ser adicionados sem alterar o modelo.

3 - Uma newsletter em que um número variável de assinantes recebe o conteúdo novo e pode assinar/cancelar a qualquer momento.
R: Faz sentido usar Observer. Os assinantes são registrados ou removidos e recebem uma notificação quando há conteúdo novo.

4 - Uma classe Configuracao que guarda valores lidos uma única vez por um único consumidor fixo, sem notificações.
R: Não faz sentido usar Observer. O acesso direto já resolve, pois não existem mudanças para notificar.

5 - Uma classe Ponto, com apenas x e y, que só guarda coordenadas e nunca avisa ninguém sobre mudanças.
R: Não faz sentido usar Observer. A classe só guarda dados e não precisa notificar outros objetos.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 2: Rastreando o anti-pattern em um projeto real

1 - Por que as referências a DisplayTemperatura e DisplayEstatisticas representam acoplamento forte?
R: A estação cria e chama os displays concretos diretamente. Isso obriga a modificar a estação quando os tipos de display mudam.

2 - O que é preciso alterar na estação para adicionar um DisplayUmidade?
R: Criar a classe DisplayUmidade, adicionar um campo e instanciá-la na estação. Também é necessário chamar atualizar(umidade) dentro de setMedicoes().

3 - Por que não dá para registrar/remover um display em tempo de execução? O que faltaria?
R: Os displays são campos fixos, sem métodos de cadastro ou remoção. Falta uma lista de observadores com operações para registrar, remover e notificar.

4 - Refatoração com Observer:

Diagrama antes:

EstacaoMeteorologica
  - temperatura, umidade, pressao: double
  + setMedicoes(double, double, double): void
  |-- cria e conhece --> DisplayTemperatura
  |                      + atualizar(double): void
  +-- cria e conhece --> DisplayEstatisticas
                         - historico: List<Double>
                         + atualizar(double): void

Diagrama depois:

<<interface>> Sujeito
  + registrar(Observador): void
  + remover(Observador): void
  + notificar(): void
           ^ implementa
EstacaoMeteorologica
  - observadores: List<Observador>
  - temperatura, umidade, pressao: double
  + setMedicoes(double, double, double): void
           | conhece 0..*
           v
<<interface>> Observador
  + atualizar(double, double, double): void
           ^ implementam
           |-- DisplayTemperatura
           +-- DisplayEstatisticas
                 - historico: List<Double>

Código refatorado:

import java.util.ArrayList;
import java.util.List;

interface Observador {
    void atualizar(double temperatura, double umidade, double pressao);
}

interface Sujeito {
    void registrar(Observador observador);
    void remover(Observador observador);
    void notificar();
}

class EstacaoMeteorologica implements Sujeito {
    private final List<Observador> observadores = new ArrayList<>();
    private double temperatura;
    private double umidade;
    private double pressao;

    @Override
    public void registrar(Observador observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    @Override
    public void remover(Observador observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificar() {
        for (Observador observador : new ArrayList<>(observadores)) {
            observador.atualizar(temperatura, umidade, pressao);
        }
    }

    public void setMedicoes(double temperatura, double umidade, double pressao) {
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.pressao = pressao;
        notificar();
    }
}

class DisplayTemperatura implements Observador {
    @Override
    public void atualizar(double temperatura, double umidade, double pressao) {
        System.out.println("Temperatura atual: " + temperatura + "C");
    }
}

class DisplayEstatisticas implements Observador {
    private final List<Double> historico = new ArrayList<>();

    @Override
    public void atualizar(double temperatura, double umidade, double pressao) {
        historico.add(temperatura);
        double soma = historico.stream().mapToDouble(Double::doubleValue).sum();
        System.out.println("Media das temperaturas: " + (soma / historico.size()) + "C");
    }
}

class Main {
    public static void main(String[] args) {
        EstacaoMeteorologica estacao = new EstacaoMeteorologica();
        Observador temperatura = new DisplayTemperatura();
        Observador estatisticas = new DisplayEstatisticas();
        estacao.registrar(temperatura);
        estacao.registrar(estatisticas);

        estacao.setMedicoes(25.0, 60.0, 1013.0);
        estacao.setMedicoes(27.0, 55.0, 1010.0);

        estacao.remover(estatisticas);
        estacao.setMedicoes(28.0, 50.0, 1009.0);
    }
}

R: Observador define atualizar() com as três medições. Cada display usa os dados necessários, sem exigir que a estação conheça seu tipo.

Sujeito define o registro, a remoção e a notificação. EstacaoMeteorologica guarda a lista e avisa os interessados após atualizar as medições; a cópia da lista permite alterações no cadastro durante uma notificação.

DisplayTemperatura apresenta a temperatura e DisplayEstatisticas calcula a média. O Main escolhe os displays, separando a configuração do painel da estação.

5 - Como adicionar um display novo fica mais simples? Como reutilizar a estação em outro painel, sem recompilar?
R: Basta criar uma classe que implemente Observador e registrá-la. Outro painel pode escolher seus próprios displays sem alterar ou recompilar a estação; apenas o novo display e o código de configuração precisam ser compilados.
*/
