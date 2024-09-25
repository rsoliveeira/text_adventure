package model;

public class Acao {
    private int idAcao;
    private String descricao;
    private int idItem; // Associado a um item em uma cena

    public Acao(int idAcao, String descricao, int idItem) {
        this.idAcao = idAcao;
        this.descricao = descricao;
        this.idItem = idItem;
    }

    // Getters e Setters
    public int getIdAcao() {
        return idAcao;
    }

    public void setIdAcao(int idAcao) {
        this.idAcao = idAcao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }
}
