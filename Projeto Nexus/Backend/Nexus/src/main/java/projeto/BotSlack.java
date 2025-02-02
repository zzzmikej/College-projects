package projeto;

import org.json.JSONObject;

import java.io.IOException;

public class BotSlack {
    // Pegar do Monitoramento as informações dos componente e puxar como parametro para enviar ao slack

    private String construirMensagemDePorcentagemIndividual(String componente, double porcentagem) {
        return """
                ALERTA - %s Utilizado %.2f%%""".formatted(componente, porcentagem);
    }

    public void notificarUsoCPUPorcentagem(String CPU, double porcentagem) {
        String mensagem = construirMensagemDePorcentagemIndividual(CPU, porcentagem);
        enviarMensagemSlack(mensagem);
    }

    public void notificarUsoMemoriaPorcentagem (String memoria, double porcentagem) {
        String mensagem = construirMensagemDePorcentagemIndividual(memoria, porcentagem);
        enviarMensagemSlack(mensagem);
    }

    public void notificarUsoDiscoPorcentagem(String disco , double porcentagem) {
        String mensagem = construirMensagemDePorcentagemIndividual(disco, porcentagem);
        enviarMensagemSlack(mensagem);
    }

    private void enviarMensagemSlack(String mensagem) {
        JSONObject json = new JSONObject();
        json.put("text", mensagem);
        try {
            Slack.sendMessage(json);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
