package entities;
import db.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RespostaDAO {

    static Connection conexao = ConnectionFactory.getDBConnection();

    /**
     * Adiciona uma resposta na tabela Resposta.
     * @param r Objeto de Resposta contendo as informações sobre uma resposta.
     */
    public static void adicionaResposta(Resposta r) {

        String sql = "INSERT INTO Resposta (idPergunta, idEleicao, resposta, votoResposta) VALUES (?,?,?,?)";
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setInt(1, r.getIdPergunta());
                stmt.setInt(2, r.getIdEleicao());
                stmt.setString(3, r.getResposta());
                stmt.setInt(4, r.getVotoRespota());
                stmt.execute();
            }
         catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    /**
     * Lista todas as respostas de uma pergunta a partir do id da prergunta.
     * @param idPergunta valor único atribuído a uma pergunta.
     * @return respostas Lista de repostas de uma determinada pergunta.
     */
    public static List<Resposta> listaRespostaPorIdPergunta(int idPergunta) {

        List<Resposta> respostas = new ArrayList<>();
        String sql = "SELECT * FROM Resposta WHERE idPergunta = " + idPergunta + ";";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Resposta c = new Resposta(
                        rs.getInt("idResposta"),
                        rs.getInt("idPergunta"),
                        rs.getInt("idEleicao"),
                        rs.getString("resposta"),
                        rs.getInt("votoResposta"));
                respostas.add(c);
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return respostas;
    }

    /**
     * Adiciona um voto na resposta de uma pergunta.
     * @param idResposta Informação única identificadora de uma resposta.
     **/
    public static void vota(int idResposta) {

        String sql = "UPDATE Resposta SET votoResposta = votoResposta+1 WHERE idResposta = '" + idResposta + "'";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.execute();
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }
}