/* public Cliente(String nome, String sobrenome, String email) {
    this.nome = nome;
    this.sobrenome = sobrenome;
    this.email = email;
} */

/* Tarefa 1: adicionar os parâmetros telefone, endereco, cpf, nascimento, cidade, estado, cep e pais ao construtor da classe Cliente */

public Cliente (
    String nome, 
    String sobrenome, 
    String email, 
    String telefone, 
    String endereco, 
    String cpf, 
    String nascimento, 
    String cidade, 
    String estado, 
    String cep, 
    String pais
) {
    this.nome = nome;
    this.sobrenome = sobrenome;
    this.email = email;
    this.telefone = telefone;
    this.endereco = endereco;
    this.cpf = cpf;
    this.nascimento = nascimento;
    this.cidade = cidade;
    this.estado = estado;
    this.cep = cep;
    this.pais = pais;
}

/* ----------------------------------------------------------------------------------------------------------------- */

/* public class PagamentoService {
    public void pagar(String tipo, Float valor) {
        switch (tipo) {
            case "pix":    new Pix().pagar(valor); break;
            case "boleto": new Boleto().pagar(valor); break;
            case "cartao": new Cartao().pagar(valor); break;
        }
    }
} */

/* Tarefa 2: criar uma nova classe FaturaService e um método histórico que use o mesmo switch para gerar o histórico de pagamentos (chamando historico() de cada meio de pagamento) */

public class FaturaService {
    public void historico(String tipo) {
        switch (tipo) {
            case "pix":    new Pix().historico(); break;
            case "boleto": new Boleto().historico(); break;
            case "cartao": new Cartao().historico(); break;
        }
    }
}

/* ----------------------------------------------------------------------------------------------------------------- */

/* public class Config {
    public static String urlBanco = "jdbc:mysql://localhost:3306/loja";
    public static String usuarioBanco = "admin";
    public static String ambiente = "producao";
    public static int tentativasLogin = 3;
} */

/* Tarefa 3: Crie um método chamado autenticar numa classe chamada LoginService que altera o valor de tentativasLogin para 5 */

public class LoginService {
    public void autenticar() {
        Config.tentativasLogin = 5;
    }
}

/* ----------------------------------------------------------------------------------------------------------------- */

/* Pedido rascunho = new Pedido();
rascunho.cliente    = pedido.cliente;
rascunho.itens      = pedido.itens;
rascunho.valorTotal = pedido.valorTotal;
rascunho.desconto   = pedido.desconto;
rascunho.endereco   = "Rua das Flores, 100"; */

/* Tarefa 4: duplicar um objeto Pedido para gerar um rascunho, copiando todos os campos do pedido original */

Pedido rascunho = new Pedido();
rascunho.cliente    = pedido.cliente;
rascunho.itens      = new ArrayList<>(pedido.itens);
rascunho.valorTotal = pedido.valorTotal;
rascunho.desconto   = pedido.desconto;
rascunho.endereco   = pedido.endereco;