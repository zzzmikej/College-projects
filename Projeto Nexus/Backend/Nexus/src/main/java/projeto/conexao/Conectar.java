package projeto.conexao;

import projeto.captura.Monitoramento;
import projeto.menu.Menu;
import projeto.Logs;
import projeto.print.Prints;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conectar {
    ConectarSQL conectarSQL = new ConectarSQL();
    private String url = "jdbc:mysql://localhost:3306/NEXUS";
    private String user = "root";
    private String passwd = "nexus123";

    private Logs informacoes = new Logs();
    private Menu menu = new Menu();

    private String email;
    private String pass;
    private Boolean logado;

    public Boolean Logar(String email, String pass) {
        this.email = email;
        this.pass = pass;
        Prints prints = new Prints();

        Monitoramento monitoramento = new Monitoramento();
        Boolean login = null;
        try {
            login = conectarSQL.Logar(email, pass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String sqlSelect = "SELECT emailCorporativo, token FROM Funcionario;";
        if (login == true){
            System.out.println("Sessao encerrada\n\n");
        } else {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conexao = DriverManager.getConnection(url, user, passwd);
                Statement statement = conexao.createStatement();
                ResultSet resposta = statement.executeQuery(sqlSelect);

                while (resposta.next()) {
                    String username = resposta.getString("emailCorporativo");
                    String token = resposta.getString("token");

                    if (this.email.equals(username) && this.pass.equals(token)) {
                        logado = true;

                        prints.limparConsole();
                        System.out.println("""
                                Login Realizado com Sucesso!
                                                            
                                    Seja Bem-Vindo
                                """);
                        System.out.println(username);
                        monitoramento.monitor(username);

                    } else {
                        logado = false;
                    }
                }

                resposta.close();
                statement.close();
                conexao.close();
            } catch (SQLException | ClassNotFoundException throwables) {
                System.out.println("""
                                            
                        Nenhum usuario encontrado
                                            
                        Por favor verifique as credenciais
                        """);
                logado = false;
            }
            return logado;
        }
        return login;
    }

    public Conectar DadosDisco(String modelo, Double capMax, Double usoAtual, String montagem, String endIPV4, Integer fkAlerta, Integer fkComponente, String email) {

        try {
            conectarSQL.DadosDisco(modelo, capMax, usoAtual, montagem, endIPV4, fkAlerta, fkComponente, email);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conexao = DriverManager.getConnection(url, user, passwd);
            String cadastro = "INSERT INTO Registro (modelo, capacidadeMax, usoAtual, montagem, enderecoIPV4, dataHora, fkAlerta, fkComponente, fkMaquina) VALUES (?, ?, ?, ?, ?, NOW(), ?, ?, (SELECT idMaquina FROM Maquina JOIN Funcionario ON Maquina.fkFuncionario = idFuncionario WHERE emailCorporativo = ?));";

            PreparedStatement instrucao = conexao.prepareStatement(cadastro);

            instrucao.setString(1, modelo);
            instrucao.setDouble(2, capMax);
            instrucao.setDouble(3, usoAtual);
            instrucao.setString(4, montagem);
            instrucao.setString(5, endIPV4);
            instrucao.setInt(6, fkAlerta);
            instrucao.setInt(7, fkComponente);
            instrucao.setString(8, email);

            instrucao.execute();

            instrucao.close();
            conexao.close();
        } catch (
                SQLException | ClassNotFoundException throwables) {
            System.err.println("conexao nao estabelecida");
            throwables.printStackTrace();
        }
        return null;
    }

    public Conectar inserirProcessos(String name, Double usoCPU, Double usoMem, Double usoDisk, String email) {

        try {
            conectarSQL.inserirProcessos(name, usoCPU,usoMem, usoDisk, email);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conexao = DriverManager.getConnection(url, user, passwd);

            String cadastro = "INSERT INTO Processo (nome, usoAtualRAM, usoAtualDisco, usoAtualCPU, dataHora, fkMaquina) VALUES (?, ?, ?, ?, NOW(), (SELECT idMaquina FROM Maquina JOIN Funcionario ON Maquina.fkFuncionario = idFuncionario WHERE emailCorporativo = ?));";

            PreparedStatement instrucao = conexao.prepareStatement(cadastro);

            instrucao.setString(1, name);
            instrucao.setDouble(2, usoMem);
            instrucao.setDouble(3, usoDisk);
            instrucao.setDouble(4, usoCPU);
            instrucao.setString(5, email);

            instrucao.execute();

            instrucao.close();
            conexao.close();
        } catch (
                SQLException | ClassNotFoundException throwables) {
            System.err.println("conexao nao estabelecida");
            throwables.printStackTrace();
        }
        return null;
    }

    public Conectar inserirMemoria(String modelo, Double capMax, Double usoAtual, String montagem, String endIPV4, Integer fkAlerta, Integer fkComponente, String email){

        try {
            conectarSQL.inserirMemoria(modelo, capMax, usoAtual, montagem, endIPV4, fkAlerta, fkComponente, email);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conexao = DriverManager.getConnection(url, user, passwd);

            String cadastro = "INSERT INTO Registro (modelo, capacidadeMax, usoAtual, montagem, enderecoIPV4, dataHora, fkAlerta, fkComponente, fkMaquina) VALUES (?, ?, ?, ?, ?, NOW(), ?, ?, (SELECT idMaquina FROM Maquina JOIN Funcionario ON Maquina.fkFuncionario = idFuncionario WHERE emailCorporativo = ?));";

            PreparedStatement instrucao = conexao.prepareStatement(cadastro);

            instrucao.setString(1, modelo);
            instrucao.setDouble(2, capMax);
            instrucao.setDouble(3, usoAtual);
            instrucao.setString(4, montagem);
            instrucao.setString(5, endIPV4);
            instrucao.setInt(6, fkAlerta);
            instrucao.setInt(7, fkComponente);
            instrucao.setString(8, email);

            instrucao.execute();

            instrucao.close();
            conexao.close();
        } catch (
                SQLException | ClassNotFoundException throwables) {
            System.err.println("conexao nao estabelecida");
            throwables.printStackTrace();
        }
        return null;
    }

    public Conectar inserirProcessador(String modelo, Double capMax, Double usoAtual, String montagem, String endIPV4, Integer fkAlerta, Integer fkComponente, String email){

        conectarSQL.inserirProcessador(modelo, capMax, usoAtual, montagem, endIPV4, fkAlerta, fkComponente, email);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conexao = DriverManager.getConnection(url, user, passwd);

            String cadastro = "INSERT INTO Registro (modelo, capacidadeMax, usoAtual, montagem, enderecoIPV4, dataHora, fkAlerta, fkComponente, fkMaquina) VALUES (?, ?, ?, ?, ?, NOW(), ?, ?, (SELECT idMaquina FROM Maquina JOIN Funcionario ON Maquina.fkFuncionario = idFuncionario WHERE emailCorporativo = ?));";

            PreparedStatement instrucao = conexao.prepareStatement(cadastro);

            instrucao.setString(1, modelo);
            instrucao.setDouble(2, capMax);
            instrucao.setDouble(3, usoAtual);
            instrucao.setString(4, montagem);
            instrucao.setString(5, endIPV4);
            instrucao.setInt(6, fkAlerta);
            instrucao.setInt(7, fkComponente);
            instrucao.setString(8, email);

            instrucao.execute();

            instrucao.close();
            conexao.close();
        } catch (
                SQLException | ClassNotFoundException throwables) {
            System.err.println("conexao nao estabelecida");
            throwables.printStackTrace();
        }
        return null;
    }
}
