import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    /// Formatação de data dd/mm/aaaa
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /// Formatação numérica pt-BR: separador de milhar "." e decimal ","
    private static final Locale LOCALE_BR = Locale.of("pt", "BR");

    public static void main(String[] args) {

        /// 3.1 - Inserir todos os funcionários, na mesma ordem e informações da tabela.
        List<Funcionario> funcionarios = new ArrayList<>();
        funcionarios.add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"));
        funcionarios.add(new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"));
        funcionarios.add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"));
        funcionarios.add(new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"));
        funcionarios.add(new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"));
        funcionarios.add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"));
        funcionarios.add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"));
        funcionarios.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"));
        funcionarios.add(new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"));
        funcionarios.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente"));

        /// 3.2 - Remover o funcionário "João" da lista.
        funcionarios.removeIf(f -> f.getNome().equals("João"));

        /// 3.3 - Imprimir todos os funcionários com todas suas informações.
        System.out.println("===== 3.3 - Lista de funcionários =====");
        funcionarios.forEach(Main::imprimirFuncionario);

        /// 3.4 - Aumento de 10% no salário de todos os funcionários.
        for (Funcionario f : funcionarios) {
            BigDecimal aumento = f.getSalario().multiply(new BigDecimal("0.10"));
            f.setSalario(f.getSalario().add(aumento).setScale(2, RoundingMode.HALF_UP));
        }
        System.out.println("\n===== 3.4 - Após aumento de 10% =====");
        funcionarios.forEach(Main::imprimirFuncionario);

        /// 3.5 - Agrupar os funcionários por função em um Map.
        Map<String, List<Funcionario>> funcionariosPorFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));

        /// 3.6 - Imprimir os funcionários agrupados por função.
        System.out.println("\n===== 3.6 - Funcionários agrupados por função =====");
        for (Map.Entry<String, List<Funcionario>> entrada : funcionariosPorFuncao.entrySet()) {
            System.out.println("Função: " + entrada.getKey());
            entrada.getValue().forEach(Main::imprimirFuncionario);
        }

        /// 3.8 - Funcionários que fazem aniversário nos meses 10 e 12.
        System.out.println("\n===== 3.8 - Aniversariantes de outubro (10) e dezembro (12) =====");
        funcionarios.stream()
                .filter(f -> f.getDataNascimento().getMonthValue() == 10
                        || f.getDataNascimento().getMonthValue() == 12)
                .forEach(Main::imprimirFuncionario);

        /// 3.9 - Funcionário com maior idade: nome e idade.
        System.out.println("\n===== 3.9 - Funcionário com maior idade =====");
        /// Maior idade = data de nascimento mais antiga (MIN), não a mais recente.
        Funcionario maisVelho = funcionarios.stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento))
                .orElse(null);
        if (maisVelho != null) {
            int idade = Period.between(maisVelho.getDataNascimento(), LocalDate.now()).getYears();
            System.out.println("Nome: " + maisVelho.getNome() + " | Idade: " + idade + " anos");
        }

        /// 3.10 - Lista de funcionários em ordem alfabética.
        System.out.println("\n===== 3.10 - Funcionários em ordem alfabética =====");
        funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .forEach(Main::imprimirFuncionario);

        /// 3.11 - Total dos salários dos funcionários.
        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("\n===== 3.11 - Total dos salários =====");
        System.out.println("Total: " + formatarValor(totalSalarios));

        /// 3.12 - Quantos salários mínimos cada funcionário ganha (salário mínimo = R$1212,00).
        BigDecimal salarioMinimo = new BigDecimal("1212.00");
        System.out.println("\n===== 3.12 - Quantidade de salários mínimos por funcionário =====");
        for (Funcionario f : funcionarios) {
            BigDecimal quantidadeSalariosMinimos = f.getSalario().divide(salarioMinimo, 2, RoundingMode.HALF_UP);
            System.out.println(f.getNome() + ": " + formatarValor(quantidadeSalariosMinimos) + " salários mínimos");
        }

    }

    /// Imprimir todos os funcionários com todas suas informações
    private static void imprimirFuncionario(Funcionario f) {
        System.out.println(
                "Nome: " + f.getNome()
                        + " | Data Nascimento: " + f.getDataNascimento().format(FORMATO_DATA)
                        + " | Salário: " + formatarValor(f.getSalario())
                        + " | Função: " + f.getFuncao()
        );
    }

    /// Formata BigDecimal no padrão pt-BR: milhar com ".", decimal com ","
    private static String formatarValor(BigDecimal valor) {
        java.text.NumberFormat formato = java.text.NumberFormat.getNumberInstance(LOCALE_BR);
        formato.setMinimumFractionDigits(2);
        formato.setMaximumFractionDigits(2);
        return formato.format(valor);
    }
}