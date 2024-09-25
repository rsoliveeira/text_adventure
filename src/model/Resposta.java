package model;

public class Resposta {
    private int idResposta;
    private String descricao;
    private String resultado; // Poderá ser uma mudança de cena ou outra informação
    private int idAcao;

    public Resposta(int idResposta, String descricao, String resultado, int idAcao) {
        this.idResposta = idResposta;
        this.descricao = descricao;
        this.resultado = resultado;
        this.idAcao = idAcao;
    }

    // Getters e Setters
    public int getIdResposta() {
        return idResposta;
    }

    public void setIdResposta(int idResposta) {
        this.idResposta = idResposta;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public int getIdAcao() {
        return idAcao;
    }

    public void setIdAcao(int idAcao) {
        this.idAcao = idAcao;
    }
}
