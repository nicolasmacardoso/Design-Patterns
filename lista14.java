/*
Exercício 1: Aplicações

1 - Uma sala de chat em que dezenas de usuários trocam mensagens e as regras de conversa (quem fala com quem, sons, privacidade) mudam com frequência, sem que cada usuário conheça os demais.
R: Faz sentido usar Mediator. A sala controla as mensagens e as regras, sem cada usuário precisar conhecer os demais.

2 - Uma tela de diálogo com muitos widgets que reagem uns aos outros: ao marcar um Checkbox, um Botão é habilitado e um Campo de texto é limpo, formando uma teia de dependências.
R: Faz sentido usar Mediator. Ele coordena as reações dos widgets, evitando dependências diretas entre eles.

3 - Uma central de despacho de entregas que precisa coordenar motoristas, pedidos e entregas, decidindo qual pedido vai para qual motorista conforme as regras mudam.
R: Faz sentido usar Mediator. A central organiza os pedidos e motoristas, concentrando as regras de despacho.

4 - Uma biblioteca em que a classe Impressora se comunica com um único Spooler fixo, sempre pelo mesmo método simples, e essa interação nunca cresce nem ganha novos objetos.
R: Não faz sentido usar Mediator. A interação é simples e fixa, então a comunicação direta já resolve.

5 - Uma classe Ponto, com apenas x e y, que só guarda coordenadas e nunca participa de interações com outros objetos.
R: Não faz sentido usar Mediator. Ponto só guarda dados e não participa de interações entre objetos.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 2: Rastreando o anti-pattern em um projeto real

1 - Usuario guarda uma lista de contatos (outros Usuario). Para N usuários, quantas ligações podem existir? O que isso indica sobre o acoplamento?
R: Podem existir N * (N - 1) referências, pois cada usuário conhece todos os outros. Isso gera alto acoplamento e crescimento quadrático.

2 - O que é preciso alterar para incluir um novo usuário (por exemplo, o Diego)? Liste os pontos tocados. E para remover alguém, que problemas podem sobrar?
R: No Main, é preciso criar Diego, adicioná-lo aos contatos de Ana, Bruno e Carla e adicionar os três aos contatos dele: seis novas ligações. Para remover alguém, é preciso limpar as referências em todas as listas, ou a pessoa pode continuar enviando ou recebendo mensagens.

3 - As regras de conversa (todos recebem? só o admin fala? mensagem privada?) não estão em nenhum lugar claro. Onde elas deveriam morar? Por quê?
R: Em SalaChat, pois ela coordena a comunicação. Assim, as regras ficam em um só lugar, sem alterar cada usuário.

4 - Proponha uma refatoração com MediadorChat, SalaChat e Usuario, apresentando os diagramas antes/depois, o código e as decisões de design.

Diagrama de classes antes:

+----------------------------------+
| Main                             |
| + main(args: String[]): void      |
+----------------------------------+
              | cria e conecta
              v
+----------------------------------+
| Usuario                          |
| - nome: String                   |
| - contatos: List<Usuario>         |----> 0..* Usuario
| + adicionarContato(Usuario): void |      (referências diretas)
| + removerContato(Usuario): void   |
| + enviar(String): void            |
| + receber(String, String): void   |
+----------------------------------+

Diagrama de classes depois:

+-------------------------------------------+
| <<interface>> MediadorChat                 |
| + registrar(Usuario): void                 |
| + remover(Usuario): void                   |
| + enviar(Usuario, String): void            |
| + enviarPrivado(Usuario, String, String):   |
|   void                                    |
+-------------------------------------------+
         ^ implementa            ^ conhece 1
         |                       |
+------------------------+   +----------------------------------+
| SalaChat               |   | Usuario                          |
| - usuarios:            |   | - nome: String                   |
|   Map<String, Usuario> |   | - mediador: MediadorChat          |
| implementa operações  |   | + getNome(): String              |
| de MediadorChat        |   | + getMediador(): MediadorChat     |
+------------------------+   | + enviar(String): void            |
         | conhece 0..*      | + enviarPrivado(String, String):   |
         +------------------>|   void                           |
                             | + receber(String, String): void   |
                             +----------------------------------+

Main cria SalaChat e os usuários e registra os participantes no mediador.

Código refatorado (mantido em comentário, como nas outras listas):

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

interface MediadorChat {
    void registrar(Usuario usuario);
    void remover(Usuario usuario);
    void enviar(Usuario remetente, String mensagem);
    void enviarPrivado(Usuario remetente, String destinatario, String mensagem);
}

class SalaChat implements MediadorChat {
    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();

    @Override
    public void registrar(Usuario usuario) {
        Objects.requireNonNull(usuario, "Usuário obrigatório");
        if (usuario.getMediador() != this) {
            throw new IllegalArgumentException("Usuário pertence a outro mediador");
        }
        Usuario existente = usuarios.get(usuario.getNome());
        if (existente != null && existente != usuario) {
            throw new IllegalArgumentException("Nome já registrado na sala");
        }
        usuarios.put(usuario.getNome(), usuario);
    }

    @Override
    public void remover(Usuario usuario) {
        usuarios.remove(usuario.getNome(), usuario);
    }

    private void validarRemetente(Usuario remetente) {
        if (usuarios.get(remetente.getNome()) != remetente) {
            throw new IllegalStateException("Remetente não participa da sala");
        }
    }

    @Override
    public void enviar(Usuario remetente, String mensagem) {
        validarRemetente(remetente);
        for (Usuario usuario : usuarios.values()) {
            if (usuario != remetente) {
                usuario.receber(remetente.getNome(), mensagem);
            }
        }
    }

    @Override
    public void enviarPrivado(Usuario remetente, String destinatario, String mensagem) {
        validarRemetente(remetente);
        Usuario usuario = usuarios.get(destinatario);
        if (usuario == null) {
            throw new IllegalArgumentException("Destinatário não participa da sala");
        }
        usuario.receber(remetente.getNome(), mensagem);
    }
}

class Usuario {
    private final String nome;
    private final MediadorChat mediador;

    public Usuario(String nome, MediadorChat mediador) {
        this.nome = Objects.requireNonNull(nome, "Nome obrigatório");
        this.mediador = Objects.requireNonNull(mediador, "Mediador obrigatório");
    }

    public String getNome() {
        return nome;
    }

    public MediadorChat getMediador() {
        return mediador;
    }

    public void enviar(String mensagem) {
        mediador.enviar(this, mensagem);
    }

    public void enviarPrivado(String destinatario, String mensagem) {
        mediador.enviarPrivado(this, destinatario, mensagem);
    }

    public void receber(String remetente, String mensagem) {
        System.out.println(nome + " recebeu de " + remetente + ": " + mensagem);
    }
}

class Main {
    public static void main(String[] args) {
        MediadorChat sala = new SalaChat();
        Usuario ana = new Usuario("Ana", sala);
        Usuario bruno = new Usuario("Bruno", sala);
        Usuario carla = new Usuario("Carla", sala);
        sala.registrar(ana);
        sala.registrar(bruno);
        sala.registrar(carla);

        ana.enviar("Bom dia, pessoal!");

        Usuario diego = new Usuario("Diego", sala);
        sala.registrar(diego);
        sala.remover(carla);
        diego.enviar("Olá, pessoal!");
        ana.enviarPrivado("Bruno", "Podemos conversar depois?");

        MediadorChat chatPrivado = new SalaChat();
        Usuario anaPrivado = new Usuario("Ana", chatPrivado);
        Usuario brunoPrivado = new Usuario("Bruno", chatPrivado);
        chatPrivado.registrar(anaPrivado);
        chatPrivado.registrar(brunoPrivado);
        anaPrivado.enviar("Esta sala tem apenas dois participantes.");
    }
}

R: MediadorChat define as operações de cadastro e envio, permitindo trocar o mediador sem alterar Usuario.

SalaChat guarda os participantes pelo nome e controla quem recebe as mensagens. As validações impedem nomes repetidos, cadastro na sala errada e envio por usuários removidos.

Usuario guarda apenas o mediador e delega o envio a ele. Assim, não precisa manter contatos nem conhecer as regras da sala.

5 - Depois de refatorar, como incluir/remover usuário muda? E como a mesma classe Usuario passa a servir em outra sala ou em um chat privado?
R: Basta usar registrar() ou remover(), sem modificar os outros usuários. A mesma classe Usuario pode receber outro mediador no construtor, inclusive uma sala com apenas duas pessoas; envioPrivado() permite falar com um destinatário específico.
*/
