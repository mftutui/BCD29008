package entities;

/**
 * Getters e setters da classe Eleitor
 */
public class Eleitor {

    private int idEleicao;
    private String loginPessoa;
    private boolean statusEleitor;

    public Eleitor(){

    }

    public Eleitor (int idEleicao, String loginPessoa, boolean statusEleitor) {
        this.idEleicao = idEleicao;
        this.loginPessoa = loginPessoa;
        this.statusEleitor = statusEleitor;
    }

    public Eleitor (String loginPessoa) {
        this.loginPessoa = loginPessoa;
    }

    public Eleitor (int idEleicao) {
        this.idEleicao = idEleicao;
    }

    public Eleitor (boolean statusEleitor) {
        this.statusEleitor = statusEleitor;
    }

    public int getIdEleicao() {return idEleicao; }

    public void setIdEleicao(int idEleitor) {this.idEleicao = idEleicao; }

    public String getLoginPessoa() {return loginPessoa; }

    public void setLoginPessoa(String loginPessoa) {this.loginPessoa = loginPessoa; }

    public boolean getStatusEleitor() {return statusEleitor; }

    public void setStatusEleitor(boolean statusEleitor) {this.statusEleitor = statusEleitor; }

    @Override
    public String toString() {
        return  String.format("|%-5d|%-25s|%-25s|%-25s|%-10b|%-10b|",
                this.idEleicao, this.loginPessoa, this.statusEleitor);
    }
}