package projeto;

import projeto.menu.Menu;
import projeto.print.Prints;

public class Main {
    public static void main(String[] args) {
        Prints prints = new Prints();
        prints.inicial();
        Menu menu = new Menu();
        menu.exibirMenu();
    }
}

