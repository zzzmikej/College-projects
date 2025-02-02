package projeto.captura.PrimeiroPlano;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.processos.Processo;
import projeto.print.Prints;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PrimeiroPlano {
    Looca looca = new Looca();
    private String name;
    private Double usoCPU;
    private Double usoMem;
    private Double usoDisk;

    public String dadosPrimeiro(String email) {
        // Supondo que looca.getGrupoDeProcessos().getProcessos() retorna uma List de Processo
        List<Processo> processos = looca.getGrupoDeProcessos().getProcessos();

        // Ordenar processos por uso de CPU em ordem decrescente
        List<Processo> top10Cpu = processos.stream()
                .sorted(Comparator.comparing(Processo::getUsoCpu).reversed())
                .limit(10)
                .collect(Collectors.toList());

        // Ordenar processos por uso de disco em ordem decrescente
        List<Processo> top10Disco = processos.stream()
                .sorted(Comparator.comparing(p -> ((Double.valueOf(p.getBytesUtilizados()) / 1024) / 1024) / 1024, Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());

        // Ordenar processos por uso de memória em ordem decrescente
        List<Processo> top10Memoria = processos.stream()
                .sorted(Comparator.comparing(Processo::getUsoMemoria).reversed())
                .limit(10)
                .collect(Collectors.toList());

        DadosPrimeiroPlano[] primeiroPlano = new DadosPrimeiroPlano[30];


        // Iterar sobre os processos ordenados por uso de CPU e preencher o array
        for (int i = 0; i < 10; i++) {
            Processo processo = top10Cpu.get(i);
            primeiroPlano[i] = new DadosPrimeiroPlano(processo.getNome(), processo.getUsoMemoria(), ((Double.valueOf(processo.getBytesUtilizados()) / 1024) / 1024) / 1024, processo.getUsoCpu(), email
            );
        }
        for (int i = 0; i < 10; i++) {
            Processo processo = top10Disco.get(i);
            primeiroPlano[i] = new DadosPrimeiroPlano(processo.getNome(), processo.getUsoMemoria(), ((Double.valueOf(processo.getBytesUtilizados()) / 1024) / 1024) / 1024, processo.getUsoCpu(), email
            );
        }
        for (int i = 0; i < 10; i++) {
            Processo processo = top10Memoria.get(i);
            primeiroPlano[i] = new DadosPrimeiroPlano(processo.getNome(), processo.getUsoMemoria(), ((Double.valueOf(processo.getBytesUtilizados()) / 1024) / 1024) / 1024, processo.getUsoCpu(), email
            );
        }
        String mensagem = """
                *------------------------------------------------------------*
                |                 Procesos em Primeiro Plano                 |
                *------------------------------------------------------------*
                """;
        return mensagem;
    }
}