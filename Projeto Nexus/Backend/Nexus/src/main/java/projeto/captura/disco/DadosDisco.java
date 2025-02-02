package projeto.captura.disco;

import projeto.conexao.Conectar;
import projeto.Logs;

public class DadosDisco {
    String modelo;
    Double capMax;
    Double usoAtual;
    String montagem;
    String endIPV4;
    Integer fkAlerta;
    Integer fkComponente;
    String email;

    public DadosDisco(String modelo, Double capMax, Double usoAtual,
                      String montagem, String endIPV4, Integer fkAlerta,
                      Integer fkComponente, String email) {
        this.modelo = modelo;
        this.capMax = capMax;
        this.usoAtual = usoAtual;
        this.montagem = montagem;
        this.endIPV4 = endIPV4;
        this.fkAlerta = fkAlerta;
        this.fkComponente = fkComponente;
        this.email = email;

        Double livre = capMax - usoAtual;

        Conectar conectar = new Conectar();
        conectar.DadosDisco(this.modelo, this.capMax, this.usoAtual, this.montagem, this.endIPV4, this.fkAlerta, this.fkComponente, this.email);
    }
}
