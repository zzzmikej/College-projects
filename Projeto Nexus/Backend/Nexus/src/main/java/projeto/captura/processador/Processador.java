package projeto.captura.processador;

import com.github.britooo.looca.api.core.Looca;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import projeto.BotSlack;
import projeto.conexao.Conectar;
import projeto.Logs;
import projeto.print.Prints;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Processador {
    public String processador(String email) {
        // Cria instancia da API Looca
        Looca looca = new Looca();

        // Cria uma instacia para puxar dados do processador
        CentralProcessor processador = new SystemInfo().getHardware().getProcessor();
        Conectar conectar = new Conectar();
        Logs logs = new Logs();
        BotSlack botSlack = new BotSlack();

        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);

        // Criacao das variaveis
        String name = "";
        String modelo = "";
        String montagem = "";
        Integer fkAlerta;
        Integer fkComponente = 1;


        // Pega a Frequencia e faz calculo de GHz
        Double frequency = Double.valueOf(processador.getMaxFreq());
        Double capMax = Double.valueOf(df.format(((frequency / 1000)/ 1000)/ 1000).replace(",","."));

        Double porcentage = looca.getProcessador().getUso();

        Double usoAtual = Double.valueOf(df.format(((capMax * porcentage) / 100.0)).replace(",","."));

        // Retira a Informacao de Genuine ou Authenthic dos processadores
        String brand = looca.getProcessador().getFabricante().replace("Genuine","").replace("Authentic","");

        // Verifica qual a fabricante do Processador
        if (brand.equals("AMD")){
            // Pega o nome do Processador e remove os Dados nao imporantes para o cadastro
            name = processador.getProcessorIdentifier().getName();
            name = name.replaceAll("Processor| 6-Core ", "");

            // Pega o nome do Processador por extenso e tira apenas o modelo
            modelo = looca.getProcessador().getNome();
            modelo = modelo.replaceAll("AMD | Processor","");

        } else if (brand.equals("Intel")){
            // Pega o nome do Processador e remove os Dados nao imporantes para o cadastro
            name = processador.getProcessorIdentifier().getName();
            name = name.replaceAll("CPU|TM|Gen|1.60GHz|@|1.40GHz", "").replace("(","").replace(")","");

            // Pega o nome do Processador por extenso e tira apenas o modelo
            modelo = looca.getProcessador().getNome();
            modelo = modelo.replaceAll("TM|CPU|@ |1.40GHz|1.60GHz|1.50GHz|1.80GHz","").replace("()","").replace("     ","");
        }

        if (porcentage <= 50) {
            fkAlerta = 10;
        } else if (porcentage > 50 && porcentage <= 75) {
            fkAlerta = 1;
            logs.gravar("\nALERTA BAIXO - CPU Utilizado %.2f%%".formatted(porcentage));

            botSlack.notificarUsoCPUPorcentagem("CPU", porcentage);
        } else if (porcentage > 75 && porcentage <= 90) {
            fkAlerta = 2;
            logs.gravar("\nALERTA MEDIO - CPU Utilizado %.2f%%".formatted(porcentage));

            botSlack.notificarUsoCPUPorcentagem("CPU", porcentage);
        } else {
            fkAlerta = 3;
            logs.gravar("\nALERTA GRAVE - CPU Utilizado %.2f%%".formatted(porcentage));

            botSlack.notificarUsoCPUPorcentagem("CPU", porcentage);
        }
        String endIPV4 = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(0).getEnderecoIpv4().get(0);
        
        conectar.inserirProcessador(modelo, capMax, usoAtual, montagem, endIPV4,
                fkAlerta, fkComponente, email);

        return """
                *------------------------------------------------------------*
                |                           Processador                      |
                *------------------------------------------------------------*
                | Fabricante:                               %s
                | Nome:                                     %s
                | Modelo:                                   %s
                | Uso:                                      %.2f%%
                | Frequencia:                               %.2fGhz
                """.formatted(brand, name, capMax, usoAtual, capMax);

    }
}
