package entities;
import db.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PerguntaDAO {

    static Connection conexao = ConnectionFactory.getDBConnection();


    /**
     * Adiciona uma pergunta a uma eleição.
     * @param p Objeto de Pergunta com as atrubuições de uma perguna.
     */
    public static void adicionaPergunta(Pergunta p) {

        String sql = "INSERT INTO Pergunta (idEleicao, pergunta, minRespostas, maxRespostas) VALUES (?,?,?,?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, p.getIdEleicao());
            stmt.setString(2, p.getPergunta());
            stmt.setInt(3, p.getMinRespostas());
            stmt.setInt(4, p.getMaxRespostas());
            stmt.execute();
        }
         catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    /**
     * Lista pergunta de acordo com o id da mesma.
     * @param idEleicao valor único atribuído a uma eleicao.
     * @return perguntas Lista perguntas de uma eleição.
     */
    public static List<Pergunta> listarPerguntaPorEleicao(int idEleicao) {

        List<Pergunta> perguntas = new ArrayList<>();
        String sql = "SELECT * FROM Pergunta WHERE idEleicao = " + idEleicao + ";";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Pergunta c = new Pergunta(
                        rs.getInt("idPergunta"),
                        rs.getInt("idEleicao"),
                        rs.getString("pergunta"),
                        rs.getInt("minRespostas"),
                        rs.getInt("maxRespostas"));
                perguntas.add(c);
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return perguntas;
    }

    /**
     * Econtra o id da pergunta pelo texto atribuido a ela.
     * @param pergunta Texto da pergunta de uma eleição.
     * @return perguntaPorId Objeto de Pergunta.
     */
    public static Pergunta retornaIDPorPergunta(String pergunta) {

        Pergunta perguntaPorID = new Pergunta();
        String sql = "SELECT idPergunta FROM Pergunta WHERE pergunta = '" + pergunta + "';";
        //System.out.println(sql);
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                perguntaPorID.setIdPergunta(rs.getInt("idPergunta"));
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }

        return perguntaPorID;
    }
}