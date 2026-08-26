/* EX 01 */
public class RelatorioVendas {

    private final List<Venda> vendas;
    private final LocalDate dataInicio;
    private final LocalDate dataFim;

    public RelatorioVendas(List<Venda> vendas, LocalDate dataInicio, LocalDate dataFim) {
        this.vendas = vendas;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public String gerarRelatorio() {
        StringBuilder sb = new StringBuilder();
        sb.append("Relatório de Vendas\n");
        sb.append("Período: ").append(dataInicio).append(" até ").append(dataFim).append("\n\n");

        double total = 0.0;
        for (Venda venda : vendas) {
            sb.append("ID: ").append(venda.getId())
              .append(" | Data: ").append(venda.getData())
              .append(" | Cliente: ").append(venda.getCliente())
              .append(" | Valor: R$ ").append(String.format("%.2f", venda.getValor()))
              .append("\n");
            total += venda.getValor();
        }

        sb.append("\nTotal de vendas: R$ ").append(String.format("%.2f", total)).append("\n");
        return sb.toString();
    }

    public void salvarRelatorio(String caminho) {
        String conteudo = gerarRelatorio();
        try (FileWriter writer = new FileWriter(caminho)) {
            writer.write(conteudo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar relatório em arquivo: " + caminho, e);
        }
    }

    public void enviarRelatorioPorEmail(String email) {
        String conteudo = gerarRelatorio();
        EmailService emailService = new EmailService();
        emailService.enviarEmail(email, "Relatório de Vendas", conteudo);
    }

    public static class Venda {
        private final String id;
        private final LocalDate data;
        private final String cliente;
        private final double valor;

        public Venda(String id, LocalDate data, String cliente, double valor) {
            this.id = id;
            this.data = data;
            this.cliente = cliente;
            this.valor = valor;
        }

        public String getId() {
            return id;
        }

        public LocalDate getData() {
            return data;
        }

        public String getCliente() {
            return cliente;
        }

        public double getValor() {
            return valor;
        }
    }
}

/* 
1 - Explique por que essa classe viola o SRP.
R: porque ela possui múltiplos motivos de mudanças, varias responsabilidades diferentes.

2 - Liste pelo menos três responsabilidades diferentes que ela está assumindo.
R: gerar relatório, enviar relatório por email e salvar o relatório

3 - Proponha uma nova estrutura de classes usando um diagrama de classes que distribua essas responsabilidades de forma mais adequada.
classes:
Venda: representar uma venda. possuirá: classe venda com dados da venda
GeradorDeRelatorioVendas: gerar o conteúdo do relatório de vendas. possuirá: classe RelatorioVendas com os dados venda e datas e método gerarRelatorio
SalvadorDeRelatorioVendas: salvar o conteúdo do relatório de vendas. possuirá: método salvarRelatorio
EnviadorDeRelatorioVendas: enviar o o conteúdo do relatório de vendas por e-mail. possuirá: método enviarRelatorioPorEmail
*/

/* EX 02 */

/* 
Imagine um sistema de cálculo de frete onde existe uma classe CalculadoraFrete com um switch/if-else interno que decide o valor com base no tipo de entrega: Normal, Rápida, Expressa. 
*/

public class CalculadoraFrete {
    public double calcularFrete(String tipoEntrega, double peso) {
        switch (tipoEntrega) {
            case "Normal":
                return peso * 5.0;
            case "Rápida":
                return peso * 10.0;
            case "Expressa":
                return peso * 20.0;
            default:
                throw new IllegalArgumentException("Tipo de entrega desconhecido: " + tipoEntrega);
        }
    }
}

/* 
A empresa deseja adicionar novos tipos de entrega (por exemplo, Entrega Noturna, Entrega Internacional) sem precisar modificar o código existente.

1 - Explique quais problemas o código atual apresenta em relação ao OCP.
R: o princípio OCP determina que a entidade deve ser aberta para extensões e fechada para modificações, nesse caso, a classe CalculadoraFrete é desenvolvida a modo que para adicionar novos tipos, seja necessário alterá-la.

2 - Proponha uma implementação criando uma interface chamada TipoEntrega e classes concretas para cada tipo de entrega, de forma que cada tipo de entrega tenha um método calcularFrete(double peso).
R: 
*/
public interface TipoEntrega {
    double calcularFrete(double peso);
}

public class EntregaNormal implements TipoEntrega {
    @Override
    public double calcularFrete(double peso) {
        return peso * 5.0;
    }
}

public class EntregaRapida implements TipoEntrega {
    @Override
    public double calcularFrete(double peso) {
        return peso * 10.0;
    }
}

public class EntregaExpressa implements TipoEntrega {
    @Override
    public double calcularFrete(double peso) {
        return peso * 20.0;
    }
}

public class CalculadoraFrete {
    public double calcularFrete(TipoEntrega tipoEntrega, double peso) {
        return tipoEntrega.calcularFrete(peso);
    }
}

/* EX 03 */

/* 
Considere uma hierarquia de classes onde Conta é a classe base e ContaCorrente, ContaPoupanca e ContaSalario são subclasses.

A classe Conta possui os seguintes métodos:

sacar(Double valor): permite sacar o valor informado da conta, desde que haja saldo suficiente.
depositar(Double valor): permite depositar dinheiro na conta.
transferir(Double valor, Conta conta): permite transferir o valor informado para outra conta.
No entanto, devido a uma regra específica do sistema, a classe ContaSalario sobrescreve os métodos sacar e transferir. Nessa classe, essas operações não realizam a operação solicitada diretamente: o valor movimentado é automaticamente direcionado para uma conta específica vinculada ao empregador.

1 - Explique, com suas palavras, quando uma subclasse viola o Princípio da Substituição de Liskov (LSP).
R: quando a subclasse não pode substituir a classe-pai sem alterar o comportamento esperado do programa.

2 - Descreva um cenário de uso em que ContaSalario poderia causar um comportamento inesperado ou incorreto ao ser utilizada onde o sistema esperava receber uma Conta genérica.
R: um sistema recebe uma conta e chama transferir(500, outraConta), esperando que os 500 reais sejam enviados para outraConta. se o objeto recebido for uma ContaSalario, o dinheiro será direcionado a conta vinculada ao empregador, diferente da informada.

3 - Explique por que o problema apresentado está relacionado ao comportamento esperado da classe, e não simplesmente à existência dos mesmos métodos na classe filha. 
R: o problema está no comportamento porque possuir os mesmos métodos não garante que a subclasse respeite o contrato da classe-pai.
*/

/* EX 04 */ 
/* Suponha uma interface ITrabalhador com os métodos: trabalhar(), comer(), dormir(). Ela é implementada por Robo e Funcionario. 

1 - Explique por que essa interface pode violar o ISP.
R: porque obriga todas as classes a implementarem os métodos trabalhar(), comer() e dormir(), mesmo quando alguns deles não fazem sentido.

2 - Proponha um conjunto de interfaces menores que segregue melhor as responsabilidades.
Escreva a nova definição dessas interfaces em pseudocódigo (ou sintaxe de alguma linguagem OO) e indique quais seriam implementadas por Robo e por Funcionario.
R:
*/
public interface Trabalhavel {
    void trabalhar();
}

public interface Alimentavel {
    void comer();
}

public interface Dorminhoco {
    void dormir();
}
/* O Robo implementaria somente a interface relacionada ao trabalho:
 */public class Robo implements Trabalhavel {
    @Override
    public void trabalhar() {
        System.out.println("O robô está trabalhando.");
    }
}
/* O Funcionario implementaria as três interfaces: */
public class Funcionario
        implements Trabalhavel, Alimentavel, Dorminhoco {

    @Override
    public void trabalhar() {
        System.out.println("O funcionário está trabalhando.");
    }

    @Override
    public void comer() {
        System.out.println("O funcionário está comendo.");
    }

    @Override
    public void dormir() {
        System.out.println("O funcionário está dormindo.");
    }
}

/* EX 05 */
/* 
Um módulo de alto nível ProcessadorDePagamento instancia diretamente uma classe concreta PagInseguro usando new e chama seus métodos em todas os métodos (ex.: processarPagamento(), verificarPagamento(), cancelarPagamento()).

1 - Explique por que isso fere o DIP.
R: porque ProcessadorDePagamento, que é um módulo de alto nível, depende diretamente da classe concreta PagInseguro.

2 - Quais seriam as consequências de manter essa dependência direta em termos de manutenção e evolução do sistema?
R: dificulta a manutenção e a evolução do sistema. Se for necessário substituir PagInseguro por outro serviço de pagamento, será preciso alterar o código de ProcessadorDePagamento

3 - Descreva brevemente por quais maneiras o desenvolvedor poderia injetar a implementação concreta.
R: 
- Pelo construtor 
- Por um método setter
- Por parâmetro de método
- Por um framework de injeção de dependências: 
*/