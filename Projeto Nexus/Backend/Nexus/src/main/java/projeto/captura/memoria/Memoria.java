package projeto.captura.memoria;

import com.github.britooo.looca.api.core.Looca;
import projeto.BotSlack;
import projeto.conexao.Conectar;
import projeto.Logs;
import projeto.print.Prints;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Memoria {
    public String memoria(String email) {
        String montagem = null;
        Integer fkAlerta;
        Integer fkComponente = 2;

        // Cria a instacia da API Looca
        Looca looca = new Looca();
        Logs logs = new Logs();
        BotSlack botSlack = new BotSlack();

        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);

        // pega a quantidade de memoria em uso e faz o calculo para Gb
        Double memEmUso = Double.valueOf(looca.getMemoria().getEmUso());
        Double usoAtual = Double.valueOf(df.format(((memEmUso / 1024) / 1024) / 1024).replace(",","."));

        // pega a quantidade de memoria disponivel e faz o calculo para Gb
        Double memDisponivel = Double.valueOf(looca.getMemoria().getDisponivel());
        Double disponivel = ((memDisponivel / 1024) / 1024) / 1024;

        // pega a quantidade de memoria total e faz o calculo para Gb
        Double memTotal = Double.valueOf(looca.getMemoria().getTotal());
        Double capMax = Double.valueOf(df.format(((memTotal / 1024) / 1024) / 1024).replace(",","."));

        String modelo = """
                RAM %.2fGb""".formatted(capMax);


        Double porcentage = Double.valueOf(df.format((usoAtual * 100) / capMax).replace(",","."));

        if (porcentage <= 50) {
            fkAlerta = 10;
        } else if (porcentage > 50 && porcentage <= 75) {
            fkAlerta = 4;
            logs.gravar("\nALERTA BAIXO - Memoria Utilizada %s%%".formatted(porcentage.toString()));

            botSlack.notificarUsoMemoriaPorcentagem("Memoria" ,porcentage);

        } else if (porcentage > 75 && porcentage <= 90) {
            fkAlerta = 5;
            logs.gravar("\nALERTA MEDIO - Memoria Utilizada %s%%".formatted(porcentage.toString()));

            botSlack.notificarUsoMemoriaPorcentagem("Memoria" ,porcentage);
        } else {
            fkAlerta = 6;
            logs.gravar("\nALERTA GRAVE - Memoria Utilizada %s%%".formatted(porcentage));

            botSlack.notificarUsoMemoriaPorcentagem("Memoria" ,porcentage);
        }
        String endIPV4 = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(0).getEnderecoIpv4().get(0);

        Conectar conectar = new Conectar();

        conectar.inserirMemoria(modelo, capMax, usoAtual, montagem, endIPV4, fkAlerta, fkComponente, email);

        // retorna a mensgem para ser impressa na Main
        return """
                *------------------------------------------------------------*
                |                           Memoria                          |
                *------------------------------------------------------------*
                | Uso:                                       %.2f Gb
                | Disponivel:                                %.2f Gb
                | Total:                                     %.2f Gb
                """.formatted(usoAtual, disponivel, capMax);


    }

}
