package projeto.print;

public class Prints {
    
    public void inicial(){
        System.out.println("Seja Bem-Vindo a Plataforma NEXUS!\n");
    }
    
    public void menu() {
        System.out.println("\nPor Favor realize o Login para poder acessar \ntodas as areas da plataforma! \n");
        System.out.println("----------------------------------------------");
        System.out.println("| 1 - Login                                  |");
        System.out.println("| 0 - Sair                                   |");
        System.out.println("----------------------------------------------");
    }
    
    public void username() {
        System.out.println("\nInsira seu email: ");
    }
    
    public void token() {
        System.out.println("\nInsira seu token:");
    }
    
    public void sair() {
        System.out.println("\nSessão Finalizada!");
    }
    
    public void menuMonitorar() {
        System.out.println("""
                Qual dos Monitoramentos deseja visualizar 
                
                *---------------------------------------*
                | 1 - Processador                       |
                | 2 - Memoria                           |
                | 3 - Disco                             |
                | 4 - Primeiro Plano                    |
                *---------------------------------------*
                """);
    }

    public void limparConsole(){
        for(int i = 0; i < 100; i++){
            System.out.println(" ");
        }
    }
}

// Realizado Pela Nexus Enterprises :)
