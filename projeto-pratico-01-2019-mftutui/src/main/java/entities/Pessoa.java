package entities;

/**
 * Getters e setters da classe Pessoa
 */
public class Pessoa {

    private String loginPessoa;
    private String nomePessoa;
    private String senhaPessoa;

    public Pessoa(){

    }

    public Pessoa (String loginPessoa, String nomePessoa, String senhaPessoa) {
        this.loginPessoa = loginPessoa;
        this.nomePessoa = nomePessoa;
        this.senhaPessoa = senhaPessoa;
    }

    public String getLoginPessoa() {return loginPessoa; }

    public void setLoginPessoa(String loginPessoa) {this.loginPessoa = loginPessoa; }

    public String getNomePessoa() {return nomePessoa; }

    public void setNomePessoa(String nomePessoa) {this.nomePessoa = nomePessoa; }

    public String getSenhaPessoa() {return senhaPessoa; }

    public void setSenhaPessoa(String senhaPessoa) {this.senhaPessoa = senhaPessoa; }

    @Override
    public String toString() {
        return  String.format("|%-5d|%-25s|%-25s|%-25s|%-10b|%-10b|",
                this.loginPessoa, this.nomePessoa, this.senhaPessoa);
    }
}