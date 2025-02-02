package projeto.menu;

import projeto.login.Login;
import projeto.print.Prints;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    private boolean sair = false;

    public void exibirMenu() {
        Login login = new Login();
        Prints prints = new Prints();
        Scanner scanner = new Scanner(System.in);

        boolean sair = false;

        while (!sair) {
            try {
                prints.menu();
                int opcao = scanner.nextInt();

                switch (opcao) {
                    case 1:
                        sair = login.login();
                        break;
                    case 0:
                        prints.sair();
                        sair = true;
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                }
            } catch (InputMismatchException e) {
                // Limpar o buffer do scanner e consumir a entrada inválida
                scanner.nextLine();

                System.out.println("Entrada inválida. Por favor, insira um número válido.");
            }
        }

        scanner.close();
    }
}
// Realizado Pela Nexus Enterprises :)
