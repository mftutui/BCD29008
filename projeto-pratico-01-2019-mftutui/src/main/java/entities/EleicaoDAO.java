package entities;
import db.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EleicaoDAO {

    static Connection conexao = ConnectionFactory.getDBConnection();

    /**
     * Insere uma eleição ao banco de dados.
     * @param e Objeto de eleição.
     */
    public static void adicionaEleicao(Eleicao e) {

        String sql = "INSERT INTO Eleicao (nomeEleicao, apuracaoEleicao) VALUES (?,?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, e.getNomeEleicao());
            stmt.setBoolean(2, e.getApuracaoEleicao());
            stmt.execute();
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    /**
     * Lista eleições abertas, com estado de eleição TRUE.
     * @return Lista de eleicões com idEleicao e nomeEleicao.
     */
    public static List<Eleicao> listarEleicaoAberta() {

        List<Eleicao> eleicoes = new ArrayList<>();
        String sql = "SELECT * FROM Eleicao WHERE estadoEleicao IS TRUE;";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Eleicao c = new Eleicao(
                        rs.getInt("idEleicao"),
                        rs.getString("nomeEleicao"));
                eleicoes.add(c);
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return eleicoes;
    }

    /**
     * Lista eleições sem estado, com estado de eleição NULL.
     * @return Lista de eleicões com idEleicao e nomeEleicao.
     */
    public static List<Eleicao> listarEleicaoSemEstado() {

        List<Eleicao> eleicoes = new ArrayList<>();
        String sql = "SELECT * FROM Eleicao WHERE estadoEleicao IS NULL;";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Eleicao c = new Eleicao(
                        rs.getInt("idEleicao"),
                        rs.getString("nomeEleicao"));
                eleicoes.add(c);
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return eleicoes;
    }

    /**
     * Lista eleições encerradas, com estado de eleição FALSE.
     * @return Lista de eleicões com idEleicao e nomeEleicao.
     */
    public static List<Eleicao> listarEleicaoEncerrada() {

        List<Eleicao> eleicoes = new ArrayList<>();
        String sql = "SELECT * FROM Eleicao WHERE estadoEleicao IS FALSE;";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Eleicao c = new Eleicao(
                        rs.getInt("idEleicao"),
                        rs.getString("nomeEleicao"));
                eleicoes.add(c);
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return eleicoes;
    }

    /**
     * Lista eleições abertas, com estado de eleição TRUE.
     * @return Lista de eleicões com idEleicao e nomeEleicao.
     */
    public static List<Eleicao> listarEleicaoApurada() {

        List<Eleicao> eleicoes = new ArrayList<>();
        String sql = "SELECT * FROM Eleicao WHERE apuracaoEleicao IS TRUE;";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Eleicao c = new Eleicao(
                        rs.getInt("idEleicao"),
                        rs.getString("nomeEleicao"));
                eleicoes.add(c);
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return eleicoes;
    }

    /**
     * Seleciona eleições pelo nome.
     * @param nomeEleicao nome atribuido na criação da eleição.
     * @return eleicaoPorNome Objeto de Eleicao.
     */
    public static Eleicao retornaIdPorNome(String nomeEleicao) {

        Eleicao eleicaoPorNome = new Eleicao();
        String sql = "SELECT idEleicao FROM Eleicao WHERE nomeEleicao = '" + nomeEleicao + "';";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                eleicaoPorNome.setIdEleicao(rs.getInt("idEleicao"));
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return eleicaoPorNome;
    }

    /**
     * Seleciona eleições pelo id.
     * @param idEleicao valor único atribuído a eleição.
     * @return nomePorEleicao Objeto de Eleicao.
     */
    public static Eleicao retornaNomePorID(int idEleicao) {

        Eleicao nomePorEleicao = new Eleicao();
        String sql = "SELECT nomeEleicao FROM Eleicao WHERE idEleicao = '" + idEleicao + "';";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                nomePorEleicao.setNomeEleicao(rs.getString("nomeEleicao"));
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return nomePorEleicao;
    }

    /**
     * Troca o estado de uma eleição para TRUE a tornando aberta.
     * @param idEleicao valor único atribuído a eleição.
     * @param dataAtual valor da data capturada no instante da consulta.
     * @return abrirEleicao Objeto de Eleicao.
     */
    public static Eleicao abrirEleicao(int idEleicao, String dataAtual) {

        Eleicao abrirEleicao = new Eleicao();
        String sql = "UPDATE Eleicao SET estadoEleicao = TRUE, inicioEleicao = '" + dataAtual + "' WHERE idEleicao = " + idEleicao + ";";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.execute();
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return abrirEleicao;
    }

    /**
     * Troca o estado de uma eleição para FALSE a tornando encerrada.
     * @param idEleicao valor único atribuído a eleição.
     * @param dataAtual valor da data capturada no instante da consulta.
     * @return encerraEleicao Objeto de Eleicao.
     */
    public static Eleicao encerrarEleicao(int idEleicao, String dataAtual) {

        Eleicao encerrarEleicao = new Eleicao();
        String sql = "UPDATE Eleicao SET estadoEleicao = FALSE , fimEleicao = '" + dataAtual + "' WHERE idEleicao = " + idEleicao + ";";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.execute();
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return encerrarEleicao;
    }

    /**
     * Troca o estado da apuração de uma eleição para TRUE.
     * @param idEleicao valor único atribuído a eleição.
     * @return apurarEleicao Objeto de Eleicao.
     */
    public static Eleicao apurarEleicao(int idEleicao) {

        Eleicao apurarEleicao = new Eleicao();
        String sql = "UPDATE Eleicao SET apuracaoEleicao = TRUE WHERE idEleicao = " + idEleicao + ";";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.execute();
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return apurarEleicao;
    }
}