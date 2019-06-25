package entities;
import db.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoaDAO {

    static Connection conexao = ConnectionFactory.getDBConnection();

    /**
     * Verifica se o login passado encontra-se na tabela Pessoa.
     * @param loginPessoa valor único atribuído a uma pessoa.
     * @return estado da consulta.
     */
    public static boolean verificaLogin(String loginPessoa) {

        Pessoa v = new Pessoa();
        String sql = "SELECT loginPessoa FROM Pessoa WHERE loginPessoa = '" + loginPessoa + "'";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                v.setLoginPessoa(rs.getString("loginPessoa"));
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }

        if (v.getLoginPessoa() == null){
            return false;
        }
        return true;
    }

    /**
     * Verifica se login e a senha passados pertencem a mesma pessoa.
     * @param loginPessoa valor único atribuído a uma pessoa.
     * @param senhaPessoa senha atribuida a uma pessoa para realizar um voto.
     * @return estado da consulta.
     */
    public static boolean verificaLoginSenha(String loginPessoa, String senhaPessoa) {

        Pessoa v = new Pessoa();
        String sql = "SELECT loginPessoa FROM Pessoa WHERE loginPessoa = '" + loginPessoa + "' AND senhaPessoa = '" + senhaPessoa + "';";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                v.setLoginPessoa(rs.getString("loginPessoa"));
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.toString());
        }

        if (v.getLoginPessoa() == null){
            return false;
        }
        return true;
    }
}
