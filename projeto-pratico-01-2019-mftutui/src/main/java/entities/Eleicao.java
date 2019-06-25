package entities;

/**
 * Getters e setters da classe Eleição
 */
public class Eleicao {

    private int idEleicao;
    private String nomeEleicao;
    private String inicioEleicao;
    private String fimEleicao;
    private boolean estadoEleicao;
    private boolean apuracaoEleicao;

    public Eleicao() {

    }

    public Eleicao(int idEleicao, String nomeEleicao, String inicioEleicao, String fimEleicao, boolean estadoEleicao, boolean apuracaoEleicao) {
        this.idEleicao = idEleicao;
        this.nomeEleicao = nomeEleicao;
        this.inicioEleicao = inicioEleicao;
        this.fimEleicao = fimEleicao;
        this.estadoEleicao = estadoEleicao;
        this.apuracaoEleicao = apuracaoEleicao;
    }

    public Eleicao(String nomeEleicao, boolean apuracaoEleicao) {
        this.nomeEleicao = nomeEleicao;
        this.apuracaoEleicao = apuracaoEleicao;
    }

    public Eleicao(int idEleicao, String nomeEleicao) {
        this.idEleicao = idEleicao;
        this.nomeEleicao = nomeEleicao;
    }

    public int getIdEleicao() {return idEleicao; }

    public void setIdEleicao(int idEleicao) {this.idEleicao = idEleicao; }

    public String getNomeEleicao() {return nomeEleicao; }

    public void setNomeEleicao(String nomeEleicao) {this.nomeEleicao = nomeEleicao; }

    public String getInicioEleicao() {return inicioEleicao; }

    public void setInicioEleicao(String inicioEleicao) {this.inicioEleicao = inicioEleicao; }

    public String getFimEleicao() {return fimEleicao; }

    public void setFimEleicao(String fimEleicao) {this.fimEleicao = fimEleicao; }

    public boolean getEstadoEleicao() {return estadoEleicao; }

    public void setEstadoEleicao(boolean estadoEleicao) {this.estadoEleicao = estadoEleicao; }

    public boolean getApuracaoEleicao() { return apuracaoEleicao; }

    public void setApuracaoEleicao(boolean apuracaoEleicao) {this.apuracaoEleicao = apuracaoEleicao; }

    @Override
    public String toString() {
        return  String.format("|%-5d|%-25s|%-25s|%-25s|%-10b|%-10b|",
                this.idEleicao, this.nomeEleicao, this.inicioEleicao, this.fimEleicao, this.estadoEleicao, this.apuracaoEleicao);
    }
}