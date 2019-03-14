package bcd;

import java.sql.*;
import java.util.Scanner;

public class Exemplo1 {

    private static final String dbPath = "src/main/resources/lab01.sqlite";

    public static void inserir() throws ClassNotFoundException, SQLException {
        Scanner teclado = new Scanner(System.in);

        System.out.println("Insira um novo Aluno no banco de dados");

        System.out.println("ID do aluno: ");
        int id = teclado.nextInt();

        System.out.println("Nome: ");
        String nome = teclado.next();
        //teclado.nextLine();

        System.out.println("Peso: ");
        int peso = teclado.nextInt();

        System.out.println("Altura em centímetros: ");
        int altura = teclado.nextInt();

        System.out.println("Email: ");
        String email = teclado.next();
        //teclado.nextLine();

        //ler string
        //String s = teclado.nextLine();
        //teclado.next(); // consumir caracter extra do ENTER
        //ler int
        //int i = teclado.nextByte();

        // Carregando classe
        Class.forName("org.sqlite.JDBC");

        // Criando conexão
        Connection connection = DriverManager.getConnection("jdbc:sqlite:"+dbPath);

        Statement stmt = connection.createStatement();

        String query = "INSERT INTO Aluno VALUES (" + id +",'" + nome +"',"+peso+"," + altura +",'" + email +"')";

        System.out.println("Você inseriu: " + query);

        stmt.executeUpdate(query);

        stmt.close();
        connection.close();
    }

    public static void listarRegistros() throws ClassNotFoundException, SQLException{
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:"+dbPath);
        Statement stmt = connection.createStatement();

        String query = "SELECT * FROM Aluno";

        ResultSet linhas = stmt.executeQuery(query);

        printTabela(linhas);

        linhas.close();
        stmt.close();
        connection.close();
    }

    public static void printTabela(ResultSet linhas) throws SQLException {
        System.out.println(String.format("|%-5s|%-25s|%-25s","Id","Nome","Email"));

        while (linhas.next()){
            System.out.println(String.format("|%-5d|%-25s|%-25s",
                    linhas.getInt("idAluno"),
                    linhas.getString("nome"),
                    linhas.getString("email")));
        }
    }

    public static void buscar() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:"+dbPath);
        Statement stmt = connection.createStatement();

        Scanner teclado = new Scanner(System.in);

        System.out.println("Procure por um email: ");
        String email = teclado.next();

        String query = "SELECT * FROM Aluno WHERE email = '"+ email + "'";

        ResultSet linhas = stmt.executeQuery(query);

        printTabela(linhas);

        linhas.close();
        stmt.close();
        connection.close();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        buscar();

    }

}
