package projeto.captura;

import projeto.captura.PrimeiroPlano.PrimeiroPlano;
import projeto.captura.disco.Discos;
import projeto.captura.memoria.Memoria;
import projeto.captura.processador.Processador;
import projeto.print.Prints;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Monitoramento {
    private Boolean sair = false;
    static String email;
    Discos disk = new Discos();
    Memoria memory = new Memoria();
    Processador processor = new Processador();
    PrimeiroPlano primeiroPlano = new PrimeiroPlano();

    public void monitor(String email) {

        this.email = email;

        Prints prints = new Prints();


        Timer timer = new Timer(this.email);
        TimerTask main = new Execution();
        timer.schedule(main, 0, 15000);

        Scanner scanner = new Scanner(System.in);


        while (!sair) {
            prints.menuMonitorar();
            int opcao = scanner.nextInt();
            switch (opcao) {
                case 1:
                    System.out.println(processor.processador(email));
                    break;
                case 2:
                    System.out.println(memory.memoria(email));
                    break;
                case 3:
                    System.out.println(disk.disco(email));
                    break;
                case 4:
                    System.out.println(primeiroPlano.dadosPrimeiro(email));
                    break;
                case 0:
                    prints.sair();
                    sair = true;
                    timer.cancel();
                    break;
                default:
                    break;
            }
        }
    }

    public static class Execution extends TimerTask {
        String user = email;

        public void run() {
            Discos disk = new Discos();
            disk.disco(this.user);

            Memoria memory = new Memoria();
            memory.memoria(this.user);

            Processador processor = new Processador();
            processor.processador(this.user);

            PrimeiroPlano firstTask = new PrimeiroPlano();
            firstTask.dadosPrimeiro(this.user);
        }
    }
}


