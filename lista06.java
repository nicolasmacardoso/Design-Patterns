/*
Exercício 1: Aplicações

1 - Um serviço de notificações que precisa enviar mensagens por email, SMS e push. O fluxo de envio é sempre o mesmo (montar a mensagem, enviar, registrar log), mas cada canal entrega de um jeito diferente, e novos canais entram frequentemente.
R: Faz sentido usar Factory Method. O serviço principal pode trabalhar com a abstração Notificador e deixar cada criador decidir qual canal concreto será usado. Isso reduz o acoplamento com EmailNotificador, SmsNotificador e PushNotificador e facilita incluir novos canais sem alterar o fluxo de envio.

2 - Uma classe simples Ponto com apenas dois campos obrigatórios (x, y), utilizada em um sistema de CAD e instanciada dezenas de vezes por segundo através do construtor tradicional.
R: Não faz sentido usar Factory Method. Não há variações de criação nem necessidade de desacoplar classes concretas, pois new Ponto(x, y) já é simples e claro. Criar fábricas nesse caso só adicionaria classes e indireção sem benefício real.

3 - Um framework de exportação de relatórios (PDF, CSV, Excel). O módulo principal não deve conhecer as classes concretas de exportação, e novas exportações devem entrar apenas com novas subclasses, sem alterar o fluxo de geração.
R: Faz sentido usar Factory Method. O módulo de geração pode depender apenas da interface Exportador, enquanto subclasses concretas criam ExportadorPdf, ExportadorCsv ou ExportadorExcel. Assim, novos formatos podem ser adicionados por extensão, seguindo o princípio OCP.

4 - Uma aplicação que precisa criar um cliente HTTP diferente conforme o ambiente: uma implementação mock nos testes e uma real (OkHttp) em produção, sem que o restante do código mude.
R: Faz sentido usar Factory Method. A aplicação pode depender de uma interface ClienteHttp, e cada criador fornece a implementação adequada para teste ou produção. Isso evita que o restante do código conheça detalhes de ambiente e torna os testes mais isolados.

5 - Uma classe Produto com três campos obrigatórios (nome, preco, quantidadeEstoque), criada em um único ponto do sistema através do construtor tradicional e sem variações.
R: Não faz sentido usar Factory Method. A criação é única, não há subclasses ou comportamentos alternativos a escolher. O construtor tradicional é suficiente e uma fábrica seria overengineering.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 2: Analogia

R: Uma rede de gráficas pode representar o Factory Method. O pedido sempre segue o mesmo processo: receber o arquivo do cliente, preparar o material, produzir e entregar. Porém, cada unidade decide qual produto concreto será criado: uma gráfica de cartões produz cartões de visita, outra produz banners e uma terceira produz adesivos.

Quando surge uma nova unidade especializada em convites, ela cria o produto Convite sem mudar o processo principal do pedido. O atendente não precisa conhecer os detalhes de impressão de cada produto; ele apenas inicia o processo e recebe o item pronto. Nesse exemplo, o processo fixo é o atendimento do pedido, as unidades são os criadores concretos e os materiais impressos são os produtos concretos.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 3: Anti-pattern

1 - Por que essa forma de escolher o canal com if/else é um problema de design?
R: NotificadorService está acoplado diretamente às classes concretas EmailNotificador, SmsNotificador e PushNotificador. A cada novo canal, a classe precisa ser modificada, concentrando decisões de criação e violando o princípio Aberto/Fechado (OCP).

2 - O que precisa ser alterado para adicionar um novo canal, como WhatsApp? Que riscos essa mudança traz para o código já testado?
R: Seria necessário criar WhatsAppNotificador e incluir mais um else if em NotificadorService. Além de aumentar a classe, a alteração pode quebrar condições existentes, tratar incorretamente um canal ou deixar de atender um caso, exigindo novos testes em uma parte que antes já funcionava.

3 - Solução com Factory Method:

interface Notificador {
    void enviar(String destinatario, String mensagem);
}

class EmailNotificador implements Notificador {
    @Override
    public void enviar(String destinatario, String mensagem) {
        // Envia e-mail.
    }
}

class SmsNotificador implements Notificador {
    @Override
    public void enviar(String destinatario, String mensagem) {
        // Envia SMS.
    }
}

class PushNotificador implements Notificador {
    @Override
    public void enviar(String destinatario, String mensagem) {
        // Envia notificação push.
    }
}

abstract class NotificadorService {
    public void enviarNotificacao(String destinatario, String mensagem) {
        Notificador notificador = criarNotificador();
        // Monta a mensagem e registra o log, se necessário.
        notificador.enviar(destinatario, mensagem);
    }

    protected abstract Notificador criarNotificador();
}

class EmailNotificadorService extends NotificadorService {
    @Override
    protected Notificador criarNotificador() {
        return new EmailNotificador();
    }
}

class SmsNotificadorService extends NotificadorService {
    @Override
    protected Notificador criarNotificador() {
        return new SmsNotificador();
    }
}

R: Notificador é a interface do produto e define o comportamento comum de envio. NotificadorService é o criador abstrato: ele contém o fluxo comum e declara o método fábrica criarNotificador(). Cada criador concreto escolhe uma implementação; para WhatsApp, basta criar WhatsAppNotificador e WhatsAppNotificadorService, sem modificar os serviços existentes.
*/
/* ----------------------------------------------------------------- */
/*
Exercício 4: Exemplo real

1 - Que papel a interface Blacksmith exerce no padrão? Qual é o método fábrica declarado nela e qual o seu tipo de retorno?
R: Blacksmith é o criador do padrão Factory Method. Ela declara o método fábrica manufactureWeapon(WeaponType weaponType), cujo retorno é Weapon. Portanto, quem usa um ferreiro depende da abstração da arma, não de uma classe específica de arma.

2 - No ElfBlacksmith, o que o método manufactureWeapon(...) retorna? Por que o tipo de retorno é a interface Weapon e não a classe concreta ElfWeapon?
R: ElfBlacksmith retorna uma nova instância de ElfWeapon com o tipo de arma solicitado. O retorno declarado como Weapon permite que o cliente use a capacidade comum das armas sem ficar acoplado a ElfWeapon; outro ferreiro pode devolver OrcWeapon e o código cliente continua igual.

3 - O que seria necessário para adicionar um novo ferreiro, como um anão, sem alterar o código que usa as armas? Relacione com o princípio OCP.
R: Seria necessário criar DwarfWeapon implementando Weapon e DwarfBlacksmith implementando Blacksmith, com manufactureWeapon(...) retornando a arma anã. O cliente continuaria recebendo Blacksmith e Weapon, sem conhecer essas novas classes. Isso aplica OCP: o sistema é aberto para extensão por novas implementações e fechado para modificação do código cliente já existente.
*/
