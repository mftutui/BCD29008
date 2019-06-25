package entities;

/**
 * Getters e setters da classe Pergunta
 */
public class Pergunta {

    private int idPergunta;
    private int idEleicao;
    private String pergunta;
    private int minRespostas;
    private int maxRespostas;

    public Pergunta(){

    }

    public Pergunta (int idPergunta, int idEleicao, String pergunta, int minRespostas, int maxRespostas) {
        this.idPergunta = idPergunta;
        this.idEleicao = idEleicao;
        this.pergunta = pergunta;
        this.minRespostas = minRespostas;
        this.maxRespostas = maxRespostas;
    }

    public Pergunta (int idEleicao, String pergunta, int minRespostas, int maxRespostas) {
        this.idEleicao = idEleicao;
        this.pergunta = pergunta;
        this.minRespostas = minRespostas;
        this.maxRespostas = maxRespostas;
    }

    public int getIdPergunta() {return idPergunta; }

    public void setIdPergunta(int idPergunta) {this.idPergunta = idPergunta; }

    public int getIdEleicao() {return idEleicao; }

    public void setIdEleicao(int idEleicao) {this.idEleicao = idEleicao; }

    public String getPergunta() {return pergunta; }

    public void setPergunta(String pergunta) {this.pergunta = pergunta; }

    public int getMinRespostas() {return minRespostas; }

    public void setMinRespostas(int minRespostas) {this.minRespostas = minRespostas; }

    public int getMaxRespostas() {return maxRespostas; }

    public void setMaxRespostas(int maxRespostas) {this.maxRespostas = maxRespostas; }

    @Override
    public String toString() {
        return  String.format("|%-5d|%-25s|%-25s|%-25s|%-10b|%-10b|",
                this.idPergunta, this.idEleicao, this.pergunta, this.minRespostas, this.maxRespostas);
    }
}