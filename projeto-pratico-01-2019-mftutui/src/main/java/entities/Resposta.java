package entities;


/**
 * Getters e setters da classe Resposta
 */
public class Resposta {

    private int idResposta;
    private int idPergunta;
    private int idEleicao;
    private String resposta;
    private int votoResposta;

    public Resposta(){

    }

    public Resposta(int idResposta, int idPergunta, int idEleicao, String resposta, int votoResposta) {
        this.idResposta = idResposta;
        this.idPergunta = idPergunta;
        this.idEleicao = idEleicao;
        this.resposta = resposta;
        this.votoResposta = votoResposta;
    }

    public Resposta(int idPergunta, int idEleicao, String resposta, int votoResposta) {
        this.idPergunta = idPergunta;
        this.idEleicao = idEleicao;
        this.resposta = resposta;
        this.votoResposta = votoResposta;
    }

    public int getIdResposta() {return idResposta; }

    public void setIdResposta(int idResposta) {this.idResposta = idResposta; }

    public int getIdPergunta() {return idPergunta; }

    public void setIdPergunta(int idPergunta) {this.idPergunta = idPergunta; }

    public int getIdEleicao() {return idEleicao; }

    public void setIdEleicao(int idEleicao) {this.idEleicao = idEleicao; }

    public String getResposta() {return resposta; }

    public void setResposta(String resposta) {this.resposta = resposta; }

    public int getVotoRespota() {return votoResposta; }

    public void setVotoRespota(int votoRespota) {this.votoResposta = votoRespota; }

    @Override
    public String toString() {
        return  String.format("|%-5d|%-5d|%-5d|%-25s|%-5d|",
                this.idResposta, this.idPergunta, this.idEleicao, this.resposta, this.votoResposta);
    }
}