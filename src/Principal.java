import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    ///Constante para salario minimo
    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    /// Formatação de data dd/mm/aaaa
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /// Formatação numérica pt-BR: separador de milhar "." e decimal ","
    private static final Locale LOCALE_BR = new Locale("pt", "BR");

    public static void main(String[] args) {

        // Inserir todos os funcionarios
        System.out.println("\nIncluindo funcionarios.");
        List<Funcionario> funcionarios = criarFuncionarios();

        // 3.2 - Remover João
        System.out.println("\nRemovendo o João.");
        removerFuncionario(funcionarios, "João");

        // 3.3 - Imprimir funcionários
        System.out.println("\nImprimindo funcionarios.");
        imprimirFuncionarios(funcionarios);

        // 3.4 - Aumento de 10%
        System.out.println("\nAumentando salario dos funcionarios.");
        aumentarSalarios(funcionarios, new BigDecimal("10"));

        // 3.5 e 3.6 - Agrupar por função
        System.out.println("\nFuncionarios agrupados por funcao.");
        Map<String, List<Funcionario>> porFuncao = agruparPorFuncao(funcionarios);
        imprimirFuncionariosAgrupadosPorFuncao(porFuncao);

        // 3.8 - Aniversariantes
        System.out.println("\nAniversariantes entre 10 e 12.");
        List<Funcionario> aniversariantes = buscarAniversariantes(funcionarios, 10, 12);
        imprimirFuncionarios(aniversariantes);

        // 3.9 - Funcionário mais velho
        System.out.println("\nFuncionario mais velho.");
        Funcionario maisVelho = encontrarMaisVelho(funcionarios);
        System.out.println(maisVelho.getNome() + " - " + calcularIdade(maisVelho.getDataNascimento()) +" anos");

        // 3.10 - Ordem alfabética
        System.out.println("\nOrdem alfabetica.");
        List<Funcionario> ordenados = ordenarPorNome(funcionarios);
        imprimirFuncionarios(ordenados);

        // 3.11 - Total dos salários
        BigDecimal total = calcularTotalSalarios(funcionarios);
        System.out.println("Total dos salários: " + formatarSalario(total));

        // 3.12 - Salários mínimos
        System.out.println("\nQuantidade de salarios minimos");
        imprimirSalariosMinimos(funcionarios);
    }

    private static List<Funcionario> criarFuncionarios() {
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

        return funcionarios;
    }

    private static void removerFuncionario(List<Funcionario> funcionarios, String nome) {

        funcionarios.removeIf(funcionario -> funcionario.getNome().equalsIgnoreCase(nome));
    }

    private static void imprimirFuncionarios(List<Funcionario> funcionarios) {

        funcionarios.forEach(funcionario -> System.out.println(formatarFuncionario(funcionario)));
    }

    private static void aumentarSalarios(List<Funcionario> funcionarios, BigDecimal percentual) {

        BigDecimal fator = BigDecimal.ONE.add(percentual.divide(BigDecimal.valueOf(100)));
        funcionarios.forEach(funcionario -> funcionario.setSalario(funcionario.getSalario().multiply(fator)));
    }

    private static Map<String, List<Funcionario>> agruparPorFuncao(List<Funcionario> funcionarios) {

        return funcionarios.stream().collect(Collectors.groupingBy(Funcionario::getFuncao, TreeMap::new, Collectors.toList()));
    }

    private static List<Funcionario> buscarAniversariantes(List<Funcionario> funcionarios, int... meses) {

        Set<Integer> mesesAniversario = Arrays.stream(meses).boxed().collect(Collectors.toSet());

        return funcionarios.stream().filter(funcionario ->
                mesesAniversario.contains(funcionario.getDataNascimento().getMonthValue())).collect(Collectors.toList());
    }

    private static Funcionario encontrarMaisVelho(List<Funcionario> funcionarios) {

        return funcionarios.stream().min(Comparator.comparing(Funcionario::getDataNascimento)).orElse(null);
    }

    private static int calcularIdade(LocalDate dataNascimento) {

        return Period.between(dataNascimento,LocalDate.now()).getYears();
    }

    private static List<Funcionario> ordenarPorNome(List<Funcionario> funcionarios) {

        return funcionarios.stream().sorted(Comparator.comparing(Funcionario::getNome)).collect(Collectors.toList());
    }

    private static BigDecimal calcularTotalSalarios(List<Funcionario> funcionarios) {

        return funcionarios.stream().map(Funcionario::getSalario).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal calcularSalariosMinimos(BigDecimal salario) {

        return salario.divide(SALARIO_MINIMO,2,RoundingMode.HALF_UP);
    }

    private static void imprimirSalariosMinimos(List<Funcionario> funcionarios) {

        funcionarios.forEach(funcionario -> {
            BigDecimal salariosMinimos = calcularSalariosMinimos(funcionario.getSalario());

            System.out.println(funcionario.getNome() + " recebe " +
                    formatarSalario(funcionario.getSalario()) + " (" + salariosMinimos + " salários mínimos)");
        });
    }

    private static String formatarFuncionario(Funcionario funcionario) {

        return String.format(
                "%s | %s | %s | %s",
                funcionario.getNome(),
                formatarData(funcionario.getDataNascimento()),
                formatarSalario(funcionario.getSalario()),
                funcionario.getFuncao()
        );
    }

    private static String formatarData(LocalDate data) {
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private static String formatarSalario(BigDecimal salario) {
        return NumberFormat
                .getCurrencyInstance(LOCALE_BR)
                .format(salario);
    }

    private static void imprimirFuncionariosAgrupadosPorFuncao(Map<String, List<Funcionario>> funcionariosPorFuncao) {

        funcionariosPorFuncao.forEach((funcao, funcionarios) -> {
            System.out.println("\nFunção: " + funcao);
            imprimirFuncionarios(funcionarios);
        });
    }
}