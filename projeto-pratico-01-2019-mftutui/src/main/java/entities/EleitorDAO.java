package entities;
import db.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EleitorDAO {

    static Connection conexao = ConnectionFactory.getDBConnection();

    /**
     * Adiciona um eleitor a uma eleição.
     * @param e Objeto de Eleitor com os parâmetros atribuidos a um eleitor.
     **/
    public static void adicionaEleitor(Eleitor e) {

        String sql = "INSERT INTO Eleitor (idEleicao, loginPessoa, statusEleitor) VALUES (?,?,?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, e.getIdEleicao());
            stmt.setString(2, e.getLoginPessoa());
            stmt.setBoolean(3, e.getStatusEleitor());
            stmt.execute();
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    /**
     * Lista as eleições que o eleitor está participando.
     * @param loginPessoa Informação única identificadora de uma pessoa.
     * @return eleitores Lista ids das eleições as quais uma pessoa é um eleitor.
     **/
    public static List<Eleitor> listarEleicaoDeEleitor(String loginPessoa) {

        List<Eleitor> eleitores = new ArrayList<>();
        String sql = "SELECT idEleicao FROM Eleitor WHERE loginPessoa = '" + loginPessoa + "'";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Eleitor c = new Eleitor(
                        rs.getInt("idEleicao"));
                eleitores.add(c);
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return eleitores;
    }

    /**
     * Mostra a lista de eleitores de uma determinada eleição.
     * @param idEleicao Número único identificador de uma eleição.
     * @return eleitores Lista de logins de eleitores.
     **/
    public static List<Eleitor> listarEleitores(int idEleicao) {

        List<Eleitor> eleitores = new ArrayList<>();
        String sql =  "SELECT loginPessoa FROM Eleitor WHERE idEleicao = '" + idEleicao + "'";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Eleitor c = new Eleitor(
                        rs.getString("loginPessoa"));
                eleitores.add(c);
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return eleitores;
    }

    /**
     * Verifica se o eleitor ja votou em determinada eleição.
     * @param idEleicao valor único atribuído a uma eleicao.
     * @param loginPessoa valor único atribuído a uma pessoa.
     * @return estado da consulta.
     */
    public static boolean verificarVotoEmEleicao(int idEleicao, String loginPessoa) {

        Eleitor v = new Eleitor();
        String sql = "SELECT statusEleitor FROM Eleitor WHERE idEleicao = "+ idEleicao +" AND loginPessoa = '"+ loginPessoa +"'";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                v.setStatusEleitor(rs.getBoolean("statusEleitor"));
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return v.getStatusEleitor();
    }

    /**
     * Registra que o eleitor ja votou em determinada eleição.
     * @param idEleicao valor único atribuído a uma eleicao.
     * @param loginPessoa valor único atribuído a uma pessoa.
     */
    public static void votaEmEleicao(int idEleicao, String loginPessoa) {

        String sql = "UPDATE Eleitor SET statusEleitor = TRUE WHERE loginPessoa = '" + loginPessoa + "' and idEleicao = " + idEleicao + ";";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.execute();
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }
}