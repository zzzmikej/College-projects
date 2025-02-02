package projeto.conexao;

import projeto.captura.Monitoramento;
import projeto.print.Prints;

import java.sql.*;

public class ConectarSQL {
    Monitoramento monitoramento = new Monitoramento();

    private String urlSQL = "jdbc:sqlserver://34.225.182.14:1433;databaseName=NEXUS;user=nexus;password=nexus123;encrypt=false;trustServerCertificate=true";

    private String email;
    private String pass;
    private Boolean logado;

    public Boolean Logar(String email, String pass) throws ClassNotFoundException {
        this.email = email;
        this.pass = pass;
        Prints prints = new Prints();

        String sqlSelect = "SELECT nome, emailCorporativo, token FROM Funcionario;";

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection conexaoSQL = DriverManager.getConnection(urlSQL);

             PreparedStatement preparedStatement = conexaoSQL.prepareStatement(sqlSelect)) {

            ResultSet respostaSQL = preparedStatement.executeQuery();

            while (respostaSQL.next()) {
                String username = respostaSQL.getString("emailCorporativo");
                String token = respostaSQL.getString("token");
                String nome = respostaSQL.getString("nome");

                if (this.email.equals(username) && this.pass.equals(token)) {

                    prints.limparConsole();
                    System.out.println("""
                            Login Realizado com Sucesso!
                            
                                Seja Bem-Vindo
                            """);
                    System.out.println(nome);

                    monitoramento.monitor(username);
                    return true;
                } else {
                    logado = false;
                }
            }

            respostaSQL.close();
            preparedStatement.close();
            conexaoSQL.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("""
                                        
                    Nenhum usuário encontrado
                                        
                    Por favor verifique as credenciais
                    """);
            logado = false;
        }
        return logado;
    }
    public ConectarSQL DadosDisco(String modelo, Double capMax, Double usoAtual, String montagem, String endIPV4, Integer fkAlerta, Integer fkComponente, String email) throws ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        try {
            Connection conexaoSQL = DriverManager.getConnection(urlSQL);
            String cadastro = "INSERT INTO Registro (modelo, capacidadeMax, usoAtual, montagem, enderecoIPV4, dataHora, fkAlerta, fkComponente, " +
                    "fkMaquina) VALUES (?, ?, ?, ?, ?, GETDATE(), ?, ?, (SELECT idMaquina FROM Maquina JOIN Funcionario ON Maquina.fkFuncionario = idFuncionario WHERE emailCorporativo = ?));\n";

            PreparedStatement instrucao = conexaoSQL.prepareStatement(cadastro);

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
            conexaoSQL.close();
        } catch (
                SQLException throwables) {
            System.err.println("conexao nao estabelecida");
            throwables.printStackTrace();
        }
        return null;
    }
    public Conectar inserirProcessos(String name, Double usoCPU, Double usoMem, Double usoDisk, String email) throws ClassNotFoundException {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        try {
            Connection conexaoSQL = DriverManager.getConnection(urlSQL);

            String cadastro = "INSERT INTO Processo (nome, usoAtualRAM, usoAtualDisco, usoAtualCPU, dataHora, fkMaquina) VALUES (?, ?, ?, ?, GETDATE(), (SELECT idMaquina FROM Maquina JOIN Funcionario ON Maquina.fkFuncionario = idFuncionario WHERE emailCorporativo = ?));";

            PreparedStatement instrucao = conexaoSQL.prepareStatement(cadastro);

            instrucao.setString(1, name);
            instrucao.setDouble(2, usoMem);
            instrucao.setDouble(3, usoDisk);
            instrucao.setDouble(4, usoCPU);
            instrucao.setString(5, email);

            instrucao.execute();

            instrucao.close();
            conexaoSQL.close();
        } catch (
                SQLException throwables) {
            System.err.println("conexao nao estabelecida");
            throwables.printStackTrace();
        }
        return null;
    }
    public Conectar inserirMemoria(String modelo, Double capMax, Double usoAtual, String montagem, String endIPV4, Integer fkAlerta, Integer fkComponente, String email) throws ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        try {
            Connection conexaoSQL = DriverManager.getConnection(urlSQL);

            String cadastro = "INSERT INTO Registro (modelo, capacidadeMax, usoAtual, montagem, enderecoIPV4, dataHora, fkAlerta, fkComponente, " +
                    "fkMaquina) VALUES (?, ?, ?, ?, ?, GETDATE(), ?, ?, (SELECT idMaquina FROM Maquina JOIN Funcionario ON Maquina.fkFuncionario = idFuncionario WHERE emailCorporativo = ?));\n";

            PreparedStatement instrucao = conexaoSQL.prepareStatement(cadastro);

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
            conexaoSQL.close();
        } catch (
                SQLException throwables) {
            System.err.println("conexao nao estabelecida");
            throwables.printStackTrace();
        }
        return null;
    }
    public Conectar inserirProcessador(String modelo, Double capMax, Double usoAtual, String montagem, String endIPV4, Integer fkAlerta, Integer fkComponente, String email){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            Connection conexaoSQL = DriverManager.getConnection(urlSQL);

            String cadastro = "INSERT INTO Registro (modelo, capacidadeMax, usoAtual, montagem, enderecoIPV4, dataHora, fkAlerta, fkComponente, " +
                    "fkMaquina) VALUES (?, ?, ?, ?, ?, GETDATE(), ?, ?, (SELECT idMaquina FROM Maquina JOIN Funcionario ON Maquina.fkFuncionario = idFuncionario WHERE emailCorporativo = ?));\n";

            PreparedStatement instrucao = conexaoSQL.prepareStatement(cadastro);

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
            conexaoSQL.close();
        } catch (
                SQLException throwables) {
            System.err.println("conexao nao estabelecida");
            throwables.printStackTrace();
        }
        return null;
    }
}