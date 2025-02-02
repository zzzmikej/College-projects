package projeto.captura.disco;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import projeto.BotSlack;
import projeto.Logs;
import projeto.print.Prints;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Discos {
    private String modelo;
    private Double capMax;
    private Double usoAtual;
    private String montagem;
    private String endIPV4 = "0.0.0.0";
    private Integer fkAlerta;
    private Integer fkComponente = 3;
    private String email;

    public String disco(String email) {
        this.email = email;
        // Instancia da funcao de Discos
        DiscoGrupo discoGrupo = new Looca().getGrupoDeDiscos();
        Looca looca = new Looca();

        Logs logs = new Logs();
        BotSlack botSlack = new BotSlack();

        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);

        // define o tamnho da lista do getDisco
        int size = discoGrupo.getDiscos().size();

        // Cria uma lista com o tamanho do getDisco definido acima
        DadosDisco[] disk = new DadosDisco[size];

        // um for que percorre todos os discos dentro do discoGrupo
        for (int i = 0; i < disk.length; i++) {
            // O nome do disco!!
            String vol = looca.getGrupoDeDiscos().getVolumes().get(i).getNome();
            String nome = discoGrupo.getDiscos().get(i).getNome();

            // O modelo do disco sera definido aqui
            modelo = discoGrupo.getDiscos().get(i).getModelo();

            // Onde o volume esta sendo montado
            montagem = discoGrupo.getVolumes().get(i).getPontoDeMontagem();

            // Tamamnho total do disco
            Double discTotal = Double.valueOf(discoGrupo.getDiscos().get(i).getTamanho());
            capMax = ((discTotal / 1024) / 1024) / 1024;

            // Total disponivel
            Double vl = Double.valueOf(discoGrupo.getVolumes().get(i).getDisponivel());
            Double livre = ((vl / 1024) / 1024) / 1024;

            // Quantidadde Usada
            usoAtual = Double.valueOf(df.format((capMax - livre)).replace(",", "."));
            endIPV4 = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(i).getEnderecoIpv4().get(0);
        }
        Double porcentage = (usoAtual * 100) / capMax;

        if (porcentage <= 50) {
            fkAlerta = 10;
        } else if (porcentage > 50 && porcentage <= 75) {
            fkAlerta = 7;
            logs.gravar("ALERTA BAIXO - Disco Utilizado %.2f%%".formatted(porcentage));

            botSlack.notificarUsoDiscoPorcentagem("Disco", porcentage);
        } else if (porcentage > 75 && porcentage <= 90) {
            fkAlerta = 8;
            logs.gravar("ALERTA MEDIO - Disco Utilizado %.2f%%".formatted(porcentage));

            botSlack.notificarUsoDiscoPorcentagem("Disco", porcentage);
        } else {
            fkAlerta = 9;
            logs.gravar("ALERTA GRAVE - Disco Utilizado %.2f%%".formatted(porcentage));

            botSlack.notificarUsoDiscoPorcentagem("Disco", porcentage);
        }

        // Envia todos os dados captados acima para o Arquivo que servira como objeto
        disk[0] = new DadosDisco(modelo, capMax, usoAtual, montagem, endIPV4, fkAlerta, fkComponente, email);

    // Imprime as mensgens juntamente com os dados dos objetos
    String mensagem = """
                *------------------------------------------------------------*
                |                            Discos                          |
                *------------------------------------------------------------*
                """;

    // percorre todo o arquivo e mostra todos os dados salvos
        for (DadosDisco dadosDisco : disk) {
        mensagem += """
                    | Modelo:                                     %s
                    | Montagem:                                   %s
                    | Espaco Total:                               %.2f Gb
                    *------------------------------------------------------------*
                    """.formatted(dadosDisco.modelo, dadosDisco.montagem,
                dadosDisco.capMax);
    }
    // retorna a String formatada para o MAIN
        return mensagem;
}
}
