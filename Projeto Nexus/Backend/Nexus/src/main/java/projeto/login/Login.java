package projeto.login;

import projeto.conexao.Conectar;
import projeto.menu.Menu;
import projeto.print.Prints;

import java.util.Scanner;

public class Login {
    String username;
    private String token;
    private Boolean logado = false;

    Menu menu = new Menu();
    Conectar conectar = new Conectar();
    Scanner scanner = new Scanner(System.in);
    Prints prints = new Prints();
    public Boolean login() {
        Integer n = 0;
        while (!logado) {
            prints.limparConsole();
            if (logado == false) {
                if (n == 0) {
                    System.out.println("            LOGIN       ");
                    prints.username();
                    username = scanner.nextLine();
                    prints.token();
                    token = scanner.nextLine();

                    logado = conectar.Logar(username, token);
                    n++;
                } else {
                    System.out.println("""
                                        
                    Nenhum usuario encontrado
                                        
                    Por favor verifique as credenciais
                    """);
                    prints.username();
                    username = scanner.nextLine();
                    prints.token();
                    token = scanner.nextLine();

                    logado = conectar.Logar(username, token);
                }
            }
        }
        return true;
    }
}

