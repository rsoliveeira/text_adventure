package model;

import java.util.List;
import java.util.Objects;

public class Cena {
    private int idCena;
    private String titulo; // Adicionei o campo titulo
    private String descricao;
    private List<Item> itens;

    // Getters e Setters
    public Integer getIdCena() {
        return idCena;
    }

    public void setIdCena(Integer idCena) {
        this.idCena = idCena;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }

    // Método toString para uma representação textual clara
    @Override
    public String toString() {
        return "Cena{" +
                "idCena=" + idCena +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", itens=" + itens +
                '}';
    }

    // Métodos equals e hashCode para comparação e uso em coleções
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cena cena = (Cena) o;
        return idCena == cena.idCena &&
                Objects.equals(titulo, cena.titulo) &&
                Objects.equals(descricao, cena.descricao) &&
                Objects.equals(itens, cena.itens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCena, titulo, descricao, itens);
    }
}
